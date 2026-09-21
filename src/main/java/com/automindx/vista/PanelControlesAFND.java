package com.automindx.vista;

import com.automindx.controlador.ControladorNoDeterminista;
import com.automindx.modelo.AutomataNoDeterminista;
import com.automindx.modelo.Estado;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelControlesAFND extends JPanel {

    private final ControladorNoDeterminista controlador;
    private final PanelAFND panelAFND;

    private final JComboBox<Estado> comboOrigen;
    private final JComboBox<Estado> comboDestino;
    private final JComboBox<Estado> comboEstado;
    private final JTextField campoSimbolo;

    public PanelControlesAFND(
            ControladorNoDeterminista controlador,
            PanelAFND panelAFND) {

        this.controlador = controlador;
        this.panelAFND = panelAFND;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );
        setBackground(new Color(248, 245, 252));

        comboOrigen = new JComboBox<>();
        comboDestino = new JComboBox<>();
        comboEstado = new JComboBox<>();
        campoSimbolo = new JTextField();

        agregarTitulo("Controles del AFND");

        agregarEtiqueta("Estado de origen");
        agregarComponente(comboOrigen);

        agregarEtiqueta("Estado de destino");
        agregarComponente(comboDestino);

        agregarEtiqueta("Símbolo de transición");
        agregarComponente(campoSimbolo);

        JButton botonTransicion =
                new JButton("Crear transición");

        botonTransicion.addActionListener(
                evento -> crearTransicion()
        );

        agregarComponente(botonTransicion);

        agregarSeparador();

        agregarEtiqueta("Seleccionar estado");
        agregarComponente(comboEstado);

        JButton botonInicial =
                new JButton("Establecer como inicial");

        botonInicial.addActionListener(
                evento -> establecerInicial()
        );

        agregarComponente(botonInicial);

        JButton botonFinal =
                new JButton("Alternar estado final");

        botonFinal.addActionListener(
                evento -> alternarFinal()
        );

        agregarComponente(botonFinal);

        agregarSeparador();

        JButton botonConvertir =
                new JButton("Convertir AFND a AFD");

        botonConvertir.addActionListener(
                evento -> convertir()
        );

        agregarComponente(botonConvertir);

        JButton botonActualizar =
                new JButton("Actualizar estados");

        botonActualizar.addActionListener(
                evento -> actualizarEstados()
        );

        agregarComponente(botonActualizar);

        actualizarEstados();
    }

    private void agregarTitulo(String texto) {

        JLabel titulo = new JLabel(texto);

        titulo.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(titulo);
        add(Box.createVerticalStrut(12));
    }

    private void agregarEtiqueta(String texto) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(etiqueta);
        add(Box.createVerticalStrut(4));
    }

    private void agregarComponente(JComponent componente) {

        componente.setAlignmentX(Component.LEFT_ALIGNMENT);

        componente.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 32)
        );

        add(componente);
        add(Box.createVerticalStrut(8));
    }

    private void agregarSeparador() {

        add(Box.createVerticalStrut(8));
        add(new JSeparator());
        add(Box.createVerticalStrut(12));
    }

    public void actualizarEstados() {

        Estado origenActual =
                (Estado) comboOrigen.getSelectedItem();

        Estado destinoActual =
                (Estado) comboDestino.getSelectedItem();

        Estado estadoActual =
                (Estado) comboEstado.getSelectedItem();

        comboOrigen.removeAllItems();
        comboDestino.removeAllItems();
        comboEstado.removeAllItems();

        List<Estado> estados =
                controlador.getAFND().getEstados();

        for (Estado estado : estados) {

            comboOrigen.addItem(estado);
            comboDestino.addItem(estado);
            comboEstado.addItem(estado);
        }

        restaurarSeleccion(
                comboOrigen,
                origenActual
        );

        restaurarSeleccion(
                comboDestino,
                destinoActual
        );

        restaurarSeleccion(
                comboEstado,
                estadoActual
        );
    }

    private void restaurarSeleccion(
            JComboBox<Estado> combo,
            Estado estado) {

        if (estado != null) {
            combo.setSelectedItem(estado);
        }
    }

    private void crearTransicion() {

        Estado origen =
                (Estado) comboOrigen.getSelectedItem();

        Estado destino =
                (Estado) comboDestino.getSelectedItem();

        String texto =
                campoSimbolo.getText().trim();

        if (origen == null || destino == null) {

            mostrarAdvertencia(
                    "Debes seleccionar el origen y el destino."
            );

            return;
        }

        if (texto.length() != 1) {

            mostrarAdvertencia(
                    "Ingresa un solo símbolo o el carácter ε."
            );

            return;
        }

        char simbolo = texto.charAt(0);

        if (simbolo == 'e') {
            simbolo = AutomataNoDeterminista.EPSILON;
        }

        boolean creada =
                controlador.agregarTransicion(
                        origen,
                        destino,
                        simbolo
                );

        if (!creada) {

            mostrarAdvertencia(
                    "La transición ya existe o no se pudo crear."
            );

            return;
        }

        campoSimbolo.setText("");
        panelAFND.repaint();

        JOptionPane.showMessageDialog(
                this,
                "Transición creada correctamente.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void establecerInicial() {

        Estado estado =
                (Estado) comboEstado.getSelectedItem();

        if (estado == null) {
            return;
        }

        controlador.establecerEstadoInicial(estado);
        panelAFND.repaint();
    }

    private void alternarFinal() {

        Estado estado =
                (Estado) comboEstado.getSelectedItem();

        if (estado == null) {
            return;
        }

        controlador.alternarEstadoFinal(estado);
        panelAFND.repaint();
    }

    private void convertir() {

        if (controlador.getAFND().getEstadoInicial() == null) {

            mostrarAdvertencia(
                    "Debes establecer un estado inicial antes de convertir."
            );

            return;
        }

        controlador.convertirAFDaFD();

        JOptionPane.showMessageDialog(
                this,
                "El AFND se convirtió correctamente a un AFD.",
                "Conversión completada",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void mostrarAdvertencia(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
        );
    }
}