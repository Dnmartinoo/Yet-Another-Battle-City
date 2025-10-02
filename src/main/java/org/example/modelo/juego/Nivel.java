package org.example.modelo.juego;

import org.example.modelo.entorno.*;
import org.example.modelo.fisica.MundoFisico;
import org.example.modelo.personajes.*;
import org.example.modelo.fisica.Rectangulo;

import java.util.ArrayList;
import java.util.List;

public class Nivel{
    private final List<Bloque> bloques = new ArrayList<>();
    private final List<Enemigo> enemigos = new ArrayList<>();
    private final List<Jugador> jugadores = new ArrayList<>(2);
    private Base base;
    private final Spawner spawner;
    private boolean victoria = false, derrota = false;
    private MundoFisico mundo;

    public java.util.List<Jugador> jugadores() { return java.util.List.copyOf(jugadores); }
    public java.util.List<Enemigo> enemigos()  { return java.util.List.copyOf(enemigos); }
    public java.util.List<Bloque> bloques()    { return java.util.List.copyOf(bloques); }
    public Base base()                         { return base; }

    public Nivel(Rectangulo rectangulo, Spawner spawner) {
        this.spawner = spawner;


    }

    public void crearMundo(NivelData data) {
        // Bloques (y posible Base)
        bloques.clear();
        Base baseRef = null;
        for (var bd : data.bloques()) {
            var b = BloqueFactory.crear(bd.tipo, bd.x, bd.y);
            bloques.add(b);
            if (b instanceof Base br) baseRef = br; // si vino desde data
        }

        // Si no vino Base en data, crear una por defecto
        if (baseRef == null) {
            // Posición por defecto (abajo-centro); podés ajustar a gusto
            double bx = (data.ancho() - BloqueFactory.TILE) / 2.0;
            double by = data.alto() - BloqueFactory.TILE - 20.0;
            baseRef = new Base(new org.example.modelo.fisica.Vector(bx, by), BloqueFactory.TILE);
            bloques.add(baseRef);
        }
        this.base = baseRef;  // <-- ¡clave!

        // Jugadores
        jugadores.clear();
        jugadores.add(new Jugador(new org.example.modelo.fisica.Vector(
                data.jugador1X(), data.jugador1Y()
        )));
        if (data.coop) {
            jugadores.add(new Jugador(new org.example.modelo.fisica.Vector(
                    data.jugador2X(), data.jugador2Y()
            )));
        }

        // Enemigos
        enemigos.clear();
        spawner.cargarPendientes(data.enemigos());

        int anchoTiles = data.ancho() / BloqueFactory.TILE;
        int altoTiles  = data.alto() / BloqueFactory.TILE;
        Bloque[][] grid = new Bloque[altoTiles][anchoTiles];
        for (Bloque b : bloques) {
            int x = (int)(b.posicion().x() / BloqueFactory.TILE);
            int y = (int)(b.posicion().y() / BloqueFactory.TILE);
            if (x >= 0 && x < anchoTiles && y >= 0 && y < altoTiles) {
                grid[y][x] = b;
            }
        }
        this.mundo = new MundoFisico(BloqueFactory.TILE, anchoTiles, altoTiles, grid);
    }


    public void tick(long ahoraMs, InputEstado j1, InputEstado j2) {
        if (derrota || victoria) return;

        // Inputs (si los usás desde acá; si no, quedan neutros)
        if (!jugadores.isEmpty()){
            aplicarInput(jugadores.get(0), j1, ahoraMs);
        }
        if (jugadores.size() > 1) {
            aplicarInput(jugadores.get(1), j2, ahoraMs);
        }

        // Actualizar estado de jugadores/enemigos
        for (Jugador j: jugadores) {
            j.actualizarEstado(ahoraMs);
        }
        for (Enemigo e : enemigos) {
            e.actualizarIA(ahoraMs, this.mundo);
        }

        // Spawnear (rate-limit adentro del Spawner)
        enemigos.addAll(spawner.talVezSpawnear(ahoraMs));

        // Condiciones de fin
        if (base.estaDestruido()) {
            derrota = true;
        }
        if (enemigos.isEmpty() && spawner.yaTermino()) {
            victoria = true;
        }
    }

    private void aplicarInput(Jugador j, InputEstado in, long ahora) {
        // Si más adelante querés usar input desde motor.tick:
        // - setear velocidad según in.arriba/abajo/izq/der
        // - manejar in.disparar con cooldown
    }

    // en Nivel.java (agregá esto)
    public boolean colisionaConBloqueSolido(org.example.modelo.fisica.Rectangulo area) {
        for (var b : bloques) {
            // Sólo los bloques que bloquean movimiento y tengan hitbox
            if (b.bloqueaMovimiento() && b instanceof org.example.modelo.fisica.Cuerpo c) {
                if (c.hitbox().intersecta(area)) return true;
            }
        }
        return false;
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
        // hook si necesitás algo al inicio del nivel
    }



}
