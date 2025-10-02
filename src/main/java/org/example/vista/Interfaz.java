package org.example.vista;

import javafx.stage.Stage;
import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.juego.*;
import org.example.modelo.niveles.XmlNivelLoader;

// ✅ si aún no tenés XML, usá un creador de prueba
// import org.example.modelo.juego.CreadorMundoDePrueba;

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
        boolean coop = cantJugadores == 1;
        Nivel nivel = CrearNivel.crearNivelDemo(coop);


        motor.cargarNivel(nivel);

        controlador = new ControladorJuego(stage, this::mostrarMenu);
        controlador.iniciar(motor, cantJugadores);
    }


    void salir() {
        stage.close();
    }



    private Nivel crearNivelMock(int cantJugadores) {
        boolean coop = (cantJugadores == 2);

        try (var xml = getClass().getResourceAsStream("/niveles/Level0.xml");
             var xsd = getClass().getResourceAsStream("/niveles/schema/levelConfig.xsd")) {

            if (xml == null) {
                throw new IllegalStateException("No encontré /niveles/Level0.xml en resources");
            }

            // 1) Cargar datos del nivel desde XML (con XSD si está disponible)
            XmlNivelLoader loader = (xsd != null) ? new XmlNivelLoader(xsd) : new XmlNivelLoader();
            NivelData data = loader.cargar(xml);

            // 2) Ajustar coop según la selección del menú
            // (si tu NivelData no tiene setCoop, agregalo; o usá un constructor con 'coop')
            data.setCoop(coop);

            // 3) Crear mundo y spawner a partir de los datos del XML
            Rectangulo mundo = new Rectangulo(0, 0, data.ancho(), data.alto());
            Rectangulo zonaSpawn = new Rectangulo(data.spawnX(), data.spawnY(), data.spawnW(), data.spawnH());
            Spawner spawner = new Spawner();

            // 4) Construir el nivel y poblarlo con data
            Nivel nivel = new Nivel(mundo, spawner);
            nivel.crearMundo(data);

            return nivel;

        } catch (Exception e) {
            throw new RuntimeException("Falló la carga del nivel desde XML", e);
        }
    }


}
