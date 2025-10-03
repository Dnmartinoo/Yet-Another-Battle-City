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

    private final int  maxPorVentana;
    private final long ventanaMs;
    private final long minGapMs;

    private final Deque<Long> tiemposSpawn = new ArrayDeque<>();            // timestamps de spawns efectivos
    private final Deque<NivelData.EnemigoDato> pendientes = new ArrayDeque<>();
    private long nextSpawnMs = 0L;

    public Spawner() {
        this.maxPorVentana = JuegoConfig.ENEMY_SPAWN_MAX_IN_WINDOW;
        this.ventanaMs     = JuegoConfig.ENEMY_SPAWN_WINDOW_MS;
        this.minGapMs      = JuegoConfig.ENEMY_MIN_SPAWN_GAP_MS;
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

            var ed   = pendientes.pollFirst();
            TipoPersonaje tipo = ed.tipo;                 // ya es TipoPersonaje
            Vector pos         = new Vector(ed.x, ed.y);

            Enemigo nuevo = new Enemigo(tipo, pos);
            salientes.add(nuevo);

            tiemposSpawn.addLast(ahoraMs);

            nextSpawnMs = ahoraMs + minGapMs;

            huecos--;
            disponiblesPorVentana--;
        }

        return List.copyOf(salientes);
    }

    private void limpiarVentana(long ahoraMs) {
        while (!tiemposSpawn.isEmpty() && (ahoraMs - tiemposSpawn.peekFirst() > ventanaMs)) {
            tiemposSpawn.removeFirst();
        }
    }
}
