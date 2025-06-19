package main.java.almadorada.service;

import main.java.almadorada.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StockService {

    // Datos de conexión hardcodeados
    private final String url = "jdbc:mysql://localhost:3306/alma_dorada?useSSL=false&serverTimezone=UTC";
    private final String user = "root";
    private final String password = "";

    // Método para obtener la conexión
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }


    /**
     * Obtiene un producto por su Stock
     * @param stock Stock del producto
     * @return Optional con el producto si existe
     */
    public Optional<Producto> obtenerPorStock(int stock) {
        String sql = """
                SELECT p.*, c.nombre AS nombre_categoria
                FROM productos p
                LEFT JOIN categorias c ON p.id_categoria = c.id_categoria
                WHERE p.stock = ?
                """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, stock);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearProducto(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener producto por Stock: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
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

}
