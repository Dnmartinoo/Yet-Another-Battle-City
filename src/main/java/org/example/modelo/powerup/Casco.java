package org.example.modelo.powerup;

import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.JuegoConfig;
import org.example.modelo.juego.Spriteeable;
import org.example.modelo.personajes.Jugador;

public class Casco extends PowerUpBase implements Spriteeable {


    public Casco(Vector posicion) {
        super(posicion);
    }

    @Override
    public ComandoPowerUp aplicar(Jugador jugador) {
        long ahora = System.currentTimeMillis();
        Jugador.activarInvulnerabilidadPor(JuegoConfig.CASCO_DURACION_MS, ahora);
        return ComandoPowerUp.NONE;
    }

    @Override
    public String spriteId() {
        return "power_helmet";
    }

    @Override
    public Vector velocidad() {
        return null;
    }
}

