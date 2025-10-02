package org.example.modelo.juego;

import org.example.modelo.entorno.*;
import org.example.modelo.personajes.Enemigo;

import java.util.ArrayList;
import java.util.List;

public class NivelData {
    public boolean coop = false;
    private int ancho = 800, alto = 600;

    private double jugador1X = 100, jugador1Y = 500;
    private double jugador2X = 200, jugador2Y = 500;

    private double spawnX = 390, spawnY = 0, spawnW = 20, spawnH = 20;

    public static final class BloqueDato {
        public final String tipo; public final double x,y;
        public BloqueDato(String t,double x,double y){ this.tipo=t; this.x=x; this.y=y; }
    }
    public static final class EnemigoDato {
        public final String tipo; public final double x,y;
        public EnemigoDato(String t,double x,double y){ this.tipo=t; this.x=x; this.y=y; }
    }

    private final List<BloqueDato> bloques = new ArrayList<>();
    private final List<EnemigoDato> enemigos = new ArrayList<>();

    public NivelData() {}
    public NivelData(boolean coop){ this.coop = coop; }

    // setters
    public void setCoop(boolean v){ this.coop = v; }
    public void setAncho(int v){ this.ancho = v; }
    public void setAlto(int v){ this.alto = v; }
    public void setJugador1(double x,double y){ this.jugador1X = x; this.jugador1Y = y; }
    public void setJugador2(double x,double y){ this.jugador2X = x; this.jugador2Y = y; }
    public void setZonaSpawn(double x,double y,double w,double h){ this.spawnX=x; this.spawnY=y; this.spawnW=w; this.spawnH=h; }
    public void addBloque(String tipo,double x,double y){ bloques.add(new BloqueDato(tipo,x,y)); }
    public void addEnemigo(String tipo,double x,double y){ enemigos.add(new EnemigoDato(tipo,x,y)); }

    // getters
    public boolean coop(){ return coop; }
    public int ancho(){ return ancho; }
    public int alto(){ return alto; }
    public double jugador1X(){ return jugador1X; }
    public double jugador1Y(){ return jugador1Y; }
    public double jugador2X(){ return jugador2X; }
    public double jugador2Y(){ return jugador2Y; }
    public double spawnX(){ return spawnX; }
    public double spawnY(){ return spawnY; }
    public double spawnW(){ return spawnW; }
    public double spawnH(){ return spawnH; }

    // ✅ construirBloques usando la factory (evita new Ladrillo() & cia)
    public List<Bloque> construirBloques() {
        List<Bloque> res = new ArrayList<>();
        for (BloqueDato bd : bloques) {
            res.add(BloqueFactory.crear(bd.tipo, bd.x, bd.y));
        }
        return res;
    }

    public List<Enemigo> construirEnemigosIniciales() {
        return new ArrayList<>();
    }

    public List<NivelData.EnemigoDato> enemigos() {
        return List.copyOf(enemigos);
    }

    public List<NivelData.BloqueDato> bloques() {
        return List.copyOf(bloques);
    }
}
