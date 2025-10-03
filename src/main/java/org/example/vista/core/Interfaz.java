package org.example.vista.core;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.modelo.juego.MotorJuego;
import org.example.vista.campania.CampaniaFactory;
import org.example.vista.config.ConstantesUI;
import org.example.vista.menu.MenuPrincipal;

public class Interfaz {
    private final Stage stage;
    private ControladorJuego controlador;

    public Interfaz(Stage stage) {
        this.stage = stage;
        this.stage.getIcons().add(new Image(ConstantesUI.ICONO_APP));
    }

    public void comenzar() {
        AudioInitializer.inicializar();
        mostrarMenu();
    }

    void mostrarMenu() {
        MenuPrincipal menu = new MenuPrincipal(stage, this::iniciarJuego, this::salir);
        menu.mostrarMenu();
    }

    void iniciarJuego(int cantJugadores) {
        boolean coop = (cantJugadores == 2);
        MotorJuego motor = CampaniaFactory.crearMotorCampania(coop, ConstantesUI.NIVELES_XML);

        this.controlador = new ControladorJuego(stage, this::mostrarMenu);
        this.controlador.iniciar(motor, cantJugadores);
    }

    void salir() {
        stage.close();
    }
}
