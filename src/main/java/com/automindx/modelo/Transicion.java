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

    public Estado getOrigen() {
        return origen;
    }

    public void setOrigen(Estado origen) {
        this.origen = origen;
    }

    public Estado getDestino() {
        return destino;
    }

    public void setDestino(Estado destino) {
        this.destino = destino;
    }

    public char getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(char simbolo) {
        this.simbolo = simbolo;
    }
}
