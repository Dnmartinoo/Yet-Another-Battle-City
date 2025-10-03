package org.example.vista;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.function.IntConsumer;

public class MenuPrincipal {
    private final Stage stage;
    private final IntConsumer iniciarJuego;
    private final Runnable salir;

    private final String[] opciones = {"1 JUGADOR", "2 JUGADORES", "SALIR"};
    private int seleccion = 0;

    private VBox menuBox;
    private ImageView selector;

    // --- Logo ---
    private ImageView logoView;

    public MenuPrincipal(Stage stage, IntConsumer iniciarJuego, Runnable salir) {
        this.stage = stage;
        this.iniciarJuego = iniciarJuego;
        this.salir = salir;
    }

    public void mostrarMenu() {
        Label titulo = new Label("BATTLE CITY");
        titulo.setTextFill(Color.ORANGE);
        titulo.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 40));

        selector = new ImageView(ManagerSprites.get("player1_0"));
        selector.setFitWidth(20);
        selector.setFitHeight(20);
        selector.setRotate(90);

        construirMenu();

        logoView = crearLogoImageView();

        VBox contenedor = new VBox(35);
        contenedor.setAlignment(Pos.CENTER);

        if (logoView != null) contenedor.getChildren().add(logoView);
        contenedor.getChildren().addAll(titulo, menuBox);

        StackPane root = new StackPane(contenedor);
        root.setStyle("-fx-background-color: black;");

        Scene scene = new Scene(root, 800, 600);

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP -> {
                    seleccion = (seleccion - 1 + opciones.length) % opciones.length;
                    updateSelectorPosition();
                }
                case DOWN -> {
                    seleccion = (seleccion + 1) % opciones.length;
                    updateSelectorPosition();
                }
                case ENTER -> ejecutarSeleccion();
                default -> {}
            }
        });

        stage.setScene(scene);
        stage.setTitle("BATTLE CITY");
        stage.show();

        animarAparicion(contenedor);
    }

    private void construirMenu() {
        menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < opciones.length; i++) {
            Label lbl = new Label(opciones[i]);
            lbl.setTextFill(Color.WHITE);
            lbl.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

            HBox fila = new HBox(10);
            fila.setAlignment(Pos.CENTER);

            if (i == seleccion) fila.getChildren().addAll(selector, lbl);
            else fila.getChildren().add(lbl);

            menuBox.getChildren().add(fila);
        }
    }

    private void updateSelectorPosition() {
        menuBox.getChildren().clear();
        for (int i = 0; i < opciones.length; i++) {
            Label lbl = new Label(opciones[i]);
            lbl.setTextFill(Color.WHITE);
            lbl.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

            HBox fila = new HBox(10);
            fila.setAlignment(Pos.CENTER);

            if (i == seleccion) fila.getChildren().addAll(selector, lbl);
            else fila.getChildren().add(lbl);

            menuBox.getChildren().add(fila);
        }
    }

    private void ejecutarSeleccion() {
        switch (seleccion) {
            case 0 -> iniciarJuego.accept(1);
            case 1 -> iniciarJuego.accept(2);
            case 2 -> salir.run();
        }
    }

    private ImageView crearLogoImageView() {
        try {
            Image img = new Image("/sprites/logo.png", true);
            if (img.isError()) return null;

            ImageView iv = new ImageView(img);
            iv.setPreserveRatio(true);
            iv.setFitWidth(240);
            iv.setSmooth(true);

            DropShadow ds = new DropShadow();
            ds.setRadius(18);
            ds.setSpread(0.15);
            ds.setColor(Color.rgb(255, 165, 0, 0.65));
            iv.setEffect(ds);

            iv.setOpacity(0);
            FadeTransition ft = new FadeTransition(Duration.seconds(1.4), iv);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.setInterpolator(Interpolator.EASE_OUT);
            ft.play();

            return iv;
        } catch (Exception __) {
            return null;
        }
    }

    private void animarAparicion(VBox contenedor) {
        menuBox.setTranslateY(400);
        TranslateTransition tt = new TranslateTransition(Duration.seconds(2.0), menuBox);
        tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);
        tt.play();
    }
}
