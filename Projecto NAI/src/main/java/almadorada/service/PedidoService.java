package main.java.almadorada.service;

import main.java.almadorada.model.Pedido;
import main.java.almadorada.model.Producto; // Importa tu clase Producto si no está ya

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoService {

    private final String url = "jdbc:mysql://localhost:3306/alma_dorada?useSSL=false&serverTimezone=UTC";
    private final String user = "root";
    private final String password = "";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Obtiene todos los pedidos
     * @return lista de pedidos
     */
    public List<Pedido> obtenerTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos ORDER BY id_pedido";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pedido p = mapearPedido(rs);
                lista.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener pedidos: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtiene un pedido por ID
     * @param id id del pedido
     * @return Optional con pedido si existe
     */
    public Optional<Pedido> obtenerPorId(int id) {
        String sql = "SELECT * FROM pedidos WHERE id_pedido = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearPedido(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener pedido por ID: " + e.getMessage());
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * Inserta un nuevo pedido
     * @param p pedido a insertar
     */
    public void insertar(Pedido p) {
        String sql = """
                INSERT INTO pedidos
                (fecha, nombre_cliente, correo, telefono, direccion, productos, subtotal, total, notas_adicionales, estado)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setearParametrosPedido(ps, p);
            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setIdPedido(rs.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Actualiza un pedido existente
     * @param p pedido con datos actualizados
     */
    public void actualizar(Pedido p) {
        String sql = """
                UPDATE pedidos SET
                fecha = ?, nombre_cliente = ?, correo = ?, telefono = ?, direccion = ?, productos = ?, subtotal = ?, total = ?, notas_adicionales = ?, estado = ?
                WHERE id_pedido = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setearParametrosPedido(ps, p);
            ps.setInt(11, p.getIdPedido());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                System.err.println("No se encontró pedido para actualizar con id: " + p.getIdPedido());
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Elimina un pedido por su ID
     * @param idPedido id del pedido
     */
    public void eliminar(int idPedido) {
        String sql = "DELETE FROM pedidos WHERE id_pedido = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            int filas = ps.executeUpdate();

            if (filas == 0) {
                System.err.println("No se encontró pedido para eliminar con id: " + idPedido);
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Valida que el estado sea uno permitido
     * @param estado estado a validar
     * @return true si válido, false si no
     */
    public boolean validarEstado(String estado) {
        if (estado == null) return false;
        String[] estadosValidos = {"Pendiente", "Procesando", "Enviado", "Entregado", "Cancelado"};
        for (String e : estadosValidos) {
            if (e.equalsIgnoreCase(estado.trim())) {
                return true;
            }
        }
        return false;
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setIdPedido(rs.getInt("id_pedido"));

        Timestamp ts = rs.getTimestamp("fecha");
        if (ts != null) {
            p.setFecha(ts.toLocalDateTime());
        } else {
            p.setFecha(null);
        }

        p.setNombreCliente(rs.getString("nombre_cliente"));
        p.setCorreo(rs.getString("correo"));
        p.setTelefono(rs.getString("telefono"));
        p.setDireccion(rs.getString("direccion"));

        // Aquí productos se guarda como String concatenado
        p.setProductos(rs.getString("productos"));

        p.setSubtotal(rs.getDouble("subtotal"));
        p.setTotal(rs.getDouble("total"));
        p.setNotasAdicionales(rs.getString("notas_adicionales"));
        p.setEstado(rs.getString("estado"));

        return p;
    }

    private void setearParametrosPedido(PreparedStatement ps, Pedido p) throws SQLException {
        if (p.getFecha() != null) {
            ps.setTimestamp(1, Timestamp.valueOf(p.getFecha()));
        } else {
            ps.setTimestamp(1, null);
        }

        ps.setString(2, p.getNombreCliente());
        ps.setString(3, p.getCorreo());
        ps.setString(4, p.getTelefono());
        ps.setString(5, p.getDireccion());

        // Convertir la lista de productos a string concatenado
        String productosString = convertirProductosAString(p.getListaProductos());
        ps.setString(6, productosString);

        ps.setDouble(7, p.getSubtotal());
        ps.setDouble(8, p.getTotal());
        ps.setString(9, p.getNotasAdicionales());
        ps.setString(10, p.getEstado());
    }

    /**
     * Convierte la lista de productos a un solo String concatenado
     * @param productos lista de productos (puede ser null o vacía)
     * @return string con todos los productos concatenados, separados por "; "
     */
    private String convertirProductosAString(List<Producto> productos) {
        if (productos == null || productos.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Producto producto : productos) {
            // Ajusta aquí el formato de cada producto según lo que quieras guardar
            sb.append(producto.toString()).append("; ");
        }
        return sb.toString().trim();
    }
}
