// src/main/java/org/example/vista/RenderizadorJavaFX.java
package org.example.vista;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.modelo.juego.Nivel;
import org.example.modelo.fisica.Cuerpo;

public final class RenderizadorJavaFX implements Renderizador {

    @Override
    public void render(Nivel nivel, Canvas canvas) {
        GraphicsContext g = canvas.getGraphicsContext2D();

        // Fondo
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (nivel == null) return;

        // ENEMIGOS (rojo)
        g.setFill(Color.CRIMSON);
        for (var e : nivel.enemigos()) {
            var hb = e.hitbox();
            g.fillRect(hb.x(), hb.y(), hb.w(), hb.h());
        }

        // JUGADORES (verde)
        g.setFill(Color.LIMEGREEN);
        for (var j : nivel.jugadores()) {
            var hb = j.hitbox();
            g.fillRect(hb.x(), hb.y(), hb.w(), hb.h());
        }

        // BLOQUES (gris)
        g.setFill(Color.DARKGRAY);
        for (var b : nivel.bloques()) {
            var hb = b.hitbox(); // Bloque extiende Cuerpo → no hace falta instanceof
            g.fillRect(hb.x(), hb.y(), hb.w(), hb.h());
        }

        // BASE destacada (oro) – es un Bloque, por eso tiene hitbox()
        var base = nivel.base();
        if (base != null) {
            var hb = base.hitbox();
            g.setFill(Color.GOLD);
            g.fillRect(hb.x(), hb.y(), hb.w(), hb.h());
        }

        // BALAS (blancas)
        g.setFill(Color.WHITESMOKE);
        for (var p : nivel.proyectiles()) {
            var hb = p.hitbox();
            g.fillRect(hb.x(), hb.y(), hb.w(), hb.h());
        }
    }
}
