package com.automindx.modelo;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Automata {
     private List<Estado> estados;
    private Set<Character> alfabeto;
    private List<Transicion> transiciones;
    private Estado estadoInicial;
    private List<Estado> estadosFinales;

    public Automata() {
        this.estados = new ArrayList<>();
        this.alfabeto = new HashSet<>();
        this.transiciones = new ArrayList<>();
        this.estadosFinales = new ArrayList<>();
    }

}
