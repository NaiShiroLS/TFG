package main.java.almadorada.gui;

import main.java.almadorada.gui.panels.*;

import javax.swing.*;
import java.awt.*;

public class VistaGeneral extends JFrame {

    private JPanel contenedorCentral;

    public VistaGeneral() {
        setTitle("Alma Dorada - Administración");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(ColorPalette.WHITE_LIGHT);

        initUI();
    }

    private void initUI() {
        // Panel de navegación
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());
        menuPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        menuPanel.setBackground(ColorPalette.CREAM_LIGHT);

        JPanel menuContent = new JPanel();
        menuContent.setLayout(new BoxLayout(menuContent, BoxLayout.Y_AXIS));
        menuContent.setBackground(ColorPalette.CREAM_LIGHT);

        // Título
        JLabel titulo = new JLabel("Alma Dorada Gestión", SwingConstants.CENTER);
        titulo.setFont(ColorPalette.FONT_TITLE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

        // Botones de menú
        JButton btnVistaGeneral = createMenuButton("Vista General");
        JButton btnProductos = createMenuButton("Gestión de Productos");
        JButton btnCategorias = createMenuButton("Gestión de Categorías");
        JButton btnPedidos = createMenuButton("Gestión de Pedidos");
        JButton btnStock = createMenuButton("Gestión de Stock");
        JButton btnCerrarSesion = createMenuButton("Cerrar Sesión");

        // Eventos
        btnVistaGeneral.addActionListener(e -> mostrarPanel(new VistaGeneralPanel()));
        btnCategorias.addActionListener(e -> mostrarPanel(new CategoriasPanel()));
        btnProductos.addActionListener(e -> mostrarPanel(new ProductosPanel()));
        btnStock.addActionListener(e -> mostrarPanel(new StockPanel()));
        btnPedidos.addActionListener(e -> mostrarPanel(new PedidosPanel()));
        btnCerrarSesion.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de que deseas cerrar sesión?",
                    "Confirmar Cierre de Sesión",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (opcion == JOptionPane.YES_OPTION) {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    LoginDialog loginDialog = new LoginDialog();
                    loginDialog.setVisible(true);
                });
            }
        });

        btnVistaGeneral.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnProductos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCategorias.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPedidos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnStock.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Agregar componentes al panel de contenido
        menuContent.add(titulo);
        menuContent.add(btnVistaGeneral);
        menuContent.add(Box.createVerticalStrut(10));
        menuContent.add(btnProductos);
        menuContent.add(Box.createVerticalStrut(10));
        menuContent.add(btnCategorias);
        menuContent.add(Box.createVerticalStrut(10));
        menuContent.add(btnPedidos);
        menuContent.add(Box.createVerticalStrut(10));
        menuContent.add(btnStock);

        // Agregar al menú principal
        menuPanel.add(menuContent, BorderLayout.CENTER);
        menuPanel.add(btnCerrarSesion, BorderLayout.SOUTH);

        add(menuPanel, BorderLayout.WEST);

        // Panel central
        contenedorCentral = new JPanel(new BorderLayout());
        contenedorCentral.setBackground(ColorPalette.WHITE_LIGHT);
        add(contenedorCentral, BorderLayout.CENTER);

        // Mostrar panel inicial
        mostrarPanel(new VistaGeneralPanel());
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed() || getModel().isRollover()) {
                    g2.setColor(ColorPalette.GOLD_STRONG);
                } else {
                    g2.setColor(ColorPalette.CREAM_LIGHT);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ColorPalette.CREAM_LIGHT);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };

        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setPreferredSize(new Dimension(240, 45));
        button.setMaximumSize(new Dimension(240, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(ColorPalette.FONT_SUBTITLE.deriveFont(Font.PLAIN));
        button.setForeground(ColorPalette.GRAY_MEDIUM_DARK);

        button.addChangeListener(e -> {
            ButtonModel model = button.getModel();
            if (model.isRollover() || model.isPressed()) {
                button.setForeground(Color.WHITE);
                button.setFont(ColorPalette.FONT_SUBTITLE.deriveFont(Font.BOLD));
            } else {
                button.setForeground(ColorPalette.GRAY_MEDIUM_DARK);
                button.setFont(ColorPalette.FONT_SUBTITLE.deriveFont(Font.PLAIN));
            }
        });

        return button;
    }

    private void mostrarPanel(JPanel panel) {
        contenedorCentral.removeAll();
        contenedorCentral.add(panel, BorderLayout.CENTER);
        contenedorCentral.revalidate();
        contenedorCentral.repaint();
    }
}
