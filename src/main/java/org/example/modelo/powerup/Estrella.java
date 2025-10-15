
package org.example.modelo.powerup;

import org.example.modelo.personajes.Jugador;
import org.example.modelo.fisica.Vector;

public class Estrella extends PowerUpBase {

    public Estrella(Vector posicion) {
        super(posicion);
    }

    @Override
    public ComandoPowerUp aplicar(Jugador jugador) {
        jugador.setDisparoPotenciado(true);
        return ComandoPowerUp.NONE;
    }

    @Override
    public Vector velocidad() {
        return Vector.CERO;
    }
}

