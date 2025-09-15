package org.example.modelo.entorno;

public class Acero implements Bloque {
    public boolean bloqueaMovimiento() {
        return true;
    }

    public boolean bloqueaProyectiles() {
        return true;
    }

    public boolean esDestruible() {
        return false;
    }

    public boolean estaDestruido() {
        return false;
    }
}
