package com.automindx.modelo;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Estado {

    private static final AtomicLong CONTADOR_ID =
            new AtomicLong(0);

    private final long id;

    private String nombre;
    private boolean inicial;
    private boolean estadoFinal;

    private int x;
    private int y;

    public Estado(
            String nombre,
            boolean inicial,
            boolean estadoFinal,
            int x,
            int y) {

        this.id = CONTADOR_ID.getAndIncrement();

        this.nombre = nombre;
        this.inicial = inicial;
        this.estadoFinal = estadoFinal;
        this.x = x;
        this.y = y;
    }

    public long getId() {
        return id;
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

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Estado)) {
            return false;
        }

        Estado otro = (Estado) obj;

        return id == otro.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return nombre;
    }
}