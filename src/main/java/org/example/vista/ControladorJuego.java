package org.example.vista;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
            if (e.getCode() == KeyCode.ESCAPE) { terminar(); return; }
            pressed.add(e.getCode());
        });
        scene.setOnKeyReleased(e -> pressed.remove(e.getCode()));
        // Si la ventana pierde foco, soltar todo para evitar teclas “pegadas”
        scene.focusOwnerProperty().addListener((obs, oldV, newV) -> {
            if (newV == null) pressed.clear();
        });

        // Bucle principal (usa dt en segundos)
        final long[] lastMs = {0L};
        loop = new AnimationTimer() {
            @Override public void handle(long now) {
                long ahoraMs = System.currentTimeMillis();
                if (lastMs[0] == 0L) lastMs[0] = ahoraMs;
                double dt = (ahoraMs - lastMs[0]) / 1000.0;
                lastMs[0] = ahoraMs;

                var nivel = motor.nivel();
                if (nivel == null) return;
                var jugadores = nivel.jugadores();

                // --- J1: WASD si existe ---
                if (!jugadores.isEmpty()) {
                    var j1 = jugadores.get(0);
                    boolean movio1 = false;
                    if (pressed.contains(KeyCode.W)) { j1.moverArriba();    movio1 = true; }
                    if (pressed.contains(KeyCode.S)) { j1.moverAbajo();     movio1 = true; }
                    if (pressed.contains(KeyCode.A)) { j1.moverIzquierda(); movio1 = true; }
                    if (pressed.contains(KeyCode.D)) { j1.moverDerecha();   movio1 = true; }
                    if (!movio1) j1.detener();
                    j1.setPosicion(j1.posicion().mas(j1.velocidad().por(dt)));
                }

                // --- J2: Flechas si existe ---
                if (jugadores.size() > 1) {
                    var j2 = jugadores.get(1);
                    boolean movio2 = false;
                    if (pressed.contains(KeyCode.UP))    { j2.moverArriba();    movio2 = true; }
                    if (pressed.contains(KeyCode.DOWN))  { j2.moverAbajo();     movio2 = true; }
                    if (pressed.contains(KeyCode.LEFT))  { j2.moverIzquierda(); movio2 = true; }
                    if (pressed.contains(KeyCode.RIGHT)) { j2.moverDerecha();   movio2 = true; }
                    if (!movio2) j2.detener();
                    j2.setPosicion(j2.posicion().mas(j2.velocidad().por(dt)));
                }
                motor.tick(ahoraMs, InputEstado.neutro(), InputEstado.neutro());
                dibujarNivel();

            }
        };
        loop.start();

        stage.setOnCloseRequest(ev -> {
            if (loop != null) loop.stop();
        });
    }

    // =========================================
    // Finalización del juego
    // =========================================
    public void terminarDesdeModelo() { terminar(); }

    private void terminar() {
        if (loop != null) loop.stop();
        onGameEnd.run(); // volver al menú
    }

    // =========================================
    // Render
    // =========================================
    private void dibujarNivel() {
        GraphicsContext g = canvas.getGraphicsContext2D();

        // fondo
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        var nivel = motor.nivel();
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

        // (Opcional) BASE/BLOQUES cuando también implementen Cuerpo
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
