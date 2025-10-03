
package org.example.modelo.powerup;

import org.example.modelo.juego.config.JuegoConfig;
import org.example.modelo.juego.Spriteeable;
import org.example.modelo.personajes.Jugador;
import org.example.modelo.fisica.Vector;

public class Granada extends PowerUpBase implements Spriteeable {

    public Granada(Vector posicion) {
        super(posicion);
    }

    @Override
    public ComandoPowerUp aplicar(Jugador jugador) {
        return ComandoPowerUp.DESTUIR_TODOS_ENEMIGOS;
    }

    @Override
    public String spriteId() { return JuegoConfig.SPRITE_POWER_GRENADE; }

    @Override
    public Vector velocidad() { return Vector.CERO; }
}
