package org.example.modelo.powerup;

import org.example.modelo.personajes.Jugador;


public class Estrella {
    public ComandoPowerUp aplicar(Jugador jugador) {
        jugador.setDisparoPotenciado(true);
        return ComandoPowerUp.NONE;
    }
}
