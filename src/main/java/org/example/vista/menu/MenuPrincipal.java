package org.example.vista.menu;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.vista.config.ConstantesUI;

import java.util.function.IntConsumer;

public class MenuPrincipal {
    private final Stage stage;
    private final IntConsumer iniciarJuego;
    private final Runnable salir;

    private final MenuModel model = new MenuModel();
    private final MenuRenderer renderer = new MenuRenderer(model);
    private final MenuAnimator animator = new MenuAnimator();

    public MenuPrincipal(Stage stage, IntConsumer iniciarJuego, Runnable salir) {
        this.stage = stage;
        this.iniciarJuego = iniciarJuego;
        this.salir = salir;
    }

    public void mostrarMenu() {
        var root  = renderer.buildRoot();
        var scene = new Scene(root, ConstantesUI.MENU_WIDTH, ConstantesUI.MENU_HEIGHT);

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP   -> { model.prev(); renderer.opcionesMenu(); }
                case DOWN -> { model.next(); renderer.opcionesMenu(); }
                case ENTER -> ejecutarSeleccion();
                default -> {}
            }
        });

        stage.setScene(scene);
        stage.setTitle(ConstantesUI.WINDOW_TITLE);
        stage.show();

        animator.animateIn(renderer);
    }

    private void ejecutarSeleccion() {
        switch (model.seleccion()) {
            case 0 -> iniciarJuego.accept(1);
            case 1 -> iniciarJuego.accept(2);
            case 2 -> salir.run();
        }
    }
}
