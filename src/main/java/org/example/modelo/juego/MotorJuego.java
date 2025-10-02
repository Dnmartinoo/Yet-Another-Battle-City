package org.example.modelo.juego;

import org.example.modelo.powerup.*;
import org.example.modelo.personajes.*;
import java.util.*;

public class MotorJuego {
    private Nivel nivelActual;

    public void cargarNivel(Nivel nivel) {
        this.nivelActual = nivel;
        this.nivelActual.iniciar();
    }

    public void tick(long ahoraMs, InputEstado j1, InputEstado j2) {
        if (nivelActual == null) return;
        nivelActual.tick(ahoraMs,j1,j2);
    }

    public boolean estaTerminado() {
        return nivelActual == null || nivelActual.estaTerminado();
    }

    public EstadoNivel estado() {
        return (nivelActual != null) ? nivelActual.estado() : EstadoNivel.vacio();
    }

    public Nivel nivel() { return nivelActual; }
}


