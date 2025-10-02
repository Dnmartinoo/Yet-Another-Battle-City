package org.example.modelo.juego;

import org.example.modelo.entorno.*;
import org.example.modelo.fisica.Vector;
import org.example.modelo.personajes.*;
import org.example.modelo.fisica.Rectangulo;

import java.util.ArrayList;
import java.util.List;

public class Nivel {
    private final List<Bloque> bloques = new ArrayList<>();
    private final List<Enemigo> enemigos = new ArrayList<>();
    private final List<Jugador> jugadores = new ArrayList<>(2);
    private Base base;
    private final Spawner spawner;
    private boolean victoria = false, derrota = false;
    public java.util.List<Jugador> jugadores() { return java.util.List.copyOf(jugadores); }
    public java.util.List<Enemigo> enemigos()  { return java.util.List.copyOf(enemigos); }
    public java.util.List<Bloque> bloques()    { return java.util.List.copyOf(bloques); }
    public Base base()                         { return base; }


    public Nivel(Rectangulo rectangulo, Spawner spawner) {
        this.spawner = spawner;
    }
    public void crearMundo(NivelData data) {
        this.base = new Base();
        bloques.add(this.base);

        bloques.addAll(data.construirBloques());
        jugadores.clear();
        jugadores.add(new Jugador(new Vector(
                data.jugador1X(),
                data.jugador1Y()
        )));

        if (data.coop) {
            jugadores.add(new Jugador(new Vector(
                    data.jugador2X(),
                    data.jugador2Y()
            )));
        }

        enemigos.clear();
        enemigos.addAll(data.construirEnemigosIniciales());
    }

    public void tick(long ahoraMs, InputEstado j1, InputEstado j2) {
        if (derrota || victoria) return;

        if (!jugadores.isEmpty()){
           aplicarInput(jugadores.get(0), j1, ahoraMs);
        }

        if (jugadores.size() > 1) {
            aplicarInput(jugadores.get(1), j2, ahoraMs);
        }

        for (Jugador j: jugadores) {
            j.actualizarEstado(ahoraMs);
        }

        for (Enemigo e : enemigos) {
            e.actualizarIA(ahoraMs);
        }

        //enemigos.addAll(spawner.talVezSpawnear(ahoraMs, );

        if (base.estaDestruido()) {
            derrota = true;
        }

        if (enemigos.isEmpty() && spawner.yaTermino()) {
            victoria = true;
        }
    }

    private void aplicarInput(Jugador j, InputEstado in, long ahora) {

    }

    public void eliminarTodosLosEnemigos() {
        enemigos.clear();
        spawner.cancelarPendientes();
    }

    public boolean estaTerminado() {
        return victoria || derrota;
    }

    public EstadoNivel estado() {
        return new EstadoNivel(victoria, derrota, jugadores.size(), enemigos.size());
    }

    public void iniciar() {
    }
}
