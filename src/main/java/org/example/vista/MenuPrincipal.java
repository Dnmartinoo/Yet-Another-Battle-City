package org.example.vista;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

        VBox contenedor = new VBox(50, titulo, menuBox);
        contenedor.setAlignment(Pos.CENTER);

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
            }
        });

        stage.setScene(scene);
        stage.setTitle("BATTLE CITY");
        stage.show();


        menuBox.setTranslateY(400);
        TranslateTransition tt = new TranslateTransition(Duration.seconds(2.5), menuBox);
        tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);
        tt.play();
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
}
