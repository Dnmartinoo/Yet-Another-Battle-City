package org.example.modelo.powerup;

import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;
import org.example.modelo.fisica.Cuerpo;


public abstract class PowerUpBase implements PowerUp, Cuerpo {
    protected final Vector posicion;
    protected final double w = 20;
    protected final double h = 20;
    
    public PowerUpBase(Vector posicion) {

        this.posicion = posicion;
    }
    
    @Override
    public Rectangulo hitbox() {
        return new Rectangulo(posicion.x(), posicion.y(), w, h);
    }
    
    @Override
    public Vector posicion() {
        return posicion;
    }

    @Override
    public void setPosicion(Vector nuevaPosicion) {}

    @Override
    public boolean solido() {
        return false;
    }
}
