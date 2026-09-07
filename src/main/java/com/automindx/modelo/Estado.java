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
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isInicial() {
        return inicial;
    }

    public void setInicial(boolean inicial) {
        this.inicial = inicial;
    }

    public boolean isEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(boolean estadoFinal) {
        this.estadoFinal = estadoFinal;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}