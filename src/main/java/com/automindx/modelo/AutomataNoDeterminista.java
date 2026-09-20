package com.automindx.modelo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AutomataNoDeterminista {

    public static final char EPSILON = 'ε';

    private final List<Estado> estados;
    private final Set<Character> alfabeto;
    private final List<Transicion> transiciones;
    private final List<Estado> estadosFinales;

    private Estado estadoInicial;

    public AutomataNoDeterminista() {
        estados = new ArrayList<>();
        alfabeto = new LinkedHashSet<>();
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

        if (simbolo != EPSILON) {
            alfabeto.add(simbolo);
        }
    }

    public void agregarTransicion(
            Transicion transicion) {

        if (transicion == null
                || transicion.getOrigen() == null
                || transicion.getDestino() == null) {
            return;
        }

        if (!estados.contains(transicion.getOrigen())
                || !estados.contains(transicion.getDestino())) {
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

    public List<Transicion> obtenerTransiciones(
            Estado origen,
            char simbolo) {

        List<Transicion> resultado =
                new ArrayList<>();

        if (origen == null) {
            return resultado;
        }

        for (Transicion transicion : transiciones) {

            if (transicion.getOrigen().equals(origen)
                    && transicion.getSimbolo() == simbolo) {

                resultado.add(transicion);
            }
        }

        return resultado;
    }

    public Set<Estado> obtenerDestinos(
            Estado origen,
            char simbolo) {

        Set<Estado> destinos =
                new LinkedHashSet<>();

        if (origen == null) {
            return destinos;
        }

        for (Transicion transicion :
                obtenerTransiciones(origen, simbolo)) {

            destinos.add(
                    transicion.getDestino()
            );
        }

        return destinos;
    }

    public Set<Estado> obtenerClausuraEpsilon(
            Estado estado) {

        Set<Estado> clausura =
                new LinkedHashSet<>();

        if (estado == null) {
            return clausura;
        }

        clausura.add(estado);

        boolean huboCambios = true;

        while (huboCambios) {

            huboCambios = false;

            Set<Estado> nuevosEstados =
                    new LinkedHashSet<>();

            for (Estado actual : clausura) {

                nuevosEstados.addAll(
                        obtenerDestinos(
                                actual,
                                EPSILON
                        )
                );
            }

            if (clausura.addAll(nuevosEstados)) {
                huboCambios = true;
            }
        }

        return clausura;
    }

    public Set<Estado> obtenerClausuraEpsilon(
            Set<Estado> conjunto) {

        Set<Estado> clausura =
                new LinkedHashSet<>();

        if (conjunto == null) {
            return clausura;
        }

        for (Estado estado : conjunto) {

            clausura.addAll(
                    obtenerClausuraEpsilon(estado)
            );
        }

        return clausura;
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

        if (estado == null
                || !estados.contains(estado)) {
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

        return estado != null
                && estadosFinales.contains(estado);
    }

    public boolean contieneEstadoFinal(
            Set<Estado> conjunto) {

        if (conjunto == null) {
            return false;
        }

        for (Estado estado : conjunto) {

            if (esEstadoFinal(estado)) {
                return true;
            }
        }

        return false;
    }

    private void reconstruirAlfabeto() {

        alfabeto.clear();

        for (Transicion transicion : transiciones) {

            agregarSimbolo(
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