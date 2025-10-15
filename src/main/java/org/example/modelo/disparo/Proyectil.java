package org.example.modelo.disparo;

import org.example.modelo.fisica.Cuerpo;
import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;

public class Proyectil implements Cuerpo {
    private Vector posicion;
    private final Vector direccionUnitaria;
    private final double velocidad;
    private final double w = 6, h = 6;
    private boolean vivo = true;
    private final int dano;
    private final Equipo equipo;
    private final boolean potenciada;

    public Proyectil(Vector posicion, Vector direccionUnitaria, double velocidad, int dano, Equipo eq, boolean potenciada) {
        this.posicion = posicion;
        this.direccionUnitaria = direccionUnitaria.normalizado();
        this.velocidad = velocidad;
        this.dano = dano;
        this.equipo = eq;
        this.potenciada = potenciada;
    }

    @Override public Rectangulo hitbox() { return new Rectangulo(posicion.x(), posicion.y(), w, h); }
    @Override public Vector posicion() { return posicion; }
    @Override public void setPosicion(Vector p) { this.posicion = p; }
    @Override public Vector velocidad() { return direccionUnitaria.por(velocidad); }


    public void destruir() { this.vivo = false; }
    public boolean vivo() { return vivo; }


    public int dano() { return dano; }
    public Equipo equipo() { return equipo; }
    public boolean esPotenciada() { return potenciada; }
}
