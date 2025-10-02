package org.example.vista;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.modelo.juego.EstadoEntidad;
import org.example.modelo.juego.EstadoNivel;

public final class RenderizadorJavaFX implements RenderizadorEstado {

    @Override
    public void render(EstadoNivel estado, Canvas canvas) {
        GraphicsContext g = canvas.getGraphicsContext2D();

        // Fondo
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (estado == null) return;

        for (EstadoEntidad entidad : estado.entidades()) {
            var x = entidad.x();
            var y = entidad.y();
            var w = entidad.ancho();
            var h = entidad.alto();
            String spriteId = entidad.spriteId();


            if (spriteId.startsWith("player")) {
                g.setFill(Color.LIMEGREEN);
            } else if (spriteId.startsWith("enemy")) {
                g.setFill(Color.CRIMSON);
            } else if (spriteId.startsWith("brick") || spriteId.startsWith("steel") || spriteId.startsWith("forest") || spriteId.startsWith("water") || spriteId.startsWith("base")) {
                g.setFill(Color.DARKGRAY);
                if (spriteId.equals("base")) g.setFill(Color.GOLD);
            } else if (spriteId.equals("shot")) {
                g.setFill(Color.WHITESMOKE);
            } else {
                g.setFill(Color.GRAY);
            }

            g.fillRect(x, y, w, h);


            if (entidad.cascoActivo()) {
                g.setFill(Color.AQUA);
                g.strokeRect(x, y, w, h);
            }
        }
    }
}
