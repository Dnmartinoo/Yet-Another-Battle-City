// src/main/java/org/example/vista/RenderizadorSprites.java
package org.example.vista;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.modelo.juego.EstadoEntidad;
import org.example.modelo.juego.EstadoNivel;

public final class RenderizadorSprites implements RenderizadorEstado {

    // --- HUD style ---
    private static final double HUD_ALTURA = 40.0;
    private static final double HUD_PADDING_X = 10.0;
    private static final double HUD_PADDING_Y = 25.0;
    private static final double HUD_SEPARACION = 150.0;
    private static final double HUD_OPACIDAD_FONDO = 0.35;
    private static final Font   HUD_FONT = Font.font("Consolas", 18);

    @Override
    public void render(EstadoNivel estado, Canvas canvas) {
        GraphicsContext g = canvas.getGraphicsContext2D();

        // Fondo
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (estado == null) return;

        for (EstadoEntidad entidad : estado.entidades()) {
            String spriteId = entidad.spriteId();
            if (spriteId == null || "forest".equals(spriteId)) continue;

            if (entidad.rotacion() != 0.0) {
                g.save();
                g.translate(entidad.x() + entidad.ancho() / 2.0, entidad.y() + entidad.alto() / 2.0);
                g.rotate(entidad.rotacion());
                g.drawImage(
                        ManagerSprites.get(spriteId),
                        -entidad.ancho() / 2,
                        -entidad.ancho() / 2,
                        entidad.ancho(),
                        entidad.alto()
                );
                g.restore();
            } else {
                g.drawImage(
                        ManagerSprites.get(spriteId),
                        entidad.x(),
                        entidad.y(),
                        entidad.ancho(),
                        entidad.alto()
                );
            }

            if (entidad.cascoActivo()) {
                g.drawImage(
                        ManagerSprites.get("invulnerable"),
                        entidad.x(),
                        entidad.y(),
                        entidad.ancho(),
                        entidad.alto()
                );
            }
        }

        for (EstadoEntidad entidad : estado.entidades()) {
            if ("forest".equals(entidad.spriteId())) {
                g.drawImage(
                        ManagerSprites.get("forest"),
                        entidad.x(),
                        entidad.y(),
                        entidad.ancho(),
                        entidad.alto()
                );
            }
        }

        // --- HUD ---
        dibujarHUD(estado, canvas, g);
    }



    private void dibujarHUD(EstadoNivel estado, Canvas canvas, GraphicsContext g) {
        // Panel translúcido arriba
        g.setGlobalAlpha(HUD_OPACIDAD_FONDO);
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), HUD_ALTURA);
        g.setGlobalAlpha(1.0);

        g.setFill(Color.WHITESMOKE);
        g.setFont(HUD_FONT);

        int cantJugadores  = estado.cantJugadores();
        int vidasP1        = estado.vidasP1();
        int vidasP2        = estado.vidasP2();
        int enemigosTot    = estado.enemigosTotales();
        int nivelNro       = estado.nivelNumero();

        double x = HUD_PADDING_X;
        double y = HUD_PADDING_Y;

        // P1
        g.setFill(Color.WHITESMOKE);
        g.fillText("P1 Vidas: " + vidasP1, x, y);
        x += HUD_SEPARACION;

        // P2 (si existe)
        if (cantJugadores >= 2) {
            g.setFill(Color.LIGHTSKYBLUE);
            g.fillText("P2 Vidas: " + vidasP2, x, y);
            g.setFill(Color.WHITESMOKE);
            x += HUD_SEPARACION;
        }

        // Enemigos + Nivel
        g.fillText("Enemigos: " + enemigosTot, x, y);
        x += HUD_SEPARACION;

        g.fillText("Nivel: " + nivelNro, x, y);
    }
}
