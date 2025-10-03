package org.example.modelo.disparo;

import org.example.modelo.fisica.Cuerpo;
import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.config.JuegoConfig;
import org.example.modelo.juego.Spriteeable;

public class Proyectil implements Cuerpo, Spriteeable {
    private Vector pos;
    private final Vector dirUnit;
    private final double speed;
    private final double w = 6, h = 6;
    private boolean vivo = true;

    private final int dano;
    private final Equipo equipo;
    private final boolean potenciada;

    public Proyectil(Vector pos, Vector dirUnit, double speed, int dano, Equipo eq, boolean potenciada) {
        this.pos = pos;
        this.dirUnit = dirUnit.normalizado();
        this.speed = speed;
        this.dano = dano;
        this.equipo = eq;
        this.potenciada = potenciada;
    }

    @Override public Rectangulo hitbox() { return new Rectangulo(pos.x(), pos.y(), w, h); }
    @Override public Vector posicion() { return pos; }
    @Override public void setPosicion(Vector p) { this.pos = p; }
    @Override public Vector velocidad() { return dirUnit.por(speed); }


    public void destruir() { this.vivo = false; }
    public boolean vivo() { return vivo; }


    public int dano() { return dano; }
    public Equipo equipo() { return equipo; }
    public boolean esPotenciada() { return potenciada; }

    @Override
    public String spriteId() {
        return JuegoConfig.SPRITE_SHOT;
    }
}
