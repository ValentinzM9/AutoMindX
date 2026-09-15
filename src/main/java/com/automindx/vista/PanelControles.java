package com.automindx.vista;

import com.automindx.controlador.ControladorAutomata;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.GridLayout;

/**
 * Panel de controles de AutoMindX.
 */
public class PanelControles extends JPanel {

    private ControladorAutomata controlador;

    private JTextField campoCadena;

    private JLabel etiquetaResultado;

    private JButton botonValidar;

    private JRadioButton opcionEjemplo;
    private JRadioButton opcionDiseñar;

    public PanelControles(
            ControladorAutomata controlador) {

        this.controlador = controlador;

        configurarPanel();
    }

    /**
     * Configura el panel.
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

        /*
         * Título del modo.
         */
        JLabel tituloModo =
                new JLabel(
                        "Modo de trabajo:"
                );

        add(tituloModo);

        /*
         * Opción para probar el ejemplo.
         */
        opcionEjemplo =
                new JRadioButton(
                        "Probar ejemplo",
                        true
                );

        add(opcionEjemplo);

        /*
         * Opción para diseñar.
         */
        opcionDiseñar =
                new JRadioButton(
                        "Diseñar mi autómata"
                );

        add(opcionDiseñar);

        /*
         * Agrupar las dos opciones.
         */
        ButtonGroup grupoModos =
                new ButtonGroup();

        grupoModos.add(opcionEjemplo);
        grupoModos.add(opcionDiseñar);

        /*
         * Separación visual.
         */
        add(
                new JLabel(
                        "------------------------"
                )
        );

        /*
         * Cadena.
         */
        JLabel etiquetaCadena =
                new JLabel(
                        "Cadena a validar:"
                );

        add(etiquetaCadena);

        /*
         * Campo de cadena.
         */
        campoCadena =
                new JTextField();

        add(campoCadena);

        /*
         * Botón validar.
         */
        botonValidar =
                new JButton(
                        "VALIDAR CADENA"
                );

        add(botonValidar);

        /*
         * Título del resultado.
         */
        JLabel tituloResultado =
                new JLabel(
                        "Resultado:"
                );

        add(tituloResultado);

        /*
         * Resultado.
         */
        etiquetaResultado =
                new JLabel(
                        "Pendiente"
                );

        add(etiquetaResultado);

        /*
         * Evento de validación.
         */
        botonValidar.addActionListener(
                e -> validarCadena()
        );

        /*
         * Evento del modo de trabajo.
         */
        opcionEjemplo.addActionListener(
                e -> cambiarModoEjemplo()
        );

        opcionDiseñar.addActionListener(
                e -> cambiarModoDiseño()
        );
    }

    /**
     * Valida la cadena ingresada.
     */
    private void validarCadena() {

        String cadena =
                campoCadena.getText();

        boolean resultado =
                controlador.validarCadena(
                        cadena
                );

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

    /**
     * Acción cuando se selecciona
     * el modo de ejemplo.
     */
    private void cambiarModoEjemplo() {

        etiquetaResultado.setText(
                "Modo ejemplo"
        );
    }

    /**
     * Acción cuando se selecciona
     * el modo de diseño.
     */
    private void cambiarModoDiseño() {

        etiquetaResultado.setText(
                "Modo diseño"
        );
    }
}