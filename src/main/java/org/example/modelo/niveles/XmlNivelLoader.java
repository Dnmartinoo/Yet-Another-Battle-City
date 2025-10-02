package org.example.modelo.niveles;

import org.example.modelo.juego.NivelData;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlNivelLoader {

    private final Schema schema; // puede ser null

    /** Loader sin validación XSD */
    public XmlNivelLoader() {
        this.schema = null;
    }

    /** Loader con validación XSD */
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

            Element raiz = doc.getDocumentElement(); // <nivel>

            // Atributos del nivel
            int ancho = parseIntAttr(raiz, "ancho", 800);
            int alto  = parseIntAttr(raiz, "alto", 600);
            boolean coop = parseBoolAttr(raiz, "coop", false);

            // Spawner
            Rect zonaSpawn = leerSpawner(raiz);

            // Jugadores
            Pos p1 = new Pos(100, 500);
            Pos p2 = new Pos(200, 500);
            NodeList jugadores = raiz.getElementsByTagName("jugadores");
            if (jugadores.getLength() > 0) {
                Element eJugadores = (Element) jugadores.item(0);
                NodeList lista = eJugadores.getElementsByTagName("jugador");
                for (int i=0; i<lista.getLength(); i++) {
                    Element j = (Element) lista.item(i);
                    int id = parseIntAttr(j, "id", 1);
                    double x = parseDoubleAttr(j, "x", (id==1)?100:200);
                    double y = parseDoubleAttr(j, "y", 500);
                    if (id == 1) p1 = new Pos(x, y);
                    else if (id == 2) p2 = new Pos(x, y);
                }
            }

            // Bloques
            List<BloqueParse> bloques = new ArrayList<>();
            NodeList bloquesNL = raiz.getElementsByTagName("bloques");
            if (bloquesNL.getLength() > 0) {
                Element eBloques = (Element) bloquesNL.item(0);
                NodeList lista = eBloques.getElementsByTagName("bloque");
                for (int i=0; i<lista.getLength(); i++) {
                    Element b = (Element) lista.item(i);
                    String tipo = b.getAttribute("tipo"); // LADRILLO/ACERO/AGUA/BOSQUE/BASE...
                    double x = parseDoubleAttr(b, "x", 0);
                    double y = parseDoubleAttr(b, "y", 0);
                    bloques.add(new BloqueParse(tipo, x, y));
                }
            }

            // Enemigos
            List<EnemigoParse> enemigos = new ArrayList<>();
            NodeList enemigosNL = raiz.getElementsByTagName("enemigos");
            if (enemigosNL.getLength() > 0) {
                Element eEnemigos = (Element) enemigosNL.item(0);
                NodeList lista = eEnemigos.getElementsByTagName("enemigo");
                for (int i=0; i<lista.getLength(); i++) {
                    Element e = (Element) lista.item(i);
                    String tipo = e.getAttribute("tipo"); // BASICO/RAPIDO/POTENTE/BLINDADO
                    double x = parseDoubleAttr(e, "x", 0);
                    double y = parseDoubleAttr(e, "y", 0);
                    enemigos.add(new EnemigoParse(tipo, x, y));
                }
            }

            // ---- Mapear a tu NivelData ----
            // Requiere que NivelData tenga un constructor y setters (ver bloque al final)
            NivelData data = new NivelData(coop);
            data.setAncho(ancho);
            data.setAlto(alto);
            data.setJugador1(p1.x, p1.y);
            data.setJugador2(p2.x, p2.y);
            data.setZonaSpawn(zonaSpawn.x, zonaSpawn.y, zonaSpawn.w, zonaSpawn.h);
            for (BloqueParse b : bloques) data.addBloque(b.tipo, b.x, b.y);
            for (EnemigoParse e : enemigos) data.addEnemigo(e.tipo, e.x, e.y);

            return data;

        } catch (Exception e) {
            throw new RuntimeException("Error al cargar/parsear XML de nivel", e);
        }
    }

    // -------- helpers DOM --------
    private static int parseIntAttr(Element e, String name, int def){
        String v = e.getAttribute(name);
        return (v==null || v.isBlank()) ? def : Integer.parseInt(v.trim());
    }
    private static double parseDoubleAttr(Element e, String name, double def){
        String v = e.getAttribute(name);
        return (v==null || v.isBlank()) ? def : Double.parseDouble(v.trim());
    }
    private static boolean parseBoolAttr(Element e, String name, boolean def){
        String v = e.getAttribute(name);
        return (v==null || v.isBlank()) ? def : Boolean.parseBoolean(v.trim());
    }

    private static Rect leerSpawner(Element raiz){
        NodeList sp = raiz.getElementsByTagName("spawner");
        if (sp.getLength() == 0) return new Rect(390, 0, 20, 20); // default
        Element s = (Element) sp.item(0);
        double x = parseDoubleAttr(s, "x", 390);
        double y = parseDoubleAttr(s, "y", 0);
        double w = parseDoubleAttr(s, "w", 20);
        double h = parseDoubleAttr(s, "h", 20);
        return new Rect(x, y, w, h);
    }

    // -------- structs internas para staging --------
    private static final class Pos { final double x,y; Pos(double x,double y){this.x=x;this.y=y;} }
    private static final class Rect { final double x,y,w,h; Rect(double x,double y,double w,double h){this.x=x;this.y=y;this.w=w;this.h=h;} }
    private static final class BloqueParse { final String tipo; final double x,y; BloqueParse(String t,double x,double y){this.tipo=t;this.x=x;this.y=y;} }
    private static final class EnemigoParse { final String tipo; final double x,y; EnemigoParse(String t,double x,double y){this.tipo=t;this.x=x;this.y=y;} }
}
