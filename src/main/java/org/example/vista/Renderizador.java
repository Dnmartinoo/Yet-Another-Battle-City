package org.example.vista;

import javafx.scene.canvas.Canvas;
import org.example.modelo.juego.core.Nivel;

public interface Renderizador {
    void render(Nivel nivel, Canvas canvas);
}
