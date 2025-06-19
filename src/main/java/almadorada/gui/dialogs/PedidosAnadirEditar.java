package main.java.almadorada.gui.dialogs;

import main.java.almadorada.gui.ColorPalette;
import main.java.almadorada.model.Pedido;
import main.java.almadorada.service.PedidoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class PedidosAnadirEditar extends JDialog {
    private JTextField txtNombreCliente;
    private JTextField txtCorreo;
    private JTextField txtTelefono;
    private JTextArea txtDireccion;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextArea txtNotas;
    private JComboBox<String> comboEstado;
    private boolean guardado = false;
    private Pedido pedidoGuardado;
    private Pedido pedidoEditar = null;
    private List<String> nombresProductos;
    private PedidoService pedidoService;
    private JLabel lblSubtotal;
    private JLabel lblTotal;

    public PedidosAnadirEditar(Frame parent) {
        super(parent, "Añadir Pedido", true);
        pedidoService = new PedidoService();
        initUI();
    }

    public PedidosAnadirEditar(Frame parent, Pedido pedido) {
        super(parent, "Editar Pedido", true);
        this.pedidoEditar = pedido;
        pedidoService = new PedidoService();
        initUI();
        cargarDatosPedido();
        calcularTotales();
    }

    private void initUI() {
        setTitle(pedidoEditar == null ? "Nuevo Pedido" : "Editar Pedido");
        setLocationRelativeTo(getParent());
        getContentPane().setBackground(ColorPalette.WHITE_LIGHT);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorPalette.WHITE_LIGHT);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(ColorPalette.WHITE_LIGHT);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel(pedidoEditar == null ? "Añadir Nuevo Pedido" : "Editar Pedido");
        titleLabel.setFont(ColorPalette.FONT_TITLE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel formContentPanel = new JPanel();
        formContentPanel.setLayout(new BoxLayout(formContentPanel, BoxLayout.Y_AXIS));
        formContentPanel.setBackground(ColorPalette.WHITE_LIGHT);
        formContentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel clientInfoPanel = new JPanel(new GridBagLayout());
        clientInfoPanel.setBackground(ColorPalette.WHITE_LIGHT);
        clientInfoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int clientRow = 0;
        gbc.gridx = 0; gbc.gridy = clientRow;
        gbc.gridwidth = 1; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblNombre = new JLabel("Nombre Cliente:");
        lblNombre.setFont(ColorPalette.FONT_NORMAL); lblNombre.setForeground(ColorPalette.GRAY_DARK);
        clientInfoPanel.add(lblNombre, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtNombreCliente = new JTextField(15);
        ColorPalette.styleTextField(txtNombreCliente);
        clientInfoPanel.add(txtNombreCliente, gbc);

        gbc.gridx = 2; gbc.gridy = clientRow;
        gbc.weightx = 0;
        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setFont(ColorPalette.FONT_NORMAL); lblCorreo.setForeground(ColorPalette.GRAY_DARK);
        clientInfoPanel.add(lblCorreo, gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        txtCorreo = new JTextField(15);
        ColorPalette.styleTextField(txtCorreo);
        clientInfoPanel.add(txtCorreo, gbc);


        gbc.gridx = 4; gbc.gridy = clientRow;
        gbc.weightx = 0;
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(ColorPalette.FONT_NORMAL); lblTelefono.setForeground(ColorPalette.GRAY_DARK);
        clientInfoPanel.add(lblTelefono, gbc);
        gbc.gridx = 5;
        gbc.weightx = 1.0;
        txtTelefono = new JTextField(15);
        ColorPalette.styleTextField(txtTelefono);
        clientInfoPanel.add(txtTelefono, gbc);

        clientRow++;
        gbc.gridx = 0; gbc.gridy = clientRow;
        gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setFont(ColorPalette.FONT_NORMAL); lblDireccion.setForeground(ColorPalette.GRAY_DARK);
        clientInfoPanel.add(lblDireccion, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        txtDireccion = new JTextArea(1, 20);
        txtDireccion.setLineWrap(true);
        txtDireccion.setWrapStyleWord(true);
        txtDireccion.setFont(ColorPalette.FONT_NORMAL);
        txtDireccion.setForeground(ColorPalette.GRAY_DARK);
        txtDireccion.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
        JScrollPane scrollDireccion = new JScrollPane(txtDireccion);
        scrollDireccion.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
        scrollDireccion.setPreferredSize(new Dimension(scrollDireccion.getPreferredSize().width, 30));
        scrollDireccion.setMinimumSize(new Dimension(scrollDireccion.getMinimumSize().width, 30));
        clientInfoPanel.add(scrollDireccion, gbc);

        gbc.gridx = 2; gbc.gridy = clientRow;
        gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel lblNotas = new JLabel("Notas:");
        lblNotas.setFont(ColorPalette.FONT_NORMAL); lblNotas.setForeground(ColorPalette.GRAY_DARK);
        clientInfoPanel.add(lblNotas, gbc);
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        txtNotas = new JTextArea(1, 20);
        txtNotas.setLineWrap(true);
        txtNotas.setWrapStyleWord(true);
        txtNotas.setFont(ColorPalette.FONT_NORMAL);
        txtNotas.setForeground(ColorPalette.GRAY_DARK);
        txtNotas.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
        JScrollPane scrollNotas = new JScrollPane(txtNotas);
        scrollNotas.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
        scrollNotas.setPreferredSize(new Dimension(scrollNotas.getPreferredSize().width, 30));
        scrollNotas.setMinimumSize(new Dimension(scrollNotas.getMinimumSize().width, 30));
        clientInfoPanel.add(scrollNotas, gbc);

        gbc.gridx = 4; gbc.gridy = clientRow;
        gbc.gridwidth = 1; gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(ColorPalette.FONT_NORMAL);
        lblEstado.setForeground(ColorPalette.GRAY_DARK);
        clientInfoPanel.add(lblEstado, gbc);
        gbc.gridx = 5;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        comboEstado = new JComboBox<>(new String[]{"Pendiente", "En preparación", "Enviado", "Entregado", "Cancelado"});
        ColorPalette.styleComboBox(comboEstado);

        comboEstado.setPreferredSize(new Dimension(comboEstado.getPreferredSize().width, 30));
        comboEstado.setMinimumSize(new Dimension(comboEstado.getMinimumSize().width, 30));
        comboEstado.setCursor(new Cursor(Cursor.HAND_CURSOR));

        comboEstado.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected || cellHasFocus) {
                    label.setBackground(ColorPalette.GRAY_VERY_LIGHT);
                    label.setForeground(ColorPalette.GRAY_DARK);
                } else {
                    label.setBackground(ColorPalette.WHITE_LIGHT);
                    label.setForeground(ColorPalette.GRAY_DARK);
                }
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return label;
            }
        });

        clientInfoPanel.add(comboEstado, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        formContentPanel.add(clientInfoPanel);
        formContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel productsPanel = new JPanel(new GridBagLayout());
        productsPanel.setBackground(ColorPalette.WHITE_LIGHT);
        productsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        final int PRODUCT_SECTION_COLUMNS = 6;

        int productRow = 0;
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = productRow;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblProducto = new JLabel("Producto:");
        lblProducto.setFont(ColorPalette.FONT_NORMAL);
        lblProducto.setForeground(ColorPalette.GRAY_DARK);
        productsPanel.add(lblProducto, gbc);

        gbc.gridx = 1; gbc.gridy = productRow;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        nombresProductos = pedidoService.obtenerNombresProductos();
        JComboBox<String> comboProductos = new JComboBox<>(nombresProductos.toArray(new String[0]));
        ColorPalette.styleComboBox(comboProductos);
        comboProductos.setSize(new Dimension(160, 40));
        comboProductos.setCursor(new Cursor(Cursor.HAND_CURSOR));

        comboProductos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected || cellHasFocus) {
                    label.setBackground(ColorPalette.GRAY_VERY_LIGHT);
                    label.setForeground(ColorPalette.GRAY_DARK);
                } else {
                    label.setBackground(ColorPalette.WHITE_LIGHT);
                    label.setForeground(ColorPalette.GRAY_DARK);
                }
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return label;
            }
        });
        productsPanel.add(comboProductos, gbc);

        gbc.gridx = 2; gbc.gridy = productRow;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(ColorPalette.FONT_NORMAL);
        lblCantidad.setForeground(ColorPalette.GRAY_DARK);
        productsPanel.add(lblCantidad, gbc);

        gbc.gridx = 3; gbc.gridy = productRow;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField txtCantidad = new JTextField(5);
        ColorPalette.styleTextField(txtCantidad);
        lblCantidad.setSize(new Dimension(160, 40));
        productsPanel.add(txtCantidad, gbc);

        gbc.gridx = 4; gbc.gridy = productRow;
        gbc.weightx = 0.15;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnAgregar = ColorPalette.createPrimaryButton("Añadir");
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.setFont(ColorPalette.FONT_BUTTON);
        btnAgregar.setPreferredSize(new Dimension(160, 40));
        btnAgregar.setMinimumSize(new Dimension(160, 40));
        btnAgregar.setMaximumSize(new Dimension(160, 40));
        productsPanel.add(btnAgregar, gbc);

        gbc.gridx = 5; gbc.gridy = productRow;
        gbc.weightx = 0.15;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnEliminarProducto = ColorPalette.createDangerButton("Eliminar Producto");
        btnEliminarProducto.setFont(ColorPalette.FONT_BUTTON);
        btnEliminarProducto.setPreferredSize(new Dimension(160, 40));
        btnEliminarProducto.setMinimumSize(new Dimension(160, 40));
        btnEliminarProducto.setMaximumSize(new Dimension(160, 40));
        btnEliminarProducto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        productsPanel.add(btnEliminarProducto, gbc);
        gbc.anchor = GridBagConstraints.CENTER;

        productRow++;
        gbc.gridx = 0; gbc.gridy = productRow;
        gbc.gridwidth = PRODUCT_SECTION_COLUMNS;
        gbc.weighty = 1.0;
        modeloTabla = new DefaultTableModel(new Object[]{"Producto", "Cantidad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) return Integer.class;
                return String.class;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(30);
        tablaProductos.setFont(ColorPalette.FONT_NORMAL);
        tablaProductos.setForeground(ColorPalette.GRAY_DARK);
        tablaProductos.setSelectionBackground(ColorPalette.GOLD_STRONG.brighter());

        tablaProductos.getTableHeader().setFont(ColorPalette.FONT_BUTTON);
        tablaProductos.getTableHeader().setBackground(ColorPalette.TABLE_HEADER_BG);
        tablaProductos.getTableHeader().setForeground(ColorPalette.GRAY_DARK);
        tablaProductos.getTableHeader().setReorderingAllowed(false);
        tablaProductos.getTableHeader().setResizingAllowed(false);

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tablaProductos.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Centrar el contenido de todas las celdas de la tabla
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaProductos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row % 2 == 0) {
                    c.setBackground(ColorPalette.WHITE_LIGHT);
                } else {
                    c.setBackground(ColorPalette.CREAM_VERY_LIGHT);
                }
                c.setForeground(ColorPalette.GRAY_DARK);
                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });
        for (int i = 0; i < tablaProductos.getColumnCount(); i++) {
            tablaProductos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }


        JScrollPane scroll = new JScrollPane(tablaProductos);
        scroll.setPreferredSize(new Dimension(400, 150));
        scroll.getViewport().setBackground(ColorPalette.WHITE_LIGHT);
        scroll.setBorder(BorderFactory.createLineBorder(ColorPalette.TABLE_GRID));
        productsPanel.add(scroll, gbc);

        productRow++;
        gbc.gridx = 0; gbc.gridy = productRow; gbc.gridwidth = PRODUCT_SECTION_COLUMNS; // Ajustar a nuevas columnas
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        lblSubtotal = new JLabel("Subtotal: 0.0 €");
        lblSubtotal.setFont(ColorPalette.FONT_NORMAL);
        lblSubtotal.setForeground(ColorPalette.GRAY_DARK);
        productsPanel.add(lblSubtotal, gbc);

        productRow++;
        gbc.gridx = 0; gbc.gridy = productRow; gbc.gridwidth = PRODUCT_SECTION_COLUMNS; // Ajustar a nuevas columnas
        lblTotal = new JLabel("Total (IVA 21% incluido): 0.0 €");
        lblTotal.setFont(ColorPalette.FONT_TOTAL);
        lblTotal.setForeground(ColorPalette.GOLD_STRONG);
        productsPanel.add(lblTotal, gbc);


        formContentPanel.add(productsPanel);
        formContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JScrollPane formScrollPane = new JScrollPane(formContentPanel);
        formScrollPane.getViewport().setBackground(ColorPalette.WHITE_LIGHT);
        formScrollPane.setBorder(BorderFactory.createEmptyBorder());
        formScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(formScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(ColorPalette.WHITE_LIGHT);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JButton btnGuardar = ColorPalette.createPrimaryButton("Guardar Pedido");
        btnGuardar.setFont(ColorPalette.FONT_BUTTON);
        btnGuardar.setPreferredSize(new Dimension(160, 40));
        btnGuardar.setMinimumSize(new Dimension(160, 40));
        btnGuardar.setMaximumSize(new Dimension(160, 40));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnCancelar = ColorPalette.createSecondaryButton("Cancelar");
        btnCancelar.setFont(ColorPalette.FONT_BUTTON);
        btnCancelar.setPreferredSize(new Dimension(140, 40));
        btnCancelar.setMinimumSize(new Dimension(140, 40));
        btnCancelar.setMaximumSize(new Dimension(140, 40));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));


        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnGuardar);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnAgregar.addActionListener(e -> {
            String producto = (String) comboProductos.getSelectedItem();
            String cantidadStr = txtCantidad.getText().trim();
            try {
                int cantidad = Integer.parseInt(cantidadStr);
                if (cantidad <= 0) throw new NumberFormatException();
                boolean encontrado = false;
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    String prodExistente = modeloTabla.getValueAt(i, 0).toString();
                    if (prodExistente.equals(producto)) {
                        int cantidadExistente = (int) modeloTabla.getValueAt(i, 1);
                        modeloTabla.setValueAt(cantidadExistente + cantidad, i, 1);
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    modeloTabla.addRow(new Object[]{producto, cantidad});
                }
                txtCantidad.setText("");
                calcularTotales();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            }
        });

        btnEliminarProducto.addActionListener(e -> {
            int filaSeleccionada = tablaProductos.getSelectedRow();
            if (filaSeleccionada != -1) {
                modeloTabla.removeRow(filaSeleccionada);
                calcularTotales();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnGuardar.addActionListener(e -> guardarPedido());
        btnCancelar.addActionListener(e -> dispose());

        tablaProductos.getInputMap(JTable.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DELETE"), "eliminarFila");
        tablaProductos.getActionMap().put("eliminarFila", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int filaSeleccionada = tablaProductos.getSelectedRow();
                if (filaSeleccionada != -1) {
                    modeloTabla.removeRow(filaSeleccionada);
                    calcularTotales();
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void calcularTotales() {
        double subtotal = 0.0;

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String producto = modeloTabla.getValueAt(i, 0).toString();
            int cantidad = 0;
            try {
                cantidad = Integer.parseInt(modeloTabla.getValueAt(i, 1).toString());
            } catch (Exception e) {
                cantidad = 0;
            }
            double precioUnitario = pedidoService.obtenerPrecioProducto(producto);
            subtotal += precioUnitario * cantidad;
        }

        double iva = subtotal * 0.21; // 21% IVA
        double total = subtotal + iva;

        if (lblSubtotal != null) {
            lblSubtotal.setText(String.format("Subtotal: %.2f €", subtotal));
        }
        if (lblTotal != null) {
            lblTotal.setText(String.format("Total (IVA 21%% incluido): %.2f €", total));
        }
    }

    private void cargarDatosPedido() {
        if (pedidoEditar == null) return;

        txtNombreCliente.setText(pedidoEditar.getNombreCliente());
        txtCorreo.setText(pedidoEditar.getCorreo());
        txtTelefono.setText(pedidoEditar.getTelefono());
        txtDireccion.setText(pedidoEditar.getDireccion());
        txtNotas.setText(pedidoEditar.getNotasAdicionales());

        if (pedidoEditar.getProductos() != null && !pedidoEditar.getProductos().isEmpty()) {
            String[] productosArray = pedidoEditar.getProductos().split(", ");
            for (String item : productosArray) {
                int lastXIndex = item.lastIndexOf('x');
                if (lastXIndex != -1) {
                    String productName = item.substring(0, lastXIndex).trim();
                    try {
                        int quantity = Integer.parseInt(item.substring(lastXIndex + 1).trim());
                        modeloTabla.addRow(new Object[]{productName, quantity});
                    } catch (NumberFormatException ignored) {
                        System.err.println("Could not parse quantity for product: " + item);
                    }
                } else {
                    modeloTabla.addRow(new Object[]{item.trim(), 1});
                }
            }
        }
        comboEstado.setSelectedItem(pedidoEditar.getEstado());
    }
    private void guardarPedido() {
        try {
            String nombre = txtNombreCliente.getText().trim();
            String correo = txtCorreo.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String notas = txtNotas.getText().trim();

            if (nombre.isEmpty() || correo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Faltan campos obligatorios.");
                return;
            }

            if (modeloTabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Debes añadir al menos un producto.");
                return;
            }

            List<String> productEntries = new ArrayList<>();
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                String prod = modeloTabla.getValueAt(i, 0).toString();
                int cant = Integer.parseInt(modeloTabla.getValueAt(i, 1).toString());
                for (int j = 0; j < cant; j++) {
                    productEntries.add(prod);
                }
            }
            String productosStr = String.join(", ", productEntries) + ", ";


            double subtotal = 0.0;
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                String producto = modeloTabla.getValueAt(i, 0).toString();
                int cantidad = Integer.parseInt(modeloTabla.getValueAt(i, 1).toString());
                double precioUnitario = pedidoService.obtenerPrecioProducto(producto);
                subtotal += precioUnitario * cantidad;
            }
            double total = subtotal * 1.21;

            String estado = (String) comboEstado.getSelectedItem();

            if (pedidoEditar != null) {
                pedidoEditar.setNombreCliente(nombre);
                pedidoEditar.setCorreo(correo);
                pedidoEditar.setTelefono(telefono);
                pedidoEditar.setDireccion(direccion);
                pedidoEditar.setProductos(productosStr);
                pedidoEditar.setSubtotal(subtotal);
                pedidoEditar.setTotal(total);
                pedidoEditar.setNotasAdicionales(notas);
                pedidoEditar.setEstado(estado);
                pedidoService.actualizar(pedidoEditar);
                pedidoGuardado = pedidoEditar;
            } else {
                pedidoGuardado = new Pedido(
                        LocalDateTime.now(),
                        nombre,
                        correo,
                        telefono,
                        direccion,
                        productosStr,
                        subtotal,
                        total,
                        notas,
                        estado
                );
                pedidoService.insertar(pedidoGuardado);
            }

            guardado = true;
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el pedido: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Pedido getPedido() {
        return pedidoGuardado;
    }
}