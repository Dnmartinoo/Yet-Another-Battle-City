package org.example.vista;

import org.example.modelo.juego.estado.EstadoNivel;

import javafx.scene.canvas.Canvas;

public interface RenderizadorEstado {
    void render(EstadoNivel estado, Canvas canvas);
}
