// src/main/java/org/example/modelo/juego/JuegoConfig.java
package org.example.modelo.juego;

import org.example.modelo.fisica.Vector;

public final class JuegoConfig {
    private JuegoConfig() {}
    // TANQUES

    public static final double PLAYER_SPEED = 1.0;
    public static final int    PLAYER_HEALTH = 3;
    public static final double PLAYER_FIRE_RATE = 1.0; // disparos/segundo
    public static final boolean BULLET_CARDINAL_ONLY = true;
    public static final double REGULAR_ENEMY_SPEED = 1.0;
    public static final int    REGULAR_ENEMY_HEALTH = 1;
    public static final double REGULAR_ENEMY_FIRE_RATE = 1.0;

    public static final double FAST_ENEMY_SPEED = 2.0;
    public static final int    FAST_ENEMY_HEALTH = 1;
    public static final double FAST_ENEMY_FIRE_RATE = 1.0;

    public static final double POWERFUL_ENEMY_SPEED = 1.2;
    public static final int    POWERFUL_ENEMY_HEALTH = 1;
    public static final double POWERFUL_ENEMY_FIRE_RATE = 2.0;

    public static final double HEAVY_ENEMY_SPEED = 0.8;
    public static final int    HEAVY_ENEMY_HEALTH = 3;
    public static final double HEAVY_ENEMY_FIRE_RATE = 1.0;

    // BALAS
    public static double PLAYER_BULLET_SPEED = 260.0;
    public static double ENEMY_BULLET_SPEED  = 230.0;
    public static int BULLET_DAMAGE = 1;
    public static double BULLET_SIZE = 6.0;
    public static long PLAYER_SHOOT_COOLDOWN_MS = 180L;
    public static long ENEMY_SHOOT_COOLDOWN_MS = 500;
    public static long RESPAWN_INVULN_MS = 1500;
    public static final long PLAYER_STUN_MS = 1500; // 1.5 segundos

    public static int VIDAS_INICIALES = 3;

    // FACING DEFAULT
    public static Vector PLAYER_DEFAULT_FACING = new Vector(0, -1); // arriba
    public static Vector ENEMY_DEFAULT_FACING  = new Vector(0, +1); // abajo

    // SPRITES: Bloques
    public static final String SPRITE_BRICK   = "brick";
    public static final String SPRITE_STEEL   = "steel";
    public static final String SPRITE_WATER   = "water";
    public static final String SPRITE_FOREST  = "forest";
    public static final String SPRITE_BASE    = "base";
    public static final String SPRITE_EMPTY   = "empty";

    // SPRITES: Jugadores
    public static final String SPRITE_PLAYER1_0 = "player1_0";
    public static final String SPRITE_PLAYER1_1 = "player1_1";
    public static final String SPRITE_PLAYER2_0 = "player2_0";
    public static final String SPRITE_PLAYER2_1 = "player2_1";

    // SPRITES: Enemigos
    public static final String SPRITE_ENEMY_FAST_0    = "enemy_fast_0";
    public static final String SPRITE_ENEMY_FAST_1    = "enemy_fast_1";
    public static final String SPRITE_ENEMY_HEAVY_0   = "enemy_heavy_0";
    public static final String SPRITE_ENEMY_HEAVY_1   = "enemy_heavy_1";
    public static final String SPRITE_ENEMY_POWER_0   = "enemy_power_0";
    public static final String SPRITE_ENEMY_POWER_1   = "enemy_power_1";
    public static final String SPRITE_ENEMY_REGULAR_0 = "enemy_regular_0";
    public static final String SPRITE_ENEMY_REGULAR_1 = "enemy_regular_1";

    // SPRITES: Otros
    public static final String SPRITE_TANK_DESTROYED = "tank_destroyed";
    public static final String SPRITE_SHOT           = "shot";
    public static final String SPRITE_INVULNERABLE   = "invulnerable";
    public static final String SPRITE_LOGO           = "logo";

    // SPRITES: PowerUps
    public static final String SPRITE_POWER_GRENADE = "power_grenade";
    public static final String SPRITE_POWER_HELMET  = "power_helmet";
    public static final String SPRITE_POWER_STAR    = "power_star";
    public static final String SPRITE_POWER_SHOVEL  = "power_shovel";
    public static final long DURACION_MS = 10_000;

    public static final long VICTORY_SCREEN_MS = 2000; // 2s
    public static final long DEFEAT_SCREEN_MS  = 2000; // 2s

    public static final long PLAYER_ANIM_FRAME_MS = 200;


    // Spawn de enemigos:
    public static final int    MAX_ENEMIGOS_CONCURRENTES = 4;
    public static final long ENEMY_MIN_SPAWN_GAP_MS    = 800;
    public static final int  ENEMY_SPAWN_MAX_IN_WINDOW = 10;
    public static final long ENEMY_SPAWN_WINDOW_MS     = 60_000;

    public static final double ROTACION_FIJA = 0.0;
}

