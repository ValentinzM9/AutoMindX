package com.automindx.modelo;

import java.util.ArrayList;
import java.util.List;

public class Validador {
  private Automata automata;
    private List<Estado> recorrido;
    private List<Transicion> transicionesRecorridas;

    public Validador(Automata automata) {
        this.automata = automata;
        this.recorrido = new ArrayList<>();
        this.transicionesRecorridas = new ArrayList<>();
    }

    public boolean validar(String cadena) {

        recorrido.clear();
        transicionesRecorridas.clear();

        Estado estadoActual = automata.getEstadoInicial();

        if (estadoActual == null) {
            return false;
        }

        // Guardamos el estado inicial
        recorrido.add(estadoActual);

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

            // Guardamos la transición utilizada
            transicionesRecorridas.add(transicionEncontrada);

            estadoActual = transicionEncontrada.getDestino();

            // Guardamos el nuevo estado recorrido
            recorrido.add(estadoActual);
        }

        return automata.getEstadosFinales().contains(estadoActual);
    }

    public List<Estado> getRecorrido() {
        return recorrido;
    }

    public List<Transicion> getTransicionesRecorridas() {
        return transicionesRecorridas;
    }
}
