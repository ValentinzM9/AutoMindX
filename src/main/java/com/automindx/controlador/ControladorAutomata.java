package com.automindx.controlador;

import com.automindx.modelo.Automata;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;
import com.automindx.modelo.Validador;

public class ControladorAutomata {
    
    private Automata automata;
    private Validador validador;

    public ControladorAutomata() {
        this.automata = new Automata();
        this.validador = new Validador(automata);
    }

    public void agregarEstado(Estado estado) {
        automata.agregarEstado(estado);
    }

    public void agregarSimbolo(char simbolo) {
        automata.agregarSimbolo(simbolo);
    }

    public void agregarTransicion(Transicion transicion) {
        automata.agregarTransicion(transicion);
    }

    public void establecerEstadoInicial(Estado estado) {
        automata.establecerEstadoInicial(estado);
    }

    public void agregarEstadoFinal(Estado estado) {
        automata.agregarEstadoFinal(estado);
    }

    public boolean validarCadena(String cadena) {
        return validador.validar(cadena);
    }

    public Automata getAutomata() {
        return automata;
    }

    public Validador getValidador() {
        return validador;
    }
}
