package org.example.vista.campania;

import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.juego.MotorJuego;
import org.example.modelo.juego.Nivel;
import org.example.modelo.juego.NivelData;
import org.example.modelo.juego.Spawner;
import org.example.modelo.niveles.XmlNivelLoader;
import org.example.vista.config.ConstantesUI;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class CampaniaFactory {
    private CampaniaFactory() {}

    public static MotorJuego crearMotorCampania(boolean coop, List<String> rutasXml) {
        MotorJuego motor = new MotorJuego();

        XmlNivelLoader loader = crearLoaderSeguro(ConstantesUI.XSD_NIVEL);
        List<NivelData> nivelesData = cargarNiveles(loader, rutasXml, coop);

        MotorJuego.NivelFactory factory = (data) -> {
            Rectangulo bounds = new Rectangulo(0, 0, data.ancho(), data.alto());
            Spawner spawner   = new Spawner();
            return new Nivel(bounds, spawner);
        };

        motor.configurarCampaña(nivelesData, factory);
        return motor;
    }

    private static XmlNivelLoader crearLoaderSeguro(String xsdPath) {
        try (InputStream xsd = CampaniaFactory.class.getResourceAsStream(xsdPath)) {
            return (xsd != null) ? new XmlNivelLoader(xsd) : new XmlNivelLoader();
        } catch (Exception e) {
            return new XmlNivelLoader(); // fallback robusto
        }
    }

    private static List<NivelData> cargarNiveles(XmlNivelLoader loader, List<String> rutas, boolean coop) {
        List<NivelData> out = new ArrayList<>(rutas.size());
        for (String ruta : rutas) {
            try (InputStream xml = CampaniaFactory.class.getResourceAsStream(ruta)) {
                if (xml == null) throw new IllegalStateException("No encontré: " + ruta);
                NivelData nd = loader.cargar(xml);
                nd.setCoop(coop);
                out.add(nd);
            } catch (Exception ex) {
                throw new RuntimeException("Falló la carga del nivel desde " + ruta, ex);
            }
        }
        return out;
    }
}
