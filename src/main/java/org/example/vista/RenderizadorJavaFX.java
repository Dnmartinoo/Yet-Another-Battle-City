package org.example.vista;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.modelo.juego.Nivel;

public final class RenderizadorJavaFX implements Renderizador {

    // --- HUD style ---
    private static final double HUD_ALTURA = 40.0;
    private static final double HUD_PADDING_X = 10.0;
    private static final double HUD_PADDING_Y = 25.0;
    private static final double HUD_SEPARACION = 150.0;
    private static final double HUD_OPACIDAD_FONDO = 0.35;
    private static final Font   HUD_FONT = Font.font("Consolas", 18);

    @Override
    public void render(Nivel nivel, Canvas canvas) {
        GraphicsContext g = canvas.getGraphicsContext2D();

        // Fondo
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (nivel == null) return;

        // ENEMIGOS (rojo)
        g.setFill(Color.CRIMSON);
        nivel.enemigos().forEach(e -> {
            var hb = e.hitbox();
            g.fillRect(hb.x(), hb.y(), hb.w(), hb.h());
        });

        // JUGADORES (verde)
        g.setFill(Color.LIMEGREEN);
        nivel.jugadores().forEach(j -> {
            var hb = j.hitbox();
            g.fillRect(hb.x(), hb.y(), hb.w(), hb.h());
        });

        // BLOQUES (gris)
        g.setFill(Color.DARKGRAY);
        nivel.bloques().forEach(b -> {
            var hb = b.hitbox();
            g.fillRect(hb.x(), hb.y(), hb.w(), hb.h());
        });

        // BASE (oro)
        var base = nivel.base();
        if (base != null) {
            var hb = base.hitbox();
            g.setFill(Color.GOLD);
            g.fillRect(hb.x(), hb.y(), hb.w(), hb.h());
        }

        // BALAS (blancas)
        g.setFill(Color.WHITESMOKE);
        nivel.proyectiles().forEach(p -> {
            var hb = p.hitbox();
            g.fillRect(hb.x(), hb.y(), hb.w(), hb.h());
        });

        // HUD
        dibujarHUD(nivel, canvas, g);
    }

    // =========================
    // HUD (1 o 2 jugadores)
    // =========================
    private void dibujarHUD(Nivel nivel, Canvas canvas, GraphicsContext g) {
        // Panel translúcido arriba
        g.setGlobalAlpha(HUD_OPACIDAD_FONDO);
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), HUD_ALTURA);
        g.setGlobalAlpha(1.0);

        g.setFill(Color.WHITESMOKE);
        g.setFont(HUD_FONT);

        // Datos base
        int cantJugadores = nivel.jugadores().size();

        // Vidas de jugadores (preferimos pedir al nivel si tenés getters;
        // si no, leemos directo de la lista de jugadores)
        int vidasP1 = 0;
        int vidasP2 = 0;

        if (cantJugadores >= 1) {
            // si tenés nivel.vidasJugador1():  vidasP1 = nivel.vidasJugador1();
            vidasP1 = nivel.jugadores().get(0).vidasRestantes();
        }
        if (cantJugadores >= 2) {
            // si tenés nivel.vidasJugador2():  vidasP2 = nivel.vidasJugador2();
            vidasP2 = nivel.jugadores().get(1).vidasRestantes();
        }

        // Enemigos restantes (vivos + pendientes).
        // Preferido:
        int enemigosTotales = 0;
        try {
            enemigosTotales = nivel.enemigosRestantesTotales();
        } catch (Throwable __) {
            // Fallback si no implementaste el método aún:
            // enemigosTotales = nivel.enemigos().size();
            enemigosTotales = nivel.enemigos().size();
        }

        // Número de nivel
        int nivelNro = 1;
        try {
            nivelNro = nivel.numeroDeNivel();
        } catch (Throwable __) {
            // Fallback: 1
            nivelNro = 1;
        }

        // Layout simple izquierda → derecha
        double x = HUD_PADDING_X;
        double y = HUD_PADDING_Y;

        // P1
        g.setFill(Color.WHITESMOKE);
        g.fillText("P1 Vidas: " + vidasP1, x, y);
        x += HUD_SEPARACION;

        // P2 (solo si existe)
        if (cantJugadores >= 2) {
            g.setFill(Color.LIGHTSKYBLUE);
            g.fillText("P2 Vidas: " + vidasP2, x, y);
            g.setFill(Color.WHITESMOKE);
            x += HUD_SEPARACION;
        }


        g.fillText("Enemigos: " + enemigosTotales, x, y);
        x += HUD_SEPARACION;


        g.fillText("Nivel: " + nivelNro, x, y);


    }
}
