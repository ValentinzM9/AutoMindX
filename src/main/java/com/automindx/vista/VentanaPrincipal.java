package com.automindx.vista;

import com.automindx.controlador.ControladorAutomata;
import com.automindx.controlador.ControladorNoDeterminista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class VentanaPrincipal extends JFrame {

    private final ControladorAutomata controladorDFA;
    private final ControladorNoDeterminista controladorAFND;

    private PanelAutomata panelAutomata;
    private PanelControles panelControles;
    private PanelAFND panelAFND;

    private JPanel panelCentro;
    private JPanel panelSuperior;
    private JPanel panelInferior;

    private JLabel etiquetaEstado;
    private JLabel etiquetaModo;

    private JComboBox<String> selectorModo;
    private JButton botonLimpiar;
    private JButton botonSalir;

    private static final Color FONDO =
            new Color(248, 247, 252);

    private static final Color MORADO =
            new Color(106, 76, 147);

    private static final Color MORADO_CLARO =
            new Color(235, 228, 245);

    private static final Color TEXTO =
            new Color(55, 48, 65);

    private static final Color GRIS =
            new Color(110, 105, 118);

    public VentanaPrincipal(
            ControladorAutomata controladorDFA,
            ControladorNoDeterminista controladorAFND) {

        this.controladorDFA = controladorDFA;
        this.controladorAFND = controladorAFND;

        configurarVentana();
        crearComponentes();
        configurarEventos();
    }

    private void configurarVentana() {

        setTitle(
                "AutoMindX | Simulador de Autómatas Finitos"
        );

        setSize(1200, 750);

        setMinimumSize(
                new Dimension(1000, 650)
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLayout(
                new BorderLayout(12, 12)
        );

        getContentPane().setBackground(FONDO);

        getRootPane().setBorder(
                new EmptyBorder(12, 12, 12, 12)
        );
    }

    private void crearComponentes() {

        crearPanelSuperior();

        panelAutomata =
                new PanelAutomata(controladorDFA);

        panelAutomata.setBackground(Color.WHITE);

        panelAutomata.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                new Color(224, 218, 232)
                        ),
                        "Editor visual del DFA"
                )
        );

        panelControles =
                new PanelControles(
                        controladorDFA,
                        panelAutomata
                );

        panelControles.setPreferredSize(
                new Dimension(270, 0)
        );

        panelAFND =
                new PanelAFND(controladorAFND);

        panelAFND.setBackground(Color.WHITE);

        panelAFND.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                new Color(224, 218, 232)
                        ),
                        "Editor visual del AFND"
                )
        );

        panelCentro =
                new JPanel(
                        new BorderLayout(12, 0)
                );

        panelCentro.setOpaque(false);

        mostrarModoDFA();

        add(
                panelSuperior,
                BorderLayout.NORTH
        );

        add(
                panelCentro,
                BorderLayout.CENTER
        );

        crearPanelInferior();

        add(
                panelInferior,
                BorderLayout.SOUTH
        );
    }

    private void crearPanelSuperior() {

        panelSuperior =
                new JPanel(
                        new BorderLayout(20, 0)
                );

        panelSuperior.setBackground(Color.WHITE);

        panelSuperior.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(226, 220, 235)
                        ),
                        new EmptyBorder(
                                15,
                                18,
                                15,
                                18
                        )
                )
        );

        JPanel informacion =
                new JPanel(
                        new GridLayout(2, 1)
                );

        informacion.setOpaque(false);

        JLabel titulo =
                new JLabel("AutoMindX");

        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        titulo.setForeground(MORADO);

        JLabel subtitulo =
                new JLabel(
                        "Explora, construye y valida autómatas finitos"
                );

        subtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        subtitulo.setForeground(GRIS);

        informacion.add(titulo);
        informacion.add(subtitulo);

        JPanel selectorPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                8
                        )
                );

        selectorPanel.setOpaque(false);

        etiquetaModo =
                new JLabel("Modo:");

        etiquetaModo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        etiquetaModo.setForeground(TEXTO);

        selectorModo =
                new JComboBox<>(
                        new String[]{
                                "Autómata determinista (DFA)",
                                "Autómata no determinista (AFND)"
                        }
                );

        selectorModo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        selectorModo.setPreferredSize(
                new Dimension(235, 32)
        );

        selectorModo.setBackground(MORADO_CLARO);
        selectorModo.setForeground(TEXTO);

        selectorPanel.add(etiquetaModo);
        selectorPanel.add(selectorModo);

        panelSuperior.add(
                informacion,
                BorderLayout.WEST
        );

        panelSuperior.add(
                selectorPanel,
                BorderLayout.EAST
        );
    }

    private void crearPanelInferior() {

        panelInferior =
                new JPanel(
                        new BorderLayout(12, 0)
                );

        panelInferior.setBackground(Color.WHITE);

        panelInferior.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(226, 220, 235)
                        ),
                        new EmptyBorder(
                                8,
                                12,
                                8,
                                12
                        )
                )
        );

        etiquetaEstado =
                new JLabel(
                        "●  Listo para trabajar"
                );

        etiquetaEstado.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        etiquetaEstado.setForeground(GRIS);

        botonLimpiar =
                crearBoton(
                        "Limpiar área",
                        MORADO_CLARO,
                        MORADO
                );

        botonSalir =
                crearBoton(
                        "Salir",
                        new Color(248, 232, 235),
                        new Color(156, 65, 83)
                );

        JPanel botones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        botones.setOpaque(false);

        botones.add(botonLimpiar);
        botones.add(botonSalir);

        panelInferior.add(
                etiquetaEstado,
                BorderLayout.WEST
        );

        panelInferior.add(
                new JSeparator(
                        SwingConstants.VERTICAL
                ),
                BorderLayout.CENTER
        );

        panelInferior.add(
                botones,
                BorderLayout.EAST
        );
    }

    private JButton crearBoton(
            String texto,
            Color fondo,
            Color textoColor) {

        JButton boton =
                new JButton(texto);

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        boton.setForeground(textoColor);
        boton.setBackground(fondo);
        boton.setFocusPainted(false);

        boton.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 218, 232)
                        ),
                        new EmptyBorder(
                                7,
                                14,
                                7,
                                14
                        )
                )
        );

        boton.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
                )
        );

        return boton;
    }

    private void configurarEventos() {

        selectorModo.addActionListener(
                e -> cambiarModo()
        );

        botonLimpiar.addActionListener(
                e -> limpiarArea()
        );

        botonSalir.addActionListener(
                e -> System.exit(0)
        );
    }

    private void cambiarModo() {

        if (selectorModo.getSelectedIndex() == 0) {

            mostrarModoDFA();

            etiquetaEstado.setText(
                    "●  Modo DFA seleccionado"
            );

        } else {

            mostrarModoAFND();

            etiquetaEstado.setText(
                    "●  Modo AFND seleccionado"
            );
        }
    }

    private void mostrarModoDFA() {

        panelCentro.removeAll();

        JScrollPane scroll =
                new JScrollPane(panelAutomata);

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        panelCentro.add(
                scroll,
                BorderLayout.CENTER
        );

        panelCentro.add(
                panelControles,
                BorderLayout.EAST
        );

        panelCentro.revalidate();
        panelCentro.repaint();
    }

    private void mostrarModoAFND() {

        panelCentro.removeAll();

        JScrollPane scroll =
                new JScrollPane(panelAFND);

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        panelCentro.add(
                scroll,
                BorderLayout.CENTER
        );

        panelCentro.revalidate();
        panelCentro.repaint();
    }

    private void limpiarArea() {

        if (selectorModo.getSelectedIndex() == 0) {

            controladorDFA.limpiarAutomata();

            panelAutomata.limpiarResaltado();
            panelAutomata.desactivarModosEdicion();
            panelAutomata.repaint();

            etiquetaEstado.setText(
                    "●  Área del DFA limpiada"
            );

        } else {

            controladorAFND.limpiar();

            panelAFND.repaint();

            etiquetaEstado.setText(
                    "●  Área del AFND limpiada"
            );
        }
    }
}