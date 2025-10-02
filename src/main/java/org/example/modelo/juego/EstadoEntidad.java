package org.example.modelo.juego;

public record EstadoEntidad(
        String spriteId,
        double x,
        double y,
        double ancho,
        double alto,
        boolean cascoActivo
) {}
