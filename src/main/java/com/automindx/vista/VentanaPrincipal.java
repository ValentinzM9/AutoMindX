package com.automindx.vista;
 
import com.automindx.controlador.ControladorAutomata;
import com.automindx.controlador.ControladorNoDeterminista;
 
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
 
public class VentanaPrincipal extends JFrame {
 
    private final ControladorAutomata controladorDFA;
    private final ControladorNoDeterminista controladorAFND;
 
    private PanelAutomata panelAutomata;
    private PanelControles panelControles;
    private PanelAFND panelAFND;
    private PanelControlesAFND panelControlesAFND;
    private PanelConversion panelConversion;
 
    private JPanel panelCentro;
    private JLabel etiquetaEstado;
 
    private final Color FONDO = new Color(248, 247, 252);
    private final Color MORADO = new Color(106, 76, 147);
    private final Color MORADO_CLARO = new Color(235, 228, 245);
    private final Color TEXTO = new Color(55, 48, 65);
    private final Color GRIS = new Color(110, 105, 118);
 
    public VentanaPrincipal(
            ControladorAutomata controladorDFA,
            ControladorNoDeterminista controladorAFND) {
 
        this.controladorDFA = controladorDFA;
        this.controladorAFND = controladorAFND;
 
        configurarVentana();
        inicializarComponentes();
        configurarEventos();
        mostrarModoDFA();
    }
 
    private void configurarVentana() {
        setTitle("AutoMindX | Simulador de Autómatas Finitos");
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(FONDO);
        setLayout(new BorderLayout(12, 12));
 
        ((JComponent) getContentPane()).setBorder(
                new EmptyBorder(12, 12, 12, 12)
        );
    }
 
    private void inicializarComponentes() {
        panelAutomata = new PanelAutomata(controladorDFA);
        panelAFND = new PanelAFND(controladorAFND);
        panelAFND.setPreferredSize(new Dimension(850, 600));
 
        panelControles = new PanelControles(
                controladorDFA,
                panelAutomata
        );
 
        panelConversion = new PanelConversion(controladorAFND);
 
        panelControlesAFND = new PanelControlesAFND(
                controladorAFND,
                panelAFND,
                panelConversion
        );
 
        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearBarraInferior(), BorderLayout.SOUTH);
    }
 
    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout(15, 5));
        encabezado.setBackground(Color.WHITE);
        encabezado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MORADO_CLARO),
                new EmptyBorder(12, 18, 12, 18)
        ));
 
        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setBackground(Color.WHITE);
 
        JLabel titulo = new JLabel("AutoMindX");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(MORADO);
 
        JLabel subtitulo = new JLabel(
                "Explora, construye y valida autómatas finitos"
        );
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitulo.setForeground(GRIS);
 
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(3));
        textos.add(subtitulo);
 
        JComboBox<String> selectorModo = new JComboBox<>(
                new String[]{
                        "Autómata determinista (DFA)",
                        "Autómata no determinista (AFND)"
                }
        );
 
        selectorModo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        selectorModo.setPreferredSize(new Dimension(235, 32));
        selectorModo.addActionListener(evento -> {
            if (selectorModo.getSelectedIndex() == 0) {
                mostrarModoDFA();
            } else {
                mostrarModoAFND();
            }
        });
 
        encabezado.add(textos, BorderLayout.WEST);
        encabezado.add(selectorModo, BorderLayout.EAST);
 
        return encabezado;
    }
 
    private JPanel crearBarraInferior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(Color.WHITE);
        barra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MORADO_CLARO),
                new EmptyBorder(8, 12, 8, 12)
        ));
 
        etiquetaEstado = new JLabel("●  Listo para trabajar");
        etiquetaEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        etiquetaEstado.setForeground(GRIS);
 
        JPanel botones = new JPanel(new FlowLayout(
                FlowLayout.RIGHT, 8, 0
        ));
        botones.setBackground(Color.WHITE);
 
        JButton botonLimpiar = new JButton("Limpiar áreas");
        JButton botonSalir = new JButton("Salir");
 
        estilizarBoton(botonLimpiar);
        estilizarBoton(botonSalir);
 
        botonLimpiar.addActionListener(evento -> limpiarAreas());
        botonSalir.addActionListener(evento -> System.exit(0));
 
        botones.add(botonLimpiar);
        botones.add(botonSalir);
 
        barra.add(etiquetaEstado, BorderLayout.WEST);
        barra.add(botones, BorderLayout.EAST);
 
        return barra;
    }
 
    private void estilizarBoton(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        boton.setFocusPainted(false);
        boton.setBackground(MORADO_CLARO);
        boton.setForeground(TEXTO);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
    }
 
    private void mostrarModoDFA() {
        panelCentro = new JPanel(new BorderLayout(12, 0));
        panelCentro.setBackground(FONDO);
 
        JScrollPane desplazamiento = new JScrollPane(panelAutomata);
        desplazamiento.setBorder(BorderFactory.createLineBorder(MORADO_CLARO));
 
        panelCentro.add(desplazamiento, BorderLayout.CENTER);
        panelCentro.add(panelControles, BorderLayout.EAST);
 
        actualizarCentro(panelCentro);
        cambiarEstado("●  Modo DFA activo");
    }
 
    private void mostrarModoAFND() {
        panelCentro = new JPanel(new BorderLayout(12, 0));
        panelCentro.setBackground(FONDO);
 
        JPanel panelLateral = new JPanel(new BorderLayout(8, 8));
        panelLateral.setPreferredSize(new Dimension(350, 600));
        panelLateral.setBackground(FONDO);
 
        JScrollPane controles = new JScrollPane(panelControlesAFND);
        controles.setBorder(BorderFactory.createLineBorder(MORADO_CLARO));
 
        panelLateral.add(controles, BorderLayout.NORTH);
        panelLateral.add(panelConversion, BorderLayout.CENTER);
 
        JScrollPane editor = new JScrollPane(panelAFND);
        editor.setBorder(BorderFactory.createLineBorder(MORADO_CLARO));
 
        panelCentro.add(editor, BorderLayout.CENTER);
        panelCentro.add(panelLateral, BorderLayout.EAST);
 
        actualizarCentro(panelCentro);
 
        panelAFND.desactivarModosEdicion();
        panelConversion.mostrarConversion();
 
        cambiarEstado("●  Modo AFND activo");
    }
 
    private void actualizarCentro(JPanel nuevoPanel) {
        getContentPane().removeAll();
        getContentPane().add(crearEncabezado(), BorderLayout.NORTH);
        getContentPane().add(nuevoPanel, BorderLayout.CENTER);
        getContentPane().add(crearBarraInferior(), BorderLayout.SOUTH);
        revalidate();
        repaint();
    }
 
    private void limpiarAreas() {
        controladorDFA.limpiarAutomata();
        controladorAFND.limpiar();
 
        panelAutomata.limpiarResaltado();
        panelAutomata.desactivarModosEdicion();
 
        panelAFND.desactivarModosEdicion();
        panelAFND.repaint();
 
        panelConversion.mostrarConversion();
 
        cambiarEstado("●  Áreas limpiadas correctamente");
    }
 
    private void cambiarEstado(String mensaje) {
        if (etiquetaEstado != null) {
            etiquetaEstado.setText(mensaje);
        }
    }
 
    private void configurarEventos() {
        setVisible(true);
    }
}