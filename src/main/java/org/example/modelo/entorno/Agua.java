package org.example.modelo.entorno;

import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.JuegoConfig;
import org.example.modelo.juego.Spriteeable;

public class Agua extends BloqueBase implements Spriteeable {
    private Vector posicion;
    private Rectangulo hitbox;

    public Agua(Vector posicion, int tileSize) {
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

    @Override public boolean bloqueaMovimiento() { return true; }   // Tanques no pasan
    @Override public boolean bloqueaProyectiles() { return false; } // Balas sí pasan
    @Override public boolean esDestruible() { return false; }

    @Override
    public ResultadoImpacto recibirImpacto(int dano) {
        return ResultadoImpacto.balaAtraviesa();
    }

    @Override
    public String spriteId() {
        return JuegoConfig.SPRITE_WATER;
    }

}
