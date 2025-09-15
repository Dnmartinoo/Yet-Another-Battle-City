package org.example.modelo.entorno;

public class Agua implements Bloque{
    public boolean bloqueaMovimiento() {
        return true;
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
}
