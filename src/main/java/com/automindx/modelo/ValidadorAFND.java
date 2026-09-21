package com.automindx.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidadorAFND {

    public enum Resultado {
        ACEPTADA,
        RECHAZADA_SIN_ESTADO_INICIAL,
        RECHAZADA_SIN_CAMINO,
        RECHAZADA_ESTADO_NO_FINAL
    }

    private final AutomataNoDeterminista automata;

    private List<Estado> recorrido;
    private List<Transicion> transicionesRecorridas;
    private Resultado ultimoResultado;

    private String cadenaActual;
    private Set<Configuracion> configuracionesVisitadas;

    public ValidadorAFND(AutomataNoDeterminista automata) {
        this.automata = automata;
        recorrido = new ArrayList<>();
        transicionesRecorridas = new ArrayList<>();
        ultimoResultado = null;
        configuracionesVisitadas = new HashSet<>();
    }

    public boolean validar(String cadena) {
        return validarDetallado(cadena) == Resultado.ACEPTADA;
    }

    public Resultado validarDetallado(String cadena) {
        recorrido.clear();
        transicionesRecorridas.clear();
        configuracionesVisitadas.clear();

        if (automata.getEstadoInicial() == null) {
            ultimoResultado =
                    Resultado.RECHAZADA_SIN_ESTADO_INICIAL;
            return ultimoResultado;
        }

        cadenaActual = cadena == null ? "" : cadena;

        List<Estado> caminoEstados = new ArrayList<>();
        List<Transicion> caminoTransiciones = new ArrayList<>();

        Estado inicial = automata.getEstadoInicial();

        boolean aceptada = buscarCamino(
                inicial,
                0,
                caminoEstados,
                caminoTransiciones
        );

        if (aceptada) {
            recorrido = new ArrayList<>(caminoEstados);
            transicionesRecorridas =
                    new ArrayList<>(caminoTransiciones);

            ultimoResultado = Resultado.ACEPTADA;
        } else {
            ultimoResultado = determinarRechazo();
        }

        return ultimoResultado;
    }

    private boolean buscarCamino(
            Estado estado,
            int posicion,
            List<Estado> caminoEstados,
            List<Transicion> caminoTransiciones) {

        if (estado == null) {
            return false;
        }

        Configuracion configuracion =
                new Configuracion(estado, posicion);

        if (!configuracionesVisitadas.add(configuracion)) {
            return false;
        }

        caminoEstados.add(estado);

        if (posicion == cadenaActual.length()
                && automata.esEstadoFinal(estado)) {
            return true;
        }

        List<Transicion> transiciones =
                automata.obtenerTransiciones(
                        estado,
                        AutomataNoDeterminista.EPSILON
                );

        for (Transicion transicion : transiciones) {
            caminoTransiciones.add(transicion);

            if (buscarCamino(
                    transicion.getDestino(),
                    posicion,
                    caminoEstados,
                    caminoTransiciones)) {
                return true;
            }

            caminoTransiciones.remove(
                    caminoTransiciones.size() - 1
            );
        }

        if (posicion < cadenaActual.length()) {
            char simbolo = cadenaActual.charAt(posicion);

            transiciones =
                    automata.obtenerTransiciones(
                            estado,
                            simbolo
                    );

            for (Transicion transicion : transiciones) {
                caminoTransiciones.add(transicion);

                if (buscarCamino(
                        transicion.getDestino(),
                        posicion + 1,
                        caminoEstados,
                        caminoTransiciones)) {
                    return true;
                }

                caminoTransiciones.remove(
                        caminoTransiciones.size() - 1
                );
            }
        }

        caminoEstados.remove(caminoEstados.size() - 1);

        return false;
    }

    private Resultado determinarRechazo() {
        if (cadenaActual.isEmpty()) {
            return Resultado.RECHAZADA_ESTADO_NO_FINAL;
        }

        return Resultado.RECHAZADA_SIN_CAMINO;
    }

    public List<Estado> getRecorrido() {
        return Collections.unmodifiableList(recorrido);
    }

    public List<Transicion> getTransicionesRecorridas() {
        return Collections.unmodifiableList(
                transicionesRecorridas
        );
    }

    public Resultado getUltimoResultado() {
        return ultimoResultado;
    }

    private static class Configuracion {

        private final Estado estado;
        private final int posicion;

        public Configuracion(Estado estado, int posicion) {
            this.estado = estado;
            this.posicion = posicion;
        }

        @Override
        public boolean equals(Object objeto) {
            if (this == objeto) {
                return true;
            }

            if (!(objeto instanceof Configuracion)) {
                return false;
            }

            Configuracion otra = (Configuracion) objeto;

            return posicion == otra.posicion
                    && estado.equals(otra.estado);
        }

        @Override
        public int hashCode() {
            return 31 * estado.hashCode() + posicion;
        }
    }
}
