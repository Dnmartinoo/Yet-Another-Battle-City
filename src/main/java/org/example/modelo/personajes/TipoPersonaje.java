package org.example.modelo.personajes;

import org.example.modelo.juego.config.JuegoConfig;

public enum TipoPersonaje {
    JUGADOR(
            JuegoConfig.PLAYER_SPEED,
            JuegoConfig.PLAYER_HEALTH
    ),
    regularEnemy(
            JuegoConfig.REGULAR_ENEMY_SPEED,
            JuegoConfig.REGULAR_ENEMY_HEALTH
    ),
    fastEnemy(
            JuegoConfig.FAST_ENEMY_SPEED,
            JuegoConfig.FAST_ENEMY_HEALTH
    ),
    powerfulEnemy(
            JuegoConfig.POWERFUL_ENEMY_SPEED,
            JuegoConfig.POWERFUL_ENEMY_HEALTH
    ),
    heavyEnemy(
            JuegoConfig.HEAVY_ENEMY_SPEED,
            JuegoConfig.HEAVY_ENEMY_HEALTH
    );

    private final double velocidad;
    private final int vidaBase;

    TipoPersonaje(double velocidad, int vidaBase) {
        this.velocidad = velocidad;
        this.vidaBase = vidaBase;
    }

    public double obtenerVelocidad() { return velocidad; }
    public int vidaBase() { return vidaBase; }
    public boolean esEnemigo() { return this != JUGADOR; }
}
