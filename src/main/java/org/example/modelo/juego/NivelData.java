package org.example.modelo.juego;

import org.example.modelo.personajes.TipoPersonaje;

import java.util.ArrayList;
import java.util.List;

public class NivelData {

    // ----------------- Estado del nivel (configurable) -----------------
    private boolean coop;
    private int ancho;
    private int alto;

    private double jugador1X;
    private double jugador1Y;
    private double jugador2X;
    private double jugador2Y;

    // ----------------- DTOs inmutables -----------------
    public static final class BloqueDato {
        public final String tipo;
        public final double x, y;
        public BloqueDato(String t, double x, double y) {
            this.tipo = t;
            this.x = x;
            this.y = y;
        }
    }

    public static final class EnemigoDato {
        public final TipoPersonaje tipo;
        public final double x, y;
        public EnemigoDato(TipoPersonaje t, double x, double y) {
            this.tipo = t;
            this.x = x;
            this.y = y;
        }
    }

    private final List<BloqueDato> bloques = new ArrayList<>();
    private final List<EnemigoDato> enemigos = new ArrayList<>();

    // ----------------- Constructores -----------------
    public NivelData(boolean coop) {
        this.coop = coop;
        this.ancho = JuegoConfig.NIVEL_DEFAULT_ANCHO;
        this.alto  = JuegoConfig.NIVEL_DEFAULT_ALTO;

        this.jugador1X = JuegoConfig.J1_START_X;
        this.jugador1Y = JuegoConfig.J1_START_Y;
        this.jugador2X = JuegoConfig.J2_START_X;
        this.jugador2Y = JuegoConfig.J2_START_Y;
    }

    // ----------------- Setters  -----------------
    public void setCoop(boolean v) { this.coop = v; }

    public void setAncho(int v) { this.ancho = v; }

    public void setAlto(int v) { this.alto = v; }

    public void setJugador1(double x, double y) {
        this.jugador1X = x;
        this.jugador1Y = y;
    }

    public void setJugador2(double x, double y) {
        this.jugador2X = x;
        this.jugador2Y = y;
    }

    public void addBloque(String tipo, double x, double y) {
        bloques.add(new BloqueDato(tipo, x, y));
    }

    public void addEnemigo(TipoPersonaje tipo, double x, double y) {
        enemigos.add(new EnemigoDato(tipo, x, y));
    }

    // ----------------- Getters  -----------------
    public boolean coop() { return coop; }

    public int ancho() { return ancho; }
    public int alto()  { return alto; }

    public double jugador1X() { return jugador1X; }
    public double jugador1Y() { return jugador1Y; }
    public double jugador2X() { return jugador2X; }
    public double jugador2Y() { return jugador2Y; }

    public List<BloqueDato> bloques()   { return List.copyOf(bloques); }
    public List<EnemigoDato> enemigos() { return List.copyOf(enemigos); }
}
