package org.example.modelo.entorno;

import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.JuegoConfig;
import org.example.modelo.juego.Spriteeable;

public class Base extends BloqueBase implements Spriteeable {
    private Vector posicion;
    private Rectangulo hitbox;

    public Base(Vector posicion, int tileSize) {
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
        destruido = true;
        return ResultadoImpacto.balaSeDetiene();
    }

    @Override
    public boolean esBase() {
        return true;
    }

    @Override
    public String spriteId() {
        return JuegoConfig.SPRITE_BASE;
    }

}
