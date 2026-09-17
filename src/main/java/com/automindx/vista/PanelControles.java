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

public class PanelControles extends JPanel {

    private final ControladorAutomata controlador;
    private final PanelAutomata panelAutomata;

    private JTextField campoCadena;

    private JLabel etiquetaResultado;

    private JButton botonValidar;
    private JButton botonCrearEstado;

    private JRadioButton opcionEjemplo;
    private JRadioButton opcionDiseñar;

    public PanelControles(
            ControladorAutomata controlador,
            PanelAutomata panelAutomata) {

        this.controlador = controlador;
        this.panelAutomata = panelAutomata;

        configurarPanel();
    }

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

        JLabel tituloModo =
                new JLabel(
                        "Modo de trabajo:"
                );

        add(tituloModo);

        opcionEjemplo =
                new JRadioButton(
                        "Probar ejemplo",
                        true
                );

        add(opcionEjemplo);

        opcionDiseñar =
                new JRadioButton(
                        "Diseñar mi autómata"
                );

        add(opcionDiseñar);

        ButtonGroup grupoModos =
                new ButtonGroup();

        grupoModos.add(
                opcionEjemplo
        );

        grupoModos.add(
                opcionDiseñar
        );

        add(
                new JLabel(
                        "------------------------"
                )
        );

        botonCrearEstado =
                new JButton(
                        "CREAR ESTADO"
                );

        add(
                botonCrearEstado
        );

        add(
                new JLabel(
                        "Cadena a validar:"
                )
        );

        campoCadena =
                new JTextField();

        add(
                campoCadena
        );

        botonValidar =
                new JButton(
                        "VALIDAR CADENA"
                );

        add(
                botonValidar
        );

        add(
                new JLabel(
                        "Resultado:"
                )
        );

        etiquetaResultado =
                new JLabel(
                        "Pendiente"
                );

        add(
                etiquetaResultado
        );

        botonCrearEstado.addActionListener(
                e -> activarCreacionEstado()
        );

        botonValidar.addActionListener(
                e -> validarCadena()
        );

        opcionEjemplo.addActionListener(
                e -> cambiarModoEjemplo()
        );

        opcionDiseñar.addActionListener(
                e -> cambiarModoDiseño()
        );
    }

    private void activarCreacionEstado() {

        panelAutomata.activarModoCrearEstado();

        etiquetaResultado.setText(
                "Haz clic en el área de diseño"
        );
    }

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

    private void cambiarModoEjemplo() {

        etiquetaResultado.setText(
                "Modo ejemplo"
        );

        panelAutomata.desactivarModoCrearEstado();
    }

    private void cambiarModoDiseño() {

        etiquetaResultado.setText(
                "Modo diseño"
        );
    }
}