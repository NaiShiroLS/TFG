package main.java.almadorada.gui.dialogs;

import main.java.almadorada.gui.ColorPalette;
import main.java.almadorada.model.Producto;
import main.java.almadorada.model.Categoria;
import main.java.almadorada.service.CategoriaService;

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
import java.util.Objects;

public class ProductoAnadirEditar extends JPanel {

    // Componentes del formulario
    private JTextField campoNombre, campoPrecio, campoStock;
    private JTextArea campoDescripcion;
    private JComboBox<Categoria> comboCategoria;
    private JButton btnSubirImagen;
    private JLabel labelImagenActual;
    private JButton btnGuardar, btnCancelar;

    // Estado del formulario
    private Producto productoOriginal;
    private Producto productoEnEdicion;
    private byte[] imagenSeleccionada;
    private boolean imagenCambiada = false;

    // Servicios
    private final CategoriaService categoriaService;

    // Formato para números
    private final NumberFormat formatoDecimal;

    // Listeners para los botones
    private ActionListener guardarListener;
    private ActionListener cancelarListener;

    // Define a consistent width for input fields
    private static final int INPUT_FIELD_WIDTH = 160;
    private static final int INPUT_FIELD_HEIGHT = 35;
    private static final int TEXT_AREA_HEIGHT = 80;
    private static final int IMAGE_CONTAINER_SIZE = 160;

    public ProductoAnadirEditar() {
        this(null);
    }

