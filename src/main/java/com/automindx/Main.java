package com.automindx;

import com.automindx.controlador.ControladorAutomata;
import com.automindx.vista.VentanaPrincipal;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        aplicarApariencia();

        ControladorAutomata controlador = new ControladorAutomata();

        SwingUtilities.invokeLater(() -> {

            VentanaPrincipal ventana =
                    new VentanaPrincipal(controlador);

            ventana.setVisible(true);
        });
    }

    private static void aplicarApariencia() {

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println(
                    "No se pudo aplicar FlatLaf: "
                            + e.getMessage()
            );
        }
    }
}