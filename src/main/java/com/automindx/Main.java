package com.automindx;

import com.automindx.modelo.Automata;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;
import com.automindx.vista.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {

        // Crear el autómata
        Automata automata = new Automata();

        // Crear estados
        Estado q0 = new Estado(
                "q0",
                true,
                false,
                200,
                300
        );

        Estado q1 = new Estado(
                "q1",
                false,
                true,
                500,
                300
        );

        // Agregar estados
        automata.agregarEstado(q0);
        automata.agregarEstado(q1);

        // Establecer estado inicial
        automata.establecerEstadoInicial(q0);

        // Establecer estado final
        automata.agregarEstadoFinal(q1);

        // Agregar símbolos al alfabeto
        automata.agregarSimbolo('a');
        automata.agregarSimbolo('b');

        // Crear transiciones
        Transicion t1 = new Transicion(
                q0,
                q1,
                'a'
        );

        Transicion t2 = new Transicion(
                q1,
                q1,
                'b'
        );

        // Agregar transiciones al autómata
        automata.agregarTransicion(t1);
        automata.agregarTransicion(t2);

        // Crear ventana
        VentanaPrincipal ventana =
                new VentanaPrincipal(automata);

        ventana.setVisible(true);
    }
}
