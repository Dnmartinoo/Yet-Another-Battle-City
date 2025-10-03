package org.example.modelo;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.vista.core.Interfaz;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        new Interfaz(stage).comenzar();

    }

    public static void main(String[] args) {
        launch();
    }
}
