package org.example.modelo.juego;

import org.example.modelo.personajes.Enemigo;
import org.example.modelo.fisica.Rectangulo;

import java.util.*;
import java.util.ArrayDeque;

public class Spawner {
    private final int maxEn60s = 10;
    private final long ventanaMs = 60_000;
    private final Deque<Long> tiemposSpawn = new ArrayDeque<>();
    private boolean termino = false;



    public List<Enemigo> talVezSpawnear(long ahoraMs, Rectangulo area) {
        limpiarVentana(ahoraMs);
        return List.of();
    }

    private void limpiarVentana(long ahoraMs) {
        while (!tiemposSpawn.isEmpty() && ahoraMs - tiemposSpawn.peekFirst() > ventanaMs) {
            tiemposSpawn.removeFirst();
        }
    }

    public void cancelarPendientes() {
        termino = true;
    }

    public boolean yaTermino() {
        return termino;
    }
}


