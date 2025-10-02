package org.example.vista;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.modelo.juego.InputEstado;
import org.example.modelo.juego.MotorJuego;

import java.util.HashSet;
import java.util.Set;

public final class ControladorJuego {

    private final Stage stage;
    private final Runnable onGameEnd; // callback para volver al menú
    private final Canvas canvas = new Canvas(800, 600);

    // ---- Modelo ----
    private MotorJuego motor;

    // ---- Loop / input ----
    private AnimationTimer loop;
    private final Set<KeyCode> pressed = new HashSet<>();

    public ControladorJuego(Stage stage, Runnable onGameEnd) {
        this.stage = stage;
        this.onGameEnd = onGameEnd;
    }

    // =========================================
    // Arranque de la escena de juego
    // =========================================
    public void iniciar(MotorJuego motor, int cantidadJugadores) {
        this.motor = motor;

        // Escena
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("BATTLE CITY - Jugando");
        stage.show();

        // Input continuo (teclas presionadas)
        pressed.clear();
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ESCAPE -> terminar();
                default -> pressed.add(e.getCode());
            }
        });
        scene.setOnKeyReleased(e -> pressed.remove(e.getCode()));

        // Bucle principal
        loop = new AnimationTimer() {
            @Override public void handle(long now) {
                // Construir InputEstado jugador 1 (WASD + SPACE)
                InputEstado j1 = new InputEstado(
                        pressed.contains(KeyCode.W),
                        pressed.contains(KeyCode.S),
                        pressed.contains(KeyCode.A),
                        pressed.contains(KeyCode.D),
                        pressed.contains(KeyCode.SPACE)
                );

                // Jugador 2 (flechas + ENTER) o neutro si no está habilitado
                InputEstado j2 = (cantidadJugadores == 2)
                        ? new InputEstado(
                        pressed.contains(KeyCode.UP),
                        pressed.contains(KeyCode.DOWN),
                        pressed.contains(KeyCode.LEFT),
                        pressed.contains(KeyCode.RIGHT),
                        pressed.contains(KeyCode.ENTER)
                )
                        : InputEstado.neutro();

                long ahoraMs = System.currentTimeMillis();
                motor.tick(ahoraMs, j1, j2);

                // TODO: reemplazar por render real (sprites). Por ahora, placeholder.
                dibujarNivel();

                // Fin de nivel: acá ajustá según tu API de EstadoNivel
                // Opción 1 (si implementás estaTerminado() correctamente en MotorJuego):
                // if (motor.estaTerminado()) terminar();

                // Opción 2 (si tu EstadoNivel expone banderas tipo esVictoria/esDerrota):
                // switch (motor.estado().tipo()) { case VICTORIA, DERROTA -> terminar(); default -> {} }
            }
        };
        loop.start();

        // Si se cierra la ventana, frenar el loop
        stage.setOnCloseRequest(ev -> {
            if (loop != null) loop.stop();
        });
    }

    // =========================================
    // Finalización del juego
    // =========================================
    /** Llamable desde listeners del modelo (p. ej., al detectar victoria/derrota). */
    public void terminarDesdeModelo() {
        terminar();
    }

    private void terminar() {
        if (loop != null) loop.stop();
        onGameEnd.run(); // volver al menú
    }

    // =========================================
    // Dibujo temporal (placeholder)
    // =========================================
    private void dibujarPlaceholder(int cantidadJugadores) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        g.setFill(Color.WHITE);
        g.setFont(Font.font("Verdana", 24));
        g.fillText("Jugando… (placeholder)", 240, 260);

        g.setFont(Font.font("Verdana", 16));
        g.fillText("Jugadores: " + cantidadJugadores + "   |   ESC = volver", 220, 310);
    }
    private void dibujarNivel() {
        var g = canvas.getGraphicsContext2D();

        // fondo
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        var nivel = motor.nivel();
        if (nivel == null) return;

        // ENEMIGOS (rojo)
        g.setFill(Color.CRIMSON);
        for (var e : nivel.enemigos()) {
            var hb = e.hitbox();             // Rectangulo de Tanque
            g.fillRect(hb.x(), hb.y(), hb.w(), hb.h()); // usa x()/y()/w()/h() o getters según tu clase
        }

        // JUGADORES (verde)
        g.setFill(Color.LIMEGREEN);
        for (var j : nivel.jugadores()) {
            var hb = j.hitbox();
            g.fillRect(hb.x(), hb.y(), hb.w(), hb.h());
            // anillo si invulnerable
            if (j.esInvulnerable()) {
                g.setStroke(Color.WHITE);
                g.setLineWidth(2);
                g.strokeRect(hb.x(), hb.y(), hb.w(), hb.h());
            }
        }

        // (Opcional) BASE y BLOQUES — solo si también implementan Cuerpo:
        // g.setFill(Color.DARKGRAY);
        // for (var b : nivel.bloques()) {
        //     var hb = b.hitbox();
        //     g.fillRect(hb.x(), hb.y(), hb.w(), hb.h());
        // }
        // if (nivel.base() != null) {
        //     var hb = nivel.base().hitbox();
        //     g.setFill(Color.GOLD);
        //     g.fillRect(hb.x(), hb.y(), hb.w(), hb.h());
        // }
    }


}
