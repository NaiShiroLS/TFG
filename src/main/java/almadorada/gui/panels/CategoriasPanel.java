package main.java.almadorada.gui.panels;

import main.java.almadorada.gui.ColorPalette;
import main.java.almadorada.model.Categoria;
import main.java.almadorada.service.CategoriaService;
import main.java.almadorada.gui.dialogs.CategoriaAnadirEditar;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoriasPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private CategoriaService categoriaService;
    private JComboBox<String> comboBusqueda;

    public CategoriasPanel() {
        categoriaService = new CategoriaService();
        setLayout(new BorderLayout());
        setBackground(ColorPalette.WHITE_LIGHT);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(ColorPalette.WHITE_LIGHT);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titulo = new JLabel("Gestión de Categorías", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 44));
        titulo.setForeground(Color.BLACK);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel panelFiltros = new JPanel(new BorderLayout());
        panelFiltros.setBackground(Color.WHITE);
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        izquierda.setBackground(Color.WHITE);
        JLabel lblFiltro = new JLabel(" Nombre de la Categoría:");
        lblFiltro.setFont(ColorPalette.FONT_NORMAL);
        lblFiltro.setForeground(Color.GRAY);

        comboBusqueda = new JComboBox<>();
        comboBusqueda.addItem("(Seleccionar)");
        comboBusqueda.setPreferredSize(new Dimension(180, 28));
        comboBusqueda.setFont(ColorPalette.FONT_NORMAL);
        comboBusqueda.setBackground(Color.WHITE);
        comboBusqueda.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
        comboBusqueda.setCursor(new Cursor(Cursor.HAND_CURSOR));

        izquierda.add(lblFiltro);
        izquierda.add(Box.createHorizontalStrut(10));
        izquierda.add(comboBusqueda);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        derecha.setBackground(Color.WHITE);
        JButton btnAplicarFiltros = ColorPalette.createSecondaryButton("Aplicar Filtros");
        btnAplicarFiltros.setPreferredSize(new Dimension(130, 30));
        btnAplicarFiltros.setFont(ColorPalette.FONT_BUTTON);
        btnAplicarFiltros.setCursor(new Cursor(Cursor.HAND_CURSOR));

        derecha.add(btnAplicarFiltros);

        panelFiltros.add(izquierda, BorderLayout.WEST);
        panelFiltros.add(derecha, BorderLayout.EAST);
        panelContenido.add(panelFiltros, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Editar", "Eliminar"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 2;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(40);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFont(ColorPalette.FONT_NORMAL);
        tabla.setForeground(ColorPalette.GRAY_DARK);
        tabla.setBackground(ColorPalette.WHITE_LIGHT);
        tabla.setShowGrid(true);
        tabla.setGridColor(ColorPalette.TABLE_GRID);
        tabla.setIntercellSpacing(new Dimension(1, 1));

        DefaultTableCellRenderer centrador = new DefaultTableCellRenderer();
        centrador.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centrador);
        }

        tabla.getTableHeader().setFont(ColorPalette.FONT_BUTTON);
        tabla.getTableHeader().setBackground(ColorPalette.TABLE_HEADER_BG);
        tabla.getTableHeader().setForeground(ColorPalette.GRAY_DARK);
        tabla.getTableHeader().setPreferredSize(new Dimension(100, 45));
        tabla.getTableHeader().setReorderingAllowed(false);

        tabla.getColumn("Editar").setCellRenderer(new ButtonRenderer("Editar"));
        tabla.getColumn("Editar").setCellEditor(new ButtonEditor(new JCheckBox(), "Editar"));
        tabla.getColumn("Eliminar").setCellRenderer(new ButtonRenderer("Eliminar"));
        tabla.getColumn("Eliminar").setCellEditor(new ButtonEditor(new JCheckBox(), "Eliminar"));

        tabla.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(350);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(90);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
        scroll.getViewport().setBackground(Color.WHITE);
        panelContenido.add(scroll, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton btnAnadir = ColorPalette.createPrimaryButton("Añadir Nueva Categoría");
        btnAnadir.setPreferredSize(new Dimension(180, 35));
        btnAnadir.setFont(ColorPalette.FONT_BUTTON);
        btnAnadir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelInferior.add(btnAnadir);
        panelContenido.add(panelInferior, BorderLayout.SOUTH);

        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        add(panelPrincipal, BorderLayout.CENTER);

        btnAplicarFiltros.addActionListener(e -> aplicarFiltro());
        btnAnadir.addActionListener(e -> abrirDialogoNuevaCategoria());

        cargarCategorias();
        cargarComboBox();
    }

    private void cargarCategorias() {
        modelo.setRowCount(0);
        List<Categoria> lista = categoriaService.obtenerTodos();
        for (Categoria c : lista) {
            modelo.addRow(new Object[]{c.getId(), c.getNombre(), "Editar", "Eliminar"});
        }
    }

    private void cargarComboBox() {
        comboBusqueda.removeAllItems();
        comboBusqueda.addItem("(Seleccionar)");
        List<Categoria> lista = categoriaService.obtenerTodos();
        for (Categoria c : lista) {
            comboBusqueda.addItem(c.getNombre());
        }
    }

    private void aplicarFiltro() {
        String seleccionado = (String) comboBusqueda.getSelectedItem();
        if (seleccionado == null || seleccionado.equals("(Seleccionar)")) {
            cargarCategorias();
            return;
        }
        List<Categoria> listaFiltrada = categoriaService.obtenerTodos().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(seleccionado))
                .toList();

        modelo.setRowCount(0);
        for (Categoria c : listaFiltrada) {
            modelo.addRow(new Object[]{c.getId(), c.getNombre(), "Editar", "Eliminar"});
        }
    }

    private void abrirDialogoNuevaCategoria() {
        CategoriaAnadirEditar dialog = new CategoriaAnadirEditar((Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        if (dialog.isGuardado()) {
            cargarCategorias();
            cargarComboBox();
        }
    }

    private void abrirDialogoEditarCategoria(int fila) {
        int id = (int) modelo.getValueAt(fila, 0);
        Categoria existente = categoriaService.buscarPorId(id);
        if (existente != null) {
            CategoriaAnadirEditar dialog = new CategoriaAnadirEditar((Frame) SwingUtilities.getWindowAncestor(this));
            dialog.cargarCategoria(existente);
            dialog.setVisible(true);
            if (dialog.isGuardado()) {
                cargarCategorias();
                cargarComboBox();
            }
        }
    }

    private void eliminarCategoria(int fila) {
        int id = (int) modelo.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar esta categoría?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            categoriaService.eliminar(id);
            cargarCategorias();
            cargarComboBox();
        }
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        private final String text;

        public ButtonRenderer(String text) {
            this.text = text;
            setOpaque(true);
            setFocusPainted(false);
            setBorder(new LineBorder(Color.DARK_GRAY, 1, true));
            setFont(new Font("Segoe UI", Font.BOLD, 11));
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            setText(text);
            if (text.equals("Editar")) {
                setBackground(ColorPalette.BROWN_LIGHT);
            } else {
                setBackground(ColorPalette.SALMON_INTENSE);
            }
            setForeground(Color.WHITE);
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private final String label;
        private boolean isPushed;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox, String text) {
            super(checkBox);
            this.label = text;
            button = new JButton();
            button.setOpaque(true);
            button.setFocusPainted(false);
            button.setFont(new Font("Segoe UI", Font.BOLD, 11));
            button.setBorder(new LineBorder(Color.DARK_GRAY, 1, true));
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            button.setText(label);
            if (label.equals("Editar")) {
                button.setBackground(ColorPalette.BROWN_LIGHT);
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(ColorPalette.SALMON_INTENSE);
                button.setForeground(Color.WHITE);
            }
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                SwingUtilities.invokeLater(() -> {
                    if (label.equals("Editar")) {
                        abrirDialogoEditarCategoria(currentRow);
                    } else {
                        eliminarCategoria(currentRow);
                    }
                });
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}