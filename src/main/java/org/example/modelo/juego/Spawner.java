// src/main/java/org/example/modelo/juego/Spawner.java
package org.example.modelo.juego;

import org.example.modelo.fisica.Vector;
import org.example.modelo.personajes.Enemigo;
import org.example.modelo.personajes.TipoPersonaje;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Spawner {

    // --- Config (tomadas desde JuegoConfig) ---
    private final int  maxPorVentana;
    private final long ventanaMs;
    private final long minGapMs;

    // --- Estado ---
    private final Deque<Long> tiemposSpawn = new ArrayDeque<>(); // timestamps de spawns efectivos
    private final Deque<NivelData.EnemigoDato> pendientes = new ArrayDeque<>();
    private long nextSpawnMs = 0L;

    public Spawner() {
        this.maxPorVentana = JuegoConfig.ENEMY_SPAWN_MAX_IN_WINDOW; // 10
        this.ventanaMs     = JuegoConfig.ENEMY_SPAWN_WINDOW_MS;     // 60_000
        this.minGapMs      = JuegoConfig.ENEMY_MIN_SPAWN_GAP_MS;    // 800
    }

    public void cargarPendientes(List<NivelData.EnemigoDato> lista) {
        pendientes.clear();
        if (lista != null) pendientes.addAll(lista);
        tiemposSpawn.clear();
        nextSpawnMs = 0L;
    }

    public int cantidadPendiente() {
        return pendientes.size();
    }

    public boolean yaTermino() {
        return pendientes.isEmpty();
    }

    public void cancelarPendientes() {
        pendientes.clear();
        tiemposSpawn.clear();
        nextSpawnMs = 0L;
    }


    public List<Enemigo> spawnearHastaCompletar(int vivosActuales, long ahoraMs, int maxConcurrentes) {
        limpiarVentana(ahoraMs);

        int huecos = Math.max(0, maxConcurrentes - vivosActuales);
        if (huecos <= 0) return List.of();

        int disponiblesPorVentana = Math.max(0, maxPorVentana - tiemposSpawn.size());
        if (disponiblesPorVentana <= 0) return List.of();

        List<Enemigo> salientes = new ArrayList<>();

        while (huecos > 0 && !pendientes.isEmpty() && disponiblesPorVentana > 0) {
            if (ahoraMs < nextSpawnMs) break; // respetamos separación mínima

            var ed = pendientes.pollFirst();
            var tipo = mapTipo(ed.tipo);
            var pos  = new Vector(ed.x, ed.y);

            Enemigo nuevo = new Enemigo(tipo, pos);
            salientes.add(nuevo);

            // Registrar spawn en la ventana
            tiemposSpawn.addLast(ahoraMs);

            // Programar gap mínimo para el próximo
            nextSpawnMs = ahoraMs + minGapMs;

            // Actualizar contadores
            huecos--;
            disponiblesPorVentana--;
        }

        return salientes;
    }

    private void limpiarVentana(long ahoraMs) {
        // removemos spawns más viejos que la ventana
        while (!tiemposSpawn.isEmpty() && (ahoraMs - tiemposSpawn.peekFirst() > ventanaMs)) {
            tiemposSpawn.removeFirst();
        }
    }

    private TipoPersonaje mapTipo(String s) {
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
