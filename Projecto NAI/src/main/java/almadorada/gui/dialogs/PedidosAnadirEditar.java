package main.java.almadorada.gui.dialogs;

import main.java.almadorada.model.Pedido;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Objects;

public class PedidosAnadirEditar extends JPanel {

    private JTextField campoCliente, campoFecha, campoTotal;
    private JTextArea campoNotas;
    private JComboBox<String> comboEstado;
    private JButton btnGuardar, btnCancelar;

    private Pedido pedidoOriginal;
    private Pedido pedidoEnEdicion;

    private final NumberFormat formatoDecimal;
    private final DateTimeFormatter formatoFecha;

    private ActionListener guardarListener;
    private ActionListener cancelarListener;

    /**
     * Constructor para nuevo pedido.
     */
    public PedidosAnadirEditar() {
        this(null);
    }

    /**
     * Constructor para editar pedido existente.
     * @param pedido Pedido a editar (puede ser null para nuevo)
     */
    public PedidosAnadirEditar(Pedido pedido) {
        formatoDecimal = NumberFormat.getNumberInstance(Locale.US);
        formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (pedido != null) {
            this.pedidoOriginal = pedido.clone();
            this.pedidoEnEdicion = pedido.clone();
        } else {
            this.pedidoOriginal = null;
            this.pedidoEnEdicion = new Pedido();
        }

        initUI();
        cargarPedido();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: Cliente
        gbc.gridx = 0; gbc.gridy = 0;
        panelCampos.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        campoCliente = new JTextField(20);
        panelCampos.add(campoCliente, gbc);

        // Fila 1: Fecha (string para simplificar, formato yyyy-MM-dd)
        gbc.gridx = 0; gbc.gridy = 1;
        panelCampos.add(new JLabel("Fecha (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        campoFecha = new JTextField(10);
        panelCampos.add(campoFecha, gbc);

        // Fila 2: Estado
        gbc.gridx = 0; gbc.gridy = 2;
        panelCampos.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        comboEstado = new JComboBox<>();
        comboEstado.addItem("Pendiente");
        comboEstado.addItem("Confirmado");
        comboEstado.addItem("Enviado");
        comboEstado.addItem("Entregado");
        comboEstado.addItem("Cancelado");
        panelCampos.add(comboEstado, gbc);

        // Fila 3: Total
        gbc.gridx = 0; gbc.gridy = 3;
        panelCampos.add(new JLabel("Total ($):"), gbc);
        gbc.gridx = 1;
        campoTotal = new JTextField(10);
        panelCampos.add(campoTotal, gbc);

        // Fila 4: Notas
        gbc.gridx = 0; gbc.gridy = 4;
        panelCampos.add(new JLabel("Notas:"), gbc);
        gbc.gridx = 1;
        campoNotas = new JTextArea(3, 20);
        campoNotas.setLineWrap(true);
        campoNotas.setWrapStyleWord(true);
        JScrollPane scrollNotas = new JScrollPane(campoNotas);
        panelCampos.add(scrollNotas, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

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

        // Validar que solo números o punto para total
        campoTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE) ||
                        (c == '.' && !campoTotal.getText().contains(".")))) {
                    evt.consume();
                }
            }
        });
    }

    /**
     * Carga los datos si es edición.
     */
    private void cargarPedido() {
        if (pedidoEnEdicion != null && pedidoEnEdicion.getIdPedido() > 0) {
            campoCliente.setText(pedidoEnEdicion.getNombreCliente());
            if (pedidoEnEdicion.getFecha() != null) {
                campoFecha.setText(pedidoEnEdicion.getFecha().format(formatoFecha));
            } else {
                campoFecha.setText("");
            }
            comboEstado.setSelectedItem(pedidoEnEdicion.getEstado());
            campoTotal.setText(formatoDecimal.format(pedidoEnEdicion.getTotal()));
            campoNotas.setText(pedidoEnEdicion.getNotasAdicionales());
        }
    }

    /**
     * Valida y guarda los datos del formulario en el objeto pedidoEnEdicion.
     * @return true si válido, false si hay error
     */
    public boolean guardarPedido() {
        String cliente = campoCliente.getText().trim();
        String fechaStr = campoFecha.getText().trim();
        String estado = (String) comboEstado.getSelectedItem();
        String totalStr = campoTotal.getText().trim();
        String notas = campoNotas.getText().trim();

        if (cliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del cliente no puede estar vacío.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (fechaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La fecha no puede estar vacía.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        LocalDateTime fecha;
        try {
            fecha = LocalDateTime.parse(fechaStr + "T00:00:00");
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use yyyy-MM-dd.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (estado == null || estado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un estado.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        double total;
        try {
            total = formatoDecimal.parse(totalStr).doubleValue();
            if (total < 0) {
                JOptionPane.showMessageDialog(this, "El total no puede ser negativo.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (ParseException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Formato de total inválido. Use números.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Guardar datos en el pedido
        pedidoEnEdicion.setNombreCliente(cliente);
        pedidoEnEdicion.setFecha(fecha);
        pedidoEnEdicion.setEstado(estado);
        pedidoEnEdicion.setTotal(total);
        pedidoEnEdicion.setNotasAdicionales(notas);

        return true;
    }

    /**
     * Comprueba si hay cambios pendientes respecto al pedido original.
     * @return true si hay cambios
     */
    public boolean hayCambiosPendientes() {
        if (pedidoOriginal == null) {
            return !campoCliente.getText().trim().isEmpty() ||
                    !campoFecha.getText().trim().isEmpty() ||
                    campoTotal.getText().trim().length() > 0 ||
                    !campoNotas.getText().trim().isEmpty();
        } else {
            if (!Objects.equals(campoCliente.getText().trim(),
                    pedidoOriginal.getNombreCliente() != null ? pedidoOriginal.getNombreCliente() : "")) return true;

            String fechaStrActual = campoFecha.getText().trim();
            String fechaOriginalStr = pedidoOriginal.getFecha() != null ? pedidoOriginal.getFecha().format(formatoFecha) : "";
            if (!Objects.equals(fechaStrActual, fechaOriginalStr)) return true;

            if (!Objects.equals(comboEstado.getSelectedItem(), pedidoOriginal.getEstado())) return true;

            try {
                double totalActual = formatoDecimal.parse(campoTotal.getText().trim()).doubleValue();
                if (Double.compare(totalActual, pedidoOriginal.getTotal()) != 0) return true;
            } catch (Exception e) {
                return true;
            }

            if (!Objects.equals(campoNotas.getText().trim(),
                    pedidoOriginal.getNotasAdicionales() != null ? pedidoOriginal.getNotasAdicionales() : "")) return true;

            return false;
        }
    }

    public void addGuardarListener(ActionListener listener) {
        this.guardarListener = listener;
    }

    public void addCancelarListener(ActionListener listener) {
        this.cancelarListener = listener;
    }

    public Pedido getPedidoGuardado() {
        return pedidoEnEdicion;
    }
}
