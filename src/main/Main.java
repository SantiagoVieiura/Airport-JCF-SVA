/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import com.formdev.flatlaf.FlatDarkLaf;
import core.view.AirportFrame;
import java.io.IOException;
import javax.swing.UIManager;

/**
 *
 * @author sebas
 */
public class Main {
    public static void main(String args[]) {
        System.setProperty("flatlaf.useNativeLibrary", "false");
        
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new AirportFrame().setVisible(true);
                } catch (IOException ex) {
                    javax.swing.JOptionPane.showMessageDialog(null,
                        "Ocurrió un error al iniciar la aplicación:\n" + ex.getMessage(),
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace(); // Opcional: para ver el detalle en consola
                }
            }
        });
    }
}
