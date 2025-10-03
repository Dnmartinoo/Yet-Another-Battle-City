
package org.example.modelo.powerup;

import org.example.modelo.juego.Spriteeable;
import org.example.modelo.personajes.Jugador;
import org.example.modelo.fisica.Vector;

public class Estrella extends PowerUpBase implements Spriteeable {

    public Estrella(Vector posicion) {
        super(posicion);
    }

    @Override
    public ComandoPowerUp aplicar(Jugador jugador) {
        jugador.setDisparoPotenciado(true);
        return ComandoPowerUp.NONE;
    }

    @Override
    public String spriteId() { return "power_star"; }

    @Override
    public Vector velocidad() {
        return null;
    }
}

