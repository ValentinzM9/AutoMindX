package com.automindx;

import com.automindx.controlador.ControladorAutomata;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;
import com.automindx.vista.VentanaPrincipal;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        /*
         * Crear el controlador.
         */
        ControladorAutomata controlador =
                new ControladorAutomata();

        /*
         * Crear el autómata de ejemplo.
         */
        crearAutomataEjemplo(controlador);

        /*
         * Crear la interfaz gráfica.
         */
        SwingUtilities.invokeLater(() -> {

            VentanaPrincipal ventana =
                    new VentanaPrincipal(
                            controlador
                    );

            ventana.setVisible(true);
        });
    }

    /**
     * Crea el autómata que hemos utilizado
     * durante nuestras pruebas.
     */
    private static void crearAutomataEjemplo(
            ControladorAutomata controlador) {

        /*
         * Crear estados.
         */
        Estado q0 = new Estado(
                "q0",
                true,
                false,
                250,
                300
        );

        Estado q1 = new Estado(
                "q1",
                false,
                true,
                550,
                300
        );

        /*
         * Agregar estados.
         */
        controlador.agregarEstado(q0);
        controlador.agregarEstado(q1);

        /*
         * Estado inicial.
         */
        controlador.establecerEstadoInicial(q0);

        /*
         * Estado final.
         */
        controlador.agregarEstadoFinal(q1);

        /*
         * Alfabeto.
         */
        controlador.agregarSimbolo('a');
        controlador.agregarSimbolo('b');

        /*
         * Transición:
         *
         * q0 --a--> q1
         */
        Transicion t1 =
                new Transicion(
                        q0,
                        q1,
                        'a'
                );

        /*
         * Bucle:
         *
         * q1 --b--> q1
         */
        Transicion t2 =
                new Transicion(
                        q1,
                        q1,
                        'b'
                );

        /*
         * Agregar transiciones.
         */
        controlador.agregarTransicion(t1);
        controlador.agregarTransicion(t2);
    }
}