package org.example.modelo.juego;

import org.example.modelo.fisica.Vector;
import org.example.modelo.personajes.Enemigo;
import org.example.modelo.personajes.TipoPersonaje;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Spawner {
    private final int maxEn60s = 10;
    private final long ventanaMs = 60_000;
    private final Deque<Long> tiemposSpawn = new ArrayDeque<>();
    private boolean termino = false;

    // cola con lo que vino del XML
    private final Deque<NivelData.EnemigoDato> pendientes = new ArrayDeque<>();

    public Spawner() {}

    /** Carga la cola de enemigos a emitir (desde el XML). */
    public void cargarPendientes(List<NivelData.EnemigoDato> lista) {
        pendientes.clear();
        if (lista != null) pendientes.addAll(lista);
        termino = pendientes.isEmpty();
    }

    /** Intenta spawnear respetando el rate-limit. */
    public List<Enemigo> talVezSpawnear(long ahoraMs) {
        limpiarVentana(ahoraMs);
        if (termino) return List.of();
        if (tiemposSpawn.size() >= maxEn60s) return List.of();
        if (pendientes.isEmpty()) { termino = true; return List.of(); }

        var ed = pendientes.pollFirst();
        tiemposSpawn.addLast(ahoraMs);

        TipoPersonaje tipo = mapTipo(ed.tipo);   // mapea string del XML a tu enum
        Vector pos = new Vector(ed.x, ed.y);
        Enemigo nuevo = new Enemigo(tipo, pos);

        if (pendientes.isEmpty()) termino = true;
        var out = new ArrayList<Enemigo>(1);
        out.add(nuevo);
        return out;
    }

    private void limpiarVentana(long ahoraMs) {
        while (!tiemposSpawn.isEmpty() && ahoraMs - tiemposSpawn.peekFirst() > ventanaMs) {
            tiemposSpawn.removeFirst();
        }
    }

    public void cancelarPendientes() { termino = true; pendientes.clear(); }
    public boolean yaTermino() { return termino; }

    private TipoPersonaje mapTipo(String s) {
        if (s == null) return TipoPersonaje.regularEnemy;
        return switch (s) {
            case "fastEnemy"     -> TipoPersonaje.fastEnemy;
            case "powerfulEnemy" -> TipoPersonaje.powerfulEnemy;
            case "heavyEnemy"    -> TipoPersonaje.heavyEnemy;
            case "regularEnemy"  -> TipoPersonaje.regularEnemy;
            // tolerancia por si llega en ES/mayúsculas
            case "RAPIDO"        -> TipoPersonaje.fastEnemy;
            case "POTENTE"       -> TipoPersonaje.powerfulEnemy;
            case "BLINDADO"      -> TipoPersonaje.heavyEnemy;
            case "BASICO"        -> TipoPersonaje.regularEnemy;
            default              -> TipoPersonaje.regularEnemy;
        };
    }
}
