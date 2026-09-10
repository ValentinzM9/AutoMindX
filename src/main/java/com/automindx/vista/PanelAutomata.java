package com.automindx.vista;

import com.automindx.modelo.Automata;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Arc2D;

public class PanelAutomata extends JPanel {

    private Automata automata;

    // Estado que estamos arrastrando
    private Estado estadoSeleccionado;

    // Diferencia entre el clic y el centro del estado
    private int offsetX;
    private int offsetY;

    // Radio visual de los estados
    private static final int RADIO_ESTADO = 30;

    public PanelAutomata(Automata automata) {

        this.automata = automata;

        // Detectar clic sobre un estado
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                estadoSeleccionado = buscarEstado(
                        e.getX(),
                        e.getY()
                );

                if (estadoSeleccionado != null) {

                    offsetX = e.getX()
                            - estadoSeleccionado.getX();

                    offsetY = e.getY()
                            - estadoSeleccionado.getY();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                estadoSeleccionado = null;
            }
        });

        // Detectar movimiento del mouse
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

                    // Actualizar el dibujo
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke(2));

        // Primero dibujamos las transiciones
        for (Transicion transicion
                : automata.getTransiciones()) {

            dibujarTransicion(g2, transicion);
        }

        // Después dibujamos los estados
        for (Estado estado : automata.getEstados()) {

            dibujarEstado(g2, estado);
        }
    }

    /**
     * Dibuja un estado del autómata.
     */
    private void dibujarEstado(Graphics2D g2, Estado estado) {

        int x = estado.getX();
        int y = estado.getY();

        // Círculo principal
        g2.drawOval(
                x - RADIO_ESTADO,
                y - RADIO_ESTADO,
                RADIO_ESTADO * 2,
                RADIO_ESTADO * 2
        );

        // Nombre del estado
        g2.drawString(
                estado.getNombre(),
                x - 8,
                y + 5
        );

        // Segundo círculo para estado final
        if (estado.isEstadoFinal()) {

            g2.drawOval(
                    x - 25,
                    y - 25,
                    50,
                    50
            );
        }

        // Flecha que indica el estado inicial
        if (estado.isInicial()) {

            dibujarFlechaInicial(g2, estado);
        }
    }

    /**
     * Dibuja una transición entre dos estados.
     */
    private void dibujarTransicion(
            Graphics2D g2,
            Transicion transicion) {

        Estado origen = transicion.getOrigen();
        Estado destino = transicion.getDestino();

        int x1 = origen.getX();
        int y1 = origen.getY();

        int x2 = destino.getX();
        int y2 = destino.getY();

        // Si origen y destino son el mismo estado,
        // dibujamos un bucle.
        if (origen == destino) {

            dibujarBucle(
                    g2,
                    origen,
                    transicion.getSimbolo()
            );

            return;
        }

        // Vector entre los dos estados
        double dx = x2 - x1;
        double dy = y2 - y1;

        double distancia = Math.sqrt(
                dx * dx + dy * dy
        );

        // Evitamos división por cero
        if (distancia == 0) {
            return;
        }

        // Dirección normalizada
        double ux = dx / distancia;
        double uy = dy / distancia;

        // Punto donde comienza la flecha
        int inicioX = (int) (x1 + ux * RADIO_ESTADO);
        int inicioY = (int) (y1 + uy * RADIO_ESTADO);

        // Punto donde termina la flecha
        int finX = (int) (x2 - ux * RADIO_ESTADO);
        int finY = (int) (y2 - uy * RADIO_ESTADO);

        // Dibujar línea
        g2.drawLine(
                inicioX,
                inicioY,
                finX,
                finY
        );

        // Dibujar punta de flecha
        dibujarPuntaFlecha(
                g2,
                inicioX,
                inicioY,
                finX,
                finY
        );

        // Dibujar símbolo
        int medioX = (inicioX + finX) / 2;
        int medioY = (inicioY + finY) / 2;

        g2.drawString(
                String.valueOf(transicion.getSimbolo()),
                medioX,
                medioY - 8
        );
    }

    /**
     * Dibuja un bucle cuando una transición vuelve
     * al mismo estado.
     */
    private void dibujarBucle(
            Graphics2D g2,
            Estado estado,
            char simbolo) {

        int x = estado.getX();
        int y = estado.getY();

        int ancho = 50;
        int alto = 50;

        int arcoX = x - ancho / 2;
        int arcoY = y - RADIO_ESTADO - 35;

        Arc2D.Double arco = new Arc2D.Double(
                arcoX,
                arcoY,
                ancho,
                alto,
                30,
                300,
                Arc2D.OPEN
        );

        g2.draw(arco);

        // Punta de flecha del bucle
        int puntaX = x + 20;
        int puntaY = y - 55;

        dibujarPuntaFlecha(
                g2,
                x + 15,
                y - 48,
                puntaX,
                puntaY
        );

        // Símbolo del bucle
        g2.drawString(
                String.valueOf(simbolo),
                x + 25,
                y - 55
        );
    }

    /**
     * Dibuja una flecha que indica cuál es
     * el estado inicial.
     */
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

    /**
     * Dibuja la punta de una flecha.
     */
    private void dibujarPuntaFlecha(
            Graphics2D g2,
            int x1,
            int y1,
            int x2,
            int y2) {

        double angulo = Math.atan2(
                y2 - y1,
                x2 - x1
        );

        int largo = 10;

        double angulo1 = angulo + Math.PI / 6;
        double angulo2 = angulo - Math.PI / 6;

        int x3 = (int) (
                x2 - largo * Math.cos(angulo1)
        );

        int y3 = (int) (
                y2 - largo * Math.sin(angulo1)
        );

        int x4 = (int) (
                x2 - largo * Math.cos(angulo2)
        );

        int y4 = (int) (
                y2 - largo * Math.sin(angulo2)
        );

        g2.drawLine(x2, y2, x3, y3);
        g2.drawLine(x2, y2, x4, y4);
    }

    /**
     * Busca si el usuario hizo clic sobre un estado.
     */
    private Estado buscarEstado(
            int mouseX,
            int mouseY) {

        for (Estado estado : automata.getEstados()) {

            int x = estado.getX();
            int y = estado.getY();

            int distanciaX = mouseX - x;
            int distanciaY = mouseY - y;

            if (distanciaX * distanciaX
                    + distanciaY * distanciaY
                    <= RADIO_ESTADO * RADIO_ESTADO) {

                return estado;
            }
        }

        return null;
    }
}