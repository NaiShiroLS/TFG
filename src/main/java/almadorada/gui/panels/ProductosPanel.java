package main.java.almadorada.gui.panels;

import main.java.almadorada.gui.ColorPalette;
import main.java.almadorada.model.Producto;
import main.java.almadorada.service.ProductoService;
import main.java.almadorada.gui.dialogs.ProductoAnadirEditar;
import main.java.almadorada.model.Categoria;
import main.java.almadorada.service.CategoriaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ProductosPanel extends JPanel {
    private JTable tabla;
    private DefaultTableModel modelo;
    private final ProductoService productoService = new ProductoService();
    private final CategoriaService categoriaService = new CategoriaService();

    private JTextField campoBusquedaNombre;
    private JComboBox<Categoria> comboFiltroCategoria;
    private JButton btnBuscar;

    public ProductosPanel() {
        setLayout(new BorderLayout());
        setBackground(ColorPalette.WHITE_LIGHT);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(ColorPalette.WHITE_LIGHT);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titulo = new JLabel("Gestión de Productos", SwingConstants.CENTER);
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

        // Panel de filtros
        JPanel panelFiltros = new JPanel(new BorderLayout());
        panelFiltros.setBackground(Color.WHITE);
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        izquierda.setBackground(Color.WHITE);

        JLabel lblBuscar = new JLabel("Buscar por nombre:");
        lblBuscar.setFont(ColorPalette.FONT_NORMAL);
        lblBuscar.setForeground(ColorPalette.GRAY_DARK);
        izquierda.add(lblBuscar);

        campoBusquedaNombre = new JTextField(15);
        campoBusquedaNombre.setFont(ColorPalette.FONT_NORMAL);
        campoBusquedaNombre.setPreferredSize(new Dimension(180, 28));
        campoBusquedaNombre.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
        campoBusquedaNombre.setText("");
        campoBusquedaNombre.setForeground(Color.GRAY);
        izquierda.add(campoBusquedaNombre);

        izquierda.add(Box.createHorizontalStrut(20)); // Espacio entre campos de filtro

        JLabel lblCategoria = new JLabel("Filtrar por Categoría:");
        lblCategoria.setFont(ColorPalette.FONT_NORMAL);
        lblCategoria.setForeground(ColorPalette.GRAY_DARK);
        izquierda.add(lblCategoria);

        comboFiltroCategoria = new JComboBox<>();
        comboFiltroCategoria.setFont(ColorPalette.FONT_NORMAL);
        comboFiltroCategoria.setPreferredSize(new Dimension(180, 28));
        comboFiltroCategoria.setBackground(Color.WHITE);
        comboFiltroCategoria.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
        comboFiltroCategoria.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Set hand cursor
        cargarCategoriasFiltro();
        izquierda.add(comboFiltroCategoria);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        derecha.setBackground(Color.WHITE);
        btnBuscar = ColorPalette.createSecondaryButton("Aplicar Filtros");
        btnBuscar.setPreferredSize(new Dimension(130, 30));
        btnBuscar.setFont(ColorPalette.FONT_BUTTON);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        derecha.add(btnBuscar);

        panelFiltros.add(izquierda, BorderLayout.WEST);
        panelFiltros.add(derecha, BorderLayout.EAST);
        panelContenido.add(panelFiltros, BorderLayout.NORTH);

        // Configuración de la tabla
        setupTable();
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
        scroll.getViewport().setBackground(Color.WHITE);
        panelContenido.add(scroll, BorderLayout.CENTER);

        // Panel inferior con botón Añadir
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton btnAnadir = ColorPalette.createPrimaryButton("Añadir Nuevo Producto");
        btnAnadir.setPreferredSize(new Dimension(180, 35));
        btnAnadir.setFont(ColorPalette.FONT_BUTTON);
        btnAnadir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelInferior.add(btnAnadir);
        panelContenido.add(panelInferior, BorderLayout.SOUTH);

        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        add(panelPrincipal, BorderLayout.CENTER);

        // Listeners
        btnAnadir.addActionListener(e -> abrirDialogoAnadir());
        btnBuscar.addActionListener(e -> filterProducts());

        // Placeholder behavior
        campoBusquedaNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (campoBusquedaNombre.getText().equals("Escribe el nombre del producto")) {
                    campoBusquedaNombre.setText("");
                    campoBusquedaNombre.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (campoBusquedaNombre.getText().isEmpty()) {
                    campoBusquedaNombre.setText("Escribe el nombre del producto");
                    campoBusquedaNombre.setForeground(Color.GRAY);
                }
            }
        });

        // Carga inicial
        cargarProductos();
    }

    private void setupTable() {
        modelo = new DefaultTableModel(new Object[]{
                "ID", "Imagen", "Nombre", "Categoría", "Precio", "Stock", "Editar", "Eliminar"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 6;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 6 || column == 7) {
                    return JButton.class;
                }
                return super.getColumnClass(column);
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

        tabla.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(60);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(7).setPreferredWidth(80);

        tabla.getColumn("Editar").setCellRenderer(new ButtonRenderer("Editar", ColorPalette.BROWN_LIGHT));
        tabla.getColumn("Editar").setCellEditor(new ButtonEditor(new JCheckBox(), this::editarProducto, "Editar", ColorPalette.BROWN_LIGHT)); // Changed to JCheckBox for consistency
        tabla.getColumn("Eliminar").setCellRenderer(new ButtonRenderer("Eliminar", ColorPalette.SALMON_INTENSE)); // Changed to SALMON_INTENSE for consistency
        tabla.getColumn("Eliminar").setCellEditor(new ButtonEditor(new JCheckBox(), this::eliminarSeleccionado, "Eliminar", ColorPalette.SALMON_INTENSE)); // Changed to JCheckBox and SALMON_INTENSE
    }

    private void cargarCategoriasFiltro() {
        comboFiltroCategoria.removeAllItems();
        comboFiltroCategoria.addItem(null);
        List<Categoria> categorias = categoriaService.obtenerTodos();
        for (Categoria c : categorias) {
            comboFiltroCategoria.addItem(c);
        }
    }

    private void cargarProductos() {
        modelo.setRowCount(0);
        List<Producto> productos = productoService.obtenerTodos();
        for (Producto p : productos) {
            String imagenTexto = (p.getImagen() != null) ? "v" : "x";
            modelo.addRow(new Object[]{
                    p.getId(),
                    imagenTexto,
                    p.getNombre(),
                    p.getNombreCategoria(),
                    String.format("%.2f €", p.getPrecio()),
                    p.getStock(),
                    "Editar",
                    "Eliminar"
            });
        }
    }


    private void filterProducts() {
        modelo.setRowCount(0);
        String nombre = campoBusquedaNombre.getText().trim();
        if (nombre.equals("Escribe el nombre del producto")) {
            nombre = "";
        }

        Categoria categoriaSeleccionada = (Categoria) comboFiltroCategoria.getSelectedItem();
        Integer idCategoria = (categoriaSeleccionada != null) ? categoriaSeleccionada.getId() : null;

        List<Producto> productos = productoService.obtenerTodos();
        for (Producto p : productos) {
            boolean matchesName = nombre.isEmpty() || p.getNombre().toLowerCase().contains(nombre.toLowerCase());
            boolean matchesCategory = (idCategoria == null) || (p.getIdCategoria() == idCategoria);

            if (matchesName && matchesCategory) {
                String imagenTexto = (p.getImagen() != null) ? "v" : "x";
                modelo.addRow(new Object[]{
                        p.getId(),
                        imagenTexto,
                        p.getNombre(),
                        p.getNombreCategoria(),
                        String.format("%.2f €", p.getPrecio()),
                        p.getStock(),
                        "Editar",
                        "Eliminar"
                });
            }
        }
    }


    private void abrirDialogoAnadir() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Añadir Producto", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(550, 600);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

        ProductoAnadirEditar productoForm = new ProductoAnadirEditar();
        dialog.add(productoForm, BorderLayout.CENTER);

        productoForm.addGuardarListener(e -> {
            if (productoForm.guardarProducto()) {
                Producto nuevoProducto = productoForm.getProductoGuardado();
                if (productoService.validarNombreDuplicado(nuevoProducto.getNombre(), 0)) {
                    JOptionPane.showMessageDialog(dialog, "Ya existe un producto con el mismo nombre.", "Nombre Duplicado", JOptionPane.WARNING_MESSAGE);
                } else {
                    productoService.insertar(nuevoProducto);
                    cargarProductos();
                    dialog.dispose();
                }
            }
        });

        productoForm.addCancelarListener(e -> {
            if (productoForm.hayCambiosPendientes()) {
                int confirm = JOptionPane.showConfirmDialog(dialog,
                        "Hay cambios sin guardar. ¿Deseas descartarlos?",
                        "Confirmar Cancelar",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dialog.dispose();
                }
            } else {
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    private void editarProducto(int rowIndex) {
        int idProducto = (int) modelo.getValueAt(rowIndex, 0);
        Optional<Producto> productoOpt = productoService.obtenerPorId(idProducto);

        if (productoOpt.isPresent()) {
            Producto productoAEditar = productoOpt.get();

            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Producto", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(550, 600);
            dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

            ProductoAnadirEditar productoForm = new ProductoAnadirEditar(productoAEditar);
            dialog.add(productoForm, BorderLayout.CENTER);

            productoForm.addGuardarListener(e -> {
                if (productoForm.guardarProducto()) {
                    Producto productoActualizado = productoForm.getProductoGuardado();
                    if (productoService.validarNombreDuplicado(productoActualizado.getNombre(), productoActualizado.getId())) {
                        JOptionPane.showMessageDialog(dialog, "Ya existe un producto con el mismo nombre.", "Nombre Duplicado", JOptionPane.WARNING_MESSAGE);
                    } else {
                        productoService.actualizar(productoActualizado);
                        cargarProductos();
                        dialog.dispose();
                    }
                }
            });

            productoForm.addCancelarListener(e -> {
                if (productoForm.hayCambiosPendientes()) {
                    int confirm = JOptionPane.showConfirmDialog(dialog,
                            "Hay cambios sin guardar. ¿Deseas descartarlos?",
                            "Confirmar Cancelar",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        dialog.dispose();
                    }
                } else {
                    dialog.dispose();
                }
            });

            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Producto no encontrado para editar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarSeleccionado(int rowIndex) {
        int idProducto = (int) modelo.getValueAt(rowIndex, 0);
        String nombreProducto = (String) modelo.getValueAt(rowIndex, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de eliminar el producto \"" + nombreProducto + "\"?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            productoService.eliminar(idProducto);
            cargarProductos();
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        private final String text;
        private Color buttonColor;

        public ButtonRenderer(String text, Color color) {
            this.text = text;
            this.buttonColor = color;
            setOpaque(true);
            setFocusPainted(false);
            setBorder(new javax.swing.border.LineBorder(Color.DARK_GRAY, 1, true)); // Changed to full path
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            setForeground(Color.WHITE); // Set foreground to white for all buttons

        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            setText(text);
            setBackground(buttonColor);
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private final String label;
        private boolean isPushed;
        private int currentRow;
        private Consumer<Integer> action;
        private Color buttonColor;

        public ButtonEditor(JCheckBox checkBox, Consumer<Integer> action, String text, Color color) {
            super(checkBox);
            this.label = text;
            this.action = action;
            this.buttonColor = color;

            button = new JButton();
            button.setOpaque(true);
            button.setFocusPainted(false);
            button.setFont(new Font("Segoe UI", Font.BOLD, 11));
            button.setBorder(new javax.swing.border.LineBorder(Color.DARK_GRAY, 1, true)); // Changed to full path
            button.setForeground(Color.WHITE); // Set foreground to white
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentRow = row;
            button.setText(label);
            button.setBackground(buttonColor);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                SwingUtilities.invokeLater(() -> action.accept(currentRow));
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}