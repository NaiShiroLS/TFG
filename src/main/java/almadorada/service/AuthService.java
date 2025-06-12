package main.java.almadorada.service;

import main.java.almadorada.gui.LoginDialog;
import main.java.almadorada.gui.VistaGeneral;

import javax.swing.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class AuthService {

    private static final String USUARIO_CORRECTO = "admin";
    private static final String CONTRASENA_CORRECTA = "admin";

    private static final String INSERT_SQL = "INSERT INTO usuarios (nombre_usuario, contrasena_hash) VALUES (?, ?)";
    private static final String SELECT_SQL = "SELECT * FROM usuarios WHERE nombre_usuario = ?";

    public AuthService() {
        asegurarUsuarioLeiri();
    }

    public void autenticar(LoginDialog loginVentana) {
        String usuario = loginVentana.getCampoUsuario().getText();
        String contrasena = new String(loginVentana.getCampoContrasena().getPassword());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/alma_dorada?useSSL=false&serverTimezone=UTC", "root", "");
             PreparedStatement stmt = conn.prepareStatement(SELECT_SQL)) {

            stmt.setString(1, usuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashAlmacenado = rs.getString("contrasena_hash");
                    if (hashAlmacenado.equals(hash(contrasena))) {
                        JOptionPane.showMessageDialog(loginVentana, "Inicio de sesión exitoso.", "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
                        SwingUtilities.invokeLater(() -> new VistaGeneral().setVisible(true));
                        loginVentana.dispose();
                        return;
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(loginVentana, "Error de conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        JOptionPane.showMessageDialog(loginVentana, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void asegurarUsuarioLeiri() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/alma_dorada?useSSL=false&serverTimezone=UTC", "root", "");
             PreparedStatement checkStmt = conn.prepareStatement(SELECT_SQL)) {

            checkStmt.setString(1, USUARIO_CORRECTO);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    try (PreparedStatement insertStmt = conn.prepareStatement(INSERT_SQL)) {
                        insertStmt.setString(1, USUARIO_CORRECTO);
                        insertStmt.setString(2, hash(CONTRASENA_CORRECTA));
                        insertStmt.executeUpdate();
                        System.out.println("Usuario Leiri creado automáticamente.");
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error al asegurar usuario Leiri: " + e.getMessage());
        }
    }

    private String hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();

        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
