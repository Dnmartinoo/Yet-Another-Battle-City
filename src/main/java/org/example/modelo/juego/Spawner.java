// src/main/java/org/example/modelo/juego/Spawner.java
package org.example.modelo.juego;

import org.example.modelo.fisica.Vector;
import org.example.modelo.personajes.Enemigo;
import org.example.modelo.personajes.TipoPersonaje;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class Spawner {

    private final Deque<NivelData.EnemigoDato> pendientes = new ArrayDeque<>();
    private long nextSpawnMs = 0L;

    public Spawner() {}

    public void cargarPendientes(List<NivelData.EnemigoDato> lista) {
        pendientes.clear();
        if (lista != null) pendientes.addAll(lista);
        nextSpawnMs = 0L;
    }

    public boolean yaTermino() {
        return pendientes.isEmpty();
    }

    public int cantidadPendiente() {
        return pendientes.size();
    }

    public void cancelarPendientes() {
        pendientes.clear();
        nextSpawnMs = 0L;
    }

    public List<Enemigo> spawnearHastaCompletar(int vivosActuales, long ahoraMs, int maxConcurrentes) {
        List<Enemigo> res = new ArrayList<>();
        while ((vivosActuales + res.size()) < maxConcurrentes && !pendientes.isEmpty()) {
            if (ahoraMs < nextSpawnMs) break; // cooldown activo
            var dato = pendientes.pollFirst();
            res.add(crearEnemigoDesdeDato(dato));
            nextSpawnMs = ahoraMs + JuegoConfig.ENEMY_SPAWN_INTERVAL_MS;
        }
        return res;
    }

    public List<Enemigo> talVezSpawnear(long ahoraMs) {
        if (pendientes.isEmpty()) return List.of();
        if (ahoraMs < nextSpawnMs) return List.of();
        var d = pendientes.pollFirst();
        nextSpawnMs = ahoraMs + JuegoConfig.ENEMY_SPAWN_INTERVAL_MS;
        return List.of(crearEnemigoDesdeDato(d));
    }

    private static Enemigo crearEnemigoDesdeDato(NivelData.EnemigoDato d) {
        TipoPersonaje tipo = mapTipo(d.tipo);
        return new Enemigo(tipo, new Vector(d.x, d.y));
    }

    private static TipoPersonaje mapTipo(String s) {
        if (s == null) return TipoPersonaje.regularEnemy;
        return switch (s) {
            case "fastEnemy"     -> TipoPersonaje.fastEnemy;
            case "powerfulEnemy" -> TipoPersonaje.powerfulEnemy;
            case "heavyEnemy"    -> TipoPersonaje.heavyEnemy;
            case "regularEnemy"  -> TipoPersonaje.regularEnemy;

            default              -> TipoPersonaje.regularEnemy;
        };
    }
}
