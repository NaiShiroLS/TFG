package main.java.almadorada.gui.panels;

import main.java.almadorada.model.Pedido;
import main.java.almadorada.service.PedidoService;
import main.java.almadorada.gui.dialogs.PedidosAnadirEditar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class PedidosPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private PedidoService pedidoService;

    // Filtros básicos para pedidos: por cliente y estado (puedes adaptar)
    private JTextField campoBusquedaCliente;
    private JComboBox<String> comboFiltroEstado;
    private JButton btnBuscar;
    private JButton btnLimpiarFiltros;

    public PedidosPanel() {
        setLayout(new BorderLayout());
        pedidoService = new PedidoService();

        // Panel superior para filtros
        JPanel panelControlesSuperiores = new JPanel(new GridBagLayout());
        panelControlesSuperiores.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Buscar por cliente
        gbc.gridx = 0; gbc.gridy = 0;
        panelControlesSuperiores.add(new JLabel("Buscar por cliente:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        campoBusquedaCliente = new JTextField(20);
        panelControlesSuperiores.add(campoBusquedaCliente, gbc);

        // Filtrar por estado
        gbc.gridx = 2; gbc.gridy = 0;
        panelControlesSuperiores.add(new JLabel("Filtrar por estado:"), gbc);

        gbc.gridx = 3; gbc.gridy = 0;
        comboFiltroEstado = new JComboBox<>();
        cargarEstadosFiltro();
        panelControlesSuperiores.add(comboFiltroEstado, gbc);

        // Botones buscar y limpiar
        gbc.gridx = 4; gbc.gridy = 0;
        btnBuscar = new JButton("Buscar");
        panelControlesSuperiores.add(btnBuscar, gbc);

        gbc.gridx = 5; gbc.gridy = 0;
        btnLimpiarFiltros = new JButton("Limpiar Filtros");
        panelControlesSuperiores.add(btnLimpiarFiltros, gbc);

        add(panelControlesSuperiores, BorderLayout.NORTH);

        // Configuración tabla
        modelo = new DefaultTableModel(new Object[]{"ID", "Cliente", "Fecha", "Estado", "Total", "Editar", "Eliminar"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo editar y eliminar son editables
                return column == getColumnCount() - 1 || column == getColumnCount() - 2;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == getColumnCount() - 1 || column == getColumnCount() - 2) {
                    return JButton.class;
                }
                if (column == 2) { // Fecha
                    return String.class;
                }
                if (column == 4) { // Total (precio)
                    return String.class;
                }
                return super.getColumnClass(column);
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // Renderizadores y editores botones
        ButtonEditor editorEditar = new ButtonEditor(new JTextField(), row -> editarPedido(row), "Editar");
        ButtonRenderer rendererEditar = new ButtonRenderer("Editar");
        tabla.getColumnModel().getColumn(modelo.getColumnCount() - 2).setCellRenderer(rendererEditar);

        ButtonEditor editorEliminar = new ButtonEditor(new JTextField(), row -> eliminarSeleccionado(row), "Eliminar");
        ButtonRenderer rendererEliminar = new ButtonRenderer("Eliminar");
        tabla.getColumnModel().getColumn(modelo.getColumnCount() - 1).setCellRenderer(rendererEliminar);
        tabla.getColumnModel().getColumn(modelo.getColumnCount() - 1).setCellEditor(editorEliminar);

        // Ajustar ancho columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150); // Cliente
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100); // Fecha
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100); // Estado
        tabla.getColumnModel().getColumn(4).setPreferredWidth(80);  // Total
        tabla.getColumnModel().getColumn(5).setPreferredWidth(80);  // Editar
        tabla.getColumnModel().getColumn(6).setPreferredWidth(80);  // Eliminar

        // Panel inferior con botón Añadir Pedido
        JPanel panelBotonesInferiores = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAnadir = new JButton("Añadir Pedido");
        panelBotonesInferiores.add(btnAnadir);
        add(panelBotonesInferiores, BorderLayout.SOUTH);

        // Listeners
        btnAnadir.addActionListener(e -> abrirDialogoAnadir());
        btnBuscar.addActionListener(e -> filterPedidos());
        btnLimpiarFiltros.addActionListener(e -> {
            campoBusquedaCliente.setText("");
            comboFiltroEstado.setSelectedItem(null);
            cargarPedidos();
        });

        cargarPedidos();
    }

    private void cargarEstadosFiltro() {
        comboFiltroEstado.removeAllItems();
        comboFiltroEstado.addItem(null); // opción sin filtro
        comboFiltroEstado.addItem("Pendiente");
        comboFiltroEstado.addItem("En Proceso");
        comboFiltroEstado.addItem("Completado");
        comboFiltroEstado.addItem("Cancelado");
        // Agrega los estados que manejes en tu modelo
    }

    private void cargarPedidos() {
        modelo.setRowCount(0);
        List<Pedido> pedidos = pedidoService.obtenerTodos();
        for (Pedido p : pedidos) {
            modelo.addRow(new Object[]{
                    p.getIdPedido(),
                    p.getNombreCliente(),
                    p.getFecha().toString(),  // formatea según necesites
                    p.getEstado(),
                    String.format("%.2f", p.getTotal()),
                    "Editar",
                    "Eliminar"
            });
        }
    }

    private void filterPedidos() {
        modelo.setRowCount(0);
        String clienteFiltro = campoBusquedaCliente.getText().trim().toLowerCase();
        String estadoFiltro = (String) comboFiltroEstado.getSelectedItem();

        List<Pedido> pedidos = pedidoService.obtenerTodos(); // filtrar en memoria para simplicidad
        for (Pedido p : pedidos) {
            boolean matchesCliente = clienteFiltro.isEmpty() || p.getNombreCliente().toLowerCase().contains(clienteFiltro);
            boolean matchesEstado = (estadoFiltro == null) || p.getEstado().equalsIgnoreCase(estadoFiltro);

            if (matchesCliente && matchesEstado) {
                modelo.addRow(new Object[]{
                        p.getIdPedido(),
                        p.getNombreCliente(),
                        p.getFecha().toString(),
                        p.getEstado(),
                        String.format("%.2f", p.getTotal()),
                        "Editar",
                        "Eliminar"
                });
            }
        }
    }

    private void abrirDialogoAnadir() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Añadir Pedido", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(550, 600);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

        PedidosAnadirEditar pedidoForm = new PedidosAnadirEditar();
        dialog.add(pedidoForm, BorderLayout.CENTER);

        pedidoForm.addGuardarListener(e -> {
            if (pedidoForm.guardarPedido()) {
                Pedido nuevoPedido = pedidoForm.getPedidoGuardado();
                // Aquí puedes validar duplicados si tiene sentido
                pedidoService.insertar(nuevoPedido);
                cargarPedidos();
                dialog.dispose();
            }
        });

        pedidoForm.addCancelarListener(e -> {
            if (pedidoForm.hayCambiosPendientes()) {
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

    private void editarPedido(int rowIndex) {
        int idPedido = (int) modelo.getValueAt(rowIndex, 0);
        Optional<Pedido> pedidoOpt = pedidoService.obtenerPorId(idPedido);

        if (pedidoOpt.isPresent()) {
            Pedido pedidoAEditar = pedidoOpt.get();

            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Pedido", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(550, 600);
            dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

            PedidosAnadirEditar pedidoForm = new PedidosAnadirEditar(pedidoAEditar);
            dialog.add(pedidoForm, BorderLayout.CENTER);

            pedidoForm.addGuardarListener(e -> {
                if (pedidoForm.guardarPedido()) {
                    Pedido pedidoActualizado = pedidoForm.getPedidoGuardado();
                    // Validar duplicados si aplica
                    pedidoService.actualizar(pedidoActualizado);
                    cargarPedidos();
                    dialog.dispose();
                }
            });

            pedidoForm.addCancelarListener(e -> {
                if (pedidoForm.hayCambiosPendientes()) {
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
            JOptionPane.showMessageDialog(this, "No se encontró el pedido seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarSeleccionado(int rowIndex) {
        int idPedido = (int) modelo.getValueAt(rowIndex, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar el pedido con ID " + idPedido + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            pedidoService.eliminar(idPedido);
            cargarPedidos();
        }
    }

    // Clases auxiliares para botones en tabla

    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String texto) {
            setText(texto);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object obj,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    private static class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private Consumer<Integer> accion;
        private int fila;

        public ButtonEditor(JTextField txt, Consumer<Integer> accion, String editar) {
            super(txt);
            this.accion = accion;
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object obj,
                                                     boolean isSelected, int row, int col) {
            this.fila = row;
            button.setText(obj == null ? "" : obj.toString());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            accion.accept(fila);
            return button.getText();
        }
    }

}
