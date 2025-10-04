package org.example.vista.render;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.example.modelo.juego.estado.EstadoNivel;
import org.example.vista.config.ConstantesUI;

final class HudRenderer {

    void dibujar(EstadoNivel estado, Canvas canvas, GraphicsContext g) {
        g.setGlobalAlpha(ConstantesUI.HUD_OPACIDAD_FONDO);
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), ConstantesUI.HUD_ALTURA);
        g.setGlobalAlpha(1.0);

        g.setFont(Font.font(ConstantesUI.HUD_FONT_FAMILY, ConstantesUI.HUD_FONT_SIZE));

        int cantJugadores  = estado.cantJugadores();
        int vidasP1        = estado.vidasP1();
        int vidasP2        = estado.vidasP2();
        int enemigosTot    = estado.enemigosTotales();
        int nivelNro       = estado.nivelNumero();

        double x = ConstantesUI.HUD_PADDING_X;
        double y = ConstantesUI.HUD_PADDING_Y;

        g.setFill(Color.web(ConstantesUI.HUD_COLOR_TEXT));
        g.fillText("P1 Vidas: " + vidasP1, x, y);
        x += ConstantesUI.HUD_SEPARACION;


        if (cantJugadores >= 2) {
            g.setFill(Color.web(ConstantesUI.HUD_COLOR_P2));
            g.fillText("P2 Vidas: " + vidasP2, x, y);
            x += ConstantesUI.HUD_SEPARACION;
            g.setFill(Color.web(ConstantesUI.HUD_COLOR_TEXT));
        }

        g.fillText("Enemigos: " + enemigosTot, x, y);
        x += ConstantesUI.HUD_SEPARACION;
        g.fillText("Nivel: " + nivelNro, x, y);
    }
}
