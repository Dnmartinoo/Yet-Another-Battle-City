package org.example.vista;

import javafx.scene.canvas.Canvas;
import org.example.modelo.juego.Nivel;

public interface Renderizador {
    void render(Nivel nivel, Canvas canvas);
}
