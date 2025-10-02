// src/main/java/org/example/modelo/juego/EstadoNivel.java
package org.example.modelo.juego;

import java.util.List;

public record EstadoNivel(
        boolean victoria,
        boolean derrota,
        boolean vacio,
        int cantJugadores,
        int enemigosVivos,
        int enemigosPendientes,
        int nivelNumero,
        int vidasP1,
        int vidasP2,
        List<EstadoEntidad> entidades
) {
    public int enemigosTotales() {
        return enemigosVivos + enemigosPendientes;
    }

    // Fábrica de un EstadoNivel vacío (fallback seguro)
    public static EstadoNivel empty() {
        return new EstadoNivel(
                false,   // victoria
                false,   // derrota
                true,    // vacio
                0,       // cantJugadores
                0,       // enemigosVivos
                0,       // enemigosPendientes
                1,       // nivelNumero
                0,       // vidasP1
                0,       // vidasP2
                List.of()// entidades
        );
    }

    // (opcional) constante reuseable
    public static final EstadoNivel EMPTY = empty();
}
