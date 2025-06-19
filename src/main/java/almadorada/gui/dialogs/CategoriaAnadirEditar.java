package main.java.almadorada.gui.dialogs;

import main.java.almadorada.gui.ColorPalette;
import main.java.almadorada.model.Categoria;
import main.java.almadorada.service.CategoriaService;

import javax.swing.*;
import java.awt.*;

public class CategoriaAnadirEditar extends JDialog {

    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JLabel tituloLabel;

    private boolean guardado = false;
    private Categoria categoriaGuardada;

    public CategoriaAnadirEditar(Frame parent) {
        super(parent, "Nueva Categoría", true);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setSize(450, 350);
        setLocationRelativeTo(getParent());
        setResizable(false);

        // Panel principal con fondo blanco
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Título
        tituloLabel = new JLabel("Nueva Categoría", JLabel.CENTER);
        tituloLabel.setFont(ColorPalette.FONT_SUBTITLE);
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        panelPrincipal.add(tituloLabel, BorderLayout.NORTH);

        // Panel de campos centrado
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBackground(Color.WHITE);

        // Campo Nombre de la Categoría
        JLabel lblNombre = new JLabel("Nombre de la Categoría");
        lblNombre.setFont(ColorPalette.FONT_NORMAL);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNombre.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(320, 30));
        txtNombre.setMaximumSize(new Dimension(320, 30));
        txtNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        ColorPalette.styleTextField(txtNombre);

        // Espacio entre campos
        Component espacioVertical1 = Box.createVerticalStrut(15);

        // Campo Descripción
        JLabel lblDescripcion = new JLabel("Descripción");
        lblDescripcion.setFont(ColorPalette.FONT_NORMAL);
        lblDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDescripcion.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        txtDescripcion = new JTextArea(4, 25);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setFont(ColorPalette.FONT_NORMAL);
        txtDescripcion.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        scrollDescripcion.setPreferredSize(new Dimension(320, 90));
        scrollDescripcion.setMaximumSize(new Dimension(320, 90));
        scrollDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollDescripcion.setBorder(BorderFactory.createLineBorder(ColorPalette.GRAY_VERY_LIGHT));
        scrollDescripcion.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollDescripcion.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Agregar componentes al panel de campos
        panelCampos.add(lblNombre);
        panelCampos.add(txtNombre);
        panelCampos.add(espacioVertical1);
        panelCampos.add(lblDescripcion);
        panelCampos.add(scrollDescripcion);

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        // Botón Cancelar (secundario)
        JButton btnCancelar = ColorPalette.createSecondaryButton("Cancelar");
        btnCancelar.setFont(ColorPalette.FONT_BUTTON);
        btnCancelar.setPreferredSize(new Dimension(160, 35));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Botón Guardar Categoría (primario)
        JButton btnGuardar = ColorPalette.createPrimaryButton("Guardar Categoría");
        btnGuardar.setFont(ColorPalette.FONT_BUTTON);
        btnGuardar.setPreferredSize(new Dimension(160, 35));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        add(panelPrincipal, BorderLayout.CENTER);

        // Acciones de los botones
        btnGuardar.addActionListener(e -> guardarCategoria());
        btnCancelar.addActionListener(e -> dispose());

        // Establecer foco inicial
        SwingUtilities.invokeLater(() -> txtNombre.requestFocusInWindow());
    }

    private void guardarCategoria() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre no puede estar vacío.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocusInWindow();
            return;
        }

        boolean esNueva = (categoriaGuardada == null || categoriaGuardada.getId() == 0);

        if (esNueva) {
            categoriaGuardada = new Categoria();
        }

        categoriaGuardada.setNombre(nombre);
        categoriaGuardada.setDescripcion(descripcion.isEmpty()
                ? "Una breve descripción de la categoría."
                : descripcion);

        try {
            CategoriaService service = new CategoriaService();

            if (esNueva) {
                service.insertar(categoriaGuardada);
            } else {
                service.actualizar(categoriaGuardada);
            }

            guardado = true;
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar la categoría: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Categoria getCategoria() {
        return categoriaGuardada;
    }

    public void cargarCategoria(Categoria categoria) {
        if (categoria != null) {
            this.categoriaGuardada = categoria;
            txtNombre.setText(categoria.getNombre());
            txtDescripcion.setText(categoria.getDescripcion());
            setTitle("Editar Categoría");
            tituloLabel.setText("Editar Categoría");
        }
    }
}