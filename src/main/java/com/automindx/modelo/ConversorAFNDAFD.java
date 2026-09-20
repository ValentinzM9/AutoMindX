package com.automindx.modelo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConversorAFNDAFD {

    private final AutomataNoDeterminista afnd;

    public ConversorAFNDAFD(
            AutomataNoDeterminista afnd) {

        this.afnd = afnd;
    }

    public ResultadoConversion convertir() {

        Automata afd = new Automata();

        List<PasoConversion> pasos =
                new ArrayList<>();

        Map<Set<Estado>, Estado> correspondencia =
                new LinkedHashMap<>();

        Deque<Set<Estado>> pendientes =
                new ArrayDeque<>();

        Estado inicialAFND =
                afnd.getEstadoInicial();

        if (inicialAFND == null) {

            return new ResultadoConversion(
                    afd,
                    pasos,
                    correspondencia
            );
        }

        Set<Estado> conjuntoInicial =
                new LinkedHashSet<>();

        conjuntoInicial.add(inicialAFND);

        conjuntoInicial =
                conjuntoInmutable(
                        afnd.obtenerClausuraEpsilon(
                                conjuntoInicial
                        )
                );

        Estado estadoInicialAFD =
                crearEstadoAFD(
                        conjuntoInicial,
                        0
                );

        afd.agregarEstado(
                estadoInicialAFD
        );

        afd.establecerEstadoInicial(
                estadoInicialAFD
        );

        if (afnd.contieneEstadoFinal(
                conjuntoInicial)) {

            afd.agregarEstadoFinal(
                    estadoInicialAFD
            );
        }

        correspondencia.put(
                conjuntoInicial,
                estadoInicialAFD
        );

        pendientes.add(
                conjuntoInicial
        );

        int contadorEstados = 1;

        List<Character> alfabeto =
                new ArrayList<>(
                        afnd.getAlfabeto()
                );

        alfabeto.sort(
                Comparator.naturalOrder()
        );

        while (!pendientes.isEmpty()) {

            Set<Estado> conjuntoActual =
                    pendientes.remove();

            Estado estadoActualAFD =
                    correspondencia.get(
                            conjuntoActual
                    );

            for (char simbolo : alfabeto) {

                Set<Estado> destinos =
                        obtenerDestinosConjunto(
                                conjuntoActual,
                                simbolo
                        );

                Set<Estado> nuevoConjunto =
                        conjuntoInmutable(
                                afnd.obtenerClausuraEpsilon(
                                        destinos
                                )
                        );

                Estado estadoDestinoAFD =
                        correspondencia.get(
                                nuevoConjunto
                        );

                if (estadoDestinoAFD == null) {

                    estadoDestinoAFD =
                            crearEstadoAFD(
                                    nuevoConjunto,
                                    contadorEstados
                            );

                    contadorEstados++;

                    afd.agregarEstado(
                            estadoDestinoAFD
                    );

                    if (afnd.contieneEstadoFinal(
                            nuevoConjunto)) {

                        afd.agregarEstadoFinal(
                                estadoDestinoAFD
                        );
                    }

                    correspondencia.put(
                            nuevoConjunto,
                            estadoDestinoAFD
                    );

                    pendientes.add(
                            nuevoConjunto
                    );
                }

                Transicion transicion =
                        new Transicion(
                                estadoActualAFD,
                                estadoDestinoAFD,
                                simbolo
                        );

                afd.agregarTransicion(
                        transicion
                );

                pasos.add(
                        new PasoConversion(
                                conjuntoActual,
                                simbolo,
                                nuevoConjunto,
                                estadoActualAFD.getNombre(),
                                estadoDestinoAFD.getNombre()
                        )
                );
            }
        }

        return new ResultadoConversion(
                afd,
                pasos,
                correspondencia
        );
    }

    private Set<Estado> obtenerDestinosConjunto(
            Set<Estado> conjunto,
            char simbolo) {

        Set<Estado> destinos =
                new LinkedHashSet<>();

        for (Estado estado : conjunto) {

            destinos.addAll(
                    afnd.obtenerDestinos(
                            estado,
                            simbolo
                    )
            );
        }

        return destinos;
    }

    private Estado crearEstadoAFD(
            Set<Estado> conjunto,
            int numero) {

        String nombre = "S" + numero;

        int x = 180 + (numero % 4) * 180;
        int y = 180 + (numero / 4) * 160;

        return new Estado(
                nombre,
                false,
                false,
                x,
                y
        );
    }

    private Set<Estado> conjuntoInmutable(
            Set<Estado> conjunto) {

        return Collections.unmodifiableSet(
                new LinkedHashSet<>(
                        conjunto
                )
        );
    }

    public static class PasoConversion {

        private final Set<Estado> conjuntoOrigen;
        private final char simbolo;
        private final Set<Estado> conjuntoDestino;
        private final String estadoOrigenAFD;
        private final String estadoDestinoAFD;

        public PasoConversion(
                Set<Estado> conjuntoOrigen,
                char simbolo,
                Set<Estado> conjuntoDestino,
                String estadoOrigenAFD,
                String estadoDestinoAFD) {

            this.conjuntoOrigen =
                    new LinkedHashSet<>(
                            conjuntoOrigen
                    );

            this.simbolo = simbolo;

            this.conjuntoDestino =
                    new LinkedHashSet<>(
                            conjuntoDestino
                    );

            this.estadoOrigenAFD =
                    estadoOrigenAFD;

            this.estadoDestinoAFD =
                    estadoDestinoAFD;
        }

        public Set<Estado> getConjuntoOrigen() {
            return Collections.unmodifiableSet(
                    conjuntoOrigen
            );
        }

        public char getSimbolo() {
            return simbolo;
        }

        public Set<Estado> getConjuntoDestino() {
            return Collections.unmodifiableSet(
                    conjuntoDestino
            );
        }

        public String getEstadoOrigenAFD() {
            return estadoOrigenAFD;
        }

        public String getEstadoDestinoAFD() {
            return estadoDestinoAFD;
        }
    }

    public static class ResultadoConversion {

        private final Automata afd;
        private final List<PasoConversion> pasos;
        private final Map<Set<Estado>, Estado> correspondencia;

        public ResultadoConversion(
                Automata afd,
                List<PasoConversion> pasos,
                Map<Set<Estado>, Estado> correspondencia) {

            this.afd = afd;

            this.pasos =
                    new ArrayList<>(
                            pasos
                    );

            this.correspondencia =
                    new LinkedHashMap<>(
                            correspondencia
                    );
        }

        public Automata getAfd() {
            return afd;
        }

        public List<PasoConversion> getPasos() {
            return Collections.unmodifiableList(
                    pasos
            );
        }

        public Map<Set<Estado>, Estado> getCorrespondencia() {
            return Collections.unmodifiableMap(
                    correspondencia
            );
        }
    }
}
