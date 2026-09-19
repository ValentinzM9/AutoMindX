package com.automindx.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Validador {

    public enum Resultado {
        ACEPTADA,
        RECHAZADA_SIN_ESTADO_INICIAL,
        RECHAZADA_TRANSICION_INEXISTENTE,
        RECHAZADA_ESTADO_NO_FINAL
    }

    private final Automata automata;

    private List<Estado> recorrido;
    private List<Transicion> transicionesRecorridas;

    private Resultado ultimoResultado;

    private Character simboloError;
    private Estado estadoError;
    private int posicionError;

    public Validador(Automata automata) {

        this.automata = automata;

        recorrido = new ArrayList<>();
        transicionesRecorridas = new ArrayList<>();

        ultimoResultado = null;

        simboloError = null;
        estadoError = null;
        posicionError = -1;
    }

    public boolean validar(String cadena) {

        return validarDetallado(cadena)
                == Resultado.ACEPTADA;
    }

    public Resultado validarDetallado(
            String cadena) {

        recorrido = new ArrayList<>();
        transicionesRecorridas =
                new ArrayList<>();

        simboloError = null;
        estadoError = null;
        posicionError = -1;

        Estado estadoActual =
                automata.getEstadoInicial();

        if (estadoActual == null) {

            ultimoResultado =
                    Resultado.RECHAZADA_SIN_ESTADO_INICIAL;

            return ultimoResultado;
        }

        if (cadena == null) {
            cadena = "";
        }

        recorrido.add(estadoActual);

        char[] caracteres =
                cadena.toCharArray();

        for (int i = 0;
             i < caracteres.length;
             i++) {

            char simbolo = caracteres[i];

            Transicion transicion =
                    automata.buscarTransicion(
                            estadoActual,
                            simbolo
                    );

            if (transicion == null) {

                simboloError = simbolo;
                estadoError = estadoActual;
                posicionError = i;

                ultimoResultado =
                        Resultado.RECHAZADA_TRANSICION_INEXISTENTE;

                return ultimoResultado;
            }

            transicionesRecorridas.add(
                    transicion
            );

            estadoActual =
                    transicion.getDestino();

            recorrido.add(estadoActual);
        }

        if (automata.esEstadoFinal(
                estadoActual)) {

            ultimoResultado =
                    Resultado.ACEPTADA;

        } else {

            estadoError = estadoActual;

            ultimoResultado =
                    Resultado.RECHAZADA_ESTADO_NO_FINAL;
        }

        return ultimoResultado;
    }

    public List<Estado> getRecorrido() {

        return Collections.unmodifiableList(
                recorrido
        );
    }

    public List<Transicion> getTransicionesRecorridas() {

        return Collections.unmodifiableList(
                transicionesRecorridas
        );
    }

    public Resultado getUltimoResultado() {
        return ultimoResultado;
    }

    public Character getSimboloError() {
        return simboloError;
    }

    public Estado getEstadoError() {
        return estadoError;
    }

    public int getPosicionError() {
        return posicionError;
    }
}