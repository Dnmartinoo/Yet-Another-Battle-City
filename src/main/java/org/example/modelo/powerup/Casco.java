package org.example.modelo.powerup;

import org.example.modelo.personajes.Jugador;

public class Casco {
    public final long DURACION_MS = 10_000;
    public ComandoPowerUp aplicar(Jugador jugador) {
        long ahora = System.currentTimeMillis();
        Jugador.activarInvulnerabilidadPor(DURACION_MS, ahora);
        return ComandoPowerUp.NONE;
    }
}
