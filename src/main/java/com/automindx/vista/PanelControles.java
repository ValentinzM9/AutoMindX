package com.automindx.vista;

import com.automindx.controlador.ControladorAutomata;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;

/**
 * Panel que contiene los controles para interactuar
 * con el autómata.
 */
public class PanelControles extends JPanel {

    private ControladorAutomata controlador;

    private JTextField campoCadena;
    private JLabel etiquetaResultado;
    private JButton botonValidar;

    public PanelControles(
            ControladorAutomata controlador) {

        this.controlador = controlador;

        configurarPanel();
    }

    /**
     * Configura los componentes del panel.
     */
    private void configurarPanel() {

        setBorder(
                BorderFactory.createTitledBorder(
                        "Controles"
                )
        );

        setLayout(
                new GridLayout(
                        0,
                        1,
                        5,
                        5
                )
        );

        // Título del campo
        JLabel etiquetaCadena =
                new JLabel("Cadena a validar:");

        add(etiquetaCadena);

        // Campo donde el usuario escribe la cadena
        campoCadena =
                new JTextField();

        add(campoCadena);

        // Botón para validar
        botonValidar =
                new JButton("VALIDAR CADENA");

        add(botonValidar);

        // Etiqueta del resultado
        JLabel etiquetaTituloResultado =
                new JLabel("Resultado:");

        add(etiquetaTituloResultado);

        // Resultado de la validación
        etiquetaResultado =
                new JLabel("Pendiente");

        add(etiquetaResultado);

        // Evento del botón
        botonValidar.addActionListener(
                e -> validarCadena()
        );
    }

    /**
     * Obtiene la cadena escrita por el usuario
     * y la envía al controlador.
     */
    private void validarCadena() {

        String cadena =
                campoCadena.getText();

        boolean resultado =
                controlador.validarCadena(cadena);

        if (resultado) {

            etiquetaResultado.setText(
                    "ACEPTADA"
            );

        } else {

            etiquetaResultado.setText(
                    "RECHAZADA"
            );
        }
    }
}
