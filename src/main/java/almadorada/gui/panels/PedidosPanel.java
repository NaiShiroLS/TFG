package main.java.almadorada.gui.panels;

import main.java.almadorada.gui.ColorPalette;
import main.java.almadorada.model.Pedido;
import main.java.almadorada.service.PedidoService;
import main.java.almadorada.gui.dialogs.PedidosAnadirEditar;
import main.java.almadorada.gui.dialogs.PedidoDetalles;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PedidosPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private PedidoService pedidoService;

    private JTextField txtFiltroCliente;
    private JComboBox<String> cmbFiltroEstado;

    public PedidosPanel() {
        pedidoService = new PedidoService();

        setLayout(new BorderLayout());
        setBackground(ColorPalette.WHITE_LIGHT);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(ColorPalette.WHITE_LIGHT);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Título
        JLabel titulo = new JLabel("Gestión de Pedidos", SwingConstants.CENTER);
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
        JPanel panelFiltros = new JPanel(new BorderLayout()); // Changed to BorderLayout
        panelFiltros.setBackground(Color.WHITE);
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Container for left-aligned filters
        izquierda.setBackground(Color.WHITE); //

        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setFont(ColorPalette.FONT_NORMAL);
        lblCliente.setForeground(ColorPalette.GRAY_DARK);
        izquierda.add(lblCliente); // Added to 'izquierda'

        txtFiltroCliente = new JTextField(15);
        txtFiltroCliente.setFont(ColorPalette.FONT_NORMAL);
        txtFiltroCliente.setPreferredSize(new Dimension(180, 28));
        txtFiltroCliente.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
        txtFiltroCliente.setText("");
        txtFiltroCliente.setForeground(Color.GRAY);
        izquierda.add(txtFiltroCliente);

        izquierda.add(Box.createHorizontalStrut(20)); // Espacio entre campos de filtro

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(ColorPalette.FONT_NORMAL);
        lblEstado.setForeground(ColorPalette.GRAY_DARK);
        izquierda.add(lblEstado);

        cmbFiltroEstado = new JComboBox<>(new String[]{"Todos", "Pendiente", "En proceso", "Enviado", "Entregado", "Cancelado"});
        cmbFiltroEstado.setFont(ColorPalette.FONT_NORMAL);
        cmbFiltroEstado.setPreferredSize(new Dimension(180, 28));
        cmbFiltroEstado.setBackground(Color.WHITE);
        cmbFiltroEstado.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
        cmbFiltroEstado.setCursor(new Cursor(Cursor.HAND_CURSOR));
        izquierda.add(cmbFiltroEstado); // Added to 'izquierda'

        JButton btnFiltrar = ColorPalette.createSecondaryButton("Aplicar Filtros");
        btnFiltrar.setFont(ColorPalette.FONT_BUTTON);
        btnFiltrar.setPreferredSize(new Dimension(130, 30));
        btnFiltrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // New panel for right-aligned button
        derecha.setBackground(Color.WHITE); //
        derecha.add(btnFiltrar); // Added button to 'derecha'

        panelFiltros.add(izquierda, BorderLayout.WEST); // Add left-aligned filters to WEST
        panelFiltros.add(derecha, BorderLayout.EAST); // Add right-aligned button to EAST

        panelContenido.add(panelFiltros, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{
                "ID", "Cliente", "Fecha", "Total", "Estado", "Editar", "Detalles"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6; // Editar, Detalles
            }
        };

        // Configuración de la tabla
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

        tabla.getColumnModel().getColumn(0).setPreferredWidth(40); // ID
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200); // Cliente
        tabla.getColumnModel().getColumn(2).setPreferredWidth(120); // Fecha
        tabla.getColumnModel().getColumn(3).setPreferredWidth(80);  // Total
        tabla.getColumnModel().getColumn(4).setPreferredWidth(100); // Estado
        tabla.getColumnModel().getColumn(5).setPreferredWidth(80);  // Editar
        tabla.getColumnModel().getColumn(6).setPreferredWidth(80);  // Detalles

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
        scroll.getViewport().setBackground(Color.WHITE);
        panelContenido.add(scroll, BorderLayout.CENTER);

        // Panel de botones inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));


        JButton btnAnadir = ColorPalette.createPrimaryButton("Añadir Nuevo Pedido");
        btnAnadir.setPreferredSize(new Dimension(180, 35));
        btnAnadir.setFont(ColorPalette.FONT_BUTTON);
        btnAnadir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelInferior.add(btnAnadir);

        panelContenido.add(panelInferior, BorderLayout.SOUTH);

        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        add(panelPrincipal, BorderLayout.CENTER);

        // Event listeners
        btnAnadir.addActionListener(e -> abrirDialogoAñadir());
        btnFiltrar.addActionListener(e -> cargarPedidosFiltrados(txtFiltroCliente.getText().trim(),
                (String) cmbFiltroEstado.getSelectedItem()));

        // Configurar renderers y editores para botones en la tabla
        tabla.getColumn("Editar").setCellRenderer(new BotonRenderer("Editar", ColorPalette.BROWN_LIGHT));
        tabla.getColumn("Editar").setCellEditor(new BotonEditor("Editar", this::editarPedido, ColorPalette.BROWN_LIGHT));
        tabla.getColumn("Detalles").setCellRenderer(new BotonRenderer("Ver Detalles", ColorPalette.GOLD_INTENSE)); // Changed color to blue light
        tabla.getColumn("Detalles").setCellEditor(new BotonEditor("Ver Detalles", this::verDetallesPedido, ColorPalette.BEIGE_MEDIUM)); // Changed color to blue light

        // Cargar datos iniciales
        cargarPedidosFiltrados("", "Todos");
    }

    private void cargarPedidosFiltrados(String clienteFiltro, String estadoFiltro) {
        modelo.setRowCount(0);
        List<Pedido> lista = pedidoService.obtenerTodos();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Spanish date format without time

        for (Pedido p : lista) {
            boolean coincideCliente = clienteFiltro == null || clienteFiltro.isEmpty()
                    || p.getNombreCliente().toLowerCase().contains(clienteFiltro.toLowerCase());
            boolean coincideEstado = estadoFiltro == null || estadoFiltro.equals("Todos")
                    || p.getEstado().equalsIgnoreCase(estadoFiltro);

            if (coincideCliente && coincideEstado) {
                modelo.addRow(new Object[]{
                        p.getIdPedido(),
                        p.getNombreCliente(),
                        p.getFecha().format(formato), // Formato de fecha aplicado
                        String.format("%.2f €", p.getTotal()), // Format total as currency
                        p.getEstado(),
                        "Editar",
                        "Ver Detalles"
                });
            }
        }
    }

    private void abrirDialogoAñadir() {
        PedidosAnadirEditar dialog = new PedidosAnadirEditar((Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        if (dialog.isGuardado()) {
            cargarPedidosFiltrados(txtFiltroCliente.getText().trim(), (String) cmbFiltroEstado.getSelectedItem());
        }
    }

    private void verDetallesPedido(int fila) {
        int id = (int) modelo.getValueAt(fila, 0);
        PedidoDetalles dialog = new PedidoDetalles(id, () -> {
            cargarPedidosFiltrados(txtFiltroCliente.getText().trim(), (String) cmbFiltroEstado.getSelectedItem());
        });
        dialog.setVisible(true);
    }

    private void editarPedido(int fila) {
        int id = (int) modelo.getValueAt(fila, 0);
        Pedido pedido = pedidoService.obtenerPorId(id);
        if (pedido != null) {
            PedidosAnadirEditar dialog = new PedidosAnadirEditar((Frame) SwingUtilities.getWindowAncestor(this), pedido);
            dialog.setVisible(true);
            if (dialog.isGuardado()) {
                cargarPedidosFiltrados(txtFiltroCliente.getText().trim(), (String) cmbFiltroEstado.getSelectedItem());
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el pedido con ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Renderer para botones con colores específicos
    static class BotonRenderer extends JButton implements TableCellRenderer {
        private Color buttonColor;

        public BotonRenderer(String texto, Color color) {
            setText(texto);
            this.buttonColor = color;
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 11)); // Smaller font for buttons
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true)); // Thinner border
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            setBackground(buttonColor);
            return this;
        }
    }

    // Editor para botones con interfaz funcional
    static class BotonEditor extends DefaultCellEditor {
        private final JButton button;
        private final BotonAccion accion;
        private int fila;
        private String textoBoton;
        private Color buttonColor;

        public BotonEditor(String texto, BotonAccion accion, Color color) {
            super(new JCheckBox());
            this.accion = accion;
            this.textoBoton = texto;
            this.buttonColor = color;

            this.button = new JButton(texto);
            this.button.setOpaque(true);
            this.button.setFont(new Font("Segoe UI", Font.BOLD, 11));
            this.button.setBackground(buttonColor);
            this.button.setForeground(Color.WHITE);
            this.button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));
            this.button.setFocusPainted(false);
            this.button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            this.button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            fila = row;
            button.setText(textoBoton);
            button.setBackground(buttonColor);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            SwingUtilities.invokeLater(() -> accion.ejecutar(fila));
            return textoBoton;
        }

        @Override
        public boolean stopCellEditing() {
            return super.stopCellEditing();
        }

        @FunctionalInterface
        public interface BotonAccion {
            void ejecutar(int fila);
        }
    }
}