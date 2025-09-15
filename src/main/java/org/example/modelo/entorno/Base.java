package org.example.modelo.entorno;

public class Base implements Bloque{
    private boolean destruida = false;

    public boolean bloqueaMovimiento() {
        return destruida;
    }

    public boolean bloqueaProyectiles() {
        return !destruida;
    }

    public boolean esDestruible() {
        return true;
    }

    public boolean estaDestruido() {
        return destruida;
    }

    /* if bala pega en base se rompe---> destruida = true y devolver base destruida ---> gameover¿?*/
}
