package org.example.modelo.powerup;

import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.Spriteeable;
import org.example.modelo.personajes.Jugador;

public class Casco extends PowerUpBase implements Spriteeable {
    public static final long DURACION_MS = 10_000;

    public Casco(Vector posicion) {
        super(posicion);
    }

    @Override
    public ComandoPowerUp aplicar(Jugador jugador) {
        long ahora = System.currentTimeMillis();
        Jugador.activarInvulnerabilidadPor(DURACION_MS, ahora);
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

