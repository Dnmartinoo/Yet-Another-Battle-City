package org.example.vista.campania;

import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.juego.core.MotorJuego;
import org.example.modelo.juego.core.Nivel;
import org.example.modelo.juego.core.NivelData;
import org.example.modelo.juego.core.Spawner;
import org.example.modelo.niveles.XmlNivelLoader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CampaniaFactory {

    private final String xsdPath;

    public CampaniaFactory(String xsdPath) {
        this.xsdPath = xsdPath;
    }

    public MotorJuego crearMotorCampania(boolean coop, List<String> rutasXml) {
        MotorJuego motor = new MotorJuego();

        XmlNivelLoader loader = crearLoaderSeguro(xsdPath);
        List<NivelData> nivelesData = cargarNiveles(loader, rutasXml, coop);

        MotorJuego.NivelFactory factory = (data) -> {
            Rectangulo bounds = new Rectangulo(0, 0, data.ancho(), data.alto());
            Spawner spawner   = new Spawner();
            return new Nivel(bounds, spawner);
        };

        motor.configurarCampaña(nivelesData, factory);
        return motor;
    }

    private XmlNivelLoader crearLoaderSeguro(String xsdPath) {
        try (InputStream xsd = CampaniaFactory.class.getResourceAsStream(xsdPath)) {
            return (xsd != null) ? new XmlNivelLoader(xsd) : new XmlNivelLoader();
        } catch (Exception e) {
            return new XmlNivelLoader();
        }
    }

    private List<NivelData> cargarNiveles(XmlNivelLoader loader, List<String> rutas, boolean coop) {
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
