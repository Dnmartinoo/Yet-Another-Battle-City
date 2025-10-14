package org.example.vista.core;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.vista.audio.AdaptadorDeSonido;
import org.example.vista.audio.ManagerSonido;
import org.example.modelo.juego.core.MotorJuego;
import org.example.vista.campania.CampaniaFactory;
import org.example.vista.config.ConstantesUI;
import org.example.vista.menu.MenuPrincipal;

public class Interfaz {
    private final Stage stage;
    private ControladorJuego controlador;
    CampaniaFactory factory = new CampaniaFactory(ConstantesUI.XSD_NIVEL,  new AdaptadorDeSonido(ManagerSonido.get()));
    AudioInitializer audio = new AudioInitializer(ManagerSonido.get());

    public Interfaz(Stage stage) {
        this.stage = stage;
        this.stage.getIcons().add(new Image(ConstantesUI.ICONO_APP));
    }

    public void comenzar() {
        audio.inicializar();
        mostrarMenu();
    }

    void mostrarMenu() {
        MenuPrincipal menu = new MenuPrincipal(stage, this::iniciarJuego, this::salir);
        menu.mostrarMenu();
    }

    void iniciarJuego(int cantJugadores) {
        boolean coop = (cantJugadores == 2);
        MotorJuego motor = factory.crearMotorCampania(coop, ConstantesUI.NIVELES_XML);

        this.controlador = new ControladorJuego(stage, this::mostrarMenu);
        this.controlador.iniciar(motor, cantJugadores);
    }

    void salir() {
        stage.close();
    }
}
