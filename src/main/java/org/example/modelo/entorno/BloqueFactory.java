package org.example.modelo.entorno;

import org.example.modelo.fisica.Vector;

public final class BloqueFactory {
    public static final int TILE = 20; // ahora int para matchear los ctors
    private BloqueFactory() {}

    public static Bloque crear(String tipo, double x, double y) {
        Vector pos = new Vector(x, y);
        return switch (tipo) {
            case "LADRILLO", "brickBlock"  -> new Ladrillo(pos, TILE);
            case "ACERO",    "steelBlock"  -> new Acero(pos, TILE);
            case "AGUA",     "waterBlock"  -> new Agua(pos, TILE);
            case "BOSQUE",   "forestBlock" -> new Bosque(pos, TILE);
            case "BASE",     "baseBlock"   -> new Base(pos, TILE);
            default -> new Ladrillo(pos, TILE);
        };
    }
}
