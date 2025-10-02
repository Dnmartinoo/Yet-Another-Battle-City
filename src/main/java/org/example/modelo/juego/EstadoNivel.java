package org.example.modelo.juego;

import java.util.List;

public record EstadoNivel(
        boolean victoria,
        boolean derrota,
        int cantJugadores,
        int cantEnemigos,
        List<EstadoEntidad> entidades
) {
    public static EstadoNivel vacio() {
        return new EstadoNivel(false, false, 0, 0, List.of());
    }
}

