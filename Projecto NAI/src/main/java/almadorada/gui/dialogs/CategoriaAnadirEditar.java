package main.java.almadorada.gui.dialogs;

import main.java.almadorada.model.Categoria;

import javax.swing.*;
import java.awt.*;

public class CategoriaAnadirEditar extends JDialog {

    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private boolean guardado = false;

    public CategoriaAnadirEditar(Frame parent) {
        super(parent, "Añadir Categoría", true);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(getParent());

        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField(20);

        JLabel lblDescripcion = new JLabel("Descripción:");
        txtDescripcion = new JTextArea(5, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCampos.add(lblNombre, gbc);

        gbc.gridx = 1;
        panelCampos.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelCampos.add(lblDescripcion, gbc);

        gbc.gridx = 1;
        panelCampos.add(scrollDescripcion, gbc);

        add(panelCampos, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> {
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
                return;
            }
            guardado = true;
            dispose();
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Categoria getCategoria() {
        return new Categoria(txtNombre.getText().trim(), txtDescripcion.getText().trim());
    }
}