    public ProductoAnadirEditar(Producto producto) {
        categoriaService = new CategoriaService();
        formatoDecimal = NumberFormat.getNumberInstance(Locale.US);

        if (producto != null) {
            this.productoOriginal = producto.clone();
            this.productoEnEdicion = producto.clone();
            this.imagenSeleccionada = producto.getImagen();
        } else {
            this.productoOriginal = null;
            this.productoEnEdicion = new Producto();
            this.imagenSeleccionada = null;
        }

        initUI();
        cargarCategorias();
        cargarProducto();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(ColorPalette.WHITE_LIGHT);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Título
        JLabel titulo = new JLabel(productoOriginal == null ? "Nuevo Producto" : "Editar Producto");
        titulo.setFont(ColorPalette.FONT_TITLE);
        titulo.setForeground(ColorPalette.GRAY_DARK);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(new EmptyBorder(0, 0, 30, 0));
        add(titulo, BorderLayout.NORTH);

        // Panel principal del formulario para las dos columnas, centrado
        JPanel mainContentPanel = new JPanel(new GridBagLayout());
        mainContentPanel.setBackground(ColorPalette.WHITE_LIGHT);
        add(mainContentPanel, BorderLayout.CENTER);

        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(10, 20, 10, 20);
        gbcMain.fill = GridBagConstraints.BOTH; // Keep fill for now, but adjust weighty
        gbcMain.anchor = GridBagConstraints.NORTH;
        gbcMain.weightx = 1.0;
        gbcMain.weighty = 0.0; // Set weighty to 0.0 for main columns so they don't stretch excessively

        // --- Left Column Panel ---
        JPanel leftColumnPanel = new JPanel(new GridBagLayout());
        leftColumnPanel.setBackground(ColorPalette.WHITE_LIGHT);

        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.fill = GridBagConstraints.HORIZONTAL;
        gbcLeft.weightx = 1.0;
        gbcLeft.gridx = 0;
        gbcLeft.gridy = GridBagConstraints.RELATIVE;
        gbcLeft.anchor = GridBagConstraints.WEST;
        gbcLeft.insets = new Insets(0, 0, 10, 0); // Default bottom padding for sections

        // Campo: Nombre del Producto
        leftColumnPanel.add(crearCampoFormulario("Nombre del Producto", campoNombre = new JTextField()), gbcLeft);
        campoNombre.setFont(ColorPalette.FONT_NORMAL);
        campoNombre.putClientProperty("JTextField.placeholderText", "Ej: Bolso de Noche Gala");

        // Sección: Imagen del Producto
        JPanel imageSectionPanel = new JPanel(new GridBagLayout());
        imageSectionPanel.setBackground(ColorPalette.WHITE_LIGHT);
        imageSectionPanel.setBorder(new EmptyBorder(10, 0, 0, 0)); // Top padding for this section

        GridBagConstraints gbcImage = new GridBagConstraints();
        gbcImage.gridx = 0;
        gbcImage.fill = GridBagConstraints.HORIZONTAL;
        gbcImage.weightx = 1.0;
        gbcImage.anchor = GridBagConstraints.WEST;

        // Etiqueta
        gbcImage.gridy = 0;
        gbcImage.insets = new Insets(0, 0, 5, 0);
        JLabel labelImagen = new JLabel("Imagen del Producto");
        labelImagen.setFont(ColorPalette.FONT_NORMAL);
        labelImagen.setForeground(ColorPalette.GRAY_DARK);
        imageSectionPanel.add(labelImagen, gbcImage);

        // Contenedor blanco para imagen
        gbcImage.gridy = 1;
        gbcImage.insets = new Insets(0, 0, 10, 0);
        JPanel panelImagenContainer = new JPanel(new BorderLayout());
        panelImagenContainer.setBackground(Color.WHITE);
        panelImagenContainer.setBorder(BorderFactory.createCompoundBorder(
                new ColorPalette.RoundedLineBorder(10, ColorPalette.GRAY_VERY_LIGHT),
                new EmptyBorder(15, 15, 15, 15)
        ));
        panelImagenContainer.setPreferredSize(new Dimension(IMAGE_CONTAINER_SIZE, IMAGE_CONTAINER_SIZE));
        panelImagenContainer.setMinimumSize(new Dimension(IMAGE_CONTAINER_SIZE, IMAGE_CONTAINER_SIZE));

        labelImagenActual = new JLabel("Imagen del producto", SwingConstants.CENTER);
        labelImagenActual.setFont(ColorPalette.FONT_NORMAL);
        labelImagenActual.setForeground(ColorPalette.GRAY_MEDIUM_DARK);
        labelImagenActual.setHorizontalAlignment(SwingConstants.CENTER);
        labelImagenActual.setVerticalAlignment(SwingConstants.CENTER);

        panelImagenContainer.add(labelImagenActual, BorderLayout.CENTER);
        imageSectionPanel.add(panelImagenContainer, gbcImage);

        // Botón subir imagen
        gbcImage.gridy = 2;
        gbcImage.insets = new Insets(0, 0, 0, 0);
        JPanel panelBotonImagen = new JPanel(new BorderLayout());
        panelBotonImagen.setBackground(ColorPalette.WHITE_LIGHT);
        btnSubirImagen = ColorPalette.createPrimaryButton("Subir imagen");
        btnSubirImagen.setFont(ColorPalette.FONT_BUTTON);
        btnSubirImagen.setPreferredSize(new Dimension(INPUT_FIELD_WIDTH, INPUT_FIELD_HEIGHT));
        btnSubirImagen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelBotonImagen.add(btnSubirImagen, BorderLayout.CENTER);
        imageSectionPanel.add(panelBotonImagen, gbcImage);

        // Agregar sección imagen a la columna izquierda
        gbcLeft.insets = new Insets(0, 0, 0, 0); // No bottom inset for the last item in the column if it's a section
        leftColumnPanel.add(imageSectionPanel, gbcLeft);

        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        mainContentPanel.add(leftColumnPanel, gbcMain);

        // --- Right Column Panel ---
        JPanel rightColumnPanel = new JPanel(new GridBagLayout());
        rightColumnPanel.setBackground(ColorPalette.WHITE_LIGHT);

        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.fill = GridBagConstraints.HORIZONTAL;
        gbcRight.weightx = 1.0;
        gbcRight.gridx = 0;
        gbcRight.gridy = GridBagConstraints.RELATIVE;
        gbcRight.anchor = GridBagConstraints.WEST;
        gbcRight.insets = new Insets(0, 0, 10, 0); // Default bottom padding for sections

        // Description field (label + text area)
        JPanel descPanel = new JPanel(new GridBagLayout());
        descPanel.setBackground(ColorPalette.WHITE_LIGHT);
        GridBagConstraints gbcDesc = new GridBagConstraints();
        gbcDesc.gridx = 0;
        gbcDesc.gridy = GridBagConstraints.RELATIVE;
        gbcDesc.anchor = GridBagConstraints.WEST;
        gbcDesc.fill = GridBagConstraints.HORIZONTAL;
        gbcDesc.weightx = 1.0;
        gbcDesc.insets = new Insets(0, 0, 5, 0); // Inset for label

        JLabel labelDesc = new JLabel("Descripción");
        labelDesc.setFont(ColorPalette.FONT_NORMAL);
        labelDesc.setForeground(ColorPalette.GRAY_DARK);
        descPanel.add(labelDesc, gbcDesc);

        campoDescripcion = new JTextArea(4, 0);
        campoDescripcion.setFont(ColorPalette.FONT_NORMAL);
        campoDescripcion.setLineWrap(true);
        campoDescripcion.setWrapStyleWord(true);
        campoDescripcion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT),
                new EmptyBorder(8, 8, 8, 8)
        ));
        campoDescripcion.putClientProperty("JTextArea.placeholderText", "Descripción detallada del producto...");
        JScrollPane scrollDesc = new JScrollPane(campoDescripcion);
        scrollDesc.setBackground(ColorPalette.WHITE_LIGHT);
        scrollDesc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollDesc.setPreferredSize(new Dimension(INPUT_FIELD_WIDTH, INPUT_FIELD_HEIGHT));
        scrollDesc.setMaximumSize(new Dimension(INPUT_FIELD_WIDTH, INPUT_FIELD_HEIGHT));
        gbcDesc.insets = new Insets(0, 0, 0, 0); // No bottom inset for the text area itself within its panel
        descPanel.add(scrollDesc, gbcDesc);

        rightColumnPanel.add(descPanel, gbcRight); // Add the combined description panel to the right column

        rightColumnPanel.add(crearCampoFormulario("Precio €", campoPrecio = new JTextField()), gbcRight);
        campoPrecio.setFont(ColorPalette.FONT_NORMAL);
        campoPrecio.putClientProperty("JTextField.placeholderText", "Ej: 55.00");

        rightColumnPanel.add(crearCampoFormulario("Stock", campoStock = new JTextField()), gbcRight);
        campoStock.setFont(ColorPalette.FONT_NORMAL);
        campoStock.putClientProperty("JTextField.placeholderText", "Ej: 15");

        comboCategoria = new JComboBox<>();
        comboCategoria.setFont(ColorPalette.FONT_NORMAL);
        estilizarCombo(comboCategoria);
        comboCategoria.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rightColumnPanel.add(crearCampoFormulario("Categoría", comboCategoria), gbcRight);

        gbcMain.gridx = 1;
        gbcMain.gridy = 0;
        mainContentPanel.add(rightColumnPanel, gbcMain);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(ColorPalette.WHITE_LIGHT);
        panelBotones.setBorder(new EmptyBorder(30, 0, 0, 0));

        btnCancelar = ColorPalette.createSecondaryButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(INPUT_FIELD_WIDTH, INPUT_FIELD_HEIGHT));
        btnCancelar.setFont(ColorPalette.FONT_BUTTON);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnGuardar = ColorPalette.createPrimaryButton("Guardar Producto");
        btnGuardar.setPreferredSize(new Dimension(INPUT_FIELD_WIDTH, INPUT_FIELD_HEIGHT));
        btnGuardar.setFont(ColorPalette.FONT_BUTTON);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);
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

        // Validación numérica
        campoPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE) ||
                        (c == '.' && !campoPrecio.getText().contains(".")))) {
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


    private JPanel crearCampoFormulario(String etiqueta, JComponent campo) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ColorPalette.WHITE_LIGHT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0); // This applies to the label part of the form field

        JLabel label = new JLabel(etiqueta);
        label.setFont(ColorPalette.FONT_NORMAL);
        label.setForeground(ColorPalette.GRAY_DARK);
        panel.add(label, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 10, 0); // This applies to the input component part, providing 10px bottom space

        campo.setFont(ColorPalette.FONT_NORMAL);

        if (campo instanceof JTextField) {
            JTextField textField = (JTextField) campo;
            textField.setPreferredSize(new Dimension(INPUT_FIELD_WIDTH, INPUT_FIELD_HEIGHT));
            textField.setMaximumSize(new Dimension(INPUT_FIELD_WIDTH, INPUT_FIELD_HEIGHT));
            textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT),
                    new EmptyBorder(8, 8, 8, 8)
            ));
            textField.setBackground(Color.WHITE);
        } else if (campo instanceof JComboBox) {
            JComboBox<?> comboBox = (JComboBox<?>) campo;
            comboBox.setPreferredSize(new Dimension(INPUT_FIELD_WIDTH, INPUT_FIELD_HEIGHT));
            comboBox.setMaximumSize(new Dimension(INPUT_FIELD_WIDTH, INPUT_FIELD_HEIGHT));
            comboBox.setBackground(Color.WHITE);
            comboBox.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
            comboBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    setFont(ColorPalette.FONT_NORMAL);
                    return this;
                }
            });
        }
        panel.add(campo, gbc);

        return panel;
    }

    private void estilizarCombo(JComboBox<?> combo) {
        combo.setPreferredSize(new Dimension(INPUT_FIELD_WIDTH, INPUT_FIELD_HEIGHT));
        combo.setMaximumSize(new Dimension(INPUT_FIELD_WIDTH, INPUT_FIELD_HEIGHT));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
        combo.setFont(ColorPalette.FONT_NORMAL);
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(ColorPalette.FONT_NORMAL);
                return this;
            }
        });
    }

    private void cargarCategorias() {
        comboCategoria.removeAllItems();
        comboCategoria.addItem(new Categoria(0, "Selecciona una categoría", ""));
        List<Categoria> categorias = categoriaService.obtenerTodos();
        for (Categoria c : categorias) {
            comboCategoria.addItem(c);
        }
    }

    private void cargarProducto() {
        if (productoEnEdicion != null && productoEnEdicion.getId() > 0) {
            campoNombre.setText(productoEnEdicion.getNombre());
            campoDescripcion.setText(productoEnEdicion.getDescripcion());
            campoPrecio.setText(formatoDecimal.format(productoEnEdicion.getPrecio()));
            campoStock.setText(String.valueOf(productoEnEdicion.getStock()));

            if (productoEnEdicion.getIdCategoria() > 0) {
                for (int i = 0; i < comboCategoria.getItemCount(); i++) {
                    Categoria cat = comboCategoria.getItemAt(i);
                    if (cat != null && cat.getId() == productoEnEdicion.getIdCategoria()) {
                        comboCategoria.setSelectedItem(cat);
                        break;
                    }
                }
            } else {
                comboCategoria.setSelectedItem(comboCategoria.getItemAt(0));
            }

            if (productoEnEdicion.tieneImagen()) {
                labelImagenActual.setText("Imagen cargada");
            } else {
                labelImagenActual.setText("Imagen del producto");
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
                imagenCambiada = true;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al leer la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                imagenSeleccionada = null;
                labelImagenActual.setText("Error al cargar imagen");
            }
        }
    }

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

        return true;
    }

    public boolean hayCambiosPendientes() {
        if (productoOriginal == null) {
            return !campoNombre.getText().trim().isEmpty() ||
                    !campoDescripcion.getText().trim().isEmpty() ||
                    !campoPrecio.getText().trim().isEmpty() ||
                    !campoStock.getText().trim().isEmpty() ||
                    imagenSeleccionada != null;
        } else {
            if (!Objects.equals(campoNombre.getText().trim(), productoOriginal.getNombre() != null ? productoOriginal.getNombre() : "")) return true;
            if (!Objects.equals(campoDescripcion.getText().trim(), productoOriginal.getDescripcion() != null ? productoOriginal.getDescripcion() : "")) return true;

            try {
                double precioActual = formatoDecimal.parse(campoPrecio.getText().trim()).doubleValue();
                if (Double.compare(precioActual, productoOriginal.getPrecio()) != 0) return true;
            } catch (ParseException | NumberFormatException e) {
                return true;
            }

            try {
                int stockActual = Integer.parseInt(campoStock.getText().trim());
                if (stockActual != productoOriginal.getStock()) return true;
            } catch (NumberFormatException e) {
                return true;
            }

            Categoria categoriaActual = (Categoria) comboCategoria.getSelectedItem();
            int idCategoriaActual = (categoriaActual != null) ? categoriaActual.getId() : 0;
            if (idCategoriaActual != productoOriginal.getIdCategoria()) return true;

            if (imagenCambiada) return true;
            if (productoOriginal.getImagen() == null && imagenSeleccionada != null) return true;
            if (productoOriginal.getImagen() != null && imagenSeleccionada == null) return true;
            if (productoOriginal.getImagen() != null && imagenSeleccionada != null && !java.util.Arrays.equals(productoOriginal.getImagen(), imagenSeleccionada)) return true;

            return false;
        }
    }

    // Métodos para establecer listeners desde el diálogo/frame contenedor
    public void addGuardarListener(ActionListener listener) {
        this.guardarListener = listener;
    }

    public void addCancelarListener(ActionListener listener) {
        this.cancelarListener = listener;
    }

    // Getter para obtener el producto editado/añadido después de guardar
    public Producto getProductoGuardado() {
        return productoEnEdicion;
    }
}