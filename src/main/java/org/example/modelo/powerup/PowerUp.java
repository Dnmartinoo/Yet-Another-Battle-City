package org.example.modelo.powerup;

import org.example.modelo.personajes.Jugador;

public interface PowerUp {
    ComandoPowerUp aplicar(Jugador jugador);
}
