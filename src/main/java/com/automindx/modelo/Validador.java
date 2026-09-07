package com.automindx.modelo;

public class Validador {
    private Automata automata;

    public Validador(Automata automata) {
        this.automata = automata;
    }

    public boolean validar(String cadena) {

        Estado estadoActual = automata.getEstadoInicial();

        if (estadoActual == null) {
            return false;
        }

        for (char simbolo : cadena.toCharArray()) {

            Transicion transicionEncontrada = null;

            for (Transicion transicion : automata.getTransiciones()) {

                if (transicion.getOrigen() == estadoActual
                        && transicion.getSimbolo() == simbolo) {

                    transicionEncontrada = transicion;
                    break;
                }
            }

            if (transicionEncontrada == null) {
                return false;
            }

            estadoActual = transicionEncontrada.getDestino();
        }

        return automata.getEstadosFinales().contains(estadoActual);
    }
}
