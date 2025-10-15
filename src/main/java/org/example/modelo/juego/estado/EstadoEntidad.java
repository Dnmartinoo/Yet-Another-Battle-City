package org.example.modelo.juego.estado;

public record EstadoEntidad(
        String tipo,
        double x,
        double y,
        double ancho,
        double alto,
        boolean cascoActivo,
        double direccionX,
        double direccionY
) {}
