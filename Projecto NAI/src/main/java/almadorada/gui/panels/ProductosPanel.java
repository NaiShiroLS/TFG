package main.java.almadorada.gui.panels;

import main.java.almadorada.model.Producto;
import main.java.almadorada.service.ProductoService;
import main.java.almadorada.gui.dialogs.ProductoAnadirEditar; // Updated import
import main.java.almadorada.model.Categoria; // Necesario para el JComboBox en ProductoForm
import main.java.almadorada.service.CategoriaService; // Necesario para el JComboBox en ProductoForm

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor; // Importar TableCellEditor
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer; // Para el ActionListener de los botones de la tabla

public class ProductosPanel extends JPanel {
    private JTable tabla;
    private DefaultTableModel modelo;
    private ProductoService productoService;
    private CategoriaService categoriaService; // Para cargar categorías en ProductoForm

    // Componentes de búsqueda y filtro
    private JTextField campoBusquedaNombre;
    private JComboBox<Categoria> comboFiltroCategoria;
    private JButton btnBuscar;
    private JButton btnLimpiarFiltros;

    public ProductosPanel() {
        setLayout(new BorderLayout());
        productoService = new ProductoService();
        categoriaService = new CategoriaService(); // Inicializar CategoriaService

        // Panel superior para la barra de búsqueda y filtros (como en la imagen)
        JPanel panelControlesSuperiores = new JPanel(new GridBagLayout());
        panelControlesSuperiores.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // "Buscar por nombre"
        gbc.gridx = 0; gbc.gridy = 0;
        panelControlesSuperiores.add(new JLabel("Buscar por nombre:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        campoBusquedaNombre = new JTextField(20);
        panelControlesSuperiores.add(campoBusquedaNombre, gbc);

        // "Filtrar por categoría"
        gbc.gridx = 2; gbc.gridy = 0;
        panelControlesSuperiores.add(new JLabel("Filtrar por categoría:"), gbc);

        gbc.gridx = 3; gbc.gridy = 0;
        comboFiltroCategoria = new JComboBox<>();
        cargarCategoriasFiltro(); // Cargar categorías en el combo de filtro
        panelControlesSuperiores.add(comboFiltroCategoria, gbc);

        // Botones de búsqueda y limpiar
        gbc.gridx = 4; gbc.gridy = 0;
        btnBuscar = new JButton("Buscar");
        panelControlesSuperiores.add(btnBuscar, gbc);

        gbc.gridx = 5; gbc.gridy = 0;
        btnLimpiarFiltros = new JButton("Limpiar Filtros");
        panelControlesSuperiores.add(btnLimpiarFiltros, gbc);

        add(panelControlesSuperiores, BorderLayout.NORTH);


        // Configuración de la tabla
        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción", "Precio", "Stock", "Categoría", "Imagen", "Editar", "Eliminar"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo las columnas "Editar" y "Eliminar" son editables (para que el botón funcione)
                return column == getColumnCount() - 1 || column == getColumnCount() - 2;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == getColumnCount() - 1 || column == getColumnCount() - 2) {
                    return JButton.class; // Para que el renderizador/editor de botón funcione
                }
                if (column == 6) { // Columna de Imagen (índice 6)
                    return String.class; // Para mostrar un String "Sí" o "No"
                }
                return super.getColumnClass(column);
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25); // Ajustar altura de fila para los botones
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // Asignar renderizadores y editores de botón
        ButtonEditor editorEditar = new ButtonEditor(new JTextField(), row -> editarProducto(row), "Editar");
        ButtonRenderer rendererEditar = new ButtonRenderer("Editar");
        tabla.getColumnModel().getColumn(modelo.getColumnCount() - 2).setCellRenderer(rendererEditar);
        tabla.getColumnModel().getColumn(modelo.getColumnCount() - 2).setCellEditor(editorEditar);


        ButtonEditor editorEliminar = new ButtonEditor(new JTextField(), row -> eliminarSeleccionado(row), "Eliminar");
        ButtonRenderer rendererEliminar = new ButtonRenderer("Eliminar");
        tabla.getColumnModel().getColumn(modelo.getColumnCount() - 1).setCellRenderer(rendererEliminar);
        tabla.getColumnModel().getColumn(modelo.getColumnCount() - 1).setCellEditor(editorEliminar);

        // Ajuste de ancho de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150); // Nombre
        tabla.getColumnModel().getColumn(2).setPreferredWidth(200); // Descripción
        tabla.getColumnModel().getColumn(3).setPreferredWidth(80);  // Precio
        tabla.getColumnModel().getColumn(4).setPreferredWidth(60);  // Stock
        tabla.getColumnModel().getColumn(5).setPreferredWidth(100); // Categoría
        tabla.getColumnModel().getColumn(6).setPreferredWidth(70);  // Imagen
        tabla.getColumnModel().getColumn(7).setPreferredWidth(80);  // Editar
        tabla.getColumnModel().getColumn(8).setPreferredWidth(80);  // Eliminar


        // Panel inferior para el botón "Añadir Producto"
        JPanel panelBotonesInferiores = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAnadir = new JButton("Añadir Producto");
        panelBotonesInferiores.add(btnAnadir);
        add(panelBotonesInferiores, BorderLayout.SOUTH);

        // Listeners
        btnAnadir.addActionListener(e -> abrirDialogoAnadir());
        btnBuscar.addActionListener(e -> filterProducts());
        btnLimpiarFiltros.addActionListener(e -> {
            campoBusquedaNombre.setText("");
            comboFiltroCategoria.setSelectedItem(null); // O el primer elemento si siempre debe haber uno
            cargarProductos(); // Volver a cargar todos los productos
        });

        cargarProductos();
    }

    private void cargarCategoriasFiltro() {
        comboFiltroCategoria.removeAllItems();
        comboFiltroCategoria.addItem(null); // Opción para no filtrar por categoría (mostrar "Sin categoría" o vacío)
        List<Categoria> categorias = categoriaService.obtenerTodos();
        for (Categoria c : categorias) {
            comboFiltroCategoria.addItem(c);
        }
    }

    private void cargarProductos() {
        modelo.setRowCount(0); // Limpiar la tabla
        List<Producto> productos = productoService.obtenerTodos();
        for (Producto p : productos) {
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getDescripcion(),
                    String.format("%.2f", p.getPrecio()), // Formato de precio
                    p.getStock(),
                    p.getNombreCategoria(), // Nombre de la categoría
                    p.tieneImagen() ? "Sí" : "No", // Mostrar "Sí" o "No" para la imagen
                    "Editar", // Texto del botón
                    "Eliminar" // Texto del botón
            });
        }
    }

    private void filterProducts() {
        modelo.setRowCount(0); // Limpiar la tabla
        String nombre = campoBusquedaNombre.getText().trim();
        Categoria categoriaSeleccionada = (Categoria) comboFiltroCategoria.getSelectedItem();
        Integer idCategoria = (categoriaSeleccionada != null) ? categoriaSeleccionada.getId() : null;

        List<Producto> productos = productoService.obtenerTodos(); // Obtener todos y filtrar en memoria por simplicidad
        for (Producto p : productos) {
            boolean matchesName = nombre.isEmpty() || p.getNombre().toLowerCase().contains(nombre.toLowerCase());
            boolean matchesCategory = (idCategoria == null) || (p.getIdCategoria() == idCategoria);

            if (matchesName && matchesCategory) {
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getNombre(),
                        p.getDescripcion(),
                        String.format("%.2f", p.getPrecio()),
                        p.getStock(),
                        p.getNombreCategoria(),
                        p.tieneImagen() ? "Sí" : "No",
                        "Editar",
                        "Eliminar"
                });
            }
        }
    }


    private void abrirDialogoAnadir() {
        // Crear un JDialog para alojar el formulario
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Añadir Producto", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(550, 600); // Tamaño adecuado para el formulario
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

        ProductoAnadirEditar productoForm = new ProductoAnadirEditar(); // Updated class name
        dialog.add(productoForm, BorderLayout.CENTER);

        // Añadir listeners a los botones del formulario
        productoForm.addGuardarListener(e -> {
            if (productoForm.guardarProducto()) { // guardarProducto() devuelve true si la validación es exitosa y se obtienen los datos
                Producto nuevoProducto = productoForm.getProductoGuardado();
                // Validar duplicados ANTES de insertar
                if (productoService.validarNombreDuplicado(nuevoProducto.getNombre(), 0)) { // 0 para indicar que es un nuevo producto
                    JOptionPane.showMessageDialog(dialog, "Ya existe un producto con el mismo nombre.", "Nombre Duplicado", JOptionPane.WARNING_MESSAGE);
                } else {
                    productoService.insertar(nuevoProducto);
                    cargarProductos(); // Recargar la tabla
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

            ProductoAnadirEditar productoForm = new ProductoAnadirEditar(productoAEditar); // Updated class name
            dialog.add(productoForm, BorderLayout.CENTER);

            productoForm.addGuardarListener(e -> {
                if (productoForm.guardarProducto()) {
                    Producto productoActualizado = productoForm.getProductoGuardado();
                    // Validar duplicados excluyendo el propio ID del producto
                    if (productoService.validarNombreDuplicado(productoActualizado.getNombre(), productoActualizado.getId())) {
                        JOptionPane.showMessageDialog(dialog, "Ya existe otro producto con el mismo nombre.", "Nombre Duplicado", JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void eliminarSeleccionado(int rowIndex) {
        int idProducto = (int) modelo.getValueAt(rowIndex, 0);
        String nombreProducto = (String) modelo.getValueAt(rowIndex, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar el producto \"" + nombreProducto + "\"?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            productoService.eliminar(idProducto);
            cargarProductos(); // Recargar la tabla después de eliminar
        }
    }

    // ============== CLASES INTERNAS PARA BOTONES EN LA TABLA ==============
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setText(text);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private Consumer<Integer> action; // Usa Consumer para pasar la fila al action
        private int currentRow; // Almacena la fila actual para la acción

        public ButtonEditor(JTextField textField, Consumer<Integer> action, String buttonText) {
            super(textField);
            this.action = action;
            button = new JButton();
            button.setOpaque(true);
            // El actionListener del botón se encarga de llamar a fireEditingStopped()
            // para que la tabla sepa que la edición ha terminado después de la acción.
            button.addActionListener(e -> {
                fireEditingStopped();
            });
            label = buttonText; // Set initial label for the button
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(UIManager.getColor("Button.background"));
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            currentRow = row; // Guardar la fila actual
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Ejecutar la acción cuando el botón es presionado
                action.accept(currentRow);
            }
            isPushed = false;
            return label; // Devolver el texto del botón
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false; // Reset isPushed to false before stopping
            return super.stopCellEditing();
        }

        // Ya no es necesario override fireEditingStopped() con la nueva lógica del ActionListener
    }
}