package org.example.modelo.fisica;

public interface Cuerpo {
    Rectangulo hitbox();
    Vector posicion();
    void setPosicion(Vector nuevaPosicion);
    Vector velocidad();
}
