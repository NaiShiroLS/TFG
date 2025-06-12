package main.java.almadorada.gui.panels;

import com.itextpdf.text.*;
import main.java.almadorada.gui.ColorPalette;
import main.java.almadorada.model.Producto;
import main.java.almadorada.service.CategoriaService;
import main.java.almadorada.service.ProductoService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.PageSize;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StockPanel extends JPanel {
    private JTable tabla;
    private DefaultTableModel modelo;
    private ProductoService productoService = new ProductoService();
    private CategoriaService categoriaService = new CategoriaService();

    private JTextField umbralBusqueda;
    private JButton btnBuscar;
    private JButton btnLimpiarFiltros;

    public StockPanel() {
        setLayout(new BorderLayout());
        setBackground(ColorPalette.WHITE_LIGHT);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(ColorPalette.WHITE_LIGHT);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titulo = new JLabel("Gestión de Stock", SwingConstants.CENTER);
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

        // INICIO DE LOS CAMBIOS: Distribución de filtros y botones
        JPanel panelFiltros = new JPanel(new BorderLayout()); // Change to BorderLayout
        panelFiltros.setBackground(Color.WHITE);
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Panel for left-aligned components
        izquierda.setBackground(Color.WHITE);
        JLabel lblUmbral = new JLabel("Stock Mínimo (Umbral):");
        lblUmbral.setFont(ColorPalette.FONT_NORMAL);
        lblUmbral.setForeground(ColorPalette.GRAY_DARK);

        umbralBusqueda = new JTextField(15);
        umbralBusqueda.setPreferredSize(new Dimension(150, 32));
        umbralBusqueda.setText(" 5");
        umbralBusqueda.setForeground(Color.GRAY);
        umbralBusqueda.setFont(ColorPalette.FONT_NORMAL);
        umbralBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        izquierda.add(lblUmbral);
        izquierda.add(Box.createHorizontalStrut(10));
        izquierda.add(umbralBusqueda);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Panel for right-aligned buttons
        derecha.setBackground(Color.WHITE);
        btnBuscar = ColorPalette.createSecondaryButton("Generar Informe");
        btnBuscar.setPreferredSize(new Dimension(140, 32));
        btnBuscar.setFont(ColorPalette.FONT_BUTTON);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLimpiarFiltros = ColorPalette.createSecondaryButton("Limpiar Filtro");
        btnLimpiarFiltros.setPreferredSize(new Dimension(140, 32));
        btnLimpiarFiltros.setFont(ColorPalette.FONT_BUTTON);
        btnLimpiarFiltros.setCursor(new Cursor(Cursor.HAND_CURSOR));

        derecha.add(btnBuscar);
        derecha.add(Box.createHorizontalStrut(10));
        derecha.add(btnLimpiarFiltros);

        panelFiltros.add(izquierda, BorderLayout.WEST); // Add izquierda to the WEST of panelFiltros
        panelFiltros.add(derecha, BorderLayout.EAST);   // Add derecha to the EAST of panelFiltros
        // FIN DE LOS CAMBIOS

        panelContenido.add(panelFiltros, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre Producto", "Categoría", "Stock Actual"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
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


        tabla.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabla.getColumnModel().getColumn(0).setMaxWidth(60);
        tabla.getColumnModel().getColumn(0).setMinWidth(60);

        tabla.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(3).setMaxWidth(100);
        tabla.getColumnModel().getColumn(3).setMinWidth(100);

        DefaultTableCellRenderer centradorId = new DefaultTableCellRenderer();
        centradorId.setHorizontalAlignment(JLabel.CENTER);
        tabla.getColumnModel().getColumn(0).setCellRenderer(centradorId);

        DefaultTableCellRenderer centradorStock = new DefaultTableCellRenderer();
        centradorStock.setHorizontalAlignment(JLabel.CENTER);
        tabla.getColumnModel().getColumn(3).setCellRenderer(centradorStock);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
        scroll.getViewport().setBackground(Color.WHITE);
        panelContenido.add(scroll, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton btnExportar = ColorPalette.createPrimaryButton("Exportar a PDF");
        btnExportar.setPreferredSize(new Dimension(130, 35));
        btnExportar.setFont(ColorPalette.FONT_BUTTON);
        btnExportar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelInferior.add(btnExportar);
        panelContenido.add(panelInferior, BorderLayout.SOUTH);

        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        add(panelPrincipal, BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> filterProducts());
        btnLimpiarFiltros.addActionListener(e -> {
            umbralBusqueda.setText("");
            cargarProductos();
        });
        btnExportar.addActionListener(e -> exportarTablaAPDF());

        cargarProductos();
    }

    private void cargarProductos() {
        modelo.setRowCount(0);
        List<Producto> productos = productoService.obtenerTodos();
        for (Producto p : productos) {
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getNombreCategoria(),
                    p.getStock()
            });
        }
    }

    private void filterProducts() {
        modelo.setRowCount(0);
        String stock = umbralBusqueda.getText().trim();

        List<Producto> productos = productoService.obtenerTodos();
        for (Producto p : productos) {
            int umbral = 5;

            if (!stock.isEmpty()) {
                try {
                    umbral = Integer.parseInt(stock);
                } catch (NumberFormatException e) {
                    umbral = 5;
                }
            }

            if (p.getStock() <= umbral) {
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getNombre(),
                        p.getNombreCategoria(),
                        p.getStock()
                });
            }
        }
    }

    private void exportarTablaAPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar informe como PDF");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos PDF", "pdf"));

        // Nombre por defecto con fecha y hora
        String nombrePorDefecto = "informe_stock_" +
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

                // Configurar el evento de página para el pie de página
                writer.setPageEvent(new PdfPageEventHelper() {
                    @Override
                    public void onEndPage(PdfWriter writer, Document document) {
                        try {
                            // Pie de página con fecha en formato español y número de página
                            java.time.LocalDateTime now = java.time.LocalDateTime.now();
                            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                            String fechaFormateada = now.format(formatter);

                            com.itextpdf.text.Font footerFont = new com.itextpdf.text.Font(
                                    com.itextpdf.text.Font.FontFamily.HELVETICA,
                                    9,
                                    com.itextpdf.text.Font.ITALIC,
                                    new BaseColor(0x6D, 0x6D, 0x6D)
                            );

                            PdfContentByte cb = writer.getDirectContent();

                            // Fecha a la izquierda
                            Phrase fechaPhrase = new Phrase("Generado el: " + fechaFormateada, footerFont);
                            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, fechaPhrase,
                                    document.left(), document.bottom() - 10, 0);

                            // Número de página a la derecha
                            Phrase pagePhrase = new Phrase("Página " + document.getPageNumber(), footerFont);
                            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, pagePhrase,
                                    document.right(), document.bottom() - 10, 0);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                document.open();

                // Definir colores según la paleta proporcionada
                BaseColor colorPrimario = new BaseColor(0xD2, 0xB4,0x8C); // D2B48C
                BaseColor colorSecundario = new BaseColor(0xDA, 0xA5, 0x20); // DAA520
                BaseColor colorTexto = new BaseColor(0x4A, 0x4A, 0x4A); // 4A4A4A
                BaseColor colorTextoClaro = new BaseColor(0x6D, 0x6D, 0x6D); // 6D6D6D
                BaseColor colorFondo1 = new BaseColor(0xF5, 0xF5, 0xDC); // F5F5DC
                BaseColor colorFondo2 = new BaseColor(0xF8, 0xF8, 0xF8); // F8F8F8

                // Fuentes
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
                        BaseColor.WHITE
                );

                com.itextpdf.text.Font cellFont = new com.itextpdf.text.Font(
                        com.itextpdf.text.Font.FontFamily.HELVETICA,
                        10,
                        com.itextpdf.text.Font.NORMAL,
                        colorTexto
                );

                // Encabezado del documento
                PdfPTable headerTable = new PdfPTable(1);
                headerTable.setWidthPercentage(100);

                // Celda para el título
                PdfPCell titleCell = new PdfPCell();
                titleCell.setBorder(Rectangle.NO_BORDER);
                titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                titleCell.setPadding(10);

                Paragraph title = new Paragraph("ALMA DORADA", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                titleCell.addElement(title);

                Paragraph subtitle = new Paragraph("Informe de Gestión de Stock", subtitleFont);
                subtitle.setAlignment(Element.ALIGN_CENTER);
                subtitle.setSpacingBefore(5);
                titleCell.addElement(subtitle);

                headerTable.addCell(titleCell);

                document.add(headerTable);

                // Espacio después del encabezado
                document.add(new Paragraph(" "));

                // Información del filtro aplicado (si hay)
                String umbralTexto = umbralBusqueda.getText().trim();
                if (!umbralTexto.isEmpty()) {
                    Paragraph filtroInfo = new Paragraph(
                            "Productos con stock menor o igual a: " + umbralTexto + " unidades",
                            new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11,
                                    com.itextpdf.text.Font.ITALIC, colorTextoClaro)
                    );
                    filtroInfo.setAlignment(Element.ALIGN_LEFT);
                    filtroInfo.setSpacingAfter(15);
                    document.add(filtroInfo);
                }

                // Tabla de datos
                PdfPTable pdfTable = new PdfPTable(tabla.getColumnCount());
                pdfTable.setWidthPercentage(100);
                pdfTable.setSpacingBefore(10f);
                pdfTable.setSpacingAfter(10f);

                // Configurar anchos de columnas
                float[] columnWidths = {1f, 3f, 2f, 1.5f}; // ID, Nombre, Categoría, Stock
                pdfTable.setWidths(columnWidths);

                // Encabezados de la tabla
                for (int i = 0; i < tabla.getColumnCount(); i++) {
                    PdfPCell headerCell = new PdfPCell(new Phrase(tabla.getColumnName(i), headerFont));
                    headerCell.setBackgroundColor(colorSecundario);
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    headerCell.setPadding(8);
                    headerCell.setBorderWidth(1);
                    headerCell.setBorderColor(colorFondo2);
                    pdfTable.addCell(headerCell);
                }

                // Filas de datos
                boolean alternar = false;
                for (int row = 0; row < tabla.getRowCount(); row++) {
                    for (int col = 0; col < tabla.getColumnCount(); col++) {
                        String valor = tabla.getValueAt(row, col) != null ?
                                tabla.getValueAt(row, col).toString() : "";

                        PdfPCell cell = new PdfPCell(new Phrase(valor, cellFont));

                        // Alineación según la columna
                        if (col == 0 || col == 3) { // ID y Stock - centrado
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        } else { // Nombre y Categoría - izquierda
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        }

                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setPadding(6);
                        cell.setBorderWidth(0);
                        cell.setBackgroundColor(alternar ? colorFondo1 : colorFondo2);
                        pdfTable.addCell(cell);
                    }
                    alternar = !alternar;
                }

                document.add(pdfTable);

                // Resumen al final
                if (tabla.getRowCount() > 0) {
                    document.add(new Paragraph(" "));
                    Paragraph resumen = new Paragraph(
                            "Total de productos mostrados: " + tabla.getRowCount(),
                            new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11,
                                    com.itextpdf.text.Font.BOLD, colorTexto)
                    );
                    resumen.setAlignment(Element.ALIGN_RIGHT);
                    document.add(resumen);
                }

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
}