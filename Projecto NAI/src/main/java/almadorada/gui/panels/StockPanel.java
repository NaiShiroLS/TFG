package main.java.almadorada.gui.panels;

import main.java.almadorada.model.Categoria;
import main.java.almadorada.service.CategoriaService;
import main.java.almadorada.gui.dialogs.CategoriaAnadirEditar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StockPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private CategoriaService categoriaService;

    public StockPanel() {
        categoriaService = new CategoriaService();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Gestión de Stock"));

        // Tabla de categorías
        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre Producto", "Categoría", "Stock Actual"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabla no editable directamente
            }
        };

        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // Botones inferiores
        JPanel panelBotones = new JPanel();
        JButton btnExportar = new JButton("Exportar");
        JButton btnImprimir = new JButton("Imprimir");
        panelBotones.add(btnExportar);
        panelBotones.add(btnImprimir);

        add(panelBotones, BorderLayout.SOUTH);


    }




}
