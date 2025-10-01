package org.example.modelo.fisica;

public interface Cuerpo {
    Rectangulo hitbox();
    Vector posicion();
    void setPosicion(Vector nuevaPosicion);
    Vector velocidad();
    boolean solido();
    default void integrar(double dt){
        setPosicion(posicion().mas(velocidad().por(dt)));
    }
}
