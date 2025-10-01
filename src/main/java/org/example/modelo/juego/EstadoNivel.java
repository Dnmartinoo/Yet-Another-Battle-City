package org.example.modelo.juego;

public record EstadoNivel(boolean victoria, boolean derrota, int cantJugadores, int cantEnemigos) {
    public static EstadoNivel vacio() {
        return new EstadoNivel(false, false, 0, 0);
    }
}
