package com.automindx.vista;

import javax.swing.JFrame;

import com.automindx.modelo.Automata;

public class VentanaPrincipal extends JFrame {

     public VentanaPrincipal(Automata automata) {

        setTitle("AutoMindX - Simulador de Autómatas");
        setSize(1000, 700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        PanelAutomata panelAutomata = new PanelAutomata(automata);

        add(panelAutomata);
    }
}