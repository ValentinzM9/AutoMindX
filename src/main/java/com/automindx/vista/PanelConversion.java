package com.automindx.vista;

import com.automindx.controlador.ControladorNoDeterminista;
import com.automindx.modelo.ConversorAFNDAFD;
import com.automindx.modelo.Estado;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class PanelConversion extends JPanel {

    private final ControladorNoDeterminista controlador;
    private final JTextArea areaConversion;

    public PanelConversion(
            ControladorNoDeterminista controlador) {

        this.controlador = controlador;

        setLayout(new BorderLayout(10, 10));

        setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        setBackground(Color.WHITE);

        JLabel titulo =
                new JLabel("Conversión de AFND a AFD");

        titulo.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        add(titulo, BorderLayout.NORTH);

        areaConversion = new JTextArea();

        areaConversion.setEditable(false);
        areaConversion.setLineWrap(true);
        areaConversion.setWrapStyleWord(true);
        areaConversion.setFont(
                new Font("Monospaced", Font.PLAIN, 12)
        );

        add(
                new JScrollPane(areaConversion),
                BorderLayout.CENTER
        );

        JButton botonMostrar =
                new JButton("Mostrar conversión");

        botonMostrar.addActionListener(
                evento -> mostrarConversion()
        );

        add(botonMostrar, BorderLayout.SOUTH);

        mostrarConversion();
    }

    public void mostrarConversion() {

        ConversorAFNDAFD.ResultadoConversion resultado =
                controlador.getResultadoConversion();

        if (resultado == null) {

            areaConversion.setText(
                    "Todavía no se ha realizado ninguna conversión."
            );

            return;
        }

        StringBuilder texto =
                new StringBuilder();

        texto.append("ESTADOS DEL AFD GENERADO\n");
        texto.append("========================\n\n");

        for (Estado estado :
                resultado.getAfd().getEstados()) {

            texto.append(estado.getNombre());

            if (estado.isInicial()) {
                texto.append(" [INICIAL]");
            }

            if (estado.isEstadoFinal()) {
                texto.append(" [FINAL]");
            }

            texto.append("\n");
        }

        texto.append("\nTRANSICIONES DEL AFD\n");
        texto.append("====================\n\n");

        resultado.getAfd()
                .getTransiciones()
                .forEach(
                        transicion -> texto.append(
                                transicion
                        ).append("\n")
                );

        texto.append("\nPASOS DE CONVERSIÓN\n");
        texto.append("===================\n\n");

        for (ConversorAFNDAFD.PasoConversion paso :
                resultado.getPasos()) {

            texto.append("Conjunto origen: ");
            agregarConjunto(
                    texto,
                    paso.getConjuntoOrigen()
            );

            texto.append("Símbolo: ")
                    .append(paso.getSimbolo())
                    .append("\n");

            texto.append("Conjunto destino: ");
            agregarConjunto(
                    texto,
                    paso.getConjuntoDestino()
            );

            texto.append("Transición: ")
                    .append(paso.getEstadoOrigenAFD())
                    .append(" --")
                    .append(paso.getSimbolo())
                    .append("--> ")
                    .append(paso.getEstadoDestinoAFD())
                    .append("\n");

            texto.append("------------------------------\n");
        }

        areaConversion.setText(
                texto.toString()
        );

        areaConversion.setCaretPosition(0);
    }

    private void agregarConjunto(
            StringBuilder texto,
            Set<Estado> conjunto) {

        if (conjunto.isEmpty()) {

            texto.append("∅\n");
            return;
        }

        texto.append("{");

        boolean primero = true;

        for (Estado estado : conjunto) {

            if (!primero) {
                texto.append(", ");
            }

            texto.append(estado.getNombre());

            primero = false;
        }

        texto.append("}\n");
    }
}
