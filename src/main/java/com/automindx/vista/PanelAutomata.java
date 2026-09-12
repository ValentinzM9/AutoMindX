package com.automindx.vista;

import com.automindx.modelo.Automata;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Panel gráfico donde se dibuja y manipula el autómata.
 */
public class PanelAutomata extends JPanel {

    private Automata automata;

    // Estado que actualmente se está arrastrando
    private Estado estadoSeleccionado;

    // Distancia entre el mouse y el centro del estado
    private int offsetX;
    private int offsetY;

    // Radio visual de los estados
    private static final int RADIO_ESTADO = 30;

    public PanelAutomata(Automata automata) {

        this.automata = automata;

        configurarMouse();
    }

    /**
     * Configura los eventos del mouse para permitir
     * seleccionar y arrastrar estados.
     */
    private void configurarMouse() {

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

    /**
     * Método principal de dibujo.
     */
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        try {

            g2.setStroke(
                    new BasicStroke(2)
            );

            /*
             * Primero dibujamos las transiciones.
             */
            for (Transicion transicion
                    : automata.getTransiciones()) {

                dibujarTransicion(
                        g2,
                        transicion
                );
            }

            /*
             * Después dibujamos los estados.
             */
            for (Estado estado
                    : automata.getEstados()) {

                dibujarEstado(
                        g2,
                        estado
                );
            }

        } finally {

            g2.dispose();
        }
    }

    /**
     * Dibuja un estado.
     */
    private void dibujarEstado(
            Graphics2D g2,
            Estado estado) {

        int x = estado.getX();
        int y = estado.getY();

        /*
         * Círculo principal.
         */
        g2.drawOval(
                x - RADIO_ESTADO,
                y - RADIO_ESTADO,
                RADIO_ESTADO * 2,
                RADIO_ESTADO * 2
        );

        /*
         * Nombre del estado.
         */
        g2.drawString(
                estado.getNombre(),
                x - 8,
                y + 5
        );

        /*
         * Segundo círculo si es estado final.
         */
        if (estado.isEstadoFinal()) {

            g2.drawOval(
                    x - 25,
                    y - 25,
                    50,
                    50
            );
        }

        /*
         * Flecha del estado inicial.
         */
        if (estado.isInicial()) {

            dibujarFlechaInicial(
                    g2,
                    estado
            );
        }
    }

    /**
     * Dibuja una transición.
     */
    private void dibujarTransicion(
            Graphics2D g2,
            Transicion transicion) {

        Estado origen = transicion.getOrigen();
        Estado destino = transicion.getDestino();

        /*
         * Si la transición regresa al mismo estado,
         * dibujamos un bucle.
         */
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

        /*
         * Diferencia entre los centros.
         */
        double dx = x2 - x1;
        double dy = y2 - y1;

        /*
         * Distancia entre los estados.
         */
        double distancia = Math.sqrt(
                dx * dx + dy * dy
        );

        if (distancia == 0) {
            return;
        }

        /*
         * Vector unitario.
         */
        double ux = dx / distancia;
        double uy = dy / distancia;

        /*
         * Inicio de la transición,
         * justo en el borde del estado origen.
         */
        int inicioX = (int) (
                x1 + ux * RADIO_ESTADO
        );

        int inicioY = (int) (
                y1 + uy * RADIO_ESTADO
        );

        /*
         * Final de la transición,
         * justo en el borde del estado destino.
         */
        int finX = (int) (
                x2 - ux * RADIO_ESTADO
        );

        int finY = (int) (
                y2 - uy * RADIO_ESTADO
        );

        /*
         * Dibujar línea.
         */
        g2.drawLine(
                inicioX,
                inicioY,
                finX,
                finY
        );

        /*
         * Dibujar flecha.
         */
        dibujarPuntaFlecha(
                g2,
                inicioX,
                inicioY,
                finX,
                finY
        );

        /*
         * Posición del símbolo.
         */
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

    /**
     * Dibuja una transición que comienza y termina
     * en el mismo estado.
     *
     * El bucle se construye mediante una curva
     * Bézier para que sus extremos estén conectados
     * directamente al estado.
     */
    private void dibujarBucle(
            Graphics2D g2,
            Estado estado,
            char simbolo) {

        int x = estado.getX();
        int y = estado.getY();

        /*
         * Puntos donde el bucle se conecta
         * con el estado.
         */
        int inicioX = x - 20;
        int inicioY = y - 22;

        int finX = x + 20;
        int finY = y - 22;

        /*
         * Creamos una curva.
         *
         * La curva:
         *
         *        ┌────────┐
         *       /          \
         *      /            \
         *     ●              ●
         *       \          /
         *          estado
         *
         * Los extremos quedan conectados
         * al estado.
         */
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

        /*
         * Dibujar el bucle.
         */
        g2.draw(bucle);

        /*
         * La flecha se coloca exactamente
         * en el punto donde termina la curva.
         *
         * Como la curva llega desde arriba-derecha
         * hacia el estado, la flecha apunta
         * hacia abajo-izquierda.
         */
        int puntoAnteriorX = x + 38;
        int puntoAnteriorY = y - 65;

        dibujarPuntaFlecha(
                g2,
                puntoAnteriorX,
                puntoAnteriorY,
                finX,
                finY
        );

        /*
         * Símbolo de la transición.
         */
        g2.drawString(
                String.valueOf(simbolo),
                x - 5,
                y - 88
        );
    }

    /**
     * Dibuja la flecha del estado inicial.
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

        /*
         * Línea.
         */
        g2.drawLine(
                inicioX,
                inicioY,
                finX,
                finY
        );

        /*
         * Punta.
         */
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

        /*
         * Dirección de la línea.
         */
        double angulo = Math.atan2(
                y2 - y1,
                x2 - x1
        );

        /*
         * Tamaño de la punta.
         */
        int largo = 10;

        /*
         * Ángulos de los lados de la punta.
         */
        double angulo1 =
                angulo + Math.PI / 6;

        double angulo2 =
                angulo - Math.PI / 6;

        /*
         * Primer extremo.
         */
        int x3 = (int) (
                x2 - largo * Math.cos(angulo1)
        );

        int y3 = (int) (
                y2 - largo * Math.sin(angulo1)
        );

        /*
         * Segundo extremo.
         */
        int x4 = (int) (
                x2 - largo * Math.cos(angulo2)
        );

        int y4 = (int) (
                y2 - largo * Math.sin(angulo2)
        );

        /*
         * Dibujar punta.
         */
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

    /**
     * Busca el estado sobre el que hizo clic
     * el usuario.
     */
    private Estado buscarEstado(
            int mouseX,
            int mouseY) {

        for (Estado estado
                : automata.getEstados()) {

            int x = estado.getX();
            int y = estado.getY();

            int distanciaX =
                    mouseX - x;

            int distanciaY =
                    mouseY - y;

            /*
             * Comprobar si el clic está dentro
             * del círculo.
             */
            if (distanciaX * distanciaX
                    + distanciaY * distanciaY
                    <= RADIO_ESTADO * RADIO_ESTADO) {

                return estado;
            }
        }

        return null;
    }
}