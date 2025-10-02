package org.example.modelo.niveles;

import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.juego.Nivel;
import org.example.modelo.juego.NivelData;
import org.example.modelo.juego.Spawner;
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

            int ancho = parseIntAttr(eLevel, "width", 800);
            int alto  = parseIntAttr(eLevel, "height", 600);
            boolean coopXml = false;

            Pos p1 = new Pos(100, 500);
            Pos p2 = new Pos(200, 500);
            NodeList playersNL = eLevel.getElementsByTagName("players");
            if (playersNL.getLength() > 0) {
                Element ePlayers = (Element) playersNL.item(0);
                NodeList lista = ePlayers.getElementsByTagName("player");
                for (int i = 0; i < lista.getLength(); i++) {
                    Element p = (Element) lista.item(i);
                    String id = p.getAttribute("id");
                    double x = parseDoubleAttr(p, "x", 0);
                    double y = parseDoubleAttr(p, "y", 0);
                    if (id != null && id.endsWith("1")) p1 = new Pos(x, y);
                    else if (id != null && id.endsWith("2")) p2 = new Pos(x, y);
                }
            }

            List<EnemigoParse> enemigos = new ArrayList<>();
            NodeList enemiesNL = eLevel.getElementsByTagName("enemies");
            if (enemiesNL.getLength() > 0) {
                Element eEnemies = (Element) enemiesNL.item(0);
                NodeList lista = eEnemies.getElementsByTagName("enemy");
                for (int i = 0; i < lista.getLength(); i++) {
                    Element e = (Element) lista.item(i);
                    String tipoEn = e.getAttribute("type"); // fastEnemy / regularEnemy / heavyEnemy / powerfulEnemy
                    double x = parseDoubleAttr(e, "x", 0);
                    double y = parseDoubleAttr(e, "y", 0);
                    // FIX: usar tipoEn directamente (Spawner.mapTipo ya lo entiende)
                    enemigos.add(new EnemigoParse(tipoEn, x, y));
                }
            }

            List<BloqueParse> bloques = new ArrayList<>();
            NodeList staticsNL = eLevel.getElementsByTagName("staticObjects");
            if (staticsNL.getLength() > 0) {
                Element eStatics = (Element) staticsNL.item(0);
                NodeList lista = eStatics.getElementsByTagName("staticObject");
                for (int i = 0; i < lista.getLength(); i++) {
                    Element b = (Element) lista.item(i);
                    String tipoEn = b.getAttribute("type");
                    String tipoEs = mapBlockType(tipoEn);
                    double x = parseDoubleAttr(b, "x", 0);
                    double y = parseDoubleAttr(b, "y", 0);
                    bloques.add(new BloqueParse(tipoEs, x, y));
                }
            }

            Rect zonaSpawn = new Rect(390, 0, 20, 20);

            NivelData data = new NivelData(coopXml);
            data.setAncho(ancho);
            data.setAlto(alto);
            data.setJugador1(p1.x, p1.y);
            data.setJugador2(p2.x, p2.y);
            data.setZonaSpawn(zonaSpawn.x, zonaSpawn.y, zonaSpawn.w, zonaSpawn.h);
            for (BloqueParse b : bloques)  data.addBloque(b.tipo, b.x, b.y);
            for (EnemigoParse e : enemigos) data.addEnemigo(e.tipo, e.x, e.y);

            return data;

        } catch (Exception e) {
            throw new RuntimeException("Error al cargar/parsear XML de nivel (levelConfig/level)", e);
        }
    }

    public Nivel crearNivelDesdeXml(InputStream xml, boolean coopOverride) {
        NivelData data = cargar(xml);
        data.setCoop(coopOverride);
        Rectangulo mundo = new Rectangulo(0, 0, data.ancho(), data.alto());
        // FIX: tu Spawner actual no acepta Rectangulo en el ctor
        Spawner spawner = new Spawner();
        Nivel nivel = new Nivel(mundo, spawner);
        spawner.cargarPendientes(data.enemigos());
        nivel.crearMundo(data);
        return nivel;
    }

    private static String mapBlockType(String en) {
        if (en == null) return "LADRILLO";
        return switch (en) {
            case "brickBlock"  -> "LADRILLO";
            case "steelBlock"  -> "ACERO";
            case "waterBlock"  -> "AGUA";
            case "forestBlock" -> "BOSQUE";
            case "baseBlock"   -> "BASE";
            default -> "LADRILLO";
        };
    }

    private static int parseIntAttr(Element e, String name, int def) {
        String v = e.getAttribute(name);
        return (v == null || v.isBlank()) ? def : Integer.parseInt(v.trim());
    }

    private static double parseDoubleAttr(Element e, String name, double def) {
        String v = e.getAttribute(name);
        return (v == null || v.isBlank()) ? def : Double.parseDouble(v.trim());
    }

    public NivelData cargar(String resourcePath) {
        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("Recurso no encontrado en classpath: " + resourcePath);
            }
            return cargar(in); // reutilizamos tu método existente
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar recurso XML: " + resourcePath, e);
        }
    }

    public java.util.List<NivelData> cargarLista(java.util.List<String> rutas) {
        java.util.ArrayList<NivelData> res = new java.util.ArrayList<>();
        for (String r : rutas) {
            res.add(cargar(r));
        }
        return java.util.List.copyOf(res);
    }

    private static final class Pos  { final double x, y; Pos(double x, double y){ this.x=x; this.y=y; } }
    private static final class Rect { final double x, y, w, h; Rect(double x, double y, double w, double h){ this.x=x; this.y=y; this.w=w; this.h=h; } }
    private static final class BloqueParse  { final String tipo; final double x, y; BloqueParse(String t, double x, double y){ this.tipo=t; this.x=x; this.y=y; } }
    private static final class EnemigoParse { final String tipo; final double x, y; EnemigoParse(String t, double x, double y){ this.tipo=t; this.x=x; this.y=y; } }
}
