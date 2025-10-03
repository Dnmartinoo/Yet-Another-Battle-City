
package org.example.vista;

import javafx.stage.Stage;
import org.example.audio.ManagerSonido;
import javafx.scene.image.Image;
import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.juego.MotorJuego;
import org.example.modelo.juego.Nivel;
import org.example.modelo.juego.NivelData;
import org.example.modelo.juego.Spawner;
import org.example.modelo.niveles.XmlNivelLoader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Interfaz {
    private final Stage stage;
    private ControladorJuego controlador;

    public Interfaz(Stage stage) {
        this.stage = stage;
        this.stage.getIcons().add(new Image("/sprites/logo.png"));
    }


    public void comenzar() {
        ManagerSonido.cargarEfecto("muerteTanque", "/sounds/efectos/bang.wav");
        ManagerSonido.cargarEfecto("derrota", "/sounds/efectos/explosion.wav");
        ManagerSonido.cargarEfecto("impactoBlindado", "/sounds/glass-cling.wav");
        ManagerSonido.cargarEfecto("impactoAcero", "/sounds/glass-cling.wav");
        ManagerSonido.cargarEfecto("disparar", "/sounds/efectos/laser-gun.wav");
        ManagerSonido.cargarEfecto("ladrilloRoto", "/sounds/efectos/wood-impact.wav");

        ManagerSonido.cargarMusica("/sounds/musica/tribe-drum-loop.wav");
        ManagerSonido.playMusica();
        mostrarMenu();
    }


    void mostrarMenu() {
        MenuPrincipal menu = new MenuPrincipal(stage, this::iniciarJuego, this::salir);
        menu.mostrarMenu();
    }

    void iniciarJuego(int cantJugadores) {
        boolean coop = (cantJugadores == 2);

        MotorJuego motor = new MotorJuego();

        var rutas = List.of(
                "/niveles/Level0.xml",
                "/niveles/Level2.xml",
                "/niveles/Level1.xml"
        );

        XmlNivelLoader loader;
        try (InputStream xsd = getClass().getResourceAsStream("/niveles/schema/levelConfig.xsd")) {
            loader = (xsd != null) ? new XmlNivelLoader(xsd) : new XmlNivelLoader();
        } catch (Exception e) {
            loader = new XmlNivelLoader();
        }

        List<NivelData> nivelesData = new ArrayList<>();
        for (String ruta : rutas) {
            try (InputStream xml = getClass().getResourceAsStream(ruta)) {
                if (xml == null) throw new IllegalStateException("No encontré: " + ruta);
                NivelData nd = loader.cargar(xml);
                nd.setCoop(coop);
                nivelesData.add(nd);
            } catch (Exception ex) {
                throw new RuntimeException("Falló la carga del nivel desde " + ruta, ex);
            }
        }

        MotorJuego.NivelFactory factory = (data) -> {
            Rectangulo bounds = new Rectangulo(0, 0, data.ancho(), data.alto());
            Spawner spawner   = new Spawner();
            return new Nivel(bounds, spawner);
        };

        motor.configurarCampaña(nivelesData, factory);

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
