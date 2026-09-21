package com.automindx.vista;
 
import com.automindx.controlador.ControladorNoDeterminista;
import com.automindx.modelo.AutomataNoDeterminista;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.List;
 
public class PanelAFND extends JPanel {
 
    private final ControladorNoDeterminista controlador;
 
    private Estado estadoSeleccionado;
    private Estado estadoOrigen;
 
    private int offsetX;
    private int offsetY;
 
    private boolean modoCrearEstado;
    private boolean modoCrearTransicion;
 
    private Runnable alCambiar;
 
    private static final int RADIO = 30;
 
    private static final Color COLOR_ESTADO =
            new Color(142, 68, 173);
 
    public PanelAFND(ControladorNoDeterminista controlador) {
 
        this.controlador = controlador;
 
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
 
                    estadoSeleccionado.setX(e.getX() - offsetX);
                    estadoSeleccionado.setY(e.getY() - offsetY);
 
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
 
    /** Permite que el panel de controles se entere cuando cambian los estados/transiciones. */
    public void alCambiar(Runnable accion) {
        this.alCambiar = accion;
    }
 
    private void notificarCambio() {
        if (alCambiar != null) {
            alCambiar.run();
        }
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
 
            repaint();
        }
    }
 
    private void crearEstado(int x, int y) {
 
        controlador.crearEstado(x, y);
 
        desactivarModosEdicion();
        notificarCambio();
 
        repaint();
    }
 
    private void crearTransicion(Estado origen, Estado destino) {
 
        String entrada = JOptionPane.showInputDialog(
                this,
                "Símbolo de la transición (usa 'e' para ε):",
                "Crear transición",
                JOptionPane.PLAIN_MESSAGE
        );
 
        if (entrada == null || entrada.trim().isEmpty()) {
            estadoOrigen = null;
            return;
        }
 
        char simbolo = entrada.trim().charAt(0);
 
        if (simbolo == 'e') {
            simbolo = AutomataNoDeterminista.EPSILON;
        }
 
        boolean creada = controlador.agregarTransicion(
                origen,
                destino,
                simbolo
        );
 
        if (!creada) {
 
            JOptionPane.showMessageDialog(
                    this,
                    "Esa transición ya existe.",
                    "Transición duplicada",
                    JOptionPane.WARNING_MESSAGE
            );
        }
 
        estadoOrigen = null;
        desactivarModosEdicion();
        notificarCambio();
 
        repaint();
    }
 
    private void mostrarMenu(MouseEvent e) {
 
        Estado estado = buscarEstado(e.getX(), e.getY());
 
        if (estado != null) {
            menuEstado(estado, e.getX(), e.getY());
            return;
        }
 
        Transicion transicion = buscarTransicion(e.getX(), e.getY());
 
        if (transicion != null) {
            menuTransicion(transicion, e.getX(), e.getY());
        }
    }
 
    private void menuEstado(Estado estado, int x, int y) {
 
        JPopupMenu menu = new JPopupMenu();
 
        JMenuItem inicial = new JMenuItem("Establecer como inicial");
 
        JMenuItem finalItem = new JMenuItem(
                estado.isEstadoFinal()
                        ? "Quitar estado final"
                        : "Marcar como estado final"
        );
 
        JMenuItem eliminar = new JMenuItem("Eliminar estado");
 
        inicial.addActionListener(ev -> {
            controlador.establecerEstadoInicial(estado);
            notificarCambio();
            repaint();
        });
 
        finalItem.addActionListener(ev -> {
            controlador.alternarEstadoFinal(estado);
            notificarCambio();
            repaint();
        });
 
        eliminar.addActionListener(ev -> {
            controlador.eliminarEstado(estado);
            notificarCambio();
            repaint();
        });
 
        menu.add(inicial);
        menu.add(finalItem);
        menu.addSeparator();
        menu.add(eliminar);
 
        menu.show(this, x, y);
    }
 
    private void menuTransicion(Transicion transicion, int x, int y) {
 
        JPopupMenu menu = new JPopupMenu();
 
        JMenuItem eliminar = new JMenuItem("Eliminar transición");
 
        eliminar.addActionListener(ev -> {
            controlador.eliminarTransicion(transicion);
            notificarCambio();
            repaint();
        });
 
        menu.add(eliminar);
        menu.show(this, x, y);
    }
 
    private Estado buscarEstado(int x, int y) {
 
        for (Estado estado : controlador.getAFND().getEstados()) {
 
            double distancia = Math.hypot(
                    x - estado.getX(),
                    y - estado.getY()
            );
 
            if (distancia <= RADIO) {
                return estado;
            }
        }
 
        return null;
    }
 
    private Transicion buscarTransicion(int x, int y) {
 
        for (Transicion t : controlador.getAFND().getTransiciones()) {
 
            Estado origen = t.getOrigen();
            Estado destino = t.getDestino();
 
            if (origen.equals(destino)) {
 
                double distancia = Math.hypot(
                        x - origen.getX(),
                        y - (origen.getY() - RADIO - 35)
                );
 
                if (distancia < 30) {
                    return t;
                }
 
            } else {
 
                double distancia = distanciaLinea(
                        x, y,
                        origen.getX(), origen.getY(),
                        destino.getX(), destino.getY()
                );
 
                if (distancia < 10) {
                    return t;
                }
            }
        }
 
        return null;
    }
 
