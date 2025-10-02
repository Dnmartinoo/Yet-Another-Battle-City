package org.example.modelo.powerup;

import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;

/**
 * Clase base abstracta para todos los power-ups del juego.
 * Proporciona funcionalidad común como posición y hitbox.
 */
public abstract class PowerUpBase implements PowerUp {
    protected final Vector posicion;
    protected final double w = 20;
    protected final double h = 20;
    
    public PowerUpBase(Vector posicion) {
        this.posicion = posicion;
    }
    
    public Rectangulo hitbox() {
        return new Rectangulo(posicion.x(), posicion.y(), w, h);
    }
    
    public Vector posicion() {
        return posicion;
    }
}
