package com.automindx.vista;

import com.automindx.controlador.ControladorAutomata;
import com.automindx.modelo.Validador;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

public class PanelControles extends JPanel {

    private final ControladorAutomata controlador;
    private final PanelAutomata panelAutomata;

    private JTextField campoCadena;

    private JLabel etiquetaResultado;

    private JButton botonValidar;
    private JButton botonCrearEstado;
    private JButton botonCrearTransicion;

    private JRadioButton opcionEjemplo;
    private JRadioButton opcionDiseñar;

    private static final Color COLOR_ACEPTADA =
            new Color(46, 125, 50);

    private static final Color COLOR_RECHAZADA =
            new Color(198, 40, 40);

    public PanelControles(
            ControladorAutomata controlador,
            PanelAutomata panelAutomata) {

        this.controlador = controlador;
        this.panelAutomata = panelAutomata;

        configurarPanel();

        cambiarModoDiseño();
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
                        6,
                        6
                )
        );

        JLabel tituloModo =
                new JLabel(
                        "Modo de trabajo:"
                );

        tituloModo.setFont(
                tituloModo.getFont()
                        .deriveFont(Font.BOLD)
        );

        add(tituloModo);

        opcionDiseñar =
                new JRadioButton(
                        "Diseñar mi autómata",
                        true
                );

        opcionEjemplo =
                new JRadioButton(
                        "Probar ejemplo",
                        false
                );

        add(opcionDiseñar);
        add(opcionEjemplo);

        ButtonGroup grupoModos =
                new ButtonGroup();

        grupoModos.add(opcionDiseñar);
        grupoModos.add(opcionEjemplo);

        add(new javax.swing.JSeparator());

        botonCrearEstado =
                new JButton(
                        "+ CREAR ESTADO"
                );

        botonCrearTransicion =
                new JButton(
                        "→ CREAR TRANSICIÓN"
                );

        add(botonCrearEstado);
        add(botonCrearTransicion);

        JLabel ayudaEdicion =
                new JLabel(
                        "<html><i>"
                                + "Clic derecho sobre un estado o<br>"
                                + "transición: más opciones."
                                + "</i></html>"
                );

        ayudaEdicion.setFont(
                ayudaEdicion.getFont()
                        .deriveFont(11f)
        );

        add(ayudaEdicion);

        add(new javax.swing.JSeparator());

        add(
                new JLabel(
                        "Cadena a validar:"
                )
        );

        campoCadena =
                new JTextField();

        add(campoCadena);

        botonValidar =
                new JButton(
                        "VALIDAR CADENA"
                );

        add(botonValidar);

        add(
                new JLabel(
                        "Resultado:"
                )
        );

        etiquetaResultado =
                new JLabel(
                        "Pendiente"
                );

        add(etiquetaResultado);

        botonCrearEstado.addActionListener(
                e -> activarCreacionEstado()
        );

        botonCrearTransicion.addActionListener(
                e -> activarCreacionTransicion()
        );

        botonValidar.addActionListener(
                e -> validarCadena()
        );

        campoCadena.addActionListener(
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

        panelAutomata.limpiarResaltado();

        panelAutomata.activarModoCrearEstado();

        etiquetaResultado.setForeground(
                Color.BLACK
        );

        etiquetaResultado.setText(
                "Haz clic en el área de diseño"
        );
    }

    private void activarCreacionTransicion() {

        panelAutomata.limpiarResaltado();

        panelAutomata.activarModoCrearTransicion();

        etiquetaResultado.setForeground(
                Color.BLACK
        );

        etiquetaResultado.setText(
                "<html>Clic en el estado origen y luego<br>"
                        + "en el estado destino</html>"
        );
    }

    private void validarCadena() {

        String cadena =
                campoCadena.getText();

        Validador.Resultado resultado =
                controlador.validarCadenaDetallado(
                        cadena
                );

        boolean aceptada =
                resultado
                        == Validador.Resultado.ACEPTADA;

        panelAutomata.resaltarRecorrido(
                controlador
                        .getValidador()
                        .getRecorrido(),

                controlador
                        .getValidador()
                        .getTransicionesRecorridas(),

                aceptada
        );

        etiquetaResultado.setForeground(
                aceptada
                        ? COLOR_ACEPTADA
                        : COLOR_RECHAZADA
        );

        etiquetaResultado.setText(
                mensajePara(resultado)
        );
    }

    private String mensajePara(
            Validador.Resultado resultado) {

        Validador validador =
                controlador.getValidador();

        switch (resultado) {

            case ACEPTADA:

                return "<html>ACEPTADA</html>";

            case RECHAZADA_SIN_ESTADO_INICIAL:

                return "<html>RECHAZADA<br>"
                        + "Falta estado inicial</html>";

            case RECHAZADA_TRANSICION_INEXISTENTE:

                String simbolo =
                        validador.getSimboloError() == null
                                ? ""
                                : String.valueOf(
                                        validador.getSimboloError()
                                );

                String estado =
                        validador.getEstadoError() == null
                                ? ""
                                : validador
                                        .getEstadoError()
                                        .getNombre();

                return "<html>RECHAZADA<br>"
                        + "No existe transición con '"
                        + simbolo
                        + "' desde "
                        + estado
                        + "</html>";

            case RECHAZADA_ESTADO_NO_FINAL:

                String estadoFinal =
                        validador.getEstadoError() == null
                                ? ""
                                : validador
                                        .getEstadoError()
                                        .getNombre();

                return "<html>RECHAZADA<br>"
                        + "Termina en "
                        + estadoFinal
                        + ", que no es final</html>";

            default:

                return "Pendiente";
        }
    }

    private void cambiarModoEjemplo() {

        controlador.cargarAutomataEjemplo();

        panelAutomata.limpiarResaltado();
        panelAutomata.desactivarModosEdicion();
        panelAutomata.repaint();

        aplicarModoSoloLectura(true);

        etiquetaResultado.setForeground(
                Color.BLACK
        );

        etiquetaResultado.setText(
                "<html>Ejemplo cargado.<br>"
                        + "Escribe una cadena y valida.</html>"
        );
    }

    private void cambiarModoDiseño() {

        controlador.limpiarAutomata();

        panelAutomata.limpiarResaltado();
        panelAutomata.desactivarModosEdicion();
        panelAutomata.repaint();

        aplicarModoSoloLectura(false);

        etiquetaResultado.setForeground(
                Color.BLACK
        );

        etiquetaResultado.setText(
                "<html>Lienzo limpio.<br>"
                        + "Crea estados y transiciones.</html>"
        );
    }

    private void aplicarModoSoloLectura(
            boolean soloLectura) {

        botonCrearEstado.setEnabled(
                !soloLectura
        );

        botonCrearTransicion.setEnabled(
                !soloLectura
        );
    }
}