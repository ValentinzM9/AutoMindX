package com.automindx.vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.automindx.controlador.ControladorAutomata;

public class VentanaPrincipal extends JFrame {

    private final ControladorAutomata controlador;

    private PanelAutomata panelAutomata;
    private PanelControles panelControles;

    private JPanel panelSuperior;
    private JPanel panelInferior;

    private JLabel etiquetaTitulo;
    private JLabel etiquetaEstado;

    private JButton botonLimpiar;
    private JButton botonSalir;

    public VentanaPrincipal(ControladorAutomata controlador) {

        this.controlador = controlador;

        configurarVentana();
        crearComponentes();
        configurarEventos();
    }

    private void configurarVentana() {

        setTitle(
                "AutoMindX - Simulador de Autómatas Finitos"
        );

        setSize(
                1200,
                750
        );

        setMinimumSize(
                new Dimension(
                        1000,
                        650
                )
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        getRootPane().setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );
    }

    private void crearComponentes() {

        crearPanelSuperior();

        panelAutomata =
                new PanelAutomata(
                        controlador.getAutomata()
                );

        panelAutomata.setBorder(
                BorderFactory.createTitledBorder(
                        "Área de diseño del autómata"
                )
        );

        panelControles =
                new PanelControles(
                        controlador,
                        panelAutomata
                );

        panelControles.setPreferredSize(
                new Dimension(
                        260,
                        0
                )
        );

        JScrollPane scrollAutomata =
                new JScrollPane(
                        panelAutomata
                );

        scrollAutomata.setBorder(
                BorderFactory.createEmptyBorder()
        );

        add(
                panelSuperior,
                BorderLayout.NORTH
        );

        add(
                scrollAutomata,
                BorderLayout.CENTER
        );

        add(
                panelControles,
                BorderLayout.EAST
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
                        new BorderLayout()
                );

        panelSuperior.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                java.awt.Color.LIGHT_GRAY
                        ),
                        new EmptyBorder(
                                5,
                                5,
                                10,
                                5
                        )
                )
        );

        etiquetaTitulo =
                new JLabel(
                        "AutoMindX"
                );

        etiquetaTitulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        JLabel subtitulo =
                new JLabel(
                        "Simulador visual de autómatas finitos deterministas"
                );

        subtitulo.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        JPanel informacion =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        informacion.add(
                etiquetaTitulo
        );

        informacion.add(
                subtitulo
        );

        panelSuperior.add(
                informacion,
                BorderLayout.WEST
        );
    }

    private void crearPanelInferior() {

        panelInferior =
                new JPanel(
                        new BorderLayout()
                );

        panelInferior.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                1,
                                0,
                                0,
                                0,
                                java.awt.Color.LIGHT_GRAY
                        ),
                        new EmptyBorder(
                                8,
                                5,
                                5,
                                5
                        )
                )
        );

        etiquetaEstado =
                new JLabel(
                        "Listo para trabajar"
                );

        etiquetaEstado.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        botonLimpiar =
                new JButton(
                        "LIMPIAR ÁREA"
                );

        botonSalir =
                new JButton(
                        "SALIR"
                );

        JPanel panelBotones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                5,
                                0
                        )
                );

        panelBotones.add(
                botonLimpiar
        );

        panelBotones.add(
                botonSalir
        );

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
                panelBotones,
                BorderLayout.EAST
        );
    }

    private void configurarEventos() {

        botonSalir.addActionListener(
                e -> System.exit(0)
        );

        botonLimpiar.addActionListener(
                e -> limpiarArea()
        );
    }

    private void limpiarArea() {

        controlador.getAutomata()
                .getEstados()
                .clear();

        controlador.getAutomata()
                .getTransiciones()
                .clear();

        etiquetaEstado.setText(
                "Área de diseño limpiada"
        );

        panelAutomata.repaint();
    }
}