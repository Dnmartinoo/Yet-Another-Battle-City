package org.example.vista;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public final class ControladorJuego {
    private final Stage stage;
    private final Runnable onGameEnd; // callback para volver al menú
    private final Canvas canvas = new Canvas(800, 600);
    private AnimationTimer loop;

    public ControladorJuego(Stage stage, Runnable onGameEnd) {
        this.stage = stage;
        this.onGameEnd = onGameEnd;
    }

    public void iniciar(int cantidadJugadores) {
        // Montar escena de juego (placeholder)
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("BATTLE CITY - Jugando (placeholder)");
        stage.show();

        // Input mínimo: ESC para volver al menú
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ESCAPE -> terminar();
                default -> { /* ignorar */ }
            }
        });

        // Loop mínimo: solo repintar un cartel
        loop = new AnimationTimer() {
            @Override public void handle(long now) { dibujarPlaceholder(cantidadJugadores); }
        };
        loop.start();

        // Si se cierra la ventana, frenamos el loop
        stage.setOnCloseRequest(ev -> {
            if (loop != null) loop.stop();
        });
    }

    private void dibujarPlaceholder(int cantidadJugadores) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        g.setFill(Color.WHITE);
        g.setFont(Font.font("Verdana", 24));
        g.fillText("Jugando… (placeholder)", 240, 260);

        g.setFont(Font.font("Verdana", 16));
        g.fillText("Jugadores: " + cantidadJugadores + "   |   Presioná ESC para volver", 185, 310);
    }

    private void terminar() {
        if (loop != null) loop.stop();
        onGameEnd.run(); // volver al menú
    }
}
