package org.example.modelo.powerup;

import org.example.modelo.personajes.Jugador;

public class Casco {
    public ComandoPowerUp aplicar(Jugador jugador) {
        jugador.setInvulnerable(true);
        return ComandoPowerUp.NONE;
    }
}
