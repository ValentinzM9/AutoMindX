package com.automindx;

import com.automindx.controlador.ControladorAutomata;
import com.automindx.controlador.ControladorNoDeterminista;
import com.automindx.vista.VentanaPrincipal;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        aplicarApariencia();

        SwingUtilities.invokeLater(() -> {
            ControladorAutomata controladorDFA =
                    new ControladorAutomata();

            ControladorNoDeterminista controladorAFND =
                    new ControladorNoDeterminista();

            new VentanaPrincipal(
                    controladorDFA,
                    controladorAFND
            );
        });
    }

    private static void aplicarApariencia() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println(
                    "No se pudo aplicar FlatLaf: " + e.getMessage()
            );
        }
    }
}