package org.example.modelo.powerup;

import org.example.modelo.juego.Spriteeable;
import org.example.modelo.personajes.Jugador;

public class Granada implements PowerUp, Spriteeable {
    public ComandoPowerUp aplicar(Jugador jugador){
        return ComandoPowerUp.DESTUIR_TODOS_ENEMIGOS;
    }

    @Override public String spriteId() { return "power_grenade"; }
}


