package org.example.vista.core;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.example.modelo.juego.InputEstado;
import org.example.modelo.juego.JuegoConfig;
import org.example.modelo.juego.MotorJuego;
import org.example.modelo.juego.Nivel;
import org.example.vista.*;
import org.example.vista.config.ConstantesUI;
import org.example.vista.input.TecladoAdapter;
import org.example.vista.render.OverlayRenderer;
import org.example.vista.render.RenderizadorEstado;
import org.example.vista.render.RenderizadorSprites;

import java.util.List;

public final class ControladorJuego {

    private static final TecladoAdapter.TeclasJugador CONTROLES_J1 =
            new TecladoAdapter.TeclasJugador(KeyCode.W, KeyCode.S, KeyCode.A, KeyCode.D, KeyCode.SPACE);
    private static final TecladoAdapter.TeclasJugador CONTROLES_J2 =
            new TecladoAdapter.TeclasJugador(KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.ENTER);

    private final Stage stage;
    private final Runnable onGameEnd;
    private final Canvas canvas = new Canvas(JuegoConfig.NIVEL_DEFAULT_ANCHO, JuegoConfig.NIVEL_DEFAULT_ALTO);

    private MotorJuego motor;
    private final RenderizadorEstado renderizador;
    private final OverlayRenderer overlays = new OverlayRenderer();

    private final TecladoAdapter teclado = new TecladoAdapter();
    private AnimationTimer loop;

    private final MovimientoConColisiones mover = new MovimientoConColisiones();

    private List<TecladoAdapter.TeclasJugador> controlesActivos = List.of();

    public ControladorJuego(Stage stage, Runnable onGameEnd) {
        this(stage, onGameEnd, new RenderizadorSprites());
    }
    public ControladorJuego(Stage stage, Runnable onGameEnd, RenderizadorEstado renderizador) {
        this.stage = stage;
        this.onGameEnd = onGameEnd;
        this.renderizador = renderizador;
    }

    public void iniciar(MotorJuego motor, int cantidadJugadores) {
        this.motor = motor;
        this.controlesActivos = cantidadJugadores >= 2
                ? List.of(CONTROLES_J1, CONTROLES_J2)
                : List.of(CONTROLES_J1);

        Scene scene = prepararEscenaYMostrar();
        teclado.instalar(scene, this::terminar);

        final long[] lastNs = {0L};
        loop = new AnimationTimer() {
            @Override public void handle(long now) {
                if (lastNs[0] == 0L) lastNs[0] = now;
                double dt = (now - lastNs[0]) / 1_000_000_000.0; // ns → s
                lastNs[0] = now;
                tickYRender(dt);
            }
        };
        loop.start();

        stage.setOnCloseRequest(ev -> { if (loop != null) loop.stop(); });
    }

    private void tickYRender(double dt) {
        if (motor == null) return;
        Nivel nivel = motor.nivel();
        if (nivel == null) return;

        var jugadores = nivel.jugadores();
        if (!jugadores.isEmpty()) {
            boolean up = teclado.estaPresionada(CONTROLES_J1.up());
            boolean down = teclado.estaPresionada(CONTROLES_J1.down());
            boolean left = teclado.estaPresionada(CONTROLES_J1.left());
            boolean right = teclado.estaPresionada(CONTROLES_J1.right());
            mover.aplicarMovimiento(jugadores.getFirst(), dt, nivel, up, down, left, right);
        }
        if (jugadores.size() >= 2) {
            boolean up = teclado.estaPresionada(CONTROLES_J2.up());
            boolean down = teclado.estaPresionada(CONTROLES_J2.down());
            boolean left = teclado.estaPresionada(CONTROLES_J2.left());
            boolean right = teclado.estaPresionada(CONTROLES_J2.right());
            mover.aplicarMovimiento(jugadores.get(1), dt, nivel, up, down, left, right);
        }

        InputEstado in1 = teclado.construirInput(CONTROLES_J1);
        InputEstado in2 = controlesActivos.size() >= 2
                ? teclado.construirInput(CONTROLES_J2)
                : new InputEstado(false, false, false, false, false);

        motor.tick(System.currentTimeMillis(), in1, in2);

        renderizador.render(motor.estado(), canvas);

        if (motor.enVictoria()) {
            overlays.render(canvas, ConstantesUI.SPLASH_VICTORIA, Color.LIMEGREEN);
        } else if (motor.enDerrota()) {
            overlays.render(canvas, ConstantesUI.SPLASH_DERROTA, Color.ORANGERED);
        }

        if (motor.partidaFinalizada()) terminar();
    }

    private void terminar() {
        if (loop != null) loop.stop();
        onGameEnd.run();
    }

    private Scene prepararEscenaYMostrar() {
        var root = new StackPane(canvas);
        var scene = new Scene(root, canvas.getWidth(), canvas.getHeight());
        stage.setScene(scene);
        stage.setTitle(ConstantesUI.WINDOW_TITLE);
        stage.show();
        return scene;
    }
}
