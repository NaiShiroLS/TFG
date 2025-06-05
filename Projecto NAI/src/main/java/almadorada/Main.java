package main.java.almadorada;

import main.java.almadorada.gui.VistaGeneral;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

                VistaGeneral app = new VistaGeneral();
                app.setVisible(true);



        });
    }
}
