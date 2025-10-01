package org.example.modelo.personajes;

import org.example.modelo.fisica.Vector;

public class Enemigo extends Tanque {
    public Enemigo(TipoPersonaje tipo, Vector posicion) {
        super(tipo, posicion);
        if(!tipo.esEnemigo()) {
            throw new IllegalArgumentException("Tipo no enemigo: " + tipo);
        }
    }

    public void actualizarIA(long ahoraMs) {
    }

    // IA y Cooldown de disparo later
}