package com.automindx.modelo;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Automata {
     private List<Estado> estados;
    private Set<Character> alfabeto;
    private List<Transicion> transiciones;
    private Estado estadoInicial;
    private List<Estado> estadosFinales;

    public Automata() {
        this.estados = new ArrayList<>();
        this.alfabeto = new HashSet<>();
        this.transiciones = new ArrayList<>();
        this.estadosFinales = new ArrayList<>();
    }

    public void agregarEstado(Estado estado) {
        this.estados.add(estado);
    }

    public void agregarSimbolo(char simbolo) {
        this.alfabeto.add(simbolo);
    }

    public void agregarTransicion(Transicion transicion) {
        this.transiciones.add(transicion);
    }

    public void establecerEstadoInicial(Estado estado) {
        this.estadoInicial = estado;
    }

    public void agregarEstadoFinal(Estado estado) {
        this.estadosFinales.add(estado);
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
