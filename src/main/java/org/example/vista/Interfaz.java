package org.example.vista;

import javafx.stage.Stage;
import org.example.modelo.juego.MotorJuego;
import org.example.modelo.juego.Nivel;
import org.example.modelo.niveles.XmlNivelLoader;

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
        MenuPrincipal menu = new MenuPrincipal(stage, this::iniciarJuego, this::salir);
        menu.mostrarMenu();
    }

    void iniciarJuego(int cantJugadores) {
        MotorJuego motor = new MotorJuego();
        Nivel nivel = crearNivelDesdeXml(cantJugadores); // << usa el loader
        motor.cargarNivel(nivel);

        this.controlador = new ControladorJuego(stage, this::mostrarMenu);
        this.controlador.iniciar(motor, cantJugadores);
    }

    void salir() {
        stage.close();
    }


    private Nivel crearNivelDesdeXml(int cantJugadores) {
        boolean coop = (cantJugadores == 2);

        try (var xml = getClass().getResourceAsStream("/niveles/Level0.xml");
             var xsd = getClass().getResourceAsStream("/niveles/schema/levelConfig.xsd")) {

            if (xml == null) {
                throw new IllegalStateException("No encontré /niveles/Level0.xml en resources");
            }

            XmlNivelLoader loader = (xsd != null) ? new XmlNivelLoader(xsd) : new XmlNivelLoader();
            return loader.crearNivelDesdeXml(xml, coop);

        } catch (Exception e) {
            throw new RuntimeException("Falló la carga del nivel desde XML", e);
        }
    }
}
