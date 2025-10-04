package org.example.vista.render;

import javafx.scene.canvas.GraphicsContext;
import org.example.modelo.juego.estado.EstadoEntidad;
import org.example.modelo.juego.config.JuegoConfig;
import org.example.vista.assets.ManagerSprites;

final class SpriteRenderer {

    void dibujarDosPasadas(Iterable<EstadoEntidad> entidades, GraphicsContext g) {
        for (EstadoEntidad e : entidades) {
            String spriteId = e.spriteId();
            if (spriteId == null || JuegoConfig.SPRITE_FOREST.equals(spriteId)) continue;
            dibujarEntidad(e, g);
        }

        for (EstadoEntidad e : entidades) {
            if (JuegoConfig.SPRITE_FOREST.equals(e.spriteId())) {
                g.drawImage(
                        ManagerSprites.get().get(JuegoConfig.SPRITE_FOREST),
                        e.x(), e.y(), e.ancho(), e.alto()
                );
            }
        }
    }

    private void dibujarEntidad(EstadoEntidad e, GraphicsContext g) {
        if (e.rotacion() != 0.0) {
            g.save();
            g.translate(e.x() + e.ancho() / 2.0, e.y() + e.alto() / 2.0);
            g.rotate(e.rotacion());
            g.drawImage(
                    ManagerSprites.get().get(e.spriteId()),
                    -e.ancho() / 2.0,
                    -e.alto()  / 2.0,
                    e.ancho(),
                    e.alto()
            );
            g.restore();
        } else {
            g.drawImage(ManagerSprites.get().get(e.spriteId()), e.x(), e.y(), e.ancho(), e.alto());
        }

        if (e.cascoActivo()) {
            g.drawImage(
                    ManagerSprites.get().get(JuegoConfig.SPRITE_INVULNERABLE),
                    e.x(), e.y(), e.ancho(), e.alto()
            );
        }
    }
}
