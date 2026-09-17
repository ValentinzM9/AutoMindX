package com.automindx.vista;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Path2D;

import javax.swing.JPanel;

import com.automindx.modelo.Automata;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;

public class PanelAutomata extends JPanel {

    private final Automata automata;
    private Estado estadoSeleccionado;
    private int offsetX;
    private int offsetY;
    private boolean modoCrearEstado = false;

    private static final int RADIO_ESTADO = 30;

    public PanelAutomata(Automata automata) {
        this.automata = automata;
        configurarMouse();
    }

    public void activarModoCrearEstado() {
        modoCrearEstado = true;
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    public void desactivarModoCrearEstado() {
        modoCrearEstado = false;
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void configurarMouse() {
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                if (modoCrearEstado) {

                    Estado estadoExistente =
                            buscarEstado(e.getX(), e.getY());

                    if (estadoExistente == null) {
                        crearEstado(e.getX(), e.getY());
                        desactivarModoCrearEstado();
                    }

                    return;
                }

                estadoSeleccionado =
                        buscarEstado(e.getX(), e.getY());

                if (estadoSeleccionado != null) {
                    offsetX =
                            e.getX() - estadoSeleccionado.getX();

                    offsetY =
                            e.getY() - estadoSeleccionado.getY();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                estadoSeleccionado = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {

                if (estadoSeleccionado != null) {

                    estadoSeleccionado.setX(
                            e.getX() - offsetX
                    );

                    estadoSeleccionado.setY(
                            e.getY() - offsetY
                    );

                    repaint();
                }
            }
        });
    }

    private void crearEstado(int x, int y) {

        String nombre =
                "q" + automata.getEstados().size();

        Estado nuevoEstado =
                new Estado(
                        nombre,
                        false,
                        false,
                        x,
                        y
                );

        automata.agregarEstado(nuevoEstado);

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g.create();

        try {

            g2.setStroke(new BasicStroke(2));

            for (Transicion transicion :
                    automata.getTransiciones()) {

                dibujarTransicion(
                        g2,
                        transicion
                );
            }

            for (Estado estado :
                    automata.getEstados()) {

                dibujarEstado(
                        g2,
                        estado
                );
            }

        } finally {
            g2.dispose();
        }
    }

    private void dibujarEstado(
            Graphics2D g2,
            Estado estado) {

        int x = estado.getX();
        int y = estado.getY();

        g2.drawOval(
                x - RADIO_ESTADO,
                y - RADIO_ESTADO,
                RADIO_ESTADO * 2,
                RADIO_ESTADO * 2
        );

        g2.drawString(
                estado.getNombre(),
                x - 8,
                y + 5
        );

        if (estado.isEstadoFinal()) {

            g2.drawOval(
                    x - 25,
                    y - 25,
                    50,
                    50
            );
        }

        if (estado.isInicial()) {

            dibujarFlechaInicial(
                    g2,
                    estado
            );
        }
    }

    private void dibujarTransicion(
            Graphics2D g2,
            Transicion transicion) {

        Estado origen =
                transicion.getOrigen();

        Estado destino =
                transicion.getDestino();

        if (origen == destino) {

            dibujarBucle(
                    g2,
                    origen,
                    transicion.getSimbolo()
            );

            return;
        }

        int x1 = origen.getX();
        int y1 = origen.getY();

        int x2 = destino.getX();
        int y2 = destino.getY();

        double dx = x2 - x1;
        double dy = y2 - y1;

        double distancia =
                Math.sqrt(
                        dx * dx + dy * dy
                );

        if (distancia == 0) {
            return;
        }

        double ux = dx / distancia;
        double uy = dy / distancia;

        int inicioX =
                (int) (
                        x1 + ux * RADIO_ESTADO
                );

        int inicioY =
                (int) (
                        y1 + uy * RADIO_ESTADO
                );

        int finX =
                (int) (
                        x2 - ux * RADIO_ESTADO
                );

        int finY =
                (int) (
                        y2 - uy * RADIO_ESTADO
                );

        g2.drawLine(
                inicioX,
                inicioY,
                finX,
                finY
        );

        dibujarPuntaFlecha(
                g2,
                inicioX,
                inicioY,
                finX,
                finY
        );

        int medioX =
                (inicioX + finX) / 2;

        int medioY =
                (inicioY + finY) / 2;

        g2.drawString(
                String.valueOf(
                        transicion.getSimbolo()
                ),
                medioX,
                medioY - 8
        );
    }

    private void dibujarBucle(
            Graphics2D g2,
            Estado estado,
            char simbolo) {

        int x = estado.getX();
        int y = estado.getY();

        int inicioX = x - 20;
        int inicioY = y - 22;

        int finX = x + 20;
        int finY = y - 22;

        Path2D.Double bucle =
                new Path2D.Double();

        bucle.moveTo(
                inicioX,
                inicioY
        );

        bucle.curveTo(
                x - 75,
                y - 100,
                x + 75,
                y - 100,
                finX,
                finY
        );

        g2.draw(bucle);

        int puntoAnteriorX = x + 38;
        int puntoAnteriorY = y - 65;

        dibujarPuntaFlecha(
                g2,
                puntoAnteriorX,
                puntoAnteriorY,
                finX,
                finY
        );

        g2.drawString(
                String.valueOf(simbolo),
                x - 5,
                y - 88
        );
    }

    private void dibujarFlechaInicial(
            Graphics2D g2,
            Estado estado) {

        int x = estado.getX();
        int y = estado.getY();

        int inicioX = x - 70;
        int inicioY = y;

        int finX = x - RADIO_ESTADO;
        int finY = y;

        g2.drawLine(
                inicioX,
                inicioY,
                finX,
                finY
        );

        dibujarPuntaFlecha(
                g2,
                inicioX,
                inicioY,
                finX,
                finY
        );
    }

    private void dibujarPuntaFlecha(
            Graphics2D g2,
            int x1,
            int y1,
            int x2,
            int y2) {

        double angulo =
                Math.atan2(
                        y2 - y1,
                        x2 - x1
                );

        int largo = 10;

        double angulo1 =
                angulo + Math.PI / 6;

        double angulo2 =
                angulo - Math.PI / 6;

        int x3 =
                (int) (
                        x2 - largo * Math.cos(angulo1)
                );

        int y3 =
                (int) (
                        y2 - largo * Math.sin(angulo1)
                );

        int x4 =
                (int) (
                        x2 - largo * Math.cos(angulo2)
                );

        int y4 =
                (int) (
                        y2 - largo * Math.sin(angulo2)
                );

        g2.drawLine(
                x2,
                y2,
                x3,
                y3
        );

        g2.drawLine(
                x2,
                y2,
                x4,
                y4
        );
    }

    private Estado buscarEstado(
            int mouseX,
            int mouseY) {

        for (Estado estado :
                automata.getEstados()) {

            int x = estado.getX();
            int y = estado.getY();

            int distanciaX =
                    mouseX - x;

            int distanciaY =
                    mouseY - y;

            if (
                    distanciaX * distanciaX
                    + distanciaY * distanciaY
                    <= RADIO_ESTADO * RADIO_ESTADO
            ) {
                return estado;
            }
        }

        return null;
    }
}