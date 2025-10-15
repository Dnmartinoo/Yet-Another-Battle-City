package org.example.vista.render;

import javafx.scene.canvas.GraphicsContext;
import org.example.modelo.juego.estado.EstadoEntidad;
import org.example.modelo.juego.config.JuegoConfig;
import org.example.vista.assets.ManagerSprites;

final class SpriteRenderer {

    void dibujarDosPasadas(Iterable<EstadoEntidad> entidades, GraphicsContext g) {
        ManagerSprites ms = ManagerSprites.get();
        for (EstadoEntidad e : entidades) {
            String spriteId = ms.spriteDesdeTipo(e.tipo());
            if (spriteId == null || JuegoConfig.SPRITE_FOREST.equals(spriteId)) continue;
            dibujarEntidad(e, spriteId, g, ms);
        }
        for (EstadoEntidad e : entidades) {
            if ("Bosque".equals(e.tipo())) {
                g.drawImage(
                        ms.get(JuegoConfig.SPRITE_FOREST),
                        e.x(), e.y(), e.ancho(), e.alto()
                );
            }
        }
    }

    private void dibujarEntidad(EstadoEntidad e, String spriteId, GraphicsContext g, ManagerSprites ms) {
        double rotacion = calcularRotacion(e.direccionX(), e.direccionY());

        g.save();
        g.translate(e.x() + e.ancho() / 2.0, e.y() + e.alto() / 2.0);
        g.rotate(rotacion);
        g.drawImage(
                ms.get(spriteId),
                -e.ancho() / 2.0,
                -e.alto() / 2.0,
                e.ancho(),
                e.alto()
        );
        g.restore();

        if (e.cascoActivo()) {
            g.drawImage(
                    ms.get(JuegoConfig.SPRITE_INVULNERABLE),
                    e.x(), e.y(), e.ancho(), e.alto()
            );
        }
    }

    private double calcularRotacion(double dx, double dy) {
        if (dx > 0) return 90;
        if (dx < 0) return 270;
        if (dy > 0) return 180;
        if (dy < 0) return 0;
        return 0;
    }
}
