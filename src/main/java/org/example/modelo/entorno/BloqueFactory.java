package org.example.modelo.entorno;

import org.example.modelo.entorno.bloques.*;
import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.config.JuegoConfig;

import java.util.Locale;

public final class BloqueFactory {

    public BloqueFactory() {}

    public Bloque crear(String tipo, double x, double y) {
        String t = (tipo == null ? "" : tipo).toUpperCase(Locale.ROOT);

        return switch (t) {
            case "BASE", "BASEBLOCK" -> new Base(new Vector(x, y), JuegoConfig.TILE_SIZE);
            case "LADRILLO", "BRICK", "BRICKBLOCK" -> new Ladrillo(new Vector(x, y), JuegoConfig.TILE_SIZE);
            case "ACERO", "STEEL", "STEELBLOCK"     -> new Acero(new Vector(x, y), JuegoConfig.TILE_SIZE);
            case "AGUA", "WATER", "WATERBLOCK"      -> new Agua(new Vector(x, y), JuegoConfig.TILE_SIZE);
            case "BOSQUE", "FOREST", "FORESTBLOCK"  -> new Bosque(new Vector(x, y), JuegoConfig.TILE_SIZE);
            default -> new Ladrillo(new Vector(x, y), JuegoConfig.TILE_SIZE);
        };
    }
}
