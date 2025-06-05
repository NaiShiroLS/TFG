package main.java.almadorada.gui;

import main.java.almadorada.gui.panels.CategoriasPanel;
import main.java.almadorada.gui.panels.PedidosPanel;
import main.java.almadorada.gui.panels.ProductosPanel;
// import main.java.almadorada.gui.panels.StockPanel;
// import main.java.almadorada.gui.panels.EnviosPanel;

import javax.swing.*;
import java.awt.*;

public class VistaGeneral extends JFrame {

    private JPanel contenedorCentral;

    public VistaGeneral() {
        setTitle("Alma Dorada - Administración");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        // Panel de navegación (parte izquierda o superior)
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 1, 5, 5)); // Botones verticales
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnCategorias = new JButton("Categorías");
        JButton btnProductos = new JButton("Productos");
        JButton btnStock = new JButton("Stock");
        JButton btnPedidos = new JButton("Pedidos");

        menuPanel.add(btnCategorias);
        menuPanel.add(btnProductos);
        menuPanel.add(btnStock);
        menuPanel.add(btnPedidos);

        add(menuPanel, BorderLayout.WEST);

        // Panel central donde cargaremos los distintos paneles
        contenedorCentral = new JPanel(new BorderLayout());
        add(contenedorCentral, BorderLayout.CENTER);

        // Eventos de los botones
        btnCategorias.addActionListener(e -> mostrarPanel(new CategoriasPanel()));
        btnProductos.addActionListener(e -> mostrarPanel(new ProductosPanel()));
        // btnStock.addActionListener(e -> mostrarPanel(new StockPanel()));
         btnPedidos.addActionListener(e -> mostrarPanel(new PedidosPanel()));

        // Mostramos por defecto el panel de categorías
        mostrarPanel(new CategoriasPanel());
    }

    private void mostrarPanel(JPanel panel) {
        contenedorCentral.removeAll();
        contenedorCentral.add(panel, BorderLayout.CENTER);
        contenedorCentral.revalidate();
        contenedorCentral.repaint();
    }
}
