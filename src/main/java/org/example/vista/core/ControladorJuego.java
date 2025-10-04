package org.example.vista.core;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.example.modelo.juego.input.InputEstado;
import org.example.modelo.juego.config.JuegoConfig;
import org.example.modelo.juego.core.MotorJuego;
import org.example.modelo.juego.core.Nivel;
import org.example.vista.MovimientoConColisiones;
import org.example.vista.config.ConstantesUI;
import org.example.vista.input.TecladoAdapter;
import org.example.vista.render.OverlayRenderer;
import org.example.vista.render.RenderizadorEstado;

import java.util.List;

public class ControladorJuego {

    private final Stage stage;
    private final Runnable onGameEnd;
    private final Canvas canvas;
    private final RenderizadorEstado renderizador;
    private final OverlayRenderer overlays = new OverlayRenderer();
    private final TecladoAdapter teclado = new TecladoAdapter();
    private final MovimientoConColisiones mover = new MovimientoConColisiones();

    private AnimationTimer loop;
    private MotorJuego motor;
    private List<TecladoAdapter.TeclasJugador> controlesActivos;

    public ControladorJuego(Stage stage, Runnable onGameEnd, RenderizadorEstado renderizador) {
        this.stage = stage;
        this.onGameEnd = onGameEnd;
        this.renderizador = renderizador;
        this.canvas = new Canvas(JuegoConfig.NIVEL_DEFAULT_ANCHO, JuegoConfig.NIVEL_DEFAULT_ALTO);
    }

    public ControladorJuego(Stage stage, Runnable onGameEnd) {
        this(stage, onGameEnd, new org.example.vista.render.RenderizadorSprites());
    }

    public void iniciar(MotorJuego motor, int cantidadJugadores) {
        this.motor = motor;
        this.controlesActivos = crearControles(cantidadJugadores);

        Scene scene = prepararEscenaYMostrar();
        teclado.instalar(scene, this::terminar);

        final long[] lastNs = {0L};
        loop = new AnimationTimer() {
            @Override public void handle(long now) {
                if (lastNs[0] == 0L) lastNs[0] = now;
                double dt = (now - lastNs[0]) / 1_000_000_000.0;
                lastNs[0] = now;
                tickYRender(dt, System.currentTimeMillis());
            }
        };
        loop.start();

        stage.setOnCloseRequest(ev -> { if (loop != null) loop.stop(); });
    }

    private List<TecladoAdapter.TeclasJugador> crearControles(int cantidadJugadores) {
        var j1 = new TecladoAdapter.TeclasJugador(KeyCode.W, KeyCode.S, KeyCode.A, KeyCode.D, KeyCode.SPACE);
        var j2 = new TecladoAdapter.TeclasJugador(KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.ENTER);
        return (cantidadJugadores >= 2) ? List.of(j1, j2) : List.of(j1);
    }

    private void tickYRender(double dt, long ahoraMs) {
        if (motor == null) return;
        Nivel nivel = motor.nivel();
        if (nivel == null) return;

        var jugadores = nivel.jugadores();
        if (!jugadores.isEmpty()) aplicarControles(jugadores.getFirst(), controlesActivos.get(0), dt, nivel);
        if (jugadores.size() >= 2) aplicarControles(jugadores.get(1), controlesActivos.get(1), dt, nivel);

        InputEstado in1 = teclado.construirInput(controlesActivos.get(0));
        InputEstado in2 = (controlesActivos.size() >= 2)
                ? teclado.construirInput(controlesActivos.get(1))
                : new InputEstado(false, false, false, false, false);

        motor.tick(ahoraMs, in1, in2);

        renderizador.render(motor.estado(), canvas);

        if (motor.enVictoria()) overlays.render(canvas, ConstantesUI.SPLASH_VICTORIA, Color.LIMEGREEN);
        else if (motor.enDerrota()) overlays.render(canvas, ConstantesUI.SPLASH_DERROTA, Color.ORANGERED);

        if (motor.partidaFinalizada()) terminar();
    }

    private void aplicarControles(org.example.modelo.personajes.Jugador jugador,
                                  TecladoAdapter.TeclasJugador teclas,
                                  double dt,
                                  Nivel nivel) {
        boolean up = teclado.estaPresionada(teclas.up());
        boolean down = teclado.estaPresionada(teclas.down());
        boolean left = teclado.estaPresionada(teclas.left());
        boolean right = teclado.estaPresionada(teclas.right());
        mover.aplicarMovimiento(jugador, dt, nivel, up, down, left, right);
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
