package org.example.modelo.entorno;

public class Bosque implements Bloque {


    public boolean bloqueaMovimiento() {
        return false;
    }

    public boolean bloqueaProyectiles() {
        return false;
    }

    public boolean esDestruible() {
        return false;
    }

    public boolean estaDestruido() {
        return false;
    }

    public boolean ocultaVisual() {
        return true;
    }
}
