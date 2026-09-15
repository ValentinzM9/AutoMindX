package com.automindx.vista;

import com.automindx.controlador.ControladorAutomata;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Ventana principal de AutoMindX.
 */
public class VentanaPrincipal extends JFrame {

    private ControladorAutomata controlador;

    private PanelAutomata panelAutomata;
    private PanelControles panelControles;

    public VentanaPrincipal(
            ControladorAutomata controlador) {

        this.controlador = controlador;

        configurarVentana();
        crearComponentes();
    }

    /**
     * Configura las características principales
     * de la ventana.
     */
    private void configurarVentana() {

        setTitle(
                "AutoMindX - Simulador de Autómatas"
        );

        setSize(
                1000,
                650
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLayout(
                new BorderLayout()
        );
    }

    /**
     * Crea y organiza los componentes
     * de la ventana.
     */
    private void crearComponentes() {

        /*
         * Panel donde se dibuja el autómata.
         */
        panelAutomata =
                new PanelAutomata(
                        controlador.getAutomata()
                );

        /*
         * Panel de controles.
         */
        panelControles =
                new PanelControles(
                        controlador
                );

        add(
                panelAutomata,
                BorderLayout.CENTER
        );

        /*
         * Agregar controles a la derecha.
         */
        add(
                panelControles,
                BorderLayout.EAST
        );
    }
}