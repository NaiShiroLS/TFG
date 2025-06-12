package main.java.almadorada.gui.dialogs;

import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import main.java.almadorada.gui.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer; // Import for centering table cell content
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.time.format.DateTimeFormatter; // For date formatting
import java.time.LocalDate; // For date formatting
import java.time.LocalDateTime; // For parsing date with time

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class PedidoDetalles extends JFrame {
    private JPanel contentPane;
    private JTable table;
    private JComboBox<String> estadoCombo;
    private JLabel lblMonto;
    private int pedidoId;
    private Runnable onCloseCallback;

    public PedidoDetalles(int pedidoId, Runnable onCloseCallback) {
        this.pedidoId = pedidoId;
        this.onCloseCallback = onCloseCallback;
        initializeComponents();
    }

    public PedidoDetalles(int pedidoId) {
        this(pedidoId, null);
    }

    private void initializeComponents() {
        setTitle("Detalles del Pedido #" + pedidoId);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 900, 650);
        contentPane = new JPanel();
        contentPane.setBackground(ColorPalette.WHITE_LIGHT);
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(20, 20));

        JLabel lblTitulo = new JLabel("Detalles del Pedido #" + pedidoId, SwingConstants.CENTER);
        lblTitulo.setFont(ColorPalette.FONT_TITLE);
        lblTitulo.setForeground(ColorPalette.GRAY_DARK); // Title color to black
        contentPane.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setBackground(ColorPalette.WHITE_LIGHT);

        // Panel for client information
        JPanel panelCliente = new JPanel(new GridBagLayout()); // Changed to GridBagLayout
        panelCliente.setBackground(ColorPalette.WHITE_LIGHT);
        panelCliente.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT, 1), // Thin line border
                "Información del Cliente y Pedido", // Title for the border
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                ColorPalette.FONT_SUBTITLE,
                ColorPalette.GRAY_DARK
        ));

        JTextField txtNombre = new JTextField();
        JTextField txtCorreo = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtFecha = new JTextField();
        JTextField txtDireccion = new JTextField();
        JTextField txtNotas = new JTextField();

        // Updated Estado options and white background for ComboBox
        estadoCombo = new JComboBox<>(new String[]{"Pendiente", "En preparación", "Enviado", "Entregado", "Cancelado"});
        estadoCombo.setFont(ColorPalette.FONT_NORMAL);
        estadoCombo.setBackground(Color.WHITE); // Changed to white background
        estadoCombo.setForeground(ColorPalette.GRAY_DARK);
        estadoCombo.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
        estadoCombo.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover


        ColorPalette.styleTextField(txtNombre);
        ColorPalette.styleTextField(txtCorreo);
        ColorPalette.styleTextField(txtTelefono);
        ColorPalette.styleTextField(txtFecha);
        ColorPalette.styleTextField(txtDireccion);
        ColorPalette.styleTextField(txtNotas);

        txtNombre.setEditable(false);
        txtCorreo.setEditable(false);
        txtTelefono.setEditable(false);
        txtFecha.setEditable(false);
        txtDireccion.setEditable(false);
        txtNotas.setEditable(false);

        // Labels for client info fields
        JLabel lblNombre = new JLabel("Nombre del Cliente:");
        JLabel lblCorreo = new JLabel("Email:");
        JLabel lblTelefono = new JLabel("Teléfono:");
        JLabel lblFecha = new JLabel("Fecha del Pedido:");
        JLabel lblDireccion = new JLabel("Dirección:");
        JLabel lblNotas = new JLabel("Notas adicionales:");
        JLabel lblEstado = new JLabel("Estado del Pedido:");

        lblNombre.setFont(ColorPalette.FONT_NORMAL);
        lblCorreo.setFont(ColorPalette.FONT_NORMAL);
        lblTelefono.setFont(ColorPalette.FONT_NORMAL);
        lblFecha.setFont(ColorPalette.FONT_NORMAL);
        lblDireccion.setFont(ColorPalette.FONT_NORMAL);
        lblNotas.setFont(ColorPalette.FONT_NORMAL);
        lblEstado.setFont(ColorPalette.FONT_NORMAL);

        lblNombre.setForeground(ColorPalette.GRAY_MEDIUM_DARK);
        lblCorreo.setForeground(ColorPalette.GRAY_MEDIUM_DARK);
        lblTelefono.setForeground(ColorPalette.GRAY_MEDIUM_DARK);
        lblFecha.setForeground(ColorPalette.GRAY_MEDIUM_DARK);
        lblDireccion.setForeground(ColorPalette.GRAY_MEDIUM_DARK);
        lblNotas.setForeground(ColorPalette.GRAY_MEDIUM_DARK);
        lblEstado.setForeground(ColorPalette.GRAY_MEDIUM_DARK);

        // --- GridBagLayout for panelCliente ---
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

        // Row 0: Nombre del Cliente (left) and Email (right)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0; // Reset weight for labels
        panelCliente.add(lblNombre, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Allow text field to expand
        panelCliente.add(txtNombre, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0; // Reset weight
        panelCliente.add(lblCorreo, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Allow text field to expand
        panelCliente.add(txtCorreo, gbc);


        // Row 1: Teléfono (left) and Fecha (right)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0;
        panelCliente.add(lblTelefono, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        panelCliente.add(txtTelefono, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0;
        panelCliente.add(lblFecha, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        panelCliente.add(txtFecha, gbc);

        // Row 2: Dirección (left) and Notas (right)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0;
        panelCliente.add(lblDireccion, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        panelCliente.add(txtDireccion, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0;
        panelCliente.add(lblNotas, gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        panelCliente.add(txtNotas, gbc);

        // Row 3: Estado del Pedido (left) and estadoCombo (right)
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0;
        panelCliente.add(lblEstado, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1; // Only span 1 column for the combo box
        gbc.weightx = 0.5; // Give it less weight to keep it from being too long
        panelCliente.add(estadoCombo, gbc);

        // Add an empty filler to push elements to the left (optional but good practice)
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span remaining columns
        gbc.weightx = 2.0; // Give it more weight
        panelCliente.add(Box.createHorizontalGlue(), gbc); // Use a glue component

        panelCentral.add(panelCliente, BorderLayout.NORTH);

        String[] columnNames = {"Producto", "Imagen", "Cantidad", "Precio Unitario", "Subtotal"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            public Class<?> getColumnClass(int column) {
                return column == 1 ? Icon.class : Object.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        table = new JTable(model);

        // Renderer for displaying text when image is null
        table.getColumnModel().getColumn(1).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Icon) {
                    JLabel label = new JLabel((Icon) value);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    return label;
                } else {
                    JLabel label = new JLabel("No image"); // Changed text
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setForeground(ColorPalette.GRAY_MEDIUM_DARK);
                    return label;
                }
            }
        });

        // Center all data in the table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 1) { // Apply to all columns except the image column which has its own renderer
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        table.setRowHeight(60);
        table.setFont(ColorPalette.FONT_NORMAL);
        table.setForeground(ColorPalette.GRAY_DARK);
        table.setSelectionBackground(ColorPalette.TABLE_SELECTION_BG);
        table.setSelectionForeground(ColorPalette.GRAY_DARK);
        table.setGridColor(ColorPalette.TABLE_GRID);

        // Table header styling
        table.getTableHeader().setFont(ColorPalette.FONT_SUBTITLE); // Changed to FONT_SUBTITLE
        table.getTableHeader().setBackground(ColorPalette.TABLE_HEADER_BG);
        table.getTableHeader().setForeground(ColorPalette.GRAY_DARK);
        table.getTableHeader().setReorderingAllowed(false); // Prevent column reordering
        table.getTableHeader().setResizingAllowed(true); // Allow column resizing
        table.getTableHeader().setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 40)); // Make header taller


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT, 1)); // Border for the scroll pane
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(ColorPalette.WHITE_LIGHT);

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotal.setBackground(ColorPalette.WHITE_LIGHT);
        JLabel lblTotal = new JLabel("Total del Pedido: ");
        lblTotal.setFont(ColorPalette.FONT_TOTAL); // Using FONT_TOTAL
        lblTotal.setForeground(ColorPalette.GRAY_DARK);
        lblMonto = new JLabel("0.00 €");
        lblMonto.setFont(ColorPalette.FONT_TOTAL); // Using FONT_TOTAL
        lblMonto.setForeground(ColorPalette.GOLD_INTENSE);
        panelTotal.add(lblTotal);
        panelTotal.add(lblMonto);
        panelTotal.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Padding

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10)); // Added horizontal gap
        panelBotones.setBackground(ColorPalette.WHITE_LIGHT);

        // Creating buttons with larger insets directly here
        JButton btnGuardar = ColorPalette.createSecondaryButton("Guardar cambios");
        btnGuardar.setFont(ColorPalette.FONT_NORMAL);
        btnGuardar.setPreferredSize(new Dimension(160, 35));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnPDF = ColorPalette.createPrimaryButton("Exportar a PDF");
        btnPDF.setFont(ColorPalette.FONT_NORMAL);
        btnPDF.setPreferredSize(new Dimension(160, 35));
        btnPDF.setCursor(new Cursor(Cursor.HAND_CURSOR));


        btnPDF.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnPDF);

        panelInferior.add(panelTotal, BorderLayout.CENTER);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        contentPane.add(panelInferior, BorderLayout.SOUTH);

        cargarDatosPedido(txtNombre, txtCorreo, txtTelefono, txtFecha, txtDireccion, txtNotas, model);

        btnGuardar.addActionListener(e -> guardarEstadoPedido());
        btnPDF.addActionListener(e -> exportarAPDF());

        setVisible(true);
    }

    private void cargarDatosPedido(JTextField nombre, JTextField correo, JTextField telefono,
                                   JTextField fecha, JTextField direccion, JTextField notas,
                                   DefaultTableModel model) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/alma_dorada", "root", "");
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM pedidos WHERE id_pedido = ?")) {

            stmt.setInt(1, pedidoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nombre.setText(rs.getString("nombre_cliente"));
                correo.setText(rs.getString("correo"));
                telefono.setText(rs.getString("telefono"));

                // Format date without time
                String dateString = rs.getString("fecha");
                if (dateString != null && !dateString.isEmpty()) {
                    try {
                        // Parse the full datetime string
                        LocalDateTime orderDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        // Format to display only the date in "dd/MM/yyyy"
                        DateTimeFormatter spanishFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        fecha.setText(orderDateTime.format(spanishFormatter));
                    } catch (java.time.format.DateTimeParseException e) {
                        System.err.println("Error parsing date: " + dateString + " - " + e.getMessage());
                        fecha.setText(dateString); // Fallback to raw string if parsing fails
                    }
                } else {
                    fecha.setText("");
                }

                direccion.setText(rs.getString("direccion"));
                notas.setText(rs.getString("notas_adicionales"));
                estadoCombo.setSelectedItem(rs.getString("estado"));

                String productosRaw = rs.getString("productos");
                double total = 0.0;

                String[] productos = productosRaw.split(",");
                for (String item : productos) {
                    item = item.trim();
                    if (item.isEmpty()) continue;

                    String[] partes = item.split("x");
                    int cantidad = 1;
                    String nombreProducto;

                    if (partes.length == 2) {
                        cantidad = Integer.parseInt(partes[0].trim());
                        nombreProducto = partes[1].trim();
                    } else {
                        nombreProducto = partes[0].trim();
                    }

                    double precioUnitario = obtenerPrecioProducto(con, nombreProducto);
                    double subtotal = cantidad * precioUnitario;
                    total += subtotal;

                    ImageIcon imagen = obtenerImagenProducto(con, nombreProducto);

                    model.addRow(new Object[]{
                            nombreProducto,
                            imagen,
                            cantidad,
                            String.format("%.2f €", precioUnitario),
                            String.format("%.2f €", subtotal)
                    });
                }

                lblMonto.setText(String.format("%.2f €", total));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar datos del pedido: " + e.getMessage());
        }
    }

    private double obtenerPrecioProducto(Connection con, String nombreProducto) {
        try (PreparedStatement stmt = con.prepareStatement("SELECT precio FROM productos WHERE nombre = ?")) {
            stmt.setString(1, nombreProducto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("precio");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private ImageIcon obtenerImagenProducto(Connection con, String nombreProducto) {
        try (PreparedStatement stmt = con.prepareStatement("SELECT imagen FROM productos WHERE nombre = ?")) {
            stmt.setString(1, nombreProducto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                byte[] imageBytes = rs.getBytes("imagen");
                if (imageBytes != null && imageBytes.length > 0) {
                    ImageIcon original = new ImageIcon(imageBytes);
                    Image img = original.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    return new ImageIcon(img);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void guardarEstadoPedido() {
        String nuevoEstado = estadoCombo.getSelectedItem().toString();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/alma_dorada", "root", "");
             PreparedStatement stmt = con.prepareStatement("UPDATE pedidos SET estado = ? WHERE id_pedido = ?")) {

            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, pedidoId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Estado del pedido actualizado a: " + nuevoEstado);
                if (onCloseCallback != null) onCloseCallback.run();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el pedido para actualizar.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error SQL: " + e.getMessage());
        }
    }


    private void addCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private void exportarAPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar pedido como PDF");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos PDF", "pdf"));

        // Default name with date and time
        String nombrePorDefecto = "pedido_" + pedidoId + "_" +
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd_MM_yyyy")) + ".pdf";
        fileChooser.setSelectedFile(new File(nombrePorDefecto));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
            }

            try {
                Document document = new Document(PageSize.A4);
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileToSave));

                // Configure page event for footer
                writer.setPageEvent(new PdfPageEventHelper() {
                    @Override
                    public void onEndPage(PdfWriter writer, Document document) {
                        try {
                            // Footer with Spanish formatted date and page number
                            java.time.LocalDateTime now = java.time.LocalDateTime.now();
                            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                            String fechaFormateada = now.format(formatter);

                            com.itextpdf.text.Font footerFont = new com.itextpdf.text.Font(
                                    com.itextpdf.text.Font.FontFamily.HELVETICA,
                                    9,
                                    com.itextpdf.text.Font.ITALIC,
                                    new BaseColor(ColorPalette.GRAY_MEDIUM_DARK.getRed(), ColorPalette.GRAY_MEDIUM_DARK.getGreen(), ColorPalette.GRAY_MEDIUM_DARK.getBlue())
                            );

                            PdfContentByte cb = writer.getDirectContent();

                            // Date on the left
                            Phrase fechaPhrase = new Phrase("Generado el: " + fechaFormateada, footerFont);
                            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, fechaPhrase,
                                    document.left(), document.bottom() - 10, 0);

                            // Page number on the right
                            Phrase pagePhrase = new Phrase("Página " + document.getPageNumber(), footerFont);
                            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, pagePhrase,
                                    document.right(), document.bottom() - 10, 0);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                document.open();

                // Define colors according to the provided palette
                BaseColor colorPrimario = new BaseColor(ColorPalette.GOLD_STRONG.getRed(), ColorPalette.GOLD_STRONG.getGreen(), ColorPalette.GOLD_STRONG.getBlue());
                BaseColor colorSecundario = new BaseColor(ColorPalette.GOLD_INTENSE.getRed(), ColorPalette.GOLD_INTENSE.getGreen(), ColorPalette.GOLD_INTENSE.getBlue());
                BaseColor colorTexto = new BaseColor(ColorPalette.GRAY_DARK.getRed(), ColorPalette.GRAY_DARK.getGreen(), ColorPalette.GRAY_DARK.getBlue());
                BaseColor colorTextoClaro = new BaseColor(ColorPalette.GRAY_MEDIUM_DARK.getRed(), ColorPalette.GRAY_MEDIUM_DARK.getGreen(), ColorPalette.GRAY_MEDIUM_DARK.getBlue());
                BaseColor colorFondo1 = new BaseColor(ColorPalette.CREAM_VERY_LIGHT.getRed(), ColorPalette.CREAM_VERY_LIGHT.getGreen(), ColorPalette.CREAM_VERY_LIGHT.getBlue());
                BaseColor colorFondo2 = new BaseColor(ColorPalette.WHITE_LIGHT.getRed(), ColorPalette.WHITE_LIGHT.getGreen(), ColorPalette.WHITE_LIGHT.getBlue());

                // NEW: Define header and alternating row colors from ColorPalette
                BaseColor pdfTableHeaderBg = new BaseColor(ColorPalette.CREAM_LIGHT.getRed(), ColorPalette.CREAM_LIGHT.getGreen(), ColorPalette.CREAM_LIGHT.getBlue());
                BaseColor pdfTableEvenRowBg = new BaseColor(ColorPalette.WHITE_LIGHT.getRed(), ColorPalette.WHITE_LIGHT.getGreen(), ColorPalette.WHITE_LIGHT.getBlue());
                BaseColor pdfTableOddRowBg = new BaseColor(ColorPalette.CREAM_VERY_LIGHT.getRed(), ColorPalette.CREAM_VERY_LIGHT.getGreen(), ColorPalette.CREAM_VERY_LIGHT.getBlue());


                // Fonts
                com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
                        com.itextpdf.text.Font.FontFamily.HELVETICA,
                        24,
                        com.itextpdf.text.Font.BOLD,
                        colorPrimario
                );

                com.itextpdf.text.Font subtitleFont = new com.itextpdf.text.Font(
                        com.itextpdf.text.Font.FontFamily.HELVETICA,
                        14,
                        com.itextpdf.text.Font.NORMAL,
                        colorTexto
                );

                com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(
                        com.itextpdf.text.Font.FontFamily.HELVETICA,
                        12,
                        com.itextpdf.text.Font.BOLD,
                        colorTexto // Changed to GRAY_DARK for header text
                );

                com.itextpdf.text.Font cellFont = new com.itextpdf.text.Font(
                        com.itextpdf.text.Font.FontFamily.HELVETICA,
                        10,
                        com.itextpdf.text.Font.NORMAL,
                        colorTexto
                );

                com.itextpdf.text.Font labelFont = new com.itextpdf.text.Font(
                        com.itextpdf.text.Font.FontFamily.HELVETICA,
                        11,
                        com.itextpdf.text.Font.BOLD,
                        colorTexto
                );

                com.itextpdf.text.Font valueFont = new com.itextpdf.text.Font(
                        com.itextpdf.text.Font.FontFamily.HELVETICA,
                        11,
                        com.itextpdf.text.Font.NORMAL,
                        colorTexto
                );

                // Document header
                PdfPTable headerTable = new PdfPTable(1);
                headerTable.setWidthPercentage(100);

                // Cell for the title
                PdfPCell titleCell = new PdfPCell();
                titleCell.setBorder(Rectangle.NO_BORDER);
                titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                titleCell.setPadding(10);

                Paragraph title = new Paragraph("ALMA DORADA", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                titleCell.addElement(title);

                Paragraph subtitle = new Paragraph("Detalle de Pedido #" + pedidoId, subtitleFont);
                subtitle.setAlignment(Element.ALIGN_CENTER);
                subtitle.setSpacingBefore(5);
                titleCell.addElement(subtitle);

                headerTable.addCell(titleCell);
                document.add(headerTable);

                // Space after header
                document.add(new Paragraph(" "));

                // Get order data from the database
                String[] datosPedido = obtenerDatosPedido();

                if (datosPedido == null) {
                    JOptionPane.showMessageDialog(this, "Error al obtener los datos del pedido");
                    return;
                }

                // Customer information
                PdfPTable clienteTable = new PdfPTable(2);
                clienteTable.setWidthPercentage(100);
                clienteTable.setSpacingBefore(10f);
                clienteTable.setSpacingAfter(15f);
                clienteTable.setWidths(new float[]{1f, 2f});

                // Section title
                PdfPCell clienteTitleCell = new PdfPCell(new Phrase("INFORMACIÓN DEL CLIENTE", labelFont));
                clienteTitleCell.setColspan(2);
                clienteTitleCell.setBackgroundColor(colorFondo1);
                clienteTitleCell.setPadding(8);
                clienteTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                clienteTitleCell.setBorder(Rectangle.NO_BORDER);
                clienteTable.addCell(clienteTitleCell);

                // Customer data (using data obtained from DB)
                addClientDataRow(clienteTable, "Nombre:", datosPedido[0], labelFont, valueFont, colorFondo2);
                addClientDataRow(clienteTable, "Email:", datosPedido[1], labelFont, valueFont, colorFondo1);
                addClientDataRow(clienteTable, "Teléfono:", datosPedido[2], labelFont, valueFont, colorFondo2);
                addClientDataRow(clienteTable, "Fecha del Pedido:", datosPedido[3], labelFont, valueFont, colorFondo1);
                addClientDataRow(clienteTable, "Dirección:", datosPedido[4], labelFont, valueFont, colorFondo2);
                addClientDataRow(clienteTable, "Notas:", datosPedido[5], labelFont, valueFont, colorFondo1);
                addClientDataRow(clienteTable, "Estado:", datosPedido[6], labelFont, valueFont, colorFondo2);

                document.add(clienteTable);

                // Products table
                PdfPTable productosTable = new PdfPTable(4); // No image column
                productosTable.setWidthPercentage(100);
                productosTable.setSpacingBefore(10f);
                productosTable.setSpacingAfter(10f);

                // Configure column widths
                float[] columnWidths = {3f, 1f, 1.5f, 1.5f}; // Producto, Cantidad, Precio Unit., Subtotal
                productosTable.setWidths(columnWidths);

                // Products section title
                PdfPCell productosTitleCell = new PdfPCell(new Phrase("PRODUCTOS DEL PEDIDO", labelFont));
                productosTitleCell.setColspan(4);
                productosTitleCell.setBackgroundColor(colorFondo1);
                productosTitleCell.setPadding(8);
                productosTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                productosTitleCell.setBorder(Rectangle.NO_BORDER);
                productosTable.addCell(productosTitleCell);

                // Product table headers
                String[] headers = {"Producto", "Cantidad", "Precio Unitario", "Subtotal"};
                for (String header : headers) {
                    PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
                    headerCell.setBackgroundColor(pdfTableHeaderBg); // Changed to CREAM_LIGHT
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    headerCell.setPadding(8);
                    headerCell.setBorderWidth(1);
                    headerCell.setBorderColor(colorFondo2);
                    productosTable.addCell(headerCell);
                }

                // Product rows
                boolean alternar = false;
                for (int row = 0; row < table.getRowCount(); row++) {
                    BaseColor rowBgColor = alternar ? pdfTableOddRowBg : pdfTableEvenRowBg; // Use defined alternating colors

                    // Product (column 0)
                    String producto = table.getValueAt(row, 0) != null ? table.getValueAt(row, 0).toString() : "";
                    PdfPCell productoCell = new PdfPCell(new Phrase(producto, cellFont));
                    productoCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Centered
                    productoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    productoCell.setPadding(6);
                    productoCell.setBorderWidth(0);
                    productoCell.setBackgroundColor(rowBgColor);
                    productosTable.addCell(productoCell);

                    // Quantity (column 2 of the original table)
                    String cantidad = table.getValueAt(row, 2) != null ? table.getValueAt(row, 2).toString() : "";
                    PdfPCell cantidadCell = new PdfPCell(new Phrase(cantidad, cellFont));
                    cantidadCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Centered
                    cantidadCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cantidadCell.setPadding(6);
                    cantidadCell.setBorderWidth(0);
                    cantidadCell.setBackgroundColor(rowBgColor);
                    productosTable.addCell(cantidadCell);

                    // Unit Price (column 3)
                    String precioUnit = table.getValueAt(row, 3) != null ? table.getValueAt(row, 3).toString() : "";
                    PdfPCell precioCell = new PdfPCell(new Phrase(precioUnit, cellFont));
                    precioCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Centered
                    precioCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    precioCell.setPadding(6);
                    precioCell.setBorderWidth(0);
                    precioCell.setBackgroundColor(rowBgColor);
                    productosTable.addCell(precioCell);

                    // Subtotal (column 4)
                    String subtotal = table.getValueAt(row, 4) != null ? table.getValueAt(row, 4).toString() : "";
                    PdfPCell subtotalCell = new PdfPCell(new Phrase(subtotal, cellFont));
                    subtotalCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Centered
                    subtotalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    subtotalCell.setPadding(6);
                    subtotalCell.setBorderWidth(0);
                    subtotalCell.setBackgroundColor(rowBgColor);
                    productosTable.addCell(subtotalCell);

                    alternar = !alternar;
                }

                document.add(productosTable);

                // Order total
                document.add(new Paragraph(" "));
                PdfPTable totalTable = new PdfPTable(2);
                totalTable.setWidthPercentage(100);
                totalTable.setWidths(new float[]{3f, 1f});

                PdfPCell emptyCell = new PdfPCell(new Phrase(""));
                emptyCell.setBorder(Rectangle.NO_BORDER);
                totalTable.addCell(emptyCell);

                com.itextpdf.text.Font totalFont = new com.itextpdf.text.Font(
                        com.itextpdf.text.Font.FontFamily.HELVETICA,
                        14,
                        com.itextpdf.text.Font.BOLD,
                        colorPrimario
                );

                PdfPCell totalCell = new PdfPCell(new Phrase("TOTAL: " + lblMonto.getText(), totalFont));
                totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                totalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                totalCell.setPadding(10);
                totalCell.setBackgroundColor(colorFondo1);
                totalCell.setBorderWidth(2);
                totalCell.setBorderColor(colorPrimario);
                totalTable.addCell(totalCell);

                document.add(totalTable);

                // Additional information
                document.add(new Paragraph(" "));
                Paragraph infoAdicional = new Paragraph(
                        "Este documento ha sido generado automáticamente por el sistema de gestión ALMA DORADA.",
                        new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9,
                                com.itextpdf.text.Font.ITALIC, colorTextoClaro)
                );
                infoAdicional.setAlignment(Element.ALIGN_CENTER);
                document.add(infoAdicional);

                document.close();

                JOptionPane.showMessageDialog(this,
                        "PDF generado exitosamente:\n" + fileToSave.getAbsolutePath(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al generar el PDF: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // Method to get order data from the database
    private String[] obtenerDatosPedido() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/alma_dorada", "root", "");
             PreparedStatement stmt = con.prepareStatement("SELECT nombre_cliente, correo, telefono, fecha, direccion, notas_adicionales, estado FROM pedidos WHERE id_pedido = ?")) {

            stmt.setInt(1, pedidoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String rawDate = rs.getString("fecha");
                String formattedDate = "";
                if (rawDate != null && !rawDate.isEmpty()) {
                    try {
                        // Parse the full datetime string
                        LocalDateTime orderDateTime = LocalDateTime.parse(rawDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        // Format to display only the date in "dd/MM/yyyy"
                        DateTimeFormatter spanishFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        formattedDate = orderDateTime.format(spanishFormatter);
                    } catch (java.time.format.DateTimeParseException e) {
                        System.err.println("Error parsing date for PDF: " + rawDate + " - " + e.getMessage());
                        formattedDate = rawDate; // Fallback
                    }
                }

                return new String[] {
                        rs.getString("nombre_cliente") != null ? rs.getString("nombre_cliente") : "",
                        rs.getString("correo") != null ? rs.getString("correo") : "",
                        rs.getString("telefono") != null ? rs.getString("telefono") : "",
                        formattedDate, // Use the formatted date
                        rs.getString("direccion") != null ? rs.getString("direccion") : "",
                        rs.getString("notas_adicionales") != null ? rs.getString("notas_adicionales") : "",
                        rs.getString("estado") != null ? rs.getString("estado") : ""
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method to add client data rows
    private void addClientDataRow(PdfPTable table, String label, String value,
                                  com.itextpdf.text.Font labelFont, com.itextpdf.text.Font valueFont,
                                  BaseColor backgroundColor) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        labelCell.setPadding(6);
        labelCell.setBorderWidth(0);
        labelCell.setBackgroundColor(backgroundColor);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "", valueFont));
        valueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        valueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        valueCell.setPadding(6);
        valueCell.setBorderWidth(0);
        valueCell.setBackgroundColor(backgroundColor);
        table.addCell(valueCell);
    }
}