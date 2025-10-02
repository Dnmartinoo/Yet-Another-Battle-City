package org.example.modelo.powerup;

import org.example.modelo.juego.Spriteeable;
import org.example.modelo.personajes.Jugador;


public class Estrella implements Spriteeable {
    public ComandoPowerUp aplicar(Jugador jugador) {
        jugador.setDisparoPotenciado(true);
        return ComandoPowerUp.NONE;
    }
    @Override public String spriteId() { return "power_star"; }
}
