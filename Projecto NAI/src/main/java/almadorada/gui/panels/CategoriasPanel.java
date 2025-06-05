package main.java.almadorada.gui.panels;

import main.java.almadorada.model.Categoria;
import main.java.almadorada.service.CategoriaService;
import main.java.almadorada.gui.dialogs.CategoriaAnadirEditar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoriasPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private CategoriaService categoriaService;

    public CategoriasPanel() {
        categoriaService = new CategoriaService();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Gestión de Categorías"));

        // Tabla de categorías
        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción"}, 0) {
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
        JButton btnAñadir = new JButton("Añadir");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnAñadir);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);

        btnAñadir.addActionListener(e -> abrirDialogoAñadir());
        btnEliminar.addActionListener(e -> eliminarSeleccionada());

        cargarCategorias();
    }

    private void cargarCategorias() {
        modelo.setRowCount(0); // limpiar
        List<Categoria> lista = categoriaService.obtenerTodos();
        for (Categoria c : lista) {
            modelo.addRow(new Object[]{c.getId(), c.getNombre(), c.getDescripcion()});
        }
    }

    private void abrirDialogoAñadir() {
        CategoriaAnadirEditar dialog = new CategoriaAnadirEditar((Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        if (dialog.isGuardado()) {
            categoriaService.insertar(dialog.getCategoria());
            cargarCategorias();
        }
    }

    private void eliminarSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            int id = (int) modelo.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que deseas eliminar esta categoría?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                categoriaService.eliminar(id);
                cargarCategorias();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una categoría primero.");
        }
    }
}
