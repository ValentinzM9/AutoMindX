package com.automindx.vista;

import com.automindx.controlador.ControladorNoDeterminista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelControlesAFND extends JPanel {

    private final ControladorNoDeterminista controlador;
    private final PanelAFND panelAFND;
    private final PanelConversion panelConversion;

    private JButton botonCrearEstado;
    private JButton botonCrearTransicion;
    private JButton botonConvertir;
    private JLabel etiquetaAyuda;

    private final Color MORADO = new Color(106, 76, 147);
    private final Color GRIS = new Color(110, 105, 118);

    public PanelControlesAFND(
            ControladorNoDeterminista controlador,
            PanelAFND panelAFND,
            PanelConversion panelConversion) {

        this.controlador = controlador;
        this.panelAFND = panelAFND;
        this.panelConversion = panelConversion;
        configurarPanel();
    }

    private void configurarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        agregarTitulo("Editor de AFND");

        botonCrearEstado = crearBoton("+ Crear estado");
        botonCrearTransicion = crearBoton("→ Crear transición");

        agregarComponente(botonCrearEstado);
        agregarComponente(botonCrearTransicion);

        botonCrearEstado.addActionListener(
                e -> activarCreacionEstado());

        botonCrearTransicion.addActionListener(
                e -> activarCreacionTransicion());

        agregarSeparador();
        agregarTitulo("Conversión");

        botonConvertir = crearBoton("Convertir AFND a AFD");
        agregarComponente(botonConvertir);

        botonConvertir.addActionListener(e -> convertir());

        agregarSeparador();

        etiquetaAyuda = new JLabel(
                "Clic derecho sobre un estado o transición " +
                "para ver más opciones.");
        etiquetaAyuda.setFont(
                new Font("Segoe UI", Font.ITALIC, 11));
        etiquetaAyuda.setForeground(GRIS);
        etiquetaAyuda.setAlignmentX(LEFT_ALIGNMENT);
        agregarComponente(etiquetaAyuda);

        JLabel etiquetaEpsilon =
                new JLabel("Usa 'e' para representar ε.");
        etiquetaEpsilon.setFont(
                new Font("Segoe UI", Font.ITALIC, 11));
        etiquetaEpsilon.setForeground(GRIS);
        etiquetaEpsilon.setAlignmentX(LEFT_ALIGNMENT);
        agregarComponente(etiquetaEpsilon);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setAlignmentX(LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(230, 32));
        return boton;
    }

    private void agregarTitulo(String texto) {
        JLabel titulo = new JLabel(texto);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titulo.setForeground(MORADO);
        titulo.setAlignmentX(LEFT_ALIGNMENT);
        agregarComponente(titulo);
    }

    private void agregarComponente(JComponent componente) {
        add(componente);
        add(Box.createVerticalStrut(7));
    }

    private void agregarSeparador() {
        add(Box.createVerticalStrut(3));
        add(new JSeparator());
        add(Box.createVerticalStrut(8));
    }

    private void activarCreacionEstado() {
        panelAFND.activarModoCrearEstado();
        etiquetaAyuda.setText(
                "Haz clic en el lienzo para crear un estado.");
        etiquetaAyuda.setForeground(GRIS);
    }

    private void activarCreacionTransicion() {
        panelAFND.activarModoCrearTransicion();
        etiquetaAyuda.setText(
                "Selecciona el origen y después el destino.");
        etiquetaAyuda.setForeground(GRIS);
    }

    private void convertir() {
        if (controlador.getAFND().getEstadoInicial() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes establecer un estado inicial antes " +
                    "de convertir.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        controlador.convertirAFDaFD();

        if (panelConversion != null) {
            panelConversion.mostrarConversion();
        }

        JOptionPane.showMessageDialog(
                this,
                "El AFND se convirtió correctamente a un AFD.",
                "Conversión completada",
                JOptionPane.INFORMATION_MESSAGE);
    }
}