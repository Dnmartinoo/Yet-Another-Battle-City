// src/main/java/org/example/modelo/niveles/XmlNivelLoader.java
package org.example.modelo.niveles;

import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.juego.JuegoConfig;
import org.example.modelo.juego.Nivel;
import org.example.modelo.juego.NivelData;
import org.example.modelo.juego.Spawner;
import org.example.modelo.personajes.TipoPersonaje;
import org.w3c.dom.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlNivelLoader {

    private final Schema schema; // puede ser null

    public XmlNivelLoader() { this.schema = null; }

    public XmlNivelLoader(InputStream xsd) {
        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            this.schema = sf.newSchema(new StreamSource(xsd));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar el XSD", e);
        }
    }

    public NivelData cargar(InputStream xml) {
        try {
            var dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            if (schema != null) dbf.setSchema(schema);

            Document doc = dbf.newDocumentBuilder().parse(xml);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement(); // <levelConfig>

            NodeList levelNL = root.getElementsByTagName("level");
            if (levelNL.getLength() == 0) {
                throw new IllegalArgumentException("No se encontró <level> dentro de <levelConfig>");
            }
            Element eLevel = (Element) levelNL.item(0);

            int ancho = parseIntAttr(eLevel, "width",  JuegoConfig.NIVEL_DEFAULT_ANCHO);
            int alto  = parseIntAttr(eLevel, "height", JuegoConfig.NIVEL_DEFAULT_ALTO);

            Pos p1 = new Pos(JuegoConfig.J1_START_X, JuegoConfig.J1_START_Y);
            Pos p2 = new Pos(JuegoConfig.J2_START_X, JuegoConfig.J2_START_Y);
            boolean coopXml = false;

            NodeList playersNL = eLevel.getElementsByTagName("players");
            if (playersNL.getLength() > 0) {
                Element ePlayers = (Element) playersNL.item(0);
                NodeList lista = ePlayers.getElementsByTagName("player");
                coopXml = (lista.getLength() >= 2); // si hay 2+ jugadores, coop = true

                for (int i = 0; i < lista.getLength(); i++) {
                    Element p = (Element) lista.item(i);
                    String id = p.getAttribute("id");
                    double x = parseDoubleAttr(p, "x", JuegoConfig.PLAYER_DEFAULT_X);
                    double y = parseDoubleAttr(p, "y", JuegoConfig.PLAYER_DEFAULT_Y);
                    if (id != null && id.endsWith("1")) p1 = new Pos(x, y);
                    else if (id != null && id.endsWith("2")) p2 = new Pos(x, y);
                }
            }

            // enemies
            List<EnemigoParse> enemigos = new ArrayList<>();
            NodeList enemiesNL = eLevel.getElementsByTagName("enemies");
            if (enemiesNL.getLength() > 0) {
                Element eEnemies = (Element) enemiesNL.item(0);
                NodeList lista = eEnemies.getElementsByTagName("enemy");
                for (int i = 0; i < lista.getLength(); i++) {
                    Element e = (Element) lista.item(i);
                    String tipoStr = e.getAttribute("type");
                    double x = parseDoubleAttr(e, "x", JuegoConfig.ENEMY_DEFAULT_X);
                    double y = parseDoubleAttr(e, "y", JuegoConfig.ENEMY_DEFAULT_Y);
                    enemigos.add(new EnemigoParse(tipoStr, x, y));
                }
            }

            List<BloqueParse> bloques = new ArrayList<>();
            NodeList staticsNL = eLevel.getElementsByTagName("staticObjects");
            if (staticsNL.getLength() > 0) {
                Element eStatics = (Element) staticsNL.item(0);
                NodeList lista = eStatics.getElementsByTagName("staticObject");
                for (int i = 0; i < lista.getLength(); i++) {
                    Element b = (Element) lista.item(i);
                    String tipo = b.getAttribute("type");
                    double x = parseDoubleAttr(b, "x", JuegoConfig.BLOCK_DEFAULT_X);
                    double y = parseDoubleAttr(b, "y", JuegoConfig.BLOCK_DEFAULT_Y);
                    bloques.add(new BloqueParse(tipo, x, y));
                }
            }

            NivelData data = new NivelData(coopXml);
            data.setAncho(ancho);
            data.setAlto(alto);
            data.setJugador1(p1.x, p1.y);
            data.setJugador2(p2.x, p2.y);

            for (BloqueParse b : bloques)  data.addBloque(b.tipo, b.x, b.y);
            for (EnemigoParse e : enemigos) data.addEnemigo(tryValueOf(e.tipo), e.x, e.y);

            return data;

        } catch (Exception e) {
            throw new RuntimeException("Error al cargar/parsear XML de nivel (levelConfig/level)", e);
        }
    }

    public Nivel crearNivelDesdeXml(InputStream xml, boolean coopOverride) {
        NivelData data = cargar(xml);
        data.setCoop(coopOverride);
        Rectangulo mundo = new Rectangulo(0, 0, data.ancho(), data.alto());
        Spawner spawner = new Spawner();
        Nivel nivel = new Nivel(mundo, spawner);
        nivel.crearMundo(data);
        return nivel;
    }

    private static int parseIntAttr(Element e, String name, int def) {
        String v = e.getAttribute(name);
        return (v == null || v.isBlank()) ? def : Integer.parseInt(v.trim());
    }

    private static double parseDoubleAttr(Element e, String name, double def) {
        String v = e.getAttribute(name);
        return (v == null || v.isBlank()) ? def : Double.parseDouble(v.trim());
    }

    private static TipoPersonaje tryValueOf(String s) {
        if (s == null || s.isBlank()) return JuegoConfig.ENEMY_DEFAULT_TYPE;
        try {
            return TipoPersonaje.valueOf(s.trim());
        } catch (IllegalArgumentException ex) {
            return JuegoConfig.ENEMY_DEFAULT_TYPE;
        }
    }

    private static final class Pos  { final double x, y; Pos(double x, double y){ this.x=x; this.y=y; } }
    private static final class BloqueParse  { final String tipo; final double x, y; BloqueParse(String t, double x, double y){ this.tipo=t; this.x=x; this.y=y; } }
    private static final class EnemigoParse { final String tipo; final double x, y; EnemigoParse(String t, double x, double y){ this.tipo=t; this.x=x; this.y=y; } }
}
