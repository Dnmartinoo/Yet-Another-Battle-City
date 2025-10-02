package org.example.modelo.juego;

import org.example.modelo.fisica.Vector;

public final class JuegoConfig {
    private JuegoConfig() {}

    // Velocidades de balas
    public static double PLAYER_BULLET_SPEED = 260.0;
    public static double ENEMY_BULLET_SPEED  = 230.0;

    // Daño base de balas
    public static int BULLET_DAMAGE = 1;

    // Facing por defecto (si el tanque está quieto)
    public static Vector PLAYER_DEFAULT_FACING = new Vector(0, -1); // arriba
    public static Vector ENEMY_DEFAULT_FACING  = new Vector(0, +1); // abajo

    // Cooldown de disparo enemigo (ms)
    public static long ENEMY_SHOOT_COOLDOWN_MS = 2000L;
    public static double BULLET_SIZE = 6.0;
    public static long PLAYER_SHOOT_COOLDOWN_MS = 180L;
    public static int VIDAS_INICIALES = 3;
}
