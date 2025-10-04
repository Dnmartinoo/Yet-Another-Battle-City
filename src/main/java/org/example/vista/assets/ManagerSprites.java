package org.example.vista.assets;

import javafx.scene.image.Image;
import org.example.modelo.juego.config.JuegoConfig;
import org.example.vista.config.ConstantesUI;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ManagerSprites {

    private static ManagerSprites instance;

    private final Map<String, Image> sprites = new HashMap<>();

    private ManagerSprites() {
        // ======================
        // BLOQUES
        // ======================
        put(JuegoConfig.SPRITE_BRICK,       ConstantesUI.PATH_BRICK);
        put(JuegoConfig.SPRITE_STEEL,       ConstantesUI.PATH_STEEL);
        put(JuegoConfig.SPRITE_WATER,       ConstantesUI.PATH_WATER);
        put(JuegoConfig.SPRITE_FOREST,      ConstantesUI.PATH_FOREST);
        put(JuegoConfig.SPRITE_BASE,        ConstantesUI.PATH_BASE);
        put(JuegoConfig.SPRITE_EMPTY,       ConstantesUI.PATH_EMPTY);

        // ======================
        // JUGADORES
        // ======================
        put(JuegoConfig.SPRITE_PLAYER1_0,   ConstantesUI.PATH_P1_0);
        put(JuegoConfig.SPRITE_PLAYER1_1,   ConstantesUI.PATH_P1_1);
        put(JuegoConfig.SPRITE_PLAYER2_0,   ConstantesUI.PATH_P2_0);
        put(JuegoConfig.SPRITE_PLAYER2_1,   ConstantesUI.PATH_P2_1);

        // ======================
        // ENEMIGOS
        // ======================
        put(JuegoConfig.SPRITE_ENEMY_FAST_0,    ConstantesUI.PATH_E_FAST_0);
        put(JuegoConfig.SPRITE_ENEMY_FAST_1,    ConstantesUI.PATH_E_FAST_1);
        put(JuegoConfig.SPRITE_ENEMY_HEAVY_0,   ConstantesUI.PATH_E_HEAVY_0);
        put(JuegoConfig.SPRITE_ENEMY_HEAVY_1,   ConstantesUI.PATH_E_HEAVY_1);
        put(JuegoConfig.SPRITE_ENEMY_POWER_0,   ConstantesUI.PATH_E_POWER_0);
        put(JuegoConfig.SPRITE_ENEMY_POWER_1,   ConstantesUI.PATH_E_POWER_1);
        put(JuegoConfig.SPRITE_ENEMY_REGULAR_0, ConstantesUI.PATH_E_REG_0);
        put(JuegoConfig.SPRITE_ENEMY_REGULAR_1, ConstantesUI.PATH_E_REG_1);

        // ======================
        // POWERUPS
        // ======================
        put(JuegoConfig.SPRITE_POWER_GRENADE,   ConstantesUI.PATH_PW_GRENADE);
        put(JuegoConfig.SPRITE_POWER_HELMET,    ConstantesUI.PATH_PW_HELMET);
        put(JuegoConfig.SPRITE_POWER_STAR,      ConstantesUI.PATH_PW_STAR);
        put(JuegoConfig.SPRITE_POWER_SHOVEL,    ConstantesUI.PATH_PW_SHOVEL);

        // ======================
        // OTROS
        // ======================
        put(JuegoConfig.SPRITE_TANK_DESTROYED,  ConstantesUI.PATH_TANK_DESTROYED);
        put(JuegoConfig.SPRITE_SHOT,            ConstantesUI.PATH_SHOT);
        put(JuegoConfig.SPRITE_INVULNERABLE,    ConstantesUI.PATH_INVULNERABLE);
        put(JuegoConfig.SPRITE_LOGO,            ConstantesUI.PATH_LOGO);
    }

    private void put(String key, String resourcePath) {
        Image img = cargar(resourcePath);
        sprites.put(key, img);
    }

    private Image cargar(String path) {
        var stream = ManagerSprites.class.getResourceAsStream(path);
        Objects.requireNonNull(stream, "No se encontró el recurso: " + path);
        return new Image(stream);
    }

    public Image get(String id) {
        return sprites.get(id);
    }

    public static ManagerSprites get() {
        if (instance == null) {
            instance = new ManagerSprites();
        }
        return instance;
    }
}
