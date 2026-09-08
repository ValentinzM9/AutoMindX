package com.automindx;

import com.automindx.modelo.Automata;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;
import com.automindx.modelo.Validador;

public class Main {

     public static void main(String[] args) {

        // Crear el autómata
        Automata automata = new Automata();

        // Crear estados
        Estado q0 = new Estado("q0", true, false, 100, 100);
        Estado q1 = new Estado("q1", false, true, 300, 100);

        // Agregar estados al autómata
        automata.agregarEstado(q0);
        automata.agregarEstado(q1);

        // Definir el estado inicial
        automata.establecerEstadoInicial(q0);

        // Definir el estado final
        automata.agregarEstadoFinal(q1);

        // Agregar símbolos al alfabeto
        automata.agregarSimbolo('a');
        automata.agregarSimbolo('b');

        // Crear transiciones
        Transicion t1 = new Transicion(q0, q1, 'a');
        Transicion t2 = new Transicion(q1, q1, 'b');

        // Agregar transiciones
        automata.agregarTransicion(t1);
        automata.agregarTransicion(t2);

        // Crear el validador
        Validador validador = new Validador(automata);

        // Cadena que vamos a validar
        String cadena = "ab";

        boolean resultado = validador.validar(cadena);

System.out.println("Cadena: " + cadena);

System.out.println("Recorrido:");

for (Estado estado : validador.getRecorrido()) {
    System.out.println("-> " + estado.getNombre());
}

System.out.println("Transiciones recorridas:");

for (Transicion transicion : validador.getTransicionesRecorridas()) {
    System.out.println(
        transicion.getOrigen().getNombre()
        + " --"
        + transicion.getSimbolo()
        + "--> "
        + transicion.getDestino().getNombre()
    );
}

System.out.println("Resultado: " + (resultado ? "ACEPTADA" : "RECHAZADA"));
    }
}