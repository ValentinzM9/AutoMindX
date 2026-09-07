package com.automindx.modelo;

public class Transicion {

    private Estado origen;
    private Estado destino;
    private char simbolo;

    public Transicion(Estado origen, Estado destino, char simbolo) {
        this.origen = origen;
        this.destino = destino;
        this.simbolo = simbolo;
    }
}
