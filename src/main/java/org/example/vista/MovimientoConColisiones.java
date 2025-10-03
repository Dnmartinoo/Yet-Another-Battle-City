package org.example.vista;

import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.Nivel;
import org.example.modelo.personajes.Jugador;

public final class MovimientoConColisiones {

    public void aplicarMovimiento(Jugador j, double dt, Nivel nivel,
                                  boolean up, boolean down, boolean left, boolean right) {
        if (j == null) return;

        boolean movio = false;
        if (up)    { j.moverArriba();    movio = true; }
        if (down)  { j.moverAbajo();     movio = true; }
        if (left)  { j.moverIzquierda(); movio = true; }
        if (right) { j.moverDerecha();   movio = true; }
        if (!movio) j.detener();

        var delta = j.velocidad().por(dt);
        aplicarEjeX(j, delta.x(), nivel);
        aplicarEjeY(j, delta.y(), nivel);
    }

    private void aplicarEjeX(Jugador j, double dx, Nivel nivel) {
        if (dx == 0) return;
        var nextHitboxX = j.hitbox().trasladado(new Vector(dx, 0));
        if (!nivel.colisionaConBloqueSolido(nextHitboxX)) {
            j.setPosicion(j.posicion().mas(new Vector(dx, 0)));
        }
    }

    private void aplicarEjeY(Jugador j, double dy, Nivel nivel) {
        if (dy == 0) return;
        var nextHitboxY = j.hitbox().trasladado(new Vector(0, dy));
        if (!nivel.colisionaConBloqueSolido(nextHitboxY)) {
            j.setPosicion(j.posicion().mas(new Vector(0, dy)));
        }
    }
}
