package org.example.modelo.personajes;

import org.example.modelo.juego.JuegoConfig;

public enum TipoPersonaje {
    JUGADOR(
            JuegoConfig.PLAYER_SPEED,
            JuegoConfig.PLAYER_HEALTH,
            JuegoConfig.PLAYER_FIRE_RATE
    ),
    regularEnemy(
            JuegoConfig.REGULAR_ENEMY_SPEED,
            JuegoConfig.REGULAR_ENEMY_HEALTH,
            JuegoConfig.REGULAR_ENEMY_FIRE_RATE
    ),
    fastEnemy(
            JuegoConfig.FAST_ENEMY_SPEED,
            JuegoConfig.FAST_ENEMY_HEALTH,
            JuegoConfig.FAST_ENEMY_FIRE_RATE
    ),
    powerfulEnemy(
            JuegoConfig.POWERFUL_ENEMY_SPEED,
            JuegoConfig.POWERFUL_ENEMY_HEALTH,
            JuegoConfig.POWERFUL_ENEMY_FIRE_RATE
    ),
    heavyEnemy(
            JuegoConfig.HEAVY_ENEMY_SPEED,
            JuegoConfig.HEAVY_ENEMY_HEALTH,
            JuegoConfig.HEAVY_ENEMY_FIRE_RATE
    );

    private double velocidad;
    private int vidaBase;
    private double cadencia;

    TipoPersonaje(double velocidad, int vidaBase, double cadencia) {
        this.velocidad = velocidad;
        this.vidaBase = vidaBase;
        this.cadencia = cadencia;
    }
    public double obtenerVelocidad() {
        return velocidad;
    }
    public int vidaBase() {
        return vidaBase;
    }
    public double obtenerCadencia() {
        return cadencia;
    }

    public void reducirVida() {vidaBase--;}

    public boolean esJugador() { return this == JUGADOR; }
    public boolean esEnemigo() { return this != JUGADOR; }

}
