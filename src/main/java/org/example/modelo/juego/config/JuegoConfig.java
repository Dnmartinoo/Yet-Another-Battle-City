package org.example.modelo.juego.config;

public final class JuegoConfig {

    // ==== TANQUES ====
    public static final double PLAYER_SPEED = 1.0;
    public static final int    PLAYER_HEALTH = 3;
    public static final double PLAYER_FIRE_RATE = 1.0;


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

    // ==== BALAS ====
    public static final double PLAYER_BULLET_SPEED = 260.0;
    public static final double ENEMY_BULLET_SPEED  = 230.0;
    public static final int    BULLET_DAMAGE = 1;
    public static final double BULLET_SIZE = 6.0;
    public static final long   PLAYER_SHOOT_COOLDOWN_MS = 180L;
    public static final long   ENEMY_SHOOT_COOLDOWN_MS = 500L;
    public static final long   RESPAWN_INVULN_MS = 1500L;
    public static final long   PLAYER_STUN_MS = 1500L;
    public static final int DANO_BALAS_POTENCIADAS = 999;
    public static final int    VIDAS_INICIALES = 3;


    // ==== SPRITES: Bloques ====
    public static final String SPRITE_BRICK   = "brick";
    public static final String SPRITE_STEEL   = "steel";
    public static final String SPRITE_WATER   = "water";
    public static final String SPRITE_FOREST  = "forest";
    public static final String SPRITE_BASE    = "base";
    public static final String SPRITE_EMPTY   = "empty";

    // ==== SPRITES: Jugadores ====
    public static final String SPRITE_PLAYER1_0 = "player1_0";
    public static final String SPRITE_PLAYER1_1 = "player1_1";
    public static final String SPRITE_PLAYER2_0 = "player2_0";
    public static final String SPRITE_PLAYER2_1 = "player2_1";

    // ==== SPRITES: Enemigos ====
    public static final String SPRITE_ENEMY_FAST_0    = "enemy_fast_0";
    public static final String SPRITE_ENEMY_FAST_1    = "enemy_fast_1";
    public static final String SPRITE_ENEMY_HEAVY_0   = "enemy_heavy_0";
    public static final String SPRITE_ENEMY_HEAVY_1   = "enemy_heavy_1";
    public static final String SPRITE_ENEMY_POWER_0   = "enemy_power_0";
    public static final String SPRITE_ENEMY_POWER_1   = "enemy_power_1";
    public static final String SPRITE_ENEMY_REGULAR_0 = "enemy_regular_0";
    public static final String SPRITE_ENEMY_REGULAR_1 = "enemy_regular_1";

    // ==== SPRITES: Otros ====
    public static final String SPRITE_TANK_DESTROYED = "tank_destroyed";
    public static final String SPRITE_SHOT           = "shot";
    public static final String SPRITE_INVULNERABLE   = "invulnerable";
    public static final String SPRITE_LOGO           = "logo";

    // ==== SPRITES: PowerUps ====
    public static final String SPRITE_POWER_GRENADE = "power_grenade";
    public static final String SPRITE_POWER_HELMET  = "power_helmet";
    public static final String SPRITE_POWER_STAR    = "power_star";
    public static final String SPRITE_POWER_SHOVEL  = "power_shovel";

    // ==== TIMINGS ====
    public static final long VICTORY_SCREEN_MS = 2000L;
    public static final long DEFEAT_SCREEN_MS  = 2000L;
    public static final long CASCO_DURACION_MS = 10000;

    // ==== SPAWN ENEMIGOS ====
    public static final int  MAX_ENEMIGOS_CONCURRENTES   = 4;
    public static final long ENEMY_MIN_SPAWN_GAP_MS      = 800L;
    public static final int  ENEMY_SPAWN_MAX_IN_WINDOW   = 10;
    public static final long ENEMY_SPAWN_WINDOW_MS       = 60_000L;

    // ==== OTROS ====
    public static final double ROTACION_FIJA = 0.0;

    // MotorJuego
    public static final int    NO_NIVEL          = -1;
    public static final long   TIEMPO_INICIAL_MS = 0L;
    public static final boolean PARTIDA_NO_FINALIZADA = false;
    public static final boolean PARTIDA_FINALIZADA    = true;


    // Identificadores de sonidos
    public static final String SND_DISPARAR = "disparar";
    public static final String SND_DERROTA  = "derrota";
    public static final String SND_POWERUP  = "powerup";
    public static final String SND_IMPACTO_ACERO = "impactoAcero";
    public static final String SND_LADRILLO_ROTO = "ladrilloRoto";

    // Nivel data
    public static final int NIVEL_DEFAULT_ANCHO = 800;
    public static final int NIVEL_DEFAULT_ALTO  = 600;
    public static final double J1_START_X = 100.0, J1_START_Y = 500.0;
    public static final double J2_START_X = 200.0, J2_START_Y = 500.0;


    // Fallback para atributos de <player> en XML
    public static final double PLAYER_DEFAULT_X = 0.0;
    public static final double PLAYER_DEFAULT_Y = 0.0;

    // Fallback para atributos de <enemy> en XML
    public static final double ENEMY_DEFAULT_X = 0.0;
    public static final double ENEMY_DEFAULT_Y = 0.0;

    // Fallback para atributos de <staticObject> en XML
    public static final double BLOCK_DEFAULT_X = 0.0;
    public static final double BLOCK_DEFAULT_Y = 0.0;

    // Tipo de enemigo por defecto (si en XML viene vacío o inválido)
    public static final org.example.modelo.personajes.TipoPersonaje ENEMY_DEFAULT_TYPE =
            org.example.modelo.personajes.TipoPersonaje.regularEnemy;
}
