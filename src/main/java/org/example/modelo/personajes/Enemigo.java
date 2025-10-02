package org.example.modelo.personajes;

import org.example.modelo.fisica.Vector;

public class Enemigo extends Tanque {
    @Override public boolean solido() { return true; }

    @Override
    public Vector velocidad() {
        // devolvé la dirección actual * velocidadEscalar si querés
        return Vector.CERO;
    }

    public Enemigo(TipoPersonaje tipo, Vector posicion) {
        super(tipo, posicion); // <-- ahora existe el ctor compatible
        if (!tipo.esEnemigo()) {
            throw new IllegalArgumentException("Tipo no enemigo: " + tipo);
        }
    }

    public void actualizarIA(long ahoraMs) {
        // IA
    }
}
