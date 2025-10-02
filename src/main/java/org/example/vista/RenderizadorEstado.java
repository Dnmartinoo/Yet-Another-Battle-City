package org.example.vista;

import org.example.modelo.juego.EstadoNivel;

import javafx.scene.canvas.Canvas;

public interface RenderizadorEstado {
    void render(EstadoNivel estado, Canvas canvas);
}
