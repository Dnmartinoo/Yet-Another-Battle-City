package org.example.modelo.juego;

public class InputEstado {

    public final boolean arriba, abajo, izquierda, derecha, disparar;
    public InputEstado(boolean arriba, boolean abajo, boolean izquierda, boolean derecha, boolean disparar) {
        this.arriba = arriba;
        this.abajo = abajo;
        this.izquierda = izquierda;
        this.derecha = derecha;
        this.disparar = disparar;
    }

    public static InputEstado neutro() { return new InputEstado(false,false,false,false,false); }
}
