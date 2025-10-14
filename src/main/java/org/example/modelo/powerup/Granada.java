
package org.example.modelo.powerup;

import org.example.modelo.personajes.Jugador;
import org.example.modelo.fisica.Vector;

public class Granada extends PowerUpBase {

    public Granada(Vector posicion) {
        super(posicion);
    }

    @Override
    public ComandoPowerUp aplicar(Jugador jugador) {
        return ComandoPowerUp.DESTUIR_TODOS_ENEMIGOS;
    }

    @Override
    public Vector velocidad() { return Vector.CERO; }
}
