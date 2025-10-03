package org.example.vista;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.input.InputEstado;
import org.example.modelo.juego.core.MotorJuego;

import java.util.HashSet;
import java.util.Set;

public final class ControladorJuego {

    private final Stage stage;
    private final Runnable onGameEnd;
    private final Canvas canvas = new Canvas(800, 600);

    private MotorJuego motor;

    private final RenderizadorEstado renderizador;


    private AnimationTimer loop;
    private final Set<KeyCode> pressed = new HashSet<>();


    public ControladorJuego(Stage stage, Runnable onGameEnd) {
        this(stage, onGameEnd, new RenderizadorSprites());
    }

    public ControladorJuego(Stage stage, Runnable onGameEnd, RenderizadorEstado renderizador) {
        this.stage = stage;
        this.onGameEnd = onGameEnd;
        this.renderizador = (RenderizadorEstado) renderizador;
    }


    public void iniciar(MotorJuego motor, int cantidadJugadores) {
        this.motor = motor;

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("BATTLE CITY - Jugando");
        stage.show();
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

                if (!jugadores.isEmpty()) {
                    var j1 = jugadores.get(0);
                    boolean movio1 = false;
                    if (pressed.contains(KeyCode.W)) { j1.moverArriba();    movio1 = true; }
                    if (pressed.contains(KeyCode.S)) { j1.moverAbajo();     movio1 = true; }
                    if (pressed.contains(KeyCode.A)) { j1.moverIzquierda(); movio1 = true; }
                    if (pressed.contains(KeyCode.D)) { j1.moverDerecha();   movio1 = true; }
                    if (!movio1) j1.detener();

                    var vel = j1.velocidad().por(dt);
                    if (vel.x() != 0) {
                        var nextHitboxX = j1.hitbox().trasladado(new Vector(vel.x(), 0));
                        if (!nivel.colisionaConBloqueSolido(nextHitboxX)) {
                            j1.setPosicion(j1.posicion().mas(new Vector(vel.x(), 0)));
                        }
                    }

                    if (vel.y() != 0) {
                        var nextHitboxY = j1.hitbox().trasladado(new Vector(0, vel.y()));
                        if (!nivel.colisionaConBloqueSolido(nextHitboxY)) {
                            j1.setPosicion(j1.posicion().mas(new Vector(0, vel.y())));
                        }
                    }
                }

                if (jugadores.size() > 1) {
                    var j2 = jugadores.get(1);
                    boolean movio2 = false;
                    if (pressed.contains(KeyCode.UP))    { j2.moverArriba();    movio2 = true; }
                    if (pressed.contains(KeyCode.DOWN))  { j2.moverAbajo();     movio2 = true; }
                    if (pressed.contains(KeyCode.LEFT))  { j2.moverIzquierda(); movio2 = true; }
                    if (pressed.contains(KeyCode.RIGHT)) { j2.moverDerecha();   movio2 = true; }
                    if (!movio2) j2.detener();

                    var vel = j2.velocidad().por(dt);
                    if (vel.x() != 0) {
                        var nextHitboxX = j2.hitbox().trasladado(new Vector(vel.x(), 0));
                        if (!nivel.colisionaConBloqueSolido(nextHitboxX)) {
                            j2.setPosicion(j2.posicion().mas(new Vector(vel.x(), 0)));
                        }
                    }
                    if (vel.y() != 0) {
                        var nextHitboxY = j2.hitbox().trasladado(new Vector(0, vel.y()));
                        if (!nivel.colisionaConBloqueSolido(nextHitboxY)) {
                            j2.setPosicion(j2.posicion().mas(new Vector(0, vel.y())));
                        }
                    }
                }
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
                renderizador.render(motor.estado(), canvas);
                var g = canvas.getGraphicsContext2D();
                if (motor.enVictoria()) {
                    g.setGlobalAlpha(0.65);
                    g.setFill(javafx.scene.paint.Color.BLACK);
                    g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    g.setGlobalAlpha(1.0);
                    g.setFill(javafx.scene.paint.Color.LIMEGREEN);
                    g.setFont(javafx.scene.text.Font.font("Consolas", 48));
                    g.fillText("¡VICTORIA!", canvas.getWidth()/2 - 140, canvas.getHeight()/2);
                }
                else if (motor.enDerrota()) {
                    g.setGlobalAlpha(0.65);
                    g.setFill(javafx.scene.paint.Color.BLACK);
                    g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    g.setGlobalAlpha(1.0);
                    g.setFill(javafx.scene.paint.Color.ORANGERED);
                    g.setFont(javafx.scene.text.Font.font("Consolas", 48));
                    g.fillText("DERROTA", canvas.getWidth()/2 - 120, canvas.getHeight()/2);
                }

                if (motor.partidaFinalizada()) {
                    terminar();
                }

            }
        };
        loop.start();

        stage.setOnCloseRequest(ev -> {
            if (loop != null) loop.stop();
        });
    }

    private void terminar() {
        if (loop != null) loop.stop();
        onGameEnd.run();
    }
}
