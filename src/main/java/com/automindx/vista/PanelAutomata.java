package com.automindx.vista;

import com.automindx.modelo.Automata;
import com.automindx.modelo.Estado;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class PanelAutomata extends JPanel {
    private Automata automata;

    public PanelAutomata(Automata automata) {
        this.automata = automata;
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke(2));

        for (Estado estado : automata.getEstados()) {

            int x = estado.getX();
            int y = estado.getY();

            g2.drawOval(x - 30, y - 30, 60, 60);
            
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
        }
    }
    
}
