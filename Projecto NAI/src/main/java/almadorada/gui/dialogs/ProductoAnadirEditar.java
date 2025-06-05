package main.java.almadorada.gui.dialogs;

import main.java.almadorada.model.Producto;
import main.java.almadorada.model.Categoria; // Importación correcta de tu clase Categoria
import main.java.almadorada.service.CategoriaService; // Importación correcta de tu clase CategoriaService

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Objects; // Añadido para Objects.equals en hayCambiosPendientes

public class ProductoAnadirEditar extends JPanel { // Ahora es un JPanel

    // Componentes del formulario
    private JTextField campoNombre, campoPrecio, campoStock;
    private JTextArea campoDescripcion;
    private JComboBox<Categoria> comboCategoria;
    private JButton btnSubirImagen;
    private JLabel labelImagenActual;
    private JButton btnGuardar, btnCancelar; // Botones para acciones del formulario

    // Estado del formulario
    private Producto productoOriginal; // El producto si estamos en modo edición
    private Producto productoEnEdicion; // Una copia para modificar
    private byte[] imagenSeleccionada;
    private boolean imagenCambiada = false;

    // Servicios
    private final CategoriaService categoriaService;

    // Formato para números
    private final NumberFormat formatoDecimal;

    // Listeners para los botones (serán establecidos por el contenedor)
    private ActionListener guardarListener;
    private ActionListener cancelarListener;


    /**
     * Constructor para añadir un nuevo producto.
     */
    public ProductoAnadirEditar() { // Renamed from ProductoForm()
        this(null); // Llama al constructor de edición con un producto nulo
    }

    /**
     * Constructor para editar un producto existente.
     * @param producto Producto a editar (puede ser null para un nuevo producto)
     */
    public ProductoAnadirEditar(Producto producto) { // Renamed from ProductoForm()
        categoriaService = new CategoriaService();
        formatoDecimal = NumberFormat.getNumberInstance(Locale.US); // Usar punto como separador decimal

        if (producto != null) {
            this.productoOriginal = producto.clone(); // Clonar para no modificar el original directamente
            this.productoEnEdicion = producto.clone();
            this.imagenSeleccionada = producto.getImagen(); // Cargar la imagen existente
        } else {
            this.productoOriginal = null; // Es un nuevo producto
            this.productoEnEdicion = new Producto();
            this.imagenSeleccionada = null;
        }

        initUI();
        cargarCategorias();
        cargarProducto(); // Cargar los datos si es un producto existente
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        panelCampos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        campoNombre = new JTextField(20);
        panelCampos.add(campoNombre, gbc);

        // Fila 1: Descripción
        gbc.gridx = 0; gbc.gridy = 1;
        panelCampos.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        campoDescripcion = new JTextArea(3, 20);
        campoDescripcion.setLineWrap(true);
        campoDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(campoDescripcion);
        panelCampos.add(scrollDescripcion, gbc);

        // Fila 2: Precio
        gbc.gridx = 0; gbc.gridy = 2;
        panelCampos.add(new JLabel("Precio ($):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        campoPrecio = new JTextField(10);
        panelCampos.add(campoPrecio, gbc);

        // Fila 3: Stock
        gbc.gridx = 0; gbc.gridy = 3;
        panelCampos.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        campoStock = new JTextField(10);
        panelCampos.add(campoStock, gbc);

        // Fila 4: Categoría
        gbc.gridx = 0; gbc.gridy = 4;
        panelCampos.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        comboCategoria = new JComboBox<>();
        panelCampos.add(comboCategoria, gbc);

        // Fila 5: Imagen
        gbc.gridx = 0; gbc.gridy = 5;
        panelCampos.add(new JLabel("Imagen:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        JPanel panelImagen = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnSubirImagen = new JButton("Subir Imagen");
        labelImagenActual = new JLabel("Ninguna seleccionada");
        panelImagen.add(btnSubirImagen);
        panelImagen.add(Box.createHorizontalStrut(10)); // Espacio
        panelImagen.add(labelImagenActual);
        panelCampos.add(panelImagen, gbc);

        // Fila 6: Botones de acción (Guardar y Cancelar)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Listeners
        btnSubirImagen.addActionListener(this::subirImagen);
        btnGuardar.addActionListener(e -> {
            if (guardarListener != null) {
                guardarListener.actionPerformed(e);
            }
        });
        btnCancelar.addActionListener(e -> {
            if (cancelarListener != null) {
                cancelarListener.actionPerformed(e);
            }
        });

        // Asegurar que solo se puedan ingresar números en Precio y Stock
        campoPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE) ||
                        (c == '.' && !campoPrecio.getText().contains(".")))) { // Permitir solo un punto decimal
                    evt.consume();
                }
            }
        });

        campoStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    evt.consume();
                }
            }
        });
    }

    private void cargarCategorias() {
        comboCategoria.removeAllItems();
        // Añadir una opción para "Sin categoría"
        comboCategoria.addItem(new Categoria(0, "Sin categoría", "")); // ID 0 para nulo/sin categoría
        List<Categoria> categorias = categoriaService.obtenerTodos();
        for (Categoria c : categorias) {
            comboCategoria.addItem(c);
        }
    }

    /**
     * Carga los datos del producto en los campos del formulario si estamos en modo edición.
     */
    private void cargarProducto() {
        if (productoEnEdicion != null && productoEnEdicion.getId() > 0) { // Si es un producto existente
            campoNombre.setText(productoEnEdicion.getNombre());
            campoDescripcion.setText(productoEnEdicion.getDescripcion());
            campoPrecio.setText(formatoDecimal.format(productoEnEdicion.getPrecio()));
            campoStock.setText(String.valueOf(productoEnEdicion.getStock()));

            // Seleccionar categoría
            if (productoEnEdicion.getIdCategoria() > 0) {
                for (int i = 0; i < comboCategoria.getItemCount(); i++) {
                    Categoria cat = comboCategoria.getItemAt(i);
                    if (cat != null && cat.getId() == productoEnEdicion.getIdCategoria()) {
                        comboCategoria.setSelectedItem(cat);
                        break;
                    }
                }
            } else {
                comboCategoria.setSelectedItem(comboCategoria.getItemAt(0)); // "Sin categoría"
            }

            // Mostrar estado de la imagen
            if (productoEnEdicion.tieneImagen()) {
                labelImagenActual.setText("Imagen cargada");
            } else {
                labelImagenActual.setText("Ninguna seleccionada");
            }
        }
    }

    private void subirImagen(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de Imagen", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                imagenSeleccionada = Files.readAllBytes(selectedFile.toPath());
                labelImagenActual.setText(selectedFile.getName());
                imagenCambiada = true; // Indicar que la imagen ha sido modificada
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al leer la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                imagenSeleccionada = null; // Reiniciar si hay error
                labelImagenActual.setText("Error al cargar imagen");
            }
        }
    }

    /**
     * Valida los campos del formulario y actualiza el objeto productoEnEdicion.
     * @return true si la validación es exitosa y los datos se pueden obtener, false en caso contrario.
     */
    public boolean guardarProducto() {
        String nombre = campoNombre.getText().trim();
        String descripcion = campoDescripcion.getText().trim();
        String precioStr = campoPrecio.getText().trim();
        String stockStr = campoStock.getText().trim();
        Categoria categoriaSeleccionada = (Categoria) comboCategoria.getSelectedItem();

        // Validación
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del producto no puede estar vacío.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripción del producto no puede estar vacía.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        double precio;
        try {
            precio = formatoDecimal.parse(precioStr).doubleValue();
            if (precio <= 0) {
                JOptionPane.showMessageDialog(this, "El precio debe ser un número positivo.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (ParseException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Formato de precio inválido. Use números.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        int stock;
        try {
            stock = Integer.parseInt(stockStr);
            if (stock < 0) {
                JOptionPane.showMessageDialog(this, "El stock no puede ser negativo.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Formato de stock inválido. Use números enteros.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Si la validación es exitosa, actualizar el objeto productoEnEdicion
        productoEnEdicion.setNombre(nombre);
        productoEnEdicion.setDescripcion(descripcion);
        productoEnEdicion.setPrecio(precio);
        productoEnEdicion.setStock(stock);
        productoEnEdicion.setImagen(imagenSeleccionada);
        productoEnEdicion.setIdCategoria(categoriaSeleccionada != null ? categoriaSeleccionada.getId() : 0);
        productoEnEdicion.setNombreCategoria(categoriaSeleccionada != null ? categoriaSeleccionada.getNombre() : null);

        return true; // Indicamos que los datos se han guardado temporalmente en productoEnEdicion
    }

    /**
     * Verifica si hay cambios pendientes en el formulario con respecto al producto original.
     * @return true si hay cambios, false en caso contrario.
     */
    public boolean hayCambiosPendientes() {
        // Si es un producto nuevo y no hay datos ingresados, no hay cambios pendientes para "descartar"
        if (productoOriginal == null) {
            return !campoNombre.getText().trim().isEmpty() ||
                    !campoDescripcion.getText().trim().isEmpty() ||
                    !campoPrecio.getText().trim().isEmpty() ||
                    !campoStock.getText().trim().isEmpty() ||
                    imagenSeleccionada != null; // Si se seleccionó una imagen
        } else {
            // Es edición, comparar con original
            if (!Objects.equals(campoNombre.getText().trim(), productoOriginal.getNombre() != null ? productoOriginal.getNombre() : "")) return true;
            if (!Objects.equals(campoDescripcion.getText().trim(), productoOriginal.getDescripcion() != null ? productoOriginal.getDescripcion() : "")) return true;

            // Comparar precio
            try {
                double precioActual = formatoDecimal.parse(campoPrecio.getText().trim()).doubleValue();
                if (Double.compare(precioActual, productoOriginal.getPrecio()) != 0) return true;
            } catch (ParseException | NumberFormatException e) {
                // Si el formato es inválido, ya es un cambio (o un error, pero lo consideramos un cambio)
                return true;
            }

            // Comparar stock
            try {
                int stockActual = Integer.parseInt(campoStock.getText().trim());
                if (stockActual != productoOriginal.getStock()) return true;
            } catch (NumberFormatException e) {
                // Si el formato es inválido, ya es un cambio (o un error, pero lo consideramos un cambio)
                return true;
            }

            Categoria categoriaActual = (Categoria) comboCategoria.getSelectedItem();
            int idCategoriaActual = (categoriaActual != null) ? categoriaActual.getId() : 0;
            if (idCategoriaActual != productoOriginal.getIdCategoria()) return true;

            // Comparar imagen
            if (imagenCambiada) return true; // Si se seleccionó una nueva imagen
            if (productoOriginal.getImagen() == null && imagenSeleccionada != null) return true; // Si antes no tenía y ahora sí
            if (productoOriginal.getImagen() != null && imagenSeleccionada == null) return true; // Si antes tenía y ahora no
            if (productoOriginal.getImagen() != null && imagenSeleccionada != null && !java.util.Arrays.equals(productoOriginal.getImagen(), imagenSeleccionada)) return true; // Si las imágenes son diferentes

            return false; // No hay cambios
        }
    }


    // Métodos para establecer listeners desde el diálogo/frame contenedor
    public void addGuardarListener(ActionListener listener) {
        this.guardarListener = listener;
    }

    public void addCancelarListener(ActionListener listener) {
        this.cancelarListener = listener;
    }

    // Getter para obtener el producto editado/añadido después de guardar (si es necesario desde el exterior)
    public Producto getProductoGuardado() {
        return productoEnEdicion;
    }
}