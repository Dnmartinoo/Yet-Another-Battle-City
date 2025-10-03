package org.example.vista;

import javafx.scene.image.Image;
import org.example.modelo.juego.config.JuegoConfig;

import java.util.HashMap;
import java.util.Map;

public final class ManagerSprites {
    private static final Map<String, Image> sprites = new HashMap<>();

    static {
        // ======================
        // BLOQUES
        // ======================
        put(JuegoConfig.SPRITE_BRICK, "sprites/BrickBlock20x20.png");
        put(JuegoConfig.SPRITE_STEEL, "sprites/SteelBlock20x20.png");
        put(JuegoConfig.SPRITE_WATER, "sprites/Water20x20.png");
        put(JuegoConfig.SPRITE_FOREST, "sprites/Forest20x20.png");
        put(JuegoConfig.SPRITE_BASE, "sprites/base20x20.png");
        put(JuegoConfig.SPRITE_EMPTY, "sprites/WhiteBlock20x20.png");

        // ======================
        // JUGADORES
        // ======================
        put(JuegoConfig.SPRITE_PLAYER1_0, "sprites/Player1Tank0_20x20.png");
        put(JuegoConfig.SPRITE_PLAYER1_1, "sprites/Player1Tank1_20x20.png");
        put(JuegoConfig.SPRITE_PLAYER2_0, "sprites/Player2Tank0_20x20.png");
        put(JuegoConfig.SPRITE_PLAYER2_1, "sprites/Player2Tank1_20x20.png");

        // ======================
        // ENEMIGOS
        // ======================
        put(JuegoConfig.SPRITE_ENEMY_FAST_0, "sprites/EnemyTankFast0_20x20.png");
        put(JuegoConfig.SPRITE_ENEMY_FAST_1, "sprites/EnemyTankFast1_20x20.png");
        put(JuegoConfig.SPRITE_ENEMY_HEAVY_0, "sprites/EnemyTankHeavy0_20x20.png");
        put(JuegoConfig.SPRITE_ENEMY_HEAVY_1, "sprites/EnemyTankHeavy1_20x20.png");
        put(JuegoConfig.SPRITE_ENEMY_POWER_0, "sprites/EnemyTankPowerful0_20x20.png");
        put(JuegoConfig.SPRITE_ENEMY_POWER_1, "sprites/EnemyTankPowerful1_20x20.png");
        put(JuegoConfig.SPRITE_ENEMY_REGULAR_0, "sprites/EnemyTankRegular0_20x20.png");
        put(JuegoConfig.SPRITE_ENEMY_REGULAR_1, "sprites/EnemyTankRegular1_20x20.png");

        // ======================
        // OTROS
        // ======================
        put(JuegoConfig.SPRITE_TANK_DESTROYED, "sprites/TankDestroyed_20x20.png");
        put(JuegoConfig.SPRITE_SHOT, "sprites/Shot.png");
        put(JuegoConfig.SPRITE_INVULNERABLE, "sprites/InvulnerableRing20x20.png");
        put(JuegoConfig.SPRITE_LOGO, "sprites/logo.png");

        // ======================
        // POWERUPS
        // ======================
        put(JuegoConfig.SPRITE_POWER_GRENADE, "sprites/PowerUp-Grenade20x20.png");
        put(JuegoConfig.SPRITE_POWER_HELMET, "sprites/PowerUp-Helmet20x20.png");
        put(JuegoConfig.SPRITE_POWER_STAR, "sprites/PowerUp-Star20x20.png");
        put(JuegoConfig.SPRITE_POWER_SHOVEL, "sprites/PowerUp-Shovel20x20.png");
    }

    private static void put(String key, String resourcePath) {
        Image img = load(resourcePath);
        sprites.put(key, img);
    }

    private static Image load(String path) {
        var stream = ManagerSprites.class.getClassLoader().getResourceAsStream(path);
        assert stream != null;
        return new Image(stream);
    }

    public static Image get(String id) {
        return sprites.get(id);
    }
}
