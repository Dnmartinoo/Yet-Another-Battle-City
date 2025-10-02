package org.example.modelo.entorno;

import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;

public class Ladrillo extends BloqueBase {
    private Vector posicion;
    private Rectangulo hitbox;
    private int vida = 3;

    public Ladrillo(Vector posicion, int tileSize) {
        this.posicion = posicion;
        this.hitbox = new Rectangulo(posicion.x(), posicion.y(), tileSize, tileSize);
    }

    @Override
    public Vector posicion() { return posicion; }

    @Override
    public void setPosicion(Vector nuevaPosicion) {
        this.posicion = nuevaPosicion;
        this.hitbox = new Rectangulo(nuevaPosicion.x(), nuevaPosicion.y(), hitbox.w(), hitbox.h());
    }

    @Override
    public Rectangulo hitbox() { return hitbox; }

    @Override public boolean bloqueaMovimiento() { return !destruido; }
    @Override public boolean bloqueaProyectiles() { return !destruido; }
    @Override public boolean esDestruible() { return true; }

    @Override
    public ResultadoImpacto recibirImpacto(int dano) {
        vida -= dano;
        if (vida <= 0) destruido = true;
        return ResultadoImpacto.balaSeDetiene();
    }
}
