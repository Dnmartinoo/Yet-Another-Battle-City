package org.example.vista.render;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.modelo.juego.estado.EstadoNivel;

public final class RenderizadorSprites implements RenderizadorEstado {

    private final SpriteRenderer spriteRenderer = new SpriteRenderer();
    private final HudRenderer hudRenderer = new HudRenderer();

    @Override
    public void render(EstadoNivel estado, Canvas canvas) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (estado == null) return;
        spriteRenderer.dibujarDosPasadas(estado.entidades(), g);
        hudRenderer.dibujar(estado, canvas, g);
    }
}
