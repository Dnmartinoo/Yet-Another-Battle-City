package org.example.modelo.entorno;

import org.example.modelo.entorno.bloques.*;
import org.example.modelo.fisica.Vector;

import java.util.Locale;

public final class BloqueFactory {
    public static final int TILE = 20;

    public static Bloque crear(String tipo, double x, double y) {
        String t = (tipo == null ? "" : tipo).toUpperCase(Locale.ROOT);

        return switch (t) {
            case "BASE", "BASEBLOCK" -> new Base(new Vector(x, y), TILE);
            case "LADRILLO", "BRICK", "BRICKBLOCK" -> new Ladrillo(new Vector(x, y), TILE);
            case "ACERO", "STEEL", "STEELBLOCK"     -> new Acero(new Vector(x, y), TILE);
            case "AGUA", "WATER", "WATERBLOCK"      -> new Agua(new Vector(x, y), TILE);
            case "BOSQUE", "FOREST", "FORESTBLOCK"  -> new Bosque(new Vector(x, y), TILE);
            default -> new Ladrillo(new Vector(x, y), TILE);
        };
    }
}
