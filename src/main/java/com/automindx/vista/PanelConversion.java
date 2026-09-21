package com.automindx.vista;

import com.automindx.controlador.ControladorNoDeterminista;
import com.automindx.modelo.ConversorAFNDAFD;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;
import java.util.Set;

public class PanelConversion extends JPanel {

    private final ControladorNoDeterminista controlador;
    private final JPanel contenido;

    private static final Color MORADO = new Color(106, 76, 147);
    private static final Color MORADO_CLARO = new Color(235, 228, 245);
    private static final Color FONDO = new Color(248, 247, 252);
    private static final Color TEXTO = new Color(55, 48, 65);
    private static final Color LINEA = new Color(220, 215, 228);

    public PanelConversion(ControladorNoDeterminista controlador) {
        this.controlador = controlador;

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(6, 6, 6, 6));
        setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Conversión AFND -> AFD");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titulo.setForeground(MORADO);
        add(titulo, BorderLayout.NORTH);

        contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(BorderFactory.createLineBorder(LINEA));
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        add(scroll, BorderLayout.CENTER);

        JButton actualizar = new JButton("Actualizar");
        actualizar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        actualizar.setFocusPainted(false);
        actualizar.setPreferredSize(new Dimension(85, 24));
        actualizar.addActionListener(e -> mostrarConversion());

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 2));
        pie.setBackground(Color.WHITE);
        pie.add(actualizar);
        add(pie, BorderLayout.SOUTH);

        mostrarConversion();
    }

    public void mostrarConversion() {
        contenido.removeAll();

        ConversorAFNDAFD.ResultadoConversion resultado =
                controlador.getResultadoConversion();

        if (resultado == null) {
            JLabel mensaje = new JLabel(
                    "No se ha realizado ninguna conversión."
            );
            mensaje.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            mensaje.setForeground(Color.GRAY);
            contenido.add(mensaje);
            actualizarVista();
            return;
        }

        agregarSeccion("Estados generados",
                crearTablaEstados(resultado));

        agregarSeccion("Transiciones del AFD",
                crearTablaTransiciones(resultado));

        agregarSeccion("Proceso de conversión",
                crearTablaPasos(resultado));

        actualizarVista();
    }

    private void agregarSeccion(String titulo, JTable tabla) {
        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 11));
        etiqueta.setForeground(TEXTO);
        etiqueta.setOpaque(true);
        etiqueta.setBackground(FONDO);
        etiqueta.setBorder(new EmptyBorder(4, 6, 4, 6));

        contenido.add(etiqueta);
        contenido.add(tabla);
        contenido.add(Box.createVerticalStrut(5));
    }

    private JTable crearTablaEstados(
            ConversorAFNDAFD.ResultadoConversion resultado) {

        DefaultTableModel modelo = crearModelo(
                "Estado", "Conjunto", "Tipo"
        );

        for (Map.Entry<Set<Estado>, Estado> entrada :
                resultado.getCorrespondencia().entrySet()) {

            Estado estado = entrada.getValue();
            String tipo = estado.isInicial() ? "Inicial" : "";

            if (estado.isEstadoFinal()) {
                tipo += tipo.isEmpty() ? "Final" : " / Final";
            }

            modelo.addRow(new Object[]{
                    estado.getNombre(),
                    conjuntoTexto(entrada.getKey()),
                    tipo
            });
        }

        return prepararTabla(new JTable(modelo));
    }

    private JTable crearTablaTransiciones(
            ConversorAFNDAFD.ResultadoConversion resultado) {

        DefaultTableModel modelo = crearModelo(
                "Origen", "Símbolo", "Destino"
        );

        for (Transicion t : resultado.getAfd().getTransiciones()) {
            modelo.addRow(new Object[]{
                    t.getOrigen().getNombre(),
                    simboloTexto(t.getSimbolo()),
                    t.getDestino().getNombre()
            });
        }

        return prepararTabla(new JTable(modelo));
    }

    private JTable crearTablaPasos(
            ConversorAFNDAFD.ResultadoConversion resultado) {

        DefaultTableModel modelo = crearModelo(
                "#", "Origen", "Símbolo", "Destino", "Transición"
        );

        int numero = 1;

        for (ConversorAFNDAFD.PasoConversion paso :
                resultado.getPasos()) {

            modelo.addRow(new Object[]{
                    numero++,
                    conjuntoTexto(paso.getConjuntoOrigen()),
                    simboloTexto(paso.getSimbolo()),
                    conjuntoTexto(paso.getConjuntoDestino()),
                    paso.getEstadoOrigenAFD()
                            + " -> "
                            + paso.getEstadoDestinoAFD()
            });
        }

        return prepararTabla(new JTable(modelo));
    }

    private DefaultTableModel crearModelo(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    private JTable prepararTabla(JTable tabla) {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        tabla.setRowHeight(20);
        tabla.setShowGrid(true);
        tabla.setGridColor(LINEA);
        tabla.setIntercellSpacing(new Dimension(1, 1));
        tabla.setFillsViewportHeight(true);
        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabla.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 10)
        );
        tabla.getTableHeader().setBackground(MORADO_CLARO);
        tabla.getTableHeader().setForeground(TEXTO);
        tabla.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centrado =
                new DefaultTableCellRenderer();

        centrado.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(centrado);
        }

        return tabla;
    }

    private String conjuntoTexto(Set<Estado> conjunto) {
        if (conjunto == null || conjunto.isEmpty()) {
            return "VACÍO";
        }

        StringBuilder texto = new StringBuilder("{");

        for (Estado estado : conjunto) {
            if (texto.length() > 1) {
                texto.append(",");
            }
            texto.append(estado.getNombre());
        }

        return texto.append("}").toString();
    }

    private String simboloTexto(char simbolo) {
        return simbolo == 'ε' || simbolo == 'e'
                ? "EPS"
                : String.valueOf(simbolo);
    }

    private void actualizarVista() {
        contenido.revalidate();
        contenido.repaint();
    }
}