package com.automindx.vista;

import com.automindx.controlador.ControladorNoDeterminista;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PanelAFND extends JPanel {

    private final ControladorNoDeterminista controlador;
    private Estado estadoSeleccionado;
    private int offsetX, offsetY;

    private static final int RADIO = 30;

    public PanelAFND(ControladorNoDeterminista controlador) {
        this.controlador = controlador;

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(850, 600));

        MouseAdapter mouse = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                Estado estado = buscarEstado(e.getX(), e.getY());

                if (estado != null) {
                    estadoSeleccionado = estado;
                    offsetX = e.getX() - estado.getX();
                    offsetY = e.getY() - estado.getY();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (estadoSeleccionado != null) {
                    estadoSeleccionado.setX(e.getX() - offsetX);
                    estadoSeleccionado.setY(e.getY() - offsetY);
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                estadoSeleccionado = null;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1
                        && e.getClickCount() == 2
                        && buscarEstado(e.getX(), e.getY()) == null) {

                    controlador.crearEstado(e.getX(), e.getY());
                    repaint();
                }
            }
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
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
        dibujarEstados(g2);
        g2.dispose();
    }

    private void dibujarEstados(Graphics2D g2) {
        for (Estado estado : controlador.getAFND().getEstados()) {
            int x = estado.getX();
            int y = estado.getY();

            g2.setColor(Color.WHITE);
            g2.fillOval(x - RADIO, y - RADIO, RADIO * 2, RADIO * 2);

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x - RADIO, y - RADIO, RADIO * 2, RADIO * 2);

            if (estado.isEstadoFinal()) {
                g2.drawOval(
                        x - RADIO + 5,
                        y - RADIO + 5,
                        RADIO * 2 - 10,
                        RADIO * 2 - 10
                );
            }

            if (estado.isInicial()) {
                dibujarFlechaInicial(g2, estado);
            }

            String nombre = estado.getNombre();
            FontMetrics metricas = g2.getFontMetrics();

            g2.drawString(
                    nombre,
                    x - metricas.stringWidth(nombre) / 2,
                    y + metricas.getAscent() / 2
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

        for (Transicion transicion :
                controlador.getAFND().getTransiciones()) {
            dibujarTransicion(g2, transicion);
        }
    }

    private void dibujarTransicion(
            Graphics2D g2,
            Transicion transicion) {

        Estado origen = transicion.getOrigen();
        Estado destino = transicion.getDestino();

        if (origen == null || destino == null) {
            return;
        }

        int x1 = origen.getX();
        int y1 = origen.getY();
        int x2 = destino.getX();
        int y2 = destino.getY();

        if (origen.equals(destino)) {
            dibujarBucle(g2, origen, transicion.getSimbolo());
            return;
        }

        double dx = x2 - x1;
        double dy = y2 - y1;
        double distancia = Math.hypot(dx, dy);

        if (distancia == 0) {
            return;
        }

        double ux = dx / distancia;
        double uy = dy / distancia;

        int inicioX = (int) (x1 + ux * RADIO);
        int inicioY = (int) (y1 + uy * RADIO);
        int finX = (int) (x2 - ux * RADIO);
        int finY = (int) (y2 - uy * RADIO);

        g2.drawLine(inicioX, inicioY, finX, finY);
        dibujarPuntaFlecha(g2, inicioX, inicioY, finX, finY);

        String simbolo = String.valueOf(transicion.getSimbolo());
        g2.drawString(
                simbolo,
                (inicioX + finX) / 2,
                (inicioY + finY) / 2 - 8
        );
    }

    private void dibujarPuntaFlecha(
            Graphics2D g2,
            int x1,
            int y1,
            int x2,
            int y2) {

        double angulo = Math.atan2(y2 - y1, x2 - x1);
        int longitud = 10;

        for (double variacion : new double[]{0.8, -0.8}) {
            int x = (int) (
                    x2 + longitud * Math.cos(angulo + Math.PI * variacion)
            );

            int y = (int) (
                    y2 + longitud * Math.sin(angulo + Math.PI * variacion)
            );

            g2.drawLine(x2, y2, x, y);
        }
    }

    private void dibujarBucle(
            Graphics2D g2,
            Estado estado,
            char simbolo) {

        int x = estado.getX();
        int y = estado.getY();
        int diametro = 45;

        g2.drawOval(
                x - diametro / 2,
                y - RADIO - 35,
                diametro,
                diametro
        );

        int puntaX = x;
        int puntaY = y - RADIO - 12;

        g2.drawLine(puntaX, puntaY, puntaX - 7, puntaY + 8);
        g2.drawLine(puntaX, puntaY, puntaX + 7, puntaY + 8);

        g2.drawString(
                String.valueOf(simbolo),
                x + 25,
                y - RADIO - 35
        );
    }

    private Estado buscarEstado(int x, int y) {
        List<Estado> estados = controlador.getAFND().getEstados();

        for (Estado estado : estados) {
            int dx = x - estado.getX();
            int dy = y - estado.getY();

            if (dx * dx + dy * dy <= RADIO * RADIO) {
                return estado;
            }
        }

        return null;
    }
}