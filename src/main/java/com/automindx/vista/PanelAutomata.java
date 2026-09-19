package com.automindx.vista;

import com.automindx.controlador.ControladorAutomata;
import com.automindx.modelo.Automata;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class PanelAutomata extends JPanel {

    private final ControladorAutomata controlador;
    private final Automata automata;

    private Estado estadoSeleccionado;
    private Estado estadoOrigen;

    private int offsetX;
    private int offsetY;

    private boolean modoCrearEstado;
    private boolean modoCrearTransicion;

    private List<Estado> recorrido = new ArrayList<>();
    private List<Transicion> transicionesRecorridas = new ArrayList<>();
    private boolean aceptada;

    private static final int RADIO = 30;

    private static final Color COLOR_ESTADO =
        new Color(142, 68, 173);

    private static final Color COLOR_ACEPTADA =
            new Color(46, 125, 50);

    private static final Color COLOR_RECHAZADA =
            new Color(198, 40, 40);

    public PanelAutomata(ControladorAutomata controlador) {

        this.controlador = controlador;
        this.automata = controlador.getAutomata();

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(850, 600));

        MouseAdapter mouse = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                if (SwingUtilities.isRightMouseButton(e)) {
                    mostrarMenu(e);
                    return;
                }

                if (SwingUtilities.isLeftMouseButton(e)) {
                    manejarPresion(e);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {

                if (estadoSeleccionado != null
                        && !modoCrearEstado
                        && !modoCrearTransicion) {

                    estadoSeleccionado.setX(
                            e.getX() - offsetX
                    );

                    estadoSeleccionado.setY(
                            e.getY() - offsetY
                    );

                    limpiarResaltado();
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                if (SwingUtilities.isLeftMouseButton(e)) {
                    estadoSeleccionado = null;
                }
            }
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    private void manejarPresion(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        Estado estado = buscarEstado(x, y);

        if (modoCrearEstado) {
            crearEstado(x, y);
            return;
        }

        if (modoCrearTransicion) {

            if (estado == null) {
                return;
            }

            if (estadoOrigen == null) {

                estadoOrigen = estado;

                repaint();
                return;
            }

            crearTransicion(estadoOrigen, estado);
            return;
        }

        if (estado != null) {

            estadoSeleccionado = estado;

            offsetX = x - estado.getX();
            offsetY = y - estado.getY();

            limpiarResaltado();
            repaint();
        }
    }

    private void crearEstado(int x, int y) {

        controlador.crearEstado(x, y);

        desactivarModosEdicion();
        limpiarResaltado();

        repaint();
    }

    private void crearTransicion(
            Estado origen,
            Estado destino) {

        String entrada = JOptionPane.showInputDialog(
                this,
                "Símbolo de la transición:",
                "Crear transición",
                JOptionPane.PLAIN_MESSAGE
        );

        if (entrada == null
                || entrada.trim().isEmpty()) {

            estadoOrigen = null;
            return;
        }

        char simbolo = entrada.trim().charAt(0);

        Transicion transicion =
                new Transicion(
                        origen,
                        destino,
                        simbolo
                );

        if (!controlador.agregarTransicion(transicion)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ya existe una transición desde "
                            + origen.getNombre()
                            + " con el símbolo "
                            + simbolo,
                    "Transición duplicada",
                    JOptionPane.WARNING_MESSAGE
            );
        }

        estadoOrigen = null;
        desactivarModosEdicion();
        limpiarResaltado();

        repaint();
    }

    private void mostrarMenu(MouseEvent e) {

        Estado estado = buscarEstado(
                e.getX(),
                e.getY()
        );

        if (estado != null) {
            menuEstado(estado, e.getX(), e.getY());
            return;
        }

        Transicion transicion =
                buscarTransicion(
                        e.getX(),
                        e.getY()
                );

        if (transicion != null) {
            menuTransicion(transicion, e.getX(), e.getY());
        }
    }

    private void menuEstado(
            Estado estado,
            int x,
            int y) {

        JPopupMenu menu = new JPopupMenu();

        JMenuItem inicial =
                new JMenuItem("Establecer como inicial");

        JMenuItem finalItem =
                new JMenuItem(
                        estado.isEstadoFinal()
                                ? "Quitar estado final"
                                : "Marcar como estado final"
                );

        JMenuItem eliminar =
                new JMenuItem("Eliminar estado");

        inicial.addActionListener(e -> {

            controlador.establecerEstadoInicial(
                    estado
            );

            repaint();
        });

        finalItem.addActionListener(e -> {

            controlador.alternarEstadoFinal(
                    estado
            );

            repaint();
        });

        eliminar.addActionListener(e -> {

            controlador.eliminarEstado(
                    estado
            );

            limpiarResaltado();
            repaint();
        });

        menu.add(inicial);
        menu.add(finalItem);
        menu.addSeparator();
        menu.add(eliminar);

        menu.show(this, x, y);
    }

    private void menuTransicion(
            Transicion transicion,
            int x,
            int y) {

        JPopupMenu menu = new JPopupMenu();

        JMenuItem eliminar =
                new JMenuItem(
                        "Eliminar transición"
                );

        eliminar.addActionListener(e -> {

            controlador.eliminarTransicion(
                    transicion
            );

            limpiarResaltado();
            repaint();
        });

        menu.add(eliminar);
        menu.show(this, x, y);
    }

    private Estado buscarEstado(int x, int y) {

        for (Estado estado : automata.getEstados()) {

            double distancia =
                    Math.hypot(
                            x - estado.getX(),
                            y - estado.getY()
                    );

            if (distancia <= RADIO) {
                return estado;
            }
        }

        return null;
    }

    private Transicion buscarTransicion(
            int x,
            int y) {

        for (Transicion t :
                automata.getTransiciones()) {

            Estado origen = t.getOrigen();
            Estado destino = t.getDestino();

            if (origen == destino) {

                double distancia =
                        Math.hypot(
                                x - origen.getX(),
                                y - (origen.getY() - 65)
                        );

                if (distancia < 35) {
                    return t;
                }

            } else {

                double distancia =
                        distanciaLinea(
                                x,
                                y,
                                origen.getX(),
                                origen.getY(),
                                destino.getX(),
                                destino.getY()
                        );

                if (distancia < 10) {
                    return t;
                }
            }
        }

        return null;
    }

    private double distanciaLinea(
            double px,
            double py,
            double x1,
            double y1,
            double x2,
            double y2) {

        double dx = x2 - x1;
        double dy = y2 - y1;

        if (dx == 0 && dy == 0) {
            return Math.hypot(
                    px - x1,
                    py - y1
            );
        }

        double t =
                ((px - x1) * dx
                        + (py - y1) * dy)
                        / (dx * dx + dy * dy);

        t = Math.max(
                0,
                Math.min(1, t)
        );

        double cx = x1 + t * dx;
        double cy = y1 + t * dy;

        return Math.hypot(
                px - cx,
                py - cy
        );
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

        for (Transicion t :
                automata.getTransiciones()) {

            dibujarTransicion(g2, t);
        }

        for (Estado estado :
                automata.getEstados()) {

            dibujarEstado(g2, estado);
        }

        g2.dispose();
    }

    private void dibujarEstado(
            Graphics2D g2,
            Estado estado) {

        boolean recorridoEstado =
                recorrido.contains(estado);

        Color color = COLOR_ESTADO;

        if (recorridoEstado) {
            color = aceptada
                    ? COLOR_ACEPTADA
                    : COLOR_RECHAZADA;
        }

        g2.setColor(color);
        g2.setStroke(
                new BasicStroke(2)
        );

        g2.drawOval(
                estado.getX() - RADIO,
                estado.getY() - RADIO,
                RADIO * 2,
                RADIO * 2
        );

        if (estado.isEstadoFinal()) {

            g2.drawOval(
                    estado.getX() - RADIO + 5,
                    estado.getY() - RADIO + 5,
                    RADIO * 2 - 10,
                    RADIO * 2 - 10
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

        FontMetrics fm =
                g2.getFontMetrics();

        int ancho =
                fm.stringWidth(nombre);

        g2.drawString(
                nombre,
                estado.getX() - ancho / 2,
                estado.getY() + 5
        );
    }

    private void dibujarFlechaInicial(
            Graphics2D g2,
            Estado estado) {

        int x = estado.getX() - RADIO - 45;
        int y = estado.getY();

        g2.drawLine(
                x,
                y,
                estado.getX() - RADIO,
                y
        );

        dibujarPunta(
                g2,
                x + 25,
                y,
                0
        );
    }

    private void dibujarTransicion(
            Graphics2D g2,
            Transicion transicion) {

        Estado origen =
                transicion.getOrigen();

        Estado destino =
                transicion.getDestino();

        boolean recorrida =
                transicionesRecorridas.contains(
                        transicion
                );

        g2.setColor(
                recorrida
                        ? (aceptada
                        ? COLOR_ACEPTADA
                        : COLOR_RECHAZADA)
                        : Color.DARK_GRAY
        );

        g2.setStroke(
                new BasicStroke(
                        recorrida ? 3 : 2
                )
        );

        if (origen == destino) {

            dibujarBucle(
                    g2,
                    origen,
                    transicion
            );

            return;
        }

        double dx =
                destino.getX() - origen.getX();

        double dy =
                destino.getY() - origen.getY();

        double distancia =
                Math.hypot(dx, dy);

        double ux = dx / distancia;
        double uy = dy / distancia;

        int x1 =
                (int) (origen.getX()
                        + ux * RADIO);

        int y1 =
                (int) (origen.getY()
                        + uy * RADIO);

        int x2 =
                (int) (destino.getX()
                        - ux * RADIO);

        int y2 =
                (int) (destino.getY()
                        - uy * RADIO);

        g2.drawLine(
                x1,
                y1,
                x2,
                y2
        );

        dibujarPunta(
                g2,
                x2,
                y2,
                Math.atan2(dy, dx)
        );

        String simbolo =
                String.valueOf(
                        transicion.getSimbolo()
                );

        g2.drawString(
                simbolo,
                (x1 + x2) / 2,
                (y1 + y2) / 2 - 8
        );
    }

    private void dibujarBucle(
            Graphics2D g2,
            Estado estado,
            Transicion transicion) {

        int x = estado.getX();
        int y = estado.getY();

        Path2D curva = new Path2D.Double();

        curva.moveTo(
                x - 18,
                y - 25
        );

        curva.curveTo(
                x - 65,
                y - 80,
                x + 65,
                y - 80,
                x + 18,
                y - 25
        );

        g2.draw(curva);

        dibujarPunta(
                g2,
                x + 18,
                y - 25,
                Math.toRadians(45)
        );

        g2.drawString(
                String.valueOf(
                        transicion.getSimbolo()
                ),
                x - 5,
                y - 70
        );
    }

    private void dibujarPunta(
            Graphics2D g2,
            int x,
            int y,
            double angulo) {

        int largo = 10;

        double a1 =
                angulo + Math.PI - 0.5;

        double a2 =
                angulo + Math.PI + 0.5;

        int x1 =
                (int) (
                        x + largo * Math.cos(a1)
                );

        int y1 =
                (int) (
                        y + largo * Math.sin(a1)
                );

        int x2 =
                (int) (
                        x + largo * Math.cos(a2)
                );

        int y2 =
                (int) (
                        y + largo * Math.sin(a2)
                );

        g2.drawLine(x, y, x1, y1);
        g2.drawLine(x, y, x2, y2);
    }

    public void activarModoCrearEstado() {

        modoCrearEstado = true;
        modoCrearTransicion = false;
        estadoOrigen = null;

        setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.CROSSHAIR_CURSOR
                )
        );
    }

    public void activarModoCrearTransicion() {

        modoCrearEstado = false;
        modoCrearTransicion = true;
        estadoOrigen = null;

        setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    public void desactivarModosEdicion() {

        modoCrearEstado = false;
        modoCrearTransicion = false;
        estadoOrigen = null;

        setCursor(
                Cursor.getDefaultCursor()
        );
    }

    public void resaltarRecorrido(
            List<Estado> recorrido,
            List<Transicion> transiciones,
            boolean aceptada) {

        this.recorrido =
                new ArrayList<>(
                        recorrido
                );

        this.transicionesRecorridas =
                new ArrayList<>(
                        transiciones
                );

        this.aceptada = aceptada;

        repaint();
    }

    public void limpiarResaltado() {

        recorrido.clear();
        transicionesRecorridas.clear();

        aceptada = false;

        repaint();
    }
}