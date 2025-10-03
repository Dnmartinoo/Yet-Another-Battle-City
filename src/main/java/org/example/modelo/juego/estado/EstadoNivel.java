package org.example.modelo.juego.estado;

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

    public static EstadoNivel empty() {
        return new EstadoNivel(
                false,
                false,
                true,
                0,
                0,
                0,
                1,
                0,
                0,
                List.of()
        );
    }
}
