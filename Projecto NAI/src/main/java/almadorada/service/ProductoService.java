package main.java.almadorada.service;

import main.java.almadorada.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoService {

    // Datos de conexión hardcodeados (puedes cambiar password si usas)
    private final String url = "jdbc:mysql://localhost:3306/alma_dorada?useSSL=false&serverTimezone=UTC";
    private final String user = "root";
    private final String password = "";

    // Método para obtener la conexión
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Obtiene todos los productos con información de categoría
     * @return Lista de productos
     */
    public List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = """
                SELECT p.*, c.nombre AS nombre_categoria
                FROM productos p
                LEFT JOIN categorias c ON p.id_categoria = c.id_categoria
                ORDER BY p.id_producto
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = mapearProducto(rs);
                lista.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtiene un producto por su ID
     * @param id ID del producto
     * @return Optional con el producto si existe
     */
    public Optional<Producto> obtenerPorId(int id) {
        String sql = """
                SELECT p.*, c.nombre AS nombre_categoria
                FROM productos p
                LEFT JOIN categorias c ON p.id_categoria = c.id_categoria
                WHERE p.id_producto = ?
                """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearProducto(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener producto por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Inserta un nuevo producto en la base de datos
     * @param p Producto a insertar
     */
    public void insertar(Producto p) {
        String sql = """
                INSERT INTO productos (nombre, descripcion, precio, stock, imagen, id_categoria)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setearParametrosProducto(ps, p);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setId(rs.getInt(1)); // Asignar el ID generado
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Actualiza un producto existente en la base de datos
     * @param p Producto a actualizar
     */
    public void actualizar(Producto p) {
        String sql = """
                UPDATE productos
                SET nombre = ?, descripcion = ?, precio = ?, stock = ?, imagen = ?, id_categoria = ?
                WHERE id_producto = ?
                """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setearParametrosProducto(ps, p);
            ps.setInt(7, p.getId());
            int rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Elimina un producto de la base de datos por su ID
     * @param id ID del producto a eliminar
     */
    public void eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Valida si ya existe un producto con el mismo nombre (excluyendo el ID actual para actualizaciones)
     * @param nombre Nombre a validar
     * @param idActual ID del producto que se está editando (0 si es nuevo)
     * @return true si ya existe un producto con ese nombre, false en caso contrario
     */
    public boolean validarNombreDuplicado(String nombre, int idActual) {
        String sql = "SELECT COUNT(*) FROM productos WHERE nombre = ? AND id_producto <> ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, idActual);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar nombre duplicado de producto: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id_producto"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        p.setImagen(rs.getBytes("imagen"));

        int idCategoria = rs.getInt("id_categoria");
        if (!rs.wasNull()) {
            p.setIdCategoria(idCategoria);
        } else {
            p.setIdCategoria(0);
        }

        p.setNombreCategoria(rs.getString("nombre_categoria"));
        return p;
    }

    private void setearParametrosProducto(PreparedStatement ps, Producto p) throws SQLException {
        ps.setString(1, p.getNombre());
        ps.setString(2, p.getDescripcion());
        ps.setDouble(3, p.getPrecio());
        ps.setInt(4, p.getStock());
        ps.setBytes(5, p.getImagen());
        if (p.getIdCategoria() > 0) {
            ps.setInt(6, p.getIdCategoria());
        } else {
            ps.setNull(6, Types.INTEGER);
        }
    }
}
