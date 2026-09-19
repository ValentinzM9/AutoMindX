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
        if (estado != null && !estados.contains(estado)) {
            estados.add(estado);
        }
    }

    public void agregarSimbolo(char simbolo) {
        alfabeto.add(simbolo);
    }

    public void agregarTransicion(Transicion transicion) {
        if (transicion != null && !transiciones.contains(transicion)) {
            transiciones.add(transicion);
            agregarSimbolo(transicion.getSimbolo());
        }
    }

    public void establecerEstadoInicial(Estado estado) {

        if (estado == null) {
            estadoInicial = null;
            return;
        }

        // Primero, todos los estados dejan de ser iniciales.
        for (Estado estadoActual : estados) {
            estadoActual.setInicial(false);
        }

        estadoInicial = estado;
        estado.setInicial(true);
    }

    public void agregarEstadoFinal(Estado estado) {

        if (estado != null && !estadosFinales.contains(estado)) {
            estadosFinales.add(estado);
            estado.setEstadoFinal(true);
        }
    }

    public void quitarEstadoFinal(Estado estado) {

        if (estado != null) {
            estadosFinales.remove(estado);
            estado.setEstadoFinal(false);
        }
    }

    public boolean esEstadoFinal(Estado estado) {
        return estadosFinales.contains(estado);
    }

    public Transicion buscarTransicion(
            Estado origen,
            char simbolo) {

        for (Transicion transicion : transiciones) {

            if (transicion.getOrigen() == origen
                    && transicion.getSimbolo() == simbolo) {

                return transicion;
            }
        }

        return null;
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

    public void setEstados(List<Estado> estados) {
        this.estados = estados;
    }

    public Set<Character> getAlfabeto() {
        return alfabeto;
    }

    public void setAlfabeto(Set<Character> alfabeto) {
        this.alfabeto = alfabeto;
    }

    public List<Transicion> getTransiciones() {
        return transiciones;
    }

    public void setTransiciones(List<Transicion> transiciones) {
        this.transiciones = transiciones;
    }

    public Estado getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(Estado estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public List<Estado> getEstadosFinales() {
        return estadosFinales;
    }

    public void setEstadosFinales(List<Estado> estadosFinales) {
        this.estadosFinales = estadosFinales;
    }
}