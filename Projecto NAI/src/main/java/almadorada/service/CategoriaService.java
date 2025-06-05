package main.java.almadorada.service;

import main.java.almadorada.model.Categoria;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CategoriaService {

    // Datos de conexión como constantes
    private static final String URL = "jdbc:mysql://localhost:3306/alma_dorada?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // cambia si usas contraseña

    // Método para abrir conexión
    private Connection getConnection() throws SQLException {
        Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Conexión exitosa a la base de datos.");
        return conexion;
    }


    public List<Categoria> obtenerTodos() {
        List<Categoria> categorias = new ArrayList<>();
        String query = "SELECT * FROM categorias";

        try (Connection conn = getConnection(); // Usar el método getConnection local
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setId(rs.getInt("id_categoria"));
                cat.setNombre(rs.getString("nombre"));
                cat.setDescripcion(rs.getString("descripcion"));
                categorias.add(cat);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener categorías: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return categorias;
    }

    public void insertar(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre de la categoría no puede estar vacío.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (existeNombre(categoria.getNombre(), categoria.getId())) { // Añadido id para comprobar en actualizaciones
            JOptionPane.showMessageDialog(null, "Ya existe una categoría con ese nombre.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.executeUpdate();

            // Obtener el ID generado (si tu base de datos lo genera automáticamente)
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    categoria.setId(generatedKeys.getInt(1));
                }
            }
            JOptionPane.showMessageDialog(null, "Categoría añadida exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al añadir categoría: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizar(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre de la categoría no puede estar vacío.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (existeNombre(categoria.getNombre(), categoria.getId())) {
            JOptionPane.showMessageDialog(null, "Ya existe otra categoría con ese nombre.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "UPDATE categorias SET nombre = ?, descripcion = ? WHERE id_categoria = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.setInt(3, categoria.getId());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Categoría actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar categoría: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean existeNombre(String nombre, int idActual) {
        String query = "SELECT COUNT(*) FROM categorias WHERE nombre = ? AND id_categoria <> ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setInt(2, idActual);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al comprobar nombres duplicados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    // Este método ya estaba en tu archivo, lo mantengo.
    public void eliminar(int id) {
        String query = "DELETE FROM categorias WHERE id_categoria = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Categoría eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró la categoría para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar categoría: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
