package com.automindx.vista;

import com.automindx.controlador.ControladorNoDeterminista;
import com.automindx.modelo.ConversorAFNDAFD;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Set;

public class PanelConversion extends JPanel {

    private final ControladorNoDeterminista controlador;
    private final JPanel contenido;

    private static final Color MORADO = new Color(106, 76, 147);
    private static final Color MORADO_CLARO = new Color(235, 228, 245);
    private static final Color FONDO = new Color(248, 247, 252);
    private static final Color TEXTO = new Color(55, 48, 65);

    public PanelConversion(ControladorNoDeterminista controlador) {
        this.controlador = controlador;

        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Conversión AFND → AFD");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(MORADO);
        add(titulo, BorderLayout.NORTH);

        contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(BorderFactory.createLineBorder(MORADO_CLARO));
        add(scroll, BorderLayout.CENTER);

        JButton botonMostrar = new JButton("Actualizar conversión");
        botonMostrar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        botonMostrar.setFocusPainted(false);
        botonMostrar.setBackground(MORADO_CLARO);
        botonMostrar.setForeground(TEXTO);
        botonMostrar.setBorder(new EmptyBorder(6, 12, 6, 12));
        botonMostrar.addActionListener(e -> mostrarConversion());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelBoton.setBackground(Color.WHITE);
        panelBoton.add(botonMostrar);

        add(panelBoton, BorderLayout.SOUTH);

        mostrarConversion();
    }

    public void mostrarConversion() {
        contenido.removeAll();

        ConversorAFNDAFD.ResultadoConversion resultado =
                controlador.getResultadoConversion();

        if (resultado == null) {
            JLabel mensaje = new JLabel(
                    "Todavía no se ha realizado ninguna conversión."
            );
            mensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            mensaje.setForeground(Color.GRAY);
            mensaje.setBorder(new EmptyBorder(15, 10, 15, 10));

            contenido.add(mensaje);
            actualizarVista();
            return;
        }

        contenido.add(crearTituloSeccion("Estados del AFD generado"));
        contenido.add(crearTablaEstados(resultado));

        contenido.add(Box.createVerticalStrut(12));

        contenido.add(crearTituloSeccion("Transiciones del AFD"));
        contenido.add(crearTablaTransiciones(resultado));

        contenido.add(Box.createVerticalStrut(12));

        contenido.add(crearTituloSeccion("Proceso de conversión"));
        contenido.add(crearTablaPasos(resultado));

        actualizarVista();
    }

    private JLabel crearTituloSeccion(String texto) {
        JLabel titulo = new JLabel(texto);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titulo.setForeground(TEXTO);
        titulo.setBorder(new EmptyBorder(6, 6, 6, 6));
        return titulo;
    }

    private JTable crearTablaEstados(
            ConversorAFNDAFD.ResultadoConversion resultado) {

        String[] columnas = {"Estado", "Conjunto", "Tipo"};

        DefaultTableModel modelo =
                new DefaultTableModel(columnas, 0) {
                    @Override
                    public boolean isCellEditable(int fila, int columna) {
                        return false;
                    }
                };

        for (Estado estado : resultado.getAfd().getEstados()) {
            String tipo = "";

            if (estado.isInicial()) {
                tipo = "Inicial";
            }

            if (estado.isEstadoFinal()) {
                tipo += tipo.isEmpty() ? "Final" : " / Final";
            }

            modelo.addRow(new Object[]{
                    estado.getNombre(),
                    obtenerConjuntoEstado(estado, resultado),
                    tipo
            });
        }

        return prepararTabla(new JTable(modelo));
    }

    private String obtenerConjuntoEstado(
            Estado estado,
            ConversorAFNDAFD.ResultadoConversion resultado) {

        for (ConversorAFNDAFD.PasoConversion paso :
                resultado.getPasos()) {

            if (paso.getEstadoOrigenAFD().equals(estado)) {
                return conjuntoTexto(paso.getConjuntoOrigen());
            }
        }

        for (ConversorAFNDAFD.PasoConversion paso :
                resultado.getPasos()) {

            if (paso.getEstadoDestinoAFD().equals(estado)) {
                return conjuntoTexto(paso.getConjuntoDestino());
            }
        }

        return "∅";
    }

    private JTable crearTablaTransiciones(
            ConversorAFNDAFD.ResultadoConversion resultado) {

        String[] columnas = {"Origen", "Símbolo", "Destino"};

        DefaultTableModel modelo =
                new DefaultTableModel(columnas, 0) {
                    @Override
                    public boolean isCellEditable(int fila, int columna) {
                        return false;
                    }
                };

        for (Transicion transicion :
                resultado.getAfd().getTransiciones()) {

            modelo.addRow(new Object[]{
                    transicion.getOrigen().getNombre(),
                    transicion.getSimbolo(),
                    transicion.getDestino().getNombre()
            });
        }

        return prepararTabla(new JTable(modelo));
    }

    private JTable crearTablaPasos(
            ConversorAFNDAFD.ResultadoConversion resultado) {

        String[] columnas = {
                "#", "Conjunto origen", "Símbolo",
                "Conjunto destino", "Transición"
        };

        DefaultTableModel modelo =
                new DefaultTableModel(columnas, 0) {
                    @Override
                    public boolean isCellEditable(int fila, int columna) {
                        return false;
                    }
                };

        int numero = 1;

        for (ConversorAFNDAFD.PasoConversion paso :
                resultado.getPasos()) {

            modelo.addRow(new Object[]{
                    numero++,
                    conjuntoTexto(paso.getConjuntoOrigen()),
                    paso.getSimbolo(),
                    conjuntoTexto(paso.getConjuntoDestino()),
                        paso.getEstadoOrigenAFD()
                            + " → "
                            + paso.getEstadoDestinoAFD()
            });
        }

        return prepararTabla(new JTable(modelo));
    }

    private JTable prepararTabla(JTable tabla) {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tabla.setRowHeight(24);
        tabla.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 11)
        );
        tabla.getTableHeader().setBackground(MORADO_CLARO);
        tabla.getTableHeader().setForeground(TEXTO);
        tabla.setGridColor(new Color(225, 220, 232));
        tabla.setFillsViewportHeight(true);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return tabla;
    }

    private String conjuntoTexto(Set<Estado> conjunto) {
        if (conjunto.isEmpty()) {
            return "∅";
        }

        StringBuilder texto = new StringBuilder("{");

        boolean primero = true;

        for (Estado estado : conjunto) {
            if (!primero) {
                texto.append(", ");
            }

            texto.append(estado.getNombre());
            primero = false;
        }

        return texto.append("}").toString();
    }

    private void actualizarVista() {
        contenido.revalidate();
        contenido.repaint();
    }
}
