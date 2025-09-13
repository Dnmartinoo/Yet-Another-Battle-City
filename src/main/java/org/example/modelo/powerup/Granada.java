package org.example.modelo.powerup;

import org.example.modelo.personajes.Jugador;

public class Granada implements PowerUp {
    public ComandoPowerUp aplicar(Jugador jugador){
        return ComandoPowerUp.DESTUIR_TODOS_ENEMIGOS;
    }
}


