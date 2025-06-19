package main.java.almadorada.service;

import main.java.almadorada.model.Pedido;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoService {

    private static final String URL = "jdbc:mysql://localhost:3306/alma_dorada?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Conexión exitosa a la base de datos.");
        return conn;
    }
    /**
     * Devuelve el precio unitario del producto cuyo nombre es el indicado.
     * @param nombreProducto Nombre del producto.
     * @return Precio unitario o 0.0 si no se encuentra o hay error.
     */
    public double obtenerPrecioProducto(String nombreProducto) {
        double precio = 0.0;

        String sql = "SELECT precio FROM productos WHERE nombre = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, nombreProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    precio = rs.getDouble("precio");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return precio;
    }

    public void insertar(Pedido pedido) {
        String query = "INSERT INTO pedidos (fecha, nombre_cliente, correo, telefono, direccion, productos, subtotal, total, notas_adicionales, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, Timestamp.valueOf(pedido.getFecha()));
            stmt.setString(2, pedido.getNombreCliente());
            stmt.setString(3, pedido.getCorreo());
            stmt.setString(4, pedido.getTelefono());
            stmt.setString(5, pedido.getDireccion());
            stmt.setString(6, pedido.getProductos());
            stmt.setDouble(7, pedido.getSubtotal());
            stmt.setDouble(8, pedido.getTotal());
            stmt.setString(9, pedido.getNotasAdicionales());
            stmt.setString(10, pedido.getEstado());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pedido.setIdPedido(generatedKeys.getInt(1));
                }
            }

            JOptionPane.showMessageDialog(null, "Pedido insertado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al insertar pedido: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizar(Pedido pedido) {
        String query = "UPDATE pedidos SET fecha = ?, nombre_cliente = ?, correo = ?, telefono = ?, direccion = ?, productos = ?, subtotal = ?, total = ?, notas_adicionales = ?, estado = ? " +
                "WHERE id_pedido = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setTimestamp(1, Timestamp.valueOf(pedido.getFecha()));
            stmt.setString(2, pedido.getNombreCliente());
            stmt.setString(3, pedido.getCorreo());
            stmt.setString(4, pedido.getTelefono());
            stmt.setString(5, pedido.getDireccion());
            stmt.setString(6, pedido.getProductos());
            stmt.setDouble(7, pedido.getSubtotal());
            stmt.setDouble(8, pedido.getTotal());
            stmt.setString(9, pedido.getNotasAdicionales());
            stmt.setString(10, pedido.getEstado());
            stmt.setInt(11, pedido.getIdPedido());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Pedido actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el pedido a actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar pedido: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminar(int id) {
        String query = "DELETE FROM pedidos WHERE id_pedido = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            int filas = stmt.executeUpdate();

            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Pedido eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el pedido para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar pedido: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Pedido> obtenerTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        String query = "SELECT * FROM pedidos";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setIdPedido(rs.getInt("id_pedido"));
                pedido.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                pedido.setNombreCliente(rs.getString("nombre_cliente"));
                pedido.setCorreo(rs.getString("correo"));
                pedido.setTelefono(rs.getString("telefono"));
                pedido.setDireccion(rs.getString("direccion"));
                pedido.setProductos(rs.getString("productos"));
                pedido.setSubtotal(rs.getDouble("subtotal"));
                pedido.setTotal(rs.getDouble("total"));
                pedido.setNotasAdicionales(rs.getString("notas_adicionales"));
                pedido.setEstado(rs.getString("estado"));
                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener pedidos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return pedidos;
    }

    public List<String> obtenerNombresProductos() {
        List<String> nombres = new ArrayList<>();
        String query = "SELECT nombre FROM productos";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                nombres.add(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener nombres de productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return nombres;
    }

    public int contarPedidosPendientes() {
        String sql = "SELECT COUNT(*) FROM pedidos WHERE estado = 'Pendiente'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error al contar pedidos pendientes: " + e.getMessage());
        }
        return 0;
    }


    public Pedido obtenerPorId(int id) {
        String query = "SELECT * FROM pedidos WHERE id_pedido = ?";
        Pedido pedido = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pedido = new Pedido();
                    pedido.setIdPedido(rs.getInt("id_pedido"));
                    pedido.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                    pedido.setNombreCliente(rs.getString("nombre_cliente"));
                    pedido.setCorreo(rs.getString("correo"));
                    pedido.setTelefono(rs.getString("telefono"));
                    pedido.setDireccion(rs.getString("direccion"));
                    pedido.setProductos(rs.getString("productos"));
                    pedido.setSubtotal(rs.getDouble("subtotal"));
                    pedido.setTotal(rs.getDouble("total"));
                    pedido.setNotasAdicionales(rs.getString("notas_adicionales"));
                    pedido.setEstado(rs.getString("estado"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener pedido por ID: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return pedido;
    }

}
