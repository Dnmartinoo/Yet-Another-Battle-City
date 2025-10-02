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
        scene.focusOwnerProperty().addListener((obs, oldV, newV) -> {
            if (newV == null) pressed.clear();
        });

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

                    // --- RESOLUCIÓN POR EJES CON COLISIÓN ---
                    var vel = j1.velocidad().por(dt);

                    // Eje X
                    if (vel.x() != 0) {
                        var nextHitboxX = j1.hitbox().trasladado(new org.example.modelo.fisica.Vector(vel.x(), 0));
                        if (!nivel.colisionaConBloqueSolido(nextHitboxX)) {
                            j1.setPosicion(j1.posicion().mas(new org.example.modelo.fisica.Vector(vel.x(), 0)));
                        }
                    }
                    // Eje Y
                    if (vel.y() != 0) {
                        var nextHitboxY = j1.hitbox().trasladado(new org.example.modelo.fisica.Vector(0, vel.y()));
                        if (!nivel.colisionaConBloqueSolido(nextHitboxY)) {
                            j1.setPosicion(j1.posicion().mas(new org.example.modelo.fisica.Vector(0, vel.y())));
                        }
                    }

                    scene.setOnKeyPressed(e -> {
                        if (e.getCode() == KeyCode.ESCAPE) { terminar(); return; }
                        pressed.add(e.getCode());

                        if (!nivel.jugadores().isEmpty()) {
                            if (e.getCode() == KeyCode.SPACE) {
                                nivel.jugadores().get(0).disparar();
                            }
                        }
                    });

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

                    var vel = j2.velocidad().por(dt);

                    // Eje X
                    if (vel.x() != 0) {
                        var nextHitboxX = j2.hitbox().trasladado(new org.example.modelo.fisica.Vector(vel.x(), 0));
                        if (!nivel.colisionaConBloqueSolido(nextHitboxX)) {
                            j2.setPosicion(j2.posicion().mas(new org.example.modelo.fisica.Vector(vel.x(), 0)));
                        }
                    }
                    // Eje Y
                    if (vel.y() != 0) {
                        var nextHitboxY = j2.hitbox().trasladado(new org.example.modelo.fisica.Vector(0, vel.y()));
                        if (!nivel.colisionaConBloqueSolido(nextHitboxY)) {
                            j2.setPosicion(j2.posicion().mas(new org.example.modelo.fisica.Vector(0, vel.y())));
                        }
                    }

                    scene.setOnKeyPressed(e -> {
                        if (e.getCode() == KeyCode.ESCAPE) { terminar(); return; }
                        pressed.add(e.getCode());

                        if (!nivel.jugadores().isEmpty()) {
                            if (e.getCode() == KeyCode.ENTER) {
                                nivel.jugadores().get(1).disparar();
                            }
                        }
                    });

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
        onGameEnd.run();
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
// BLOQUES
        for (var b : nivel.bloques()) {
            if (b instanceof org.example.modelo.fisica.Cuerpo c) {
                var hb = c.hitbox();
                g.setFill(Color.DARKGRAY);
                g.fillRect(hb.x(), hb.y(), hb.w(), hb.h());
            }
        }

// BASE destacada (si también es Cuerpo por el wrapper)
        if (nivel.base() instanceof org.example.modelo.fisica.Cuerpo c) {
            var hb = c.hitbox();
            g.setFill(Color.GOLD);
            g.fillRect(hb.x(), hb.y(), hb.w(), hb.h());
        }

        // BALAS (blancas)
        g.setFill(Color.WHITESMOKE);
        for (var p : motor.nivel().proyectiles()) {
            var hb = p.hitbox();
            g.fillRect(hb.x(), hb.y(), hb.w(), hb.h());
        }


    }
}
