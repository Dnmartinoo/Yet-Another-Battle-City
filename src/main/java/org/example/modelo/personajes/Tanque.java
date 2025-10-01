package org.example.modelo.personajes;

import org.example.modelo.fisica.*;

public abstract class Tanque implements Cuerpo {
    protected final TipoPersonaje tipo;
    protected Vector posicion;
    protected Vector velocidad = Vector.CERO;
    protected double ancho = 20, alto = 20;
    protected int vidaActual;

    protected Tanque(TipoPersonaje tipo, Vector posicion){
        this.tipo = tipo;
        this.posicion = posicion;
        this.vidaActual = tipo.vidaBase();
    }

    @Override public Rectangulo hitbox() {
        return new Rectangulo(posicion.x(), posicion.y(), ancho, alto);
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

    @Override public boolean solido(){
        return true;
    }

    public void setVelocidad(Vector vector) {
        this.velocidad = vector;
    }

    public int vida(){
        return vidaActual;
    }

    public boolean estaVivo() {
        return vidaActual > 0;
    }

    public void recibirImpacto(int dano) {
        vidaActual = Math.max(0, vidaActual - dano);
    }

    public double velocidadTipo() {
        return tipo.obtenerVelocidad();
    }

    public double cadencia() {
        return tipo.obtenerCadencia();
    }

    public TipoPersonaje tipo() {
        return tipo;
    }
}
