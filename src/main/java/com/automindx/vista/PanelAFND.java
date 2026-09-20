package com.automindx.vista;

import com.automindx.controlador.ControladorNoDeterminista;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PanelAFND extends JPanel {

    private final ControladorNoDeterminista controlador;

    private Estado estadoSeleccionado;
    private int offsetX;
    private int offsetY;

    private static final int RADIO_ESTADO = 30;

    public PanelAFND(
            ControladorNoDeterminista controlador) {

        this.controlador = controlador;

        setBackground(Color.WHITE);

        MouseAdapter mouseAdapter = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                Estado estado =
                        buscarEstado(e.getX(), e.getY());

                if (estado != null) {

                    estadoSeleccionado = estado;

                    offsetX =
                            e.getX() - estado.getX();

                    offsetY =
                            e.getY() - estado.getY();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {

                if (estadoSeleccionado == null) {
                    return;
                }

                int nuevoX =
                        e.getX() - offsetX;

                int nuevoY =
                        e.getY() - offsetY;

                estadoSeleccionado.setX(nuevoX);
                estadoSeleccionado.setY(nuevoY);

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                estadoSeleccionado = null;
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2
                        && e.getButton() == MouseEvent.BUTTON1) {

                    if (buscarEstado(
                            e.getX(),
                            e.getY()
                    ) == null) {

                        controlador.crearEstado(
                                e.getX(),
                                e.getY()
                        );

                        repaint();
                    }
                }
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        dibujarTransiciones(g2);
        dibujarEstados(g2);

        g2.dispose();
    }

    private void dibujarEstados(Graphics2D g2) {

        List<Estado> estados =
                controlador.getAFND().getEstados();

        for (Estado estado : estados) {

            int x = estado.getX();
            int y = estado.getY();

            g2.setColor(Color.WHITE);

            g2.fillOval(
                    x - RADIO_ESTADO,
                    y - RADIO_ESTADO,
                    RADIO_ESTADO * 2,
                    RADIO_ESTADO * 2
            );

            g2.setColor(Color.BLACK);

            g2.setStroke(
                    new BasicStroke(2)
            );

            g2.drawOval(
                    x - RADIO_ESTADO,
                    y - RADIO_ESTADO,
                    RADIO_ESTADO * 2,
                    RADIO_ESTADO * 2
            );

            if (estado.isEstadoFinal()) {

                g2.drawOval(
                        x - RADIO_ESTADO + 5,
                        y - RADIO_ESTADO + 5,
                        RADIO_ESTADO * 2 - 10,
                        RADIO_ESTADO * 2 - 10
                );
            }

            if (estado.isInicial()) {

                dibujarFlechaInicial(
                        g2,
                        estado
                );
            }

            String nombre =
                    estado.getNombre();

            int ancho =
                    g2.getFontMetrics()
                            .stringWidth(nombre);

            int alto =
                    g2.getFontMetrics()
                            .getAscent();

            g2.drawString(
                    nombre,
                    x - ancho / 2,
                    y + alto / 2
            );
        }
    }

    private void dibujarFlechaInicial(
            Graphics2D g2,
            Estado estado) {

        int x =
                estado.getX()
                        - RADIO_ESTADO
                        - 45;

        int y =
                estado.getY();

        g2.drawLine(
                x,
                y,
                estado.getX() - RADIO_ESTADO,
                y
        );

        g2.drawLine(
                x + 8,
                y - 5,
                x,
                y
        );

        g2.drawLine(
                x + 8,
                y + 5,
                x,
                y
        );
    }

    private void dibujarTransiciones(
            Graphics2D g2) {

        List<Transicion> transiciones =
                controlador.getAFND()
                        .getTransiciones();

        g2.setColor(Color.BLACK);

        for (Transicion transicion :
                transiciones) {

            dibujarTransicion(
                    g2,
                    transicion
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

        if (origen == null
                || destino == null) {
            return;
        }

        int x1 = origen.getX();
        int y1 = origen.getY();

        int x2 = destino.getX();
        int y2 = destino.getY();

        if (origen.equals(destino)) {

            dibujarBucle(
                    g2,
                    origen,
                    transicion.getSimbolo()
            );

            return;
        }

        double dx = x2 - x1;
        double dy = y2 - y1;

        double distancia =
                Math.sqrt(
                        dx * dx
                                + dy * dy
                );

        if (distancia == 0) {
            return;
        }

        double ux = dx / distancia;
        double uy = dy / distancia;

        int inicioX =
                (int) (
                        x1
                                + ux * RADIO_ESTADO
                );

        int inicioY =
                (int) (
                        y1
                                + uy * RADIO_ESTADO
                );

        int finX =
                (int) (
                        x2
                                - ux * RADIO_ESTADO
                );

        int finY =
                (int) (
                        y2
                                - uy * RADIO_ESTADO
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

        String simbolo =
                String.valueOf(
                        transicion.getSimbolo()
                );

        int textoX =
                (inicioX + finX) / 2;

        int textoY =
                (inicioY + finY) / 2 - 8;

        g2.drawString(
                simbolo,
                textoX,
                textoY
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

        int longitud = 10;

        double angulo1 =
                angulo + Math.PI * 0.8;

        double angulo2 =
                angulo - Math.PI * 0.8;

        int x3 =
                (int) (
                        x2
                                + longitud
                                * Math.cos(angulo1)
                );

        int y3 =
                (int) (
                        y2
                                + longitud
                                * Math.sin(angulo1)
                );

        int x4 =
                (int) (
                        x2
                                + longitud
                                * Math.cos(angulo2)
                );

        int y4 =
                (int) (
                        y2
                                + longitud
                                * Math.sin(angulo2)
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

    private void dibujarBucle(
            Graphics2D g2,
            Estado estado,
            char simbolo) {

        int x =
                estado.getX();

        int y =
                estado.getY();

        int diametro = 45;

        g2.drawOval(
                x - diametro / 2,
                y - RADIO_ESTADO - 35,
                diametro,
                diametro
        );

        int puntaX = x;
        int puntaY =
                y - RADIO_ESTADO - 12;

        g2.drawLine(
                puntaX,
                puntaY,
                puntaX - 7,
                puntaY + 8
        );

        g2.drawLine(
                puntaX,
                puntaY,
                puntaX + 7,
                puntaY + 8
        );

        g2.drawString(
                String.valueOf(simbolo),
                x + 25,
                y - RADIO_ESTADO - 35
        );
    }

    private Estado buscarEstado(
            int x,
            int y) {

        List<Estado> estados =
                controlador.getAFND().getEstados();

        for (Estado estado : estados) {

            int dx =
                    x - estado.getX();

            int dy =
                    y - estado.getY();

            if (dx * dx + dy * dy
                    <= RADIO_ESTADO * RADIO_ESTADO) {

                return estado;
            }
        }

        return null;
    }
}