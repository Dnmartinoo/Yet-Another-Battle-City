package org.example.modelo.entorno;

public class Ladrillo implements Bloque{
    private int hp = 3;
    private boolean destruido = false;

    public boolean bloqueaMovimiento() {
        return !destruido;
    }

    public boolean bloqueaProyectiles() {
        return !destruido;
    }

    public boolean esDestruible() {
        return true;
    }

    public boolean estaDestruido() {
        return destruido = hp == 0;
    }

    public int recibirImpacto(){
        hp --;
        return hp;
    }
}
