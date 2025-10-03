package org.example.vista.input;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import org.example.modelo.juego.input.InputEstado;

import java.util.EnumSet;
import java.util.Set;

public final class TecladoAdapter {

    public record TeclasJugador(KeyCode up, KeyCode down, KeyCode left, KeyCode right, KeyCode shoot) {}

    private final Set<KeyCode> pressed = EnumSet.noneOf(KeyCode.class);

    public void instalar(Scene scene, Runnable onEscape) {
        pressed.clear();
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) { if (onEscape != null) onEscape.run(); return; }
            pressed.add(e.getCode());
        });
        scene.setOnKeyReleased(e -> pressed.remove(e.getCode()));
        scene.focusOwnerProperty().addListener((obs, oldV, newV) -> {
            if (newV == null) pressed.clear();
        });
    }

    public boolean estaPresionada(KeyCode code) { return pressed.contains(code); }

    public InputEstado construirInput(TeclasJugador t) {
        boolean u = pressed.contains(t.up());
        boolean d = pressed.contains(t.down());
        boolean l = pressed.contains(t.left());
        boolean r = pressed.contains(t.right());
        boolean s = pressed.contains(t.shoot());
        return new InputEstado(u, d, l, r, s);
    }
}
