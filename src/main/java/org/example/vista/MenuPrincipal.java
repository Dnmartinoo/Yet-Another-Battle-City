package org.example.vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.function.IntConsumer;

public class MenuPrincipal {
    private static final String ESTILO_BTN =
            "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold;";

    private final Stage stage;
    private final IntConsumer iniciarJuego;
    private final Runnable salir;

    public MenuPrincipal(Stage stage, IntConsumer iniciarJuego, Runnable salir) {
        this.stage = stage;
        this.iniciarJuego = iniciarJuego;
        this.salir = salir;
    }

    public void mostrarMenu() {
        Label titulo = new Label("BATTLE CITY");
        titulo.setTextFill(Color.WHITE);
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        Button unJugador = new Button("1 Jugador");
        unJugador.setStyle(ESTILO_BTN);
        hover(unJugador);
        unJugador.setOnAction(e -> iniciarJuego.accept(1));

        Button dosJugadores = new Button("2 Jugadores");
        dosJugadores.setStyle(ESTILO_BTN);
        hover(dosJugadores);
        dosJugadores.setOnAction(e -> iniciarJuego.accept(2));

        Button salirBtn = new Button("SALIR");
        salirBtn.setStyle(ESTILO_BTN);
        hover(salirBtn);
        salirBtn.setOnAction(e -> salir.run());

        VBox botones = new VBox(20, unJugador, dosJugadores, salirBtn);
        botones.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setTop(titulo);
        BorderPane.setAlignment(titulo, Pos.TOP_CENTER);
        BorderPane.setMargin(titulo, new Insets(30, 0, 0, 0));
        root.setCenter(botones);
        root.setBackground(Background.fill(Color.BLACK));

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("BATTLE CITY");
        stage.show();
    }

    private void hover(Button boton) {
        boton.setOnMouseEntered(e -> { boton.setScaleX(1.2); boton.setScaleY(1.2); });
        boton.setOnMouseExited(e -> { boton.setScaleX(1.0); boton.setScaleY(1.0); });
    }
}
