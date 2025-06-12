package main.java.almadorada.gui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Clase que define la paleta de colores y estilos visuales de la aplicación.
 * Contiene definiciones de colores, fuentes y métodos para estilizar componentes UI.
 */
public class ColorPalette {
    // ========== DEFINICIÓN DE COLORES ==========
    
    /**
     * Colores base en escala de grises
     */
    public static final Color WHITE_LIGHT = new Color(0xF8F8F8);        // Blanco suave
    public static final Color GRAY_VERY_LIGHT = new Color(0xD9D9D9);    // Gris muy claro
    public static final Color GRAY_DARK = new Color(0x4A4A4A);          // Gris oscuro
    public static final Color GRAY_MEDIUM_DARK = new Color(0x6D6D6D);   // Gris medio oscuro

    /**
     * Colores neutros con tonos cálidos
     */
    public static final Color CREAM_VERY_LIGHT = new Color(0xF5F5DC);   // Crema muy claro
    public static final Color CREAM_LIGHT = new Color(0xE0D8C3);        // Crema claro
    public static final Color BEIGE_MEDIUM = new Color(0xD2B48C);       // Beige medio
    public static final Color BROWN_LIGHT = new Color(0xA88B67);        // Marrón claro
    public static final Color BROWN_DARK = new Color(0x8B4513);         // Marrón oscuro

    /**
     * Colores para elementos de acción y énfasis
     */
    public static final Color GOLD_STRONG = new Color(0xC8A849);        // Dorado fuerte
    public static final Color GOLD_INTENSE = new Color(0xDAA520);       // Dorado intenso
    public static final Color EMERALD_GREEN = new Color(0x7ABA7B);      // Verde esmeralda
    public static final Color LIGHT_GREEN = new Color(0x82B28A);        // Verde claro
    public static final Color SALMON_LIGHT = new Color(0xD67B7B);       // Salmón claro
    public static final Color SALMON_INTENSE = new Color(0xE06D6D);     // Salmón intenso

    /**
     * Colores específicos para elementos de la interfaz
     */
    public static final Color TABLE_HEADER_BG = CREAM_LIGHT;            // Fondo de encabezados de tabla
    public static final Color TABLE_SELECTION_BG = CREAM_VERY_LIGHT;    // Fondo de selección en tablas
    public static final Color TABLE_GRID = GRAY_VERY_LIGHT;            // Color de la cuadrícula de tablas

    // ========== DEFINICIÓN DE FUENTES ==========
    
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);      // Fuente para títulos
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);   // Fuente para subtítulos
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);    // Fuente normal
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);     // Fuente para botones
    public static final Font FONT_TOTAL = new Font("Segoe UI", Font.BOLD, 18);      // Fuente para totales

    /**
     * Clase interna que implementa un borde redondeado personalizado.
     */
    public static class RoundedLineBorder extends AbstractBorder {
        private int radius;
        private Color lineColor;
        private Insets borderInsets;

        public RoundedLineBorder(int radius, Color lineColor) {
            this.radius = radius;
            this.lineColor = lineColor;
            this.borderInsets = new Insets(1, 1, 1, 1);
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(lineColor);
            g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return (Insets) borderInsets.clone();
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = borderInsets.left;
            insets.top = borderInsets.top;
            insets.right = borderInsets.right;
            insets.bottom = borderInsets.bottom;
            return insets;
        }
    }

    /**
     * Crea un botón con esquinas redondeadas y estilo personalizado
     */
    private static JButton createRoundedButton(String text, Color bgColor, Color fgColor, 
                                             Color borderColor, int cornerRadius, Insets margin) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 
                                                   cornerRadius, cornerRadius));
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                super.paintBorder(g);
            }
        };

        button.setOpaque(false);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(FONT_NORMAL);
        button.setFocusPainted(false);
        button.setBorder(new RoundedLineBorder(cornerRadius, borderColor));
        button.setMargin(margin);

        return button;
    }

    // ========== MÉTODOS DE CREACIÓN DE COMPONENTES ==========

    /**
     * Crea un botón primario con estilo dorado
     */
    public static JButton createPrimaryButton(String text) {
        return createRoundedButton(text, GOLD_STRONG, Color.WHITE, GOLD_STRONG, 10, 
                                 new Insets(8, 20, 8, 20));
    }

    /**
     * Crea un botón secundario con estilo crema
     */
    public static JButton createSecondaryButton(String text) {
        return createRoundedButton(text, CREAM_LIGHT, GRAY_DARK, CREAM_LIGHT, 10, 
                                 new Insets(8, 15, 8, 15));
    }

    /**
     * Crea un botón de peligro con estilo salmón
     */
    public static JButton createDangerButton(String text) {
        return createRoundedButton(text, SALMON_INTENSE, Color.WHITE, SALMON_LIGHT, 15, 
                                 new Insets(8, 20, 8, 20));
    }

    /**
     * Aplica estilo personalizado a un campo de texto
     */
    public static void styleTextField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRAY_VERY_LIGHT),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(Color.WHITE);
        field.setFont(FONT_NORMAL);
    }

    /**
     * Aplica estilo personalizado a un combobox
     */
    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        comboBox.setBackground(WHITE_LIGHT);
        comboBox.setForeground(GRAY_DARK);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRAY_VERY_LIGHT),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        // Personalización del renderizador de la lista
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                                                        int index, boolean isSelected, 
                                                        boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, 
                                                                         index, isSelected, 
                                                                         cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
                if (isSelected) {
                    label.setBackground(GOLD_STRONG.brighter());
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(WHITE_LIGHT);
                    label.setForeground(GRAY_DARK);
                }
                return label;
            }
        });

        // Estilizar el botón de flecha del combobox
        for (Component comp : comboBox.getComponents()) {
            if (comp instanceof AbstractButton) {
                AbstractButton arrowButton = (AbstractButton) comp;
                arrowButton.setBackground(CREAM_LIGHT);
                arrowButton.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
            }
        }
    }
}