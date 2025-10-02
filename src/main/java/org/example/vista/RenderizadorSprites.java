package org.example.vista;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.modelo.juego.EstadoEntidad;
import org.example.modelo.juego.EstadoNivel;


public final class RenderizadorSprites implements RenderizadorEstado{

    @Override
    public void render(EstadoNivel estado, Canvas canvas) {
        GraphicsContext g = canvas.getGraphicsContext2D();

        g.setFill(Color.BLACK);
        g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

        if (estado == null) return;

        for (EstadoEntidad entidad : estado.entidades()) {
            String spriteId = entidad.spriteId();
            if (spriteId == null) continue;

            g.drawImage(
                    ManagerSprites.get(spriteId),
                    entidad.x(),
                    entidad.y(),
                    entidad.ancho(),
                    entidad.alto()

            );

            if (entidad.cascoActivo()) {
                g.drawImage(
                        ManagerSprites.get("invulnerable"),
                        entidad.x(),
                        entidad.y(),
                        entidad.alto(),
                        entidad.ancho()
                );
            }
        }
    }
}
