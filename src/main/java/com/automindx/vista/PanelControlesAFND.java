package com.automindx.vista;
 
import com.automindx.controlador.ControladorNoDeterminista;
 
import javax.swing.*;
import java.awt.*;
 
public class PanelControlesAFND extends JPanel {
 
    private final ControladorNoDeterminista controlador;
    private final PanelAFND panelAFND;
    private final PanelConversion panelConversion;
 
    private JButton botonCrearEstado;
    private JButton botonCrearTransicion;
    private JButton botonConvertir;
 
    private JLabel etiquetaAyuda;
 
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
 
        setBorder(
                BorderFactory.createTitledBorder(
                        "Controles"
                )
        );
 
        setLayout(
                new GridLayout(0, 1, 6, 6)
        );
 
        JLabel titulo = new JLabel("Modo de edición del AFND:");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD));
        add(titulo);
 
        botonCrearEstado = new JButton("+ CREAR ESTADO");
        botonCrearTransicion = new JButton("→ CREAR TRANSICIÓN");
 
        add(botonCrearEstado);
        add(botonCrearTransicion);
 
        etiquetaAyuda = new JLabel(
                "<html><i>"
                        + "Clic derecho sobre un estado o<br>"
                        + "transición: más opciones.<br>"
                        + "Usa 'e' como símbolo para ε."
                        + "</i></html>"
        );
 
        etiquetaAyuda.setFont(etiquetaAyuda.getFont().deriveFont(11f));
        add(etiquetaAyuda);
 
        add(new JSeparator());
 
        botonConvertir = new JButton("Convertir AFND a AFD");
        add(botonConvertir);
 
        botonCrearEstado.addActionListener(e -> activarCreacionEstado());
        botonCrearTransicion.addActionListener(e -> activarCreacionTransicion());
        botonConvertir.addActionListener(e -> convertir());
    }
 
    private void activarCreacionEstado() {
 
        panelAFND.activarModoCrearEstado();
 
        etiquetaAyuda.setText(
                "<html><i>Haz clic en el lienzo para<br>"
                        + "crear un nuevo estado.</i></html>"
        );
    }
 
    private void activarCreacionTransicion() {
 
        panelAFND.activarModoCrearTransicion();
 
        etiquetaAyuda.setText(
                "<html><i>Clic en el estado origen y luego<br>"
                        + "en el estado destino.</i></html>"
        );
    }
 
    private void convertir() {
 
        if (controlador.getAFND().getEstadoInicial() == null) {
 
            JOptionPane.showMessageDialog(
                    this,
                    "Debes establecer un estado inicial antes de convertir.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
 
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
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
 