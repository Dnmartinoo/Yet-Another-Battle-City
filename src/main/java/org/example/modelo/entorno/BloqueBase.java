package org.example.modelo.entorno;

import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;

public abstract class BloqueBase implements Bloque {
    protected Vector posicion;
    protected Rectangulo hitbox;
    protected boolean destruido = false;

    protected BloqueBase(Vector posicion, int tileSize) {
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

    @Override
    public boolean estaDestruido() { return destruido; }

    @Override
    public Vector velocidad() { return Vector.CERO; }
}
