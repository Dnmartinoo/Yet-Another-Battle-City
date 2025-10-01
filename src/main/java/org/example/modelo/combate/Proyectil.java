package org.example.modelo.combate;

import org.example.modelo.fisica.*;
import org.w3c.dom.css.Rect;

public class Proyectil implements Cuerpo {
    private Vector posicion;
    private Vector velocidad;
    private final double w = 6, h = 6;
    private final boolean deJugador;
    private final int dano;
    private boolean destruido = false;

    public Proyectil(Vector posicion, Vector velocidad,boolean deJugador, int dano) {
        this.posicion = posicion;
        this.velocidad = velocidad;
        this.deJugador = deJugador;
        this.dano = dano;
    }

    @Override public Rectangulo hitbox() {
        return new Rectangulo(posicion.x(), posicion.y(), w, h);
    }

    @Override public Vector posicion() {
        return posicion;
    }

    @Override public void setPosicion(Vector posicion) {
        this.posicion = posicion;
    }

    @Override public Vector velocidad() {
        return velocidad;
    }

    @Override public boolean solido() {
        return false;
    }

    public boolean esDeJugador() {
        return deJugador;
    }

    public int dano() {
        return dano;
    }

    public void destruir() {
        this.destruido = true;
    }

    public boolean destruido() {
        return destruido;
    }


}
