package org.example.vista;

import javafx.stage.Stage;

public class Interfaz {
    private final Stage stage;
    private ControladorJuego controlador;

    public Interfaz(Stage stage) {
        this.stage = stage;
    }

    public void comenzar() {
        mostrarMenu();
    }

    void mostrarMenu() {
        // Pasamos esta interfaz como "coordinador"
        MenuPrincipal menu = new MenuPrincipal(stage, this::iniciarJuego, this::salir);
        menu.mostrarMenu();

    }

    void iniciarJuego(int cantJugadores) {
        controlador = new ControladorJuego(stage, this::mostrarMenu);
        controlador.iniciar(cantJugadores);
    }

    void salir() {
        stage.close(); // o Platform.exit()
    }
}
