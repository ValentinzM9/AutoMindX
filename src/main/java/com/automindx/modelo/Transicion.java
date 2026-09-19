package com.automindx.modelo;

import java.util.Objects;

public class Transicion {

    private Estado origen;
    private Estado destino;
    private char simbolo;

    public Transicion(
            Estado origen,
            Estado destino,
            char simbolo) {

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

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Transicion)) {
            return false;
        }

        Transicion otra = (Transicion) obj;

        return simbolo == otra.simbolo
                && Objects.equals(
                        origen,
                        otra.origen
                )
                && Objects.equals(
                        destino,
                        otra.destino
                );
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                origen,
                destino,
                simbolo
        );
    }

    @Override
    public String toString() {

        String nombreOrigen =
                origen != null
                        ? origen.getNombre()
                        : "?";

        String nombreDestino =
                destino != null
                        ? destino.getNombre()
                        : "?";

        return nombreOrigen
                + " --"
                + simbolo
                + "--> "
                + nombreDestino;
    }
}