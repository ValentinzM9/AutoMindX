package com.automindx.controlador;

import com.automindx.modelo.Automata;
import com.automindx.modelo.AutomataNoDeterminista;
import com.automindx.modelo.ConversorAFNDAFD;
import com.automindx.modelo.Estado;
import com.automindx.modelo.Transicion;
import com.automindx.modelo.ValidadorAFND;

public class ControladorNoDeterminista {

    private final AutomataNoDeterminista afnd;
    private final ValidadorAFND validador;

    private ConversorAFNDAFD.ResultadoConversion resultadoConversion;

    public ControladorNoDeterminista() {
        afnd = new AutomataNoDeterminista();
        validador = new ValidadorAFND(afnd);
    }

    public Estado crearEstado(int x, int y) {
        String nombre = generarNombreEstado();
        Estado estado = new Estado(nombre, false, false, x, y);
        afnd.agregarEstado(estado);
        return estado;
    }

    public void agregarEstado(Estado estado) {
        afnd.agregarEstado(estado);
    }

    public void eliminarEstado(Estado estado) {
        afnd.eliminarEstado(estado);
    }

    public boolean agregarTransicion(
            Estado origen,
            Estado destino,
            char simbolo) {

        if (origen == null || destino == null) {
            return false;
        }

        Transicion transicion =
                new Transicion(origen, destino, simbolo);

        int cantidadAnterior =
                afnd.getTransiciones().size();

        afnd.agregarTransicion(transicion);

        return afnd.getTransiciones().size()
                > cantidadAnterior;
    }

    public void eliminarTransicion(Transicion transicion) {
        afnd.eliminarTransicion(transicion);
    }

    public void establecerEstadoInicial(Estado estado) {
        afnd.establecerEstadoInicial(estado);
    }

    public void quitarEstadoInicial() {
        afnd.establecerEstadoInicial(null);
    }

    public void alternarEstadoFinal(Estado estado) {
        if (afnd.esEstadoFinal(estado)) {
            afnd.quitarEstadoFinal(estado);
        } else {
            afnd.agregarEstadoFinal(estado);
        }
    }

    public void agregarEstadoFinal(Estado estado) {
        afnd.agregarEstadoFinal(estado);
    }

    public void quitarEstadoFinal(Estado estado) {
        afnd.quitarEstadoFinal(estado);
    }

    public ValidadorAFND.Resultado validarCadena(String cadena) {
        return validador.validarDetallado(cadena);
    }

    public boolean aceptarCadena(String cadena) {
        return validador.validar(cadena);
    }

    public ValidadorAFND getValidador() {
        return validador;
    }

    public void convertirAFDaFD() {
        ConversorAFNDAFD conversor =
                new ConversorAFNDAFD(afnd);

        resultadoConversion = conversor.convertir();
    }

    public Automata getAFDConvertido() {
        if (resultadoConversion == null) {
            return null;
        }

        return resultadoConversion.getAfd();
    }

    public ConversorAFNDAFD.ResultadoConversion
            getResultadoConversion() {

        return resultadoConversion;
    }

    public void limpiar() {
        afnd.limpiar();
        resultadoConversion = null;
    }

    private String generarNombreEstado() {
        int numero = 0;

        while (true) {
            String nombre = "q" + numero;
            boolean existe = false;

            for (Estado estado : afnd.getEstados()) {
                if (nombre.equals(estado.getNombre())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                return nombre;
            }

            numero++;
        }
    }

    public AutomataNoDeterminista getAFND() {
        return afnd;
    }
}