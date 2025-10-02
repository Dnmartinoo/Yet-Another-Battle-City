// src/main/java/org/example/modelo/juego/JuegoConfig.java
package org.example.modelo.juego;

import org.example.modelo.fisica.Vector;

public final class JuegoConfig {
    private JuegoConfig() {}

    // =====================
    // BALAS
    // =====================
    public static double PLAYER_BULLET_SPEED = 260.0;
    public static double ENEMY_BULLET_SPEED  = 230.0;
    public static int BULLET_DAMAGE = 1;
    public static double BULLET_SIZE = 6.0;
    public static long PLAYER_SHOOT_COOLDOWN_MS = 180L;
    public static long ENEMY_SHOOT_COOLDOWN_MS = 2000L;

    public static int VIDAS_INICIALES = 3;

    // =====================
    // FACING DEFAULT
    // =====================
    public static Vector PLAYER_DEFAULT_FACING = new Vector(0, -1); // arriba
    public static Vector ENEMY_DEFAULT_FACING  = new Vector(0, +1); // abajo

    // =====================
    // SPRITES: Bloques
    // =====================
    public static final String SPRITE_BRICK   = "brick";
    public static final String SPRITE_STEEL   = "steel";
    public static final String SPRITE_WATER   = "water";
    public static final String SPRITE_FOREST  = "forest";
    public static final String SPRITE_BASE    = "base";
    public static final String SPRITE_EMPTY   = "empty";

    // =====================
    // SPRITES: Jugadores
    // =====================
    public static final String SPRITE_PLAYER1_0 = "player1_0";
    public static final String SPRITE_PLAYER1_1 = "player1_1";
    public static final String SPRITE_PLAYER2_0 = "player2_0";
    public static final String SPRITE_PLAYER2_1 = "player2_1";

    // =====================
    // SPRITES: Enemigos
    // =====================
    public static final String SPRITE_ENEMY_FAST_0    = "enemy_fast_0";
    public static final String SPRITE_ENEMY_FAST_1    = "enemy_fast_1";
    public static final String SPRITE_ENEMY_HEAVY_0   = "enemy_heavy_0";
    public static final String SPRITE_ENEMY_HEAVY_1   = "enemy_heavy_1";
    public static final String SPRITE_ENEMY_POWER_0   = "enemy_power_0";
    public static final String SPRITE_ENEMY_POWER_1   = "enemy_power_1";
    public static final String SPRITE_ENEMY_REGULAR_0 = "enemy_regular_0";
    public static final String SPRITE_ENEMY_REGULAR_1 = "enemy_regular_1";

    // =====================
    // SPRITES: Otros
    // =====================
    public static final String SPRITE_TANK_DESTROYED = "tank_destroyed";
    public static final String SPRITE_SHOT           = "shot";
    public static final String SPRITE_INVULNERABLE   = "invulnerable";
    public static final String SPRITE_LOGO           = "logo";

    // =====================
    // SPRITES: PowerUps
    // =====================
    public static final String SPRITE_POWER_GRENADE = "power_grenade";
    public static final String SPRITE_POWER_HELMET  = "power_helmet";
    public static final String SPRITE_POWER_STAR    = "power_star";
    public static final String SPRITE_POWER_SHOVEL  = "power_shovel";

    public static final long PLAYER_ANIM_FRAME_MS = 200; // por ejemplo

}

