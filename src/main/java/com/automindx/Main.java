package com.automindx;

import com.automindx.controlador.ControladorAutomata;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;

public class Main {
public static void main(String[] args) {

        // Crear el controlador
        ControladorAutomata controlador = new ControladorAutomata();

        // Crear estados
        Estado q0 = new Estado("q0", true, false, 100, 100);
        Estado q1 = new Estado("q1", false, true, 300, 100);

        // Agregar estados
        controlador.agregarEstado(q0);
        controlador.agregarEstado(q1);

        // Establecer estado inicial
        controlador.establecerEstadoInicial(q0);

        // Establecer estado final
        controlador.agregarEstadoFinal(q1);

        // Agregar símbolos
        controlador.agregarSimbolo('a');
        controlador.agregarSimbolo('b');

        // Crear transiciones
        Transicion t1 = new Transicion(q0, q1, 'a');
        Transicion t2 = new Transicion(q1, q1, 'b');

        // Agregar transiciones
        controlador.agregarTransicion(t1);
        controlador.agregarTransicion(t2);

        // Validar cadena
        String cadena = "ab";

        boolean resultado = controlador.validarCadena(cadena);

        // Mostrar resultado
        System.out.println("Cadena: " + cadena);
        System.out.println("Resultado: "
                + (resultado ? "ACEPTADA" : "RECHAZADA"));
    }
}