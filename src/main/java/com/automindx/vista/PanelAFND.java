package com.automindx.vista;

import com.automindx.controlador.ControladorNoDeterminista;
import com.automindx.modelo.AutomataNoDeterminista;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PanelAFND extends JPanel {

    private final ControladorNoDeterminista controlador;
    private Estado estadoSeleccionado, estadoOrigenTransicion;
    private int offsetX, offsetY, mouseX, mouseY;
    private boolean modoCrearEstado, modoTransicion;

    private static final int RADIO = 30;

    public PanelAFND(ControladorNoDeterminista controlador) {
        this.controlador = controlador;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(850, 600));
        configurarMouse();
    }

    private void configurarMouse() {
        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                Estado estado = buscarEstado(mouseX, mouseY);

                if (modoCrearEstado) {
                    if (estado == null) {
                        controlador.crearEstado(mouseX, mouseY);
                        modoCrearEstado = false;
                        actualizarCursor();
                        repaint();
                    }
                    return;
                }

                if (modoTransicion) {
                    if (estado != null) estadoOrigenTransicion = estado;
                    repaint();
                    return;
                }

                if (estado != null) {
                    estadoSeleccionado = estado;
                    offsetX = mouseX - estado.getX();
                    offsetY = mouseY - estado.getY();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();

                if (modoTransicion) {
                    repaint();
                    return;
                }

                if (estadoSeleccionado != null) {
                    estadoSeleccionado.setX(mouseX - offsetX);
                    estadoSeleccionado.setY(mouseY - offsetY);
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (modoTransicion && estadoOrigenTransicion != null) {
                    Estado destino = buscarEstado(e.getX(), e.getY());

                    if (destino != null)
                        crearTransicion(estadoOrigenTransicion, destino);

                    estadoOrigenTransicion = null;
                    repaint();
                    return;
                }

                estadoSeleccionado = null;
            }
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    public void activarModoCrearEstado() {
        modoCrearEstado = true;
        modoTransicion = false;
        estadoOrigenTransicion = null;
        actualizarCursor();
    }

    public void activarModoCrearTransicion() {
        modoTransicion = true;
        modoCrearEstado = false;
        estadoOrigenTransicion = null;
        actualizarCursor();
    }

    public void desactivarModosEdicion() {
        modoCrearEstado = false;
        modoTransicion = false;
        estadoOrigenTransicion = null;
        estadoSeleccionado = null;
        actualizarCursor();
        repaint();
    }

    private void actualizarCursor() {
        boolean activo = modoCrearEstado || modoTransicion;
        setCursor(Cursor.getPredefinedCursor(
                activo ? Cursor.CROSSHAIR_CURSOR : Cursor.DEFAULT_CURSOR
        ));
    }

    private void crearTransicion(Estado origen, Estado destino) {
        String texto = JOptionPane.showInputDialog(
                this, "Ingresa el símbolo:", "Crear transición",
                JOptionPane.PLAIN_MESSAGE
        );

        if (texto == null) return;

        texto = texto.trim();

        if (texto.length() != 1) {
            JOptionPane.showMessageDialog(
                    this, "Ingresa un solo símbolo o e para EPS.",
                    "Símbolo inválido", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        char simbolo = texto.charAt(0);
        if (simbolo == 'e') simbolo = AutomataNoDeterminista.EPSILON;

        if (!controlador.agregarTransicion(origen, destino, simbolo)) {
            JOptionPane.showMessageDialog(
                    this, "No se pudo crear la transición.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE
            );
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        dibujarTransiciones(g2);
        dibujarLineaTemporal(g2);
        dibujarEstados(g2);

        g2.dispose();
    }

    private void dibujarEstados(Graphics2D g2) {
        for (Estado estado : controlador.getAFND().getEstados()) {
            int x = estado.getX(), y = estado.getY();

            g2.setColor(Color.WHITE);
            g2.fillOval(x - RADIO, y - RADIO, RADIO * 2, RADIO * 2);

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x - RADIO, y - RADIO, RADIO * 2, RADIO * 2);

            if (estado.isEstadoFinal())
                g2.drawOval(x - RADIO + 5, y - RADIO + 5,
                        RADIO * 2 - 10, RADIO * 2 - 10);

            if (estado.isInicial()) dibujarFlechaInicial(g2, estado);

            String nombre = estado.getNombre();
            FontMetrics fm = g2.getFontMetrics();

            g2.drawString(
                    nombre, x - fm.stringWidth(nombre) / 2,
                    y + fm.getAscent() / 2
            );
        }
    }

    private void dibujarFlechaInicial(Graphics2D g2, Estado estado) {
        int x = estado.getX() - RADIO - 45;
        int y = estado.getY();

        g2.drawLine(x, y, estado.getX() - RADIO, y);
        g2.drawLine(x + 8, y - 5, x, y);
        g2.drawLine(x + 8, y + 5, x, y);
    }

    private void dibujarTransiciones(Graphics2D g2) {
        g2.setColor(Color.BLACK);

        for (Transicion t : controlador.getAFND().getTransiciones())
            dibujarTransicion(g2, t);
    }

    private void dibujarTransicion(Graphics2D g2, Transicion t) {
        Estado origen = t.getOrigen(), destino = t.getDestino();

        if (origen == null || destino == null) return;

        int x1 = origen.getX(), y1 = origen.getY();
        int x2 = destino.getX(), y2 = destino.getY();

        if (origen.equals(destino)) {
            dibujarBucle(g2, origen, t.getSimbolo());
            return;
        }

        double dx = x2 - x1, dy = y2 - y1;
        double distancia = Math.hypot(dx, dy);

        if (distancia == 0) return;

        double ux = dx / distancia, uy = dy / distancia;

        int inicioX = (int) (x1 + ux * RADIO);
        int inicioY = (int) (y1 + uy * RADIO);
        int finX = (int) (x2 - ux * RADIO);
        int finY = (int) (y2 - uy * RADIO);

        g2.drawLine(inicioX, inicioY, finX, finY);
        dibujarPuntaFlecha(g2, inicioX, inicioY, finX, finY);

        g2.drawString(
                simboloTexto(t.getSimbolo()),
                (inicioX + finX) / 2,
                (inicioY + finY) / 2 - 8
        );
    }

    private void dibujarPuntaFlecha(
            Graphics2D g2, int x1, int y1, int x2, int y2) {

        double angulo = Math.atan2(y2 - y1, x2 - x1);

        for (double v : new double[]{0.8, -0.8}) {
            int x = (int) (x2 + 10 * Math.cos(angulo + Math.PI * v));
            int y = (int) (y2 + 10 * Math.sin(angulo + Math.PI * v));

            g2.drawLine(x2, y2, x, y);
        }
    }

    private void dibujarBucle(Graphics2D g2, Estado estado, char simbolo) {
        int x = estado.getX(), y = estado.getY();
        int diametro = 45;

        g2.drawOval(
                x - diametro / 2, y - RADIO - 35,
                diametro, diametro
        );

        int puntaX = x, puntaY = y - RADIO - 12;

        g2.drawLine(puntaX, puntaY, puntaX - 7, puntaY + 8);
        g2.drawLine(puntaX, puntaY, puntaX + 7, puntaY + 8);

        g2.drawString(
                simboloTexto(simbolo),
                x + 25, y - RADIO - 35
        );
    }

    private void dibujarLineaTemporal(Graphics2D g2) {
        if (!modoTransicion || estadoOrigenTransicion == null) return;

        g2.setColor(new Color(106, 76, 147));
        g2.setStroke(new BasicStroke(
                2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1, new float[]{5, 5}, 0
        ));

        g2.drawLine(
                estadoOrigenTransicion.getX(),
                estadoOrigenTransicion.getY(),
                mouseX, mouseY
        );
    }

    private String simboloTexto(char simbolo) {
        return simbolo == 'ε' || simbolo == 'e'
                ? "EPS"
                : String.valueOf(simbolo);
    }

    private Estado buscarEstado(int x, int y) {
        List<Estado> estados = controlador.getAFND().getEstados();

        for (Estado estado : estados) {
            int dx = x - estado.getX();
            int dy = y - estado.getY();

            if (dx * dx + dy * dy <= RADIO * RADIO)
                return estado;
        }

        return null;
    }
}