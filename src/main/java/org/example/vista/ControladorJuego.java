// src/main/java/org/example/vista/ControladorJuego.java
package org.example.vista;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.InputEstado;
import org.example.modelo.juego.MotorJuego;

import java.util.HashSet;
import java.util.Set;

public final class ControladorJuego {

    private final Stage stage;
    private final Runnable onGameEnd;
    private final Canvas canvas = new Canvas(800, 600);

    // ---- Modelo ----
    private MotorJuego motor;

    // ---- Render ----
    private final Renderizador renderizador;

    // ---- Loop / input ----
    private AnimationTimer loop;
    private final Set<KeyCode> pressed = new HashSet<>();

    // Inyectá un renderizador desde afuera si querés testear o cambiar tema
    public ControladorJuego(Stage stage, Runnable onGameEnd) {
        this(stage, onGameEnd, new RenderizadorJavaFX());
    }

    public ControladorJuego(Stage stage, Runnable onGameEnd, Renderizador renderizador) {
        this.stage = stage;
        this.onGameEnd = onGameEnd;
        this.renderizador = renderizador;
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

                // =========================
                // J1: WASD + SPACE (disparo)
                // =========================
                if (!jugadores.isEmpty()) {
                    var j1 = jugadores.get(0);
                    boolean movio1 = false;
                    if (pressed.contains(KeyCode.W)) { j1.moverArriba();    movio1 = true; }
                    if (pressed.contains(KeyCode.S)) { j1.moverAbajo();     movio1 = true; }
                    if (pressed.contains(KeyCode.A)) { j1.moverIzquierda(); movio1 = true; }
                    if (pressed.contains(KeyCode.D)) { j1.moverDerecha();   movio1 = true; }
                    if (!movio1) j1.detener();

                    var vel = j1.velocidad().por(dt);

                    // Eje X
                    if (vel.x() != 0) {
                        var nextHitboxX = j1.hitbox().trasladado(new Vector(vel.x(), 0));
                        if (!nivel.colisionaConBloqueSolido(nextHitboxX)) {
                            j1.setPosicion(j1.posicion().mas(new Vector(vel.x(), 0)));
                        }
                    }
                    // Eje Y
                    if (vel.y() != 0) {
                        var nextHitboxY = j1.hitbox().trasladado(new Vector(0, vel.y()));
                        if (!nivel.colisionaConBloqueSolido(nextHitboxY)) {
                            j1.setPosicion(j1.posicion().mas(new Vector(0, vel.y())));
                        }
                    }
                }

                // =========================
                // J2: Flechas + ENTER (disparo)
                // =========================
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
                        var nextHitboxX = j2.hitbox().trasladado(new Vector(vel.x(), 0));
                        if (!nivel.colisionaConBloqueSolido(nextHitboxX)) {
                            j2.setPosicion(j2.posicion().mas(new Vector(vel.x(), 0)));
                        }
                    }
                    // Eje Y
                    if (vel.y() != 0) {
                        var nextHitboxY = j2.hitbox().trasladado(new Vector(0, vel.y()));
                        if (!nivel.colisionaConBloqueSolido(nextHitboxY)) {
                            j2.setPosicion(j2.posicion().mas(new Vector(0, vel.y())));
                        }
                    }
                }

                // =========================
                // Construir InputEstado y tick del modelo
                // =========================
                boolean u1 = pressed.contains(KeyCode.W);
                boolean d1 = pressed.contains(KeyCode.S);
                boolean l1 = pressed.contains(KeyCode.A);
                boolean r1 = pressed.contains(KeyCode.D);
                boolean shoot1 = pressed.contains(KeyCode.SPACE);

                boolean u2 = pressed.contains(KeyCode.UP);
                boolean d2 = pressed.contains(KeyCode.DOWN);
                boolean l2 = pressed.contains(KeyCode.LEFT);
                boolean r2 = pressed.contains(KeyCode.RIGHT);
                boolean shoot2 = pressed.contains(KeyCode.ENTER);

                var in1 = new InputEstado(u1, d1, l1, r1, shoot1);
                var in2 = new InputEstado(u2, d2, l2, r2, shoot2);

                motor.tick(ahoraMs, in1, in2);

                // =========================
                // Render
                // =========================
                renderizador.render(motor.nivel(), canvas);
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
}
