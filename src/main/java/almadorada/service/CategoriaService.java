package main.java.almadorada.service;

import main.java.almadorada.model.Categoria;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaService {

    private static final String URL = "jdbc:mysql://localhost:3306/alma_dorada?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public Connection getConnection() throws SQLException {
        Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Conexión exitosa a la base de datos.");
        return conexion;
    }

    public List<Categoria> obtenerTodos() {
        List<Categoria> categorias = new ArrayList<>();
        String query = "SELECT * FROM categorias";

        try (Connection conn = getConnection();
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

    // Método para validar si existe una categoría con el mismo nombre
    public boolean existeCategoriaPorNombre(String nombre, int idExcluir) {
        String query = "SELECT COUNT(*) FROM categorias WHERE nombre = ? AND id_categoria != ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nombre);
            stmt.setInt(2, idExcluir);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Método para validar si una categoría tiene productos asociados
    public boolean tieneProductosAsociados(int idCategoria) {
        String query = "SELECT COUNT(*) FROM productos WHERE id_categoria = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idCategoria);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void insertar(Categoria categoria) {
        // Validar que no exista una categoría con el mismo nombre
        if (existeCategoriaPorNombre(categoria.getNombre(), -1)) {
            JOptionPane.showMessageDialog(null, "Ya existe una categoría con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.executeUpdate();

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
        // Validar que no exista una categoría con el mismo nombre (excluyendo la actual)
        if (existeCategoriaPorNombre(categoria.getNombre(), categoria.getId())) {
            JOptionPane.showMessageDialog(null, "Ya existe una categoría con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
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

    public Categoria buscarPorId(int id) {
        Categoria categoria = null;
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM categorias WHERE id_categoria = ?")) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                categoria = new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoria;
    }

    public void eliminar(int id) {
        // Validar que la categoría no tenga productos asociados
        if (tieneProductosAsociados(id)) {
            JOptionPane.showMessageDialog(null, "No se puede eliminar la categoría porque tiene productos asociados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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