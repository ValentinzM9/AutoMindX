package com.automindx.controlador;

import com.automindx.modelo.Automata;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;
import com.automindx.modelo.Validador;

public class ControladorAutomata {

    private final Automata automata;
    private final Validador validador;

    public ControladorAutomata() {
        automata = new Automata();
        validador = new Validador(automata);
    }

    public void agregarEstado(Estado estado) {
        automata.agregarEstado(estado);
    }

    public void eliminarEstado(Estado estado) {
        automata.eliminarEstado(estado);
    }

    public void agregarSimbolo(char simbolo) {
        automata.agregarSimbolo(simbolo);
    }

    public boolean agregarTransicion(Transicion transicion) {

        if (transicion == null) {
            return false;
        }

        if (transicion.getOrigen() == null
                || transicion.getDestino() == null) {
            return false;
        }

        if (automata.buscarTransicion(
                transicion.getOrigen(),
                transicion.getSimbolo()) != null) {
            return false;
        }

        automata.agregarTransicion(transicion);
        return true;
    }

    public void eliminarTransicion(Transicion transicion) {
        automata.eliminarTransicion(transicion);
    }

    public void establecerEstadoInicial(Estado estado) {
        automata.establecerEstadoInicial(estado);
    }

    public void agregarEstadoFinal(Estado estado) {
        automata.agregarEstadoFinal(estado);
    }

    public void quitarEstadoFinal(Estado estado) {
        automata.quitarEstadoFinal(estado);
    }

    public void alternarEstadoFinal(Estado estado) {

        if (automata.esEstadoFinal(estado)) {
            automata.quitarEstadoFinal(estado);
        } else {
            automata.agregarEstadoFinal(estado);
        }
    }

    public boolean validarCadena(String cadena) {

        if (cadena == null) {
            return false;
        }

        return validador.validar(cadena);
    }

    public Validador.Resultado validarCadenaDetallado(
            String cadena) {

        return validador.validarDetallado(
                cadena == null ? "" : cadena
        );
    }

    public void limpiarAutomata() {
        automata.limpiar();
    }

    public Estado crearEstado(int x, int y) {

        String nombre = generarNombreEstado();

        Estado estado = new Estado(
                nombre,
                false,
                false,
                x,
                y
        );

        automata.agregarEstado(estado);

        return estado;
    }

    private String generarNombreEstado() {

        int numero = 0;

        while (true) {

            String nombre = "q" + numero;

            boolean existe = false;

            for (Estado estado : automata.getEstados()) {

                if (nombre.equals(estado.getNombre())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                return nombre;
            }

            numero++;
        }
    }

    public void cargarAutomataEjemplo() {

        limpiarAutomata();

        Estado q0 = new Estado(
                "q0",
                false,
                false,
                250,
                300
        );

        Estado q1 = new Estado(
                "q1",
                false,
                false,
                550,
                300
        );

        agregarEstado(q0);
        agregarEstado(q1);

        establecerEstadoInicial(q0);
        agregarEstadoFinal(q1);

        agregarTransicion(
                new Transicion(
                        q0,
                        q1,
                        'a'
                )
        );

        agregarTransicion(
                new Transicion(
                        q1,
                        q1,
                        'b'
                )
        );
    }

    public Automata getAutomata() {
        return automata;
    }

    public Validador getValidador() {
        return validador;
    }
}