package main.java.almadorada.gui;

import main.java.almadorada.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JFrame {

    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton botonLogin;

    private final AuthService authService;

    public LoginDialog() {
        this.authService = new AuthService(); // Crear instancia del servicio

        setTitle("Login");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(ColorPalette.WHITE_LIGHT);

        inicializarComponentes();
        configurarEventos();
    }

    private void inicializarComponentes() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE); // Cambiado a Color.WHITE para fondo blanco puro
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        JLabel titulo = new JLabel("Acceso al Sistema de Gestión");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(ColorPalette.GRAY_DARK);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titulo);

        contentPanel.add(Box.createVerticalStrut(30));

        JPanel loginFormPanel = new JPanel(new GridBagLayout());
        loginFormPanel.setBackground(Color.WHITE);
        loginFormPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel etiquetaUsuario = new JLabel("Usuario");
        etiquetaUsuario.setFont(ColorPalette.FONT_NORMAL);
        etiquetaUsuario.setForeground(ColorPalette.GRAY_DARK);

        JLabel etiquetaContrasena = new JLabel("Contraseña");
        etiquetaContrasena.setFont(ColorPalette.FONT_NORMAL);
        etiquetaContrasena.setForeground(ColorPalette.GRAY_DARK);

        campoUsuario = new JTextField(20);
        ColorPalette.styleTextField(campoUsuario);
        campoUsuario.setBackground(Color.WHITE);
        campoUsuario.setToolTipText("introducir el nombre de usuario");

        campoContrasena = new JPasswordField(20);
        ColorPalette.styleTextField(campoContrasena);
        campoContrasena.setBackground(Color.WHITE);
        campoContrasena.setToolTipText("introducir contraseña");

        botonLogin = ColorPalette.createPrimaryButton("Iniciar Sesión");
        botonLogin.setFont(ColorPalette.FONT_BUTTON);
        botonLogin.setPreferredSize(new Dimension(160, 35));

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginFormPanel.add(etiquetaUsuario, gbc);

        gbc.gridy = 1;
        loginFormPanel.add(campoUsuario, gbc);

        gbc.gridy = 2;
        loginFormPanel.add(etiquetaContrasena, gbc);

        gbc.gridy = 3;
        loginFormPanel.add(campoContrasena, gbc);

        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        loginFormPanel.add(botonLogin, gbc);

        loginFormPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(loginFormPanel);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void configurarEventos() {
        botonLogin.addActionListener(e -> authService.autenticar(this));
    }

    public JTextField getCampoUsuario() {
        return campoUsuario;
    }

    public JPasswordField getCampoContrasena() {
        return campoContrasena;
    }

    public JButton getBotonLogin() {
        return botonLogin;
    }
}