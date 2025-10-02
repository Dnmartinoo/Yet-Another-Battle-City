package org.example.modelo.disparo;

import org.example.modelo.fisica.Cuerpo;
import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.JuegoConfig;
import org.example.modelo.juego.Spriteeable;

public class Proyectil implements Cuerpo, Spriteeable {
    private Vector pos;
    private final Vector dirUnit; // dirección unitaria
    private final double speed;   // px/s
    private final int dano;
    private final double w = 6, h = 6;
    private final Equipo equipo;
    private boolean vivo = true;

    public Proyectil(Vector pos, Vector dirUnit, double speed, int dano, Equipo eq) {
        this.pos = pos;
        this.dirUnit = dirUnit.normalizado();
        this.speed = speed;
        this.dano = dano;
        this.equipo = eq;
    }

    // ---- Cuerpo ----
    @Override public Rectangulo hitbox() { return new Rectangulo(pos.x(), pos.y(), w, h); }
    @Override public Vector posicion() { return pos; }
    @Override public void setPosicion(Vector p) { this.pos = p; }
    @Override public Vector velocidad() { return dirUnit.por(speed); }
    @Override public boolean solido() { return false; }

    public Equipo equipo() { return equipo; }
    public int dano() { return dano; }
    public boolean vivo() { return vivo; }
    public void destruir() { vivo = false; }

    @Override
    public String spriteId() {
        return JuegoConfig.SPRITE_SHOT;
    }


}
