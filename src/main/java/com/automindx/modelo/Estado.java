package com.automindx.modelo;

public class Estado {

    private String nombre;
    private boolean inicial;
    private boolean estadoFinal;
    private int x;
    private int y;

    public Estado(String nombre, boolean inicial, boolean estadoFinal, int x, int y) {
        this.nombre = nombre;
        this.inicial = inicial;
        this.estadoFinal = estadoFinal;
        this.x = x;
        this.y = y;
    }
}