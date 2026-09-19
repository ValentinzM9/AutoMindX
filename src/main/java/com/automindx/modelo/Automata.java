package com.automindx.modelo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Automata {

    private List<Estado> estados;
    private Set<Character> alfabeto;
    private List<Transicion> transiciones;
    private Estado estadoInicial;
    private List<Estado> estadosFinales;

    public Automata() {
        estados = new ArrayList<>();
        alfabeto = new HashSet<>();
        transiciones = new ArrayList<>();
        estadosFinales = new ArrayList<>();
    }

    public void agregarEstado(Estado estado) {

        if (estado != null
                && !estados.contains(estado)) {

            estados.add(estado);
        }
    }

    public void eliminarEstado(Estado estado) {

        if (estado == null) {
            return;
        }

        estados.remove(estado);
        estadosFinales.remove(estado);

        transiciones.removeIf(
                transicion ->
                        transicion.getOrigen().equals(estado)
                                || transicion.getDestino().equals(estado)
        );

        if (estado.equals(estadoInicial)) {
            estadoInicial = null;
        }

        reconstruirAlfabeto();
    }

    public void agregarSimbolo(char simbolo) {
        alfabeto.add(simbolo);
    }

    public void agregarTransicion(Transicion transicion) {

        if (transicion == null) {
            return;
        }

        if (transicion.getOrigen() == null
                || transicion.getDestino() == null) {
            return;
        }

        if (!transiciones.contains(transicion)) {

            transiciones.add(transicion);

            agregarSimbolo(
                    transicion.getSimbolo()
            );
        }
    }

    public void eliminarTransicion(
            Transicion transicion) {

        if (transicion == null) {
            return;
        }

        transiciones.remove(transicion);
        reconstruirAlfabeto();
    }

    public void establecerEstadoInicial(
            Estado estado) {

        if (estado == null) {
            estadoInicial = null;
            return;
        }

        if (!estados.contains(estado)) {
            return;
        }

        for (Estado estadoActual : estados) {
            estadoActual.setInicial(false);
        }

        estadoInicial = estado;
        estado.setInicial(true);
    }

    public void agregarEstadoFinal(
            Estado estado) {

        if (estado == null) {
            return;
        }

        if (!estados.contains(estado)) {
            return;
        }

        if (!estadosFinales.contains(estado)) {

            estadosFinales.add(estado);
            estado.setEstadoFinal(true);
        }
    }

    public void quitarEstadoFinal(
            Estado estado) {

        if (estado == null) {
            return;
        }

        estadosFinales.remove(estado);
        estado.setEstadoFinal(false);
    }

    public boolean esEstadoFinal(
            Estado estado) {

        return estadosFinales.contains(estado);
    }

    public Transicion buscarTransicion(
            Estado origen,
            char simbolo) {

        if (origen == null) {
            return null;
        }

        for (Transicion transicion :
                transiciones) {

            if (transicion.getOrigen().equals(origen)
                    && transicion.getSimbolo() == simbolo) {

                return transicion;
            }
        }

        return null;
    }

    private void reconstruirAlfabeto() {

        alfabeto.clear();

        for (Transicion transicion :
                transiciones) {

            alfabeto.add(
                    transicion.getSimbolo()
            );
        }
    }

    public void limpiar() {

        estados.clear();
        alfabeto.clear();
        transiciones.clear();
        estadosFinales.clear();
        estadoInicial = null;
    }

    public List<Estado> getEstados() {
        return estados;
    }

    public Set<Character> getAlfabeto() {
        return alfabeto;
    }

    public List<Transicion> getTransiciones() {
        return transiciones;
    }

    public Estado getEstadoInicial() {
        return estadoInicial;
    }

    public List<Estado> getEstadosFinales() {
        return estadosFinales;
    }
}