    private double distanciaLinea(
            double px, double py,
            double x1, double y1,
            double x2, double y2) {
 
        double dx = x2 - x1;
        double dy = y2 - y1;
 
        if (dx == 0 && dy == 0) {
            return Math.hypot(px - x1, py - y1);
        }
 
        double t = ((px - x1) * dx + (py - y1) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t));
 
        double cx = x1 + t * dx;
        double cy = y1 + t * dy;
 
        return Math.hypot(px - cx, py - cy);
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
 
        if (modoCrearTransicion && estadoOrigen != null) {
            g2.setColor(new Color(142, 68, 173, 120));
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(
                    estadoOrigen.getX() - RADIO - 4,
                    estadoOrigen.getY() - RADIO - 4,
                    RADIO * 2 + 8,
                    RADIO * 2 + 8
            );
        }
 
        g2.dispose();
    }
 
    private void dibujarEstados(Graphics2D g2) {
 
        for (Estado estado : controlador.getAFND().getEstados()) {
 
            int x = estado.getX();
            int y = estado.getY();
 
            g2.setColor(Color.WHITE);
            g2.fillOval(x - RADIO, y - RADIO, RADIO * 2, RADIO * 2);
 
            g2.setColor(COLOR_ESTADO);
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
 
            g2.setColor(Color.BLACK);
            g2.drawString(
                    nombre,
                    x - metricas.stringWidth(nombre) / 2,
                    y + metricas.getAscent() / 2 - 2
            );
        }
    }
 
    private void dibujarFlechaInicial(Graphics2D g2, Estado estado) {
 
        int x = estado.getX() - RADIO - 45;
        int y = estado.getY();
 
        g2.drawLine(x, y, estado.getX() - RADIO, y);
        dibujarPunta(g2, x + 25, y, 0);
    }
 
    private void dibujarTransiciones(Graphics2D g2) {
 
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(2));
 
        for (Transicion transicion : controlador.getAFND().getTransiciones()) {
            dibujarTransicion(g2, transicion);
        }
    }
 
    private void dibujarTransicion(Graphics2D g2, Transicion transicion) {
 
        Estado origen = transicion.getOrigen();
        Estado destino = transicion.getDestino();
 
        if (origen == null || destino == null) {
            return;
        }
 
        if (origen.equals(destino)) {
            dibujarBucle(g2, origen, transicion.getSimbolo());
            return;
        }
 
        int x1 = origen.getX();
        int y1 = origen.getY();
        int x2 = destino.getX();
        int y2 = destino.getY();
 
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
        dibujarPunta(g2, finX, finY, Math.atan2(dy, dx));
 
        String simbolo = String.valueOf(transicion.getSimbolo());
 
        g2.drawString(
                simbolo,
                (inicioX + finX) / 2,
                (inicioY + finY) / 2 - 8
        );
    }
 
    private void dibujarBucle(Graphics2D g2, Estado estado, char simbolo) {
 
        int x = estado.getX();
        int y = estado.getY();
 
        Path2D curva = new Path2D.Double();
 
        curva.moveTo(x - 18, y - RADIO + 5);
 
        curva.curveTo(
                x - 65, y - RADIO - 50,
                x + 65, y - RADIO - 50,
                x + 18, y - RADIO + 5
        );
 
        g2.draw(curva);
 
        dibujarPunta(g2, x + 18, y - RADIO + 5, Math.toRadians(45));
 
        g2.drawString(
                String.valueOf(simbolo),
                x - 5,
                y - RADIO - 40
        );
    }
 
    private void dibujarPunta(Graphics2D g2, int x, int y, double angulo) {
 
        int largo = 10;
 
        double a1 = angulo + Math.PI - 0.5;
        double a2 = angulo + Math.PI + 0.5;
 
        int x1 = (int) (x + largo * Math.cos(a1));
        int y1 = (int) (y + largo * Math.sin(a1));
        int x2 = (int) (x + largo * Math.cos(a2));
        int y2 = (int) (y + largo * Math.sin(a2));
 
        g2.drawLine(x, y, x1, y1);
        g2.drawLine(x, y, x2, y2);
    }
 
    public void activarModoCrearEstado() {
 
        modoCrearEstado = true;
        modoCrearTransicion = false;
        estadoOrigen = null;
 
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }
 
    public void activarModoCrearTransicion() {
 
        modoCrearEstado = false;
        modoCrearTransicion = true;
        estadoOrigen = null;
 
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
 
    public void desactivarModosEdicion() {
 
        modoCrearEstado = false;
        modoCrearTransicion = false;
        estadoOrigen = null;
 
        setCursor(Cursor.getDefaultCursor());
    }
 
    public List<Estado> getEstados() {
        return controlador.getAFND().getEstados();
    }
}