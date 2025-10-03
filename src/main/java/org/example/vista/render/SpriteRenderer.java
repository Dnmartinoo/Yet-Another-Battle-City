package org.example.vista.render;

import javafx.scene.canvas.GraphicsContext;
import org.example.modelo.juego.EstadoEntidad;
import org.example.modelo.juego.JuegoConfig;
import org.example.vista.assets.ManagerSprites;

final class SpriteRenderer {

    void dibujarDosPasadas(Iterable<EstadoEntidad> entidades, GraphicsContext g) {
        // Vacio
        for (EstadoEntidad e : entidades) {
            String spriteId = e.spriteId();
            if (spriteId == null || JuegoConfig.SPRITE_FOREST.equals(spriteId)) continue;
            dibujarEntidad(e, g);
        }
        // Bosque
        for (EstadoEntidad e : entidades) {
            if (JuegoConfig.SPRITE_FOREST.equals(e.spriteId())) {
                g.drawImage(
                        ManagerSprites.get(JuegoConfig.SPRITE_FOREST),
                        e.x(), e.y(), e.ancho(), e.alto()
                );
            }
        }
    }

    private void dibujarEntidad(EstadoEntidad e, GraphicsContext g) {
        // Rotación si aplica
        if (e.rotacion() != 0.0) {
            g.save();
            g.translate(e.x() + e.ancho() / 2.0, e.y() + e.alto() / 2.0);
            g.rotate(e.rotacion());
            g.drawImage(
                    ManagerSprites.get(e.spriteId()),
                    -e.ancho() / 2.0,
                    -e.alto()  / 2.0,
                    e.ancho(),
                    e.alto()
            );
            g.restore();
        } else {
            g.drawImage(ManagerSprites.get(e.spriteId()), e.x(), e.y(), e.ancho(), e.alto());
        }

        // Efecto de casco (anillo de invulnerabilidad)
        if (e.cascoActivo()) {
            g.drawImage(
                    ManagerSprites.get(JuegoConfig.SPRITE_INVULNERABLE),
                    e.x(), e.y(), e.ancho(), e.alto()
            );
        }
    }
}
