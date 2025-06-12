package main.java.almadorada.gui.panels;

import main.java.almadorada.gui.ColorPalette;
import main.java.almadorada.service.ProductoService;
import main.java.almadorada.service.PedidoService;

import javax.swing.*;
import java.awt.*;

public class VistaGeneralPanel extends JPanel {

    private final ProductoService productoService = new ProductoService();
    private final PedidoService pedidoService = new PedidoService();

    public VistaGeneralPanel() {
        setBackground(ColorPalette.WHITE_LIGHT);
        setLayout(new BorderLayout());

        int productosStockBajo = productoService.contarStockBajo();
        int pedidosPendientes = pedidoService.contarPedidosPendientes();
        int totalProductos = productoService.contarTotalProductos();

        // Panel superior con título y subtítulo
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ColorPalette.WHITE_LIGHT);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(60, 0, 30, 0));

        JLabel titulo = new JLabel("Bienvenido Administrador");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 44));
        titulo.setForeground(Color.BLACK);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Panel de control rápido para la gestión de tu tienda");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitulo.setForeground(Color.GRAY);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titulo);
        headerPanel.add(Box.createVerticalStrut(12));
        headerPanel.add(subtitulo);

        // Panel de datos
        JPanel datosPanel = new JPanel(new GridBagLayout());
        datosPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        // Fila 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        datosPanel.add(createInfoBox("Productos en Stock Bajo", String.valueOf(productosStockBajo), ColorPalette.SALMON_INTENSE), gbc);

        gbc.gridx = 1;
        datosPanel.add(createInfoBox("Pedidos Pendientes", String.valueOf(pedidosPendientes), ColorPalette.BROWN_LIGHT), gbc);

        // Fila 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        datosPanel.add(createInfoBox("Total Productos", String.valueOf(totalProductos), ColorPalette.GOLD_STRONG), gbc);

        add(headerPanel, BorderLayout.NORTH);
        add(datosPanel, BorderLayout.CENTER);
    }

    private JPanel createInfoBox(String titulo, String valor, Color colorValor) {
        JPanel boxPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 20;
                int padding = 10;

                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(padding + 5, padding + 5, getWidth() - 2 * padding - 10, getHeight() - 2 * padding - 10, arc, arc);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(padding, padding, getWidth() - 2 * padding - 10, getHeight() - 2 * padding - 10, arc, arc);

                g2.setColor(ColorPalette.GOLD_STRONG);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(padding, padding, getWidth() - 2 * padding - 10, getHeight() - 2 * padding - 10, arc, arc);

                super.paintComponent(g2);
            }
        };

        boxPanel.setOpaque(false);
        boxPanel.setPreferredSize(new Dimension(360, 200));
        boxPanel.setLayout(new GridBagLayout());

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        labelTitulo.setForeground(ColorPalette.BROWN_LIGHT);

        JLabel labelValor = new JLabel(valor);
        labelValor.setFont(new Font("SansSerif", Font.BOLD, 36));
        labelValor.setForeground(colorValor);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(labelTitulo);
        content.add(Box.createVerticalStrut(15));
        content.add(labelValor);

        boxPanel.add(content);

        return boxPanel;
    }
}
