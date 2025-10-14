package org.example.modelo.juego.core;

import org.example.modelo.entorno.Bloque;
import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;
import org.example.modelo.disparo.Proyectil;
import org.example.modelo.juego.config.JuegoConfig;
import org.example.modelo.juego.estado.EstadoEntidad;
import org.example.modelo.personajes.Enemigo;
import org.example.modelo.personajes.Jugador;
import org.example.modelo.powerup.PowerUp;

import java.util.List;

public final class ConstructorEntidades {

    public ConstructorEntidades() {}

    private EstadoEntidad entidadFija(String tipo, Rectangulo hb) {
        return new EstadoEntidad(
                tipo, hb.x(), hb.y(), hb.w(), hb.h(),
                false,
                0, 0
        );
    }

    private EstadoEntidad entidadDirigida(String tipo, Rectangulo hb, Vector dir) {
        return new EstadoEntidad(
                tipo, hb.x(), hb.y(), hb.w(), hb.h(),
                false,
                dir.x(), dir.y()
        );
    }

    private EstadoEntidad entidadJugador(Rectangulo hb, boolean casco, Vector dir) {
        return new EstadoEntidad(
                "Jugador", hb.x(), hb.y(), hb.w(), hb.h(),
                casco,
                dir.x(), dir.y()
        );
    }


    public void agregarBloques(List<EstadoEntidad> out, List<Bloque> bloques) {
        for (var bloque : bloques) {
            var hb = bloque.hitbox();
            String tipo = bloque.getClass().getSimpleName();
            out.add(entidadFija(tipo, hb));
        }
    }


    public void agregarProyectiles(List<EstadoEntidad> out, List<Proyectil> proyectiles) {
        for (var p : proyectiles) {
            var hb = p.hitbox();
            out.add(entidadDirigida("Proyectil", hb, p.velocidad().normalizado()));
        }
    }


    public void agregarEnemigos(List<EstadoEntidad> out, List<Enemigo> enemigos) {
        for (var e : enemigos) {
            var hb = e.hitbox();
            out.add(entidadDirigida(e.getTipo().name(), hb, e.ultimaDireccion()));
        }
    }


    public void agregarJugadores(List<EstadoEntidad> out, List<Jugador> jugadores) {
        for (var j : jugadores) {
            if (!j.estaVisible()) continue;
            var hb = j.hitbox();
            out.add(entidadJugador(hb, j.esInvulnerable(), j.ultimaDireccion()));
        }
    }

    public void agregarPowerUps(List<EstadoEntidad> out, List<PowerUp> poderes) {
        for (var p : poderes) {
            var hb = p.hitbox();
            String tipo = p.getClass().getSimpleName();
            out.add(entidadFija(tipo, hb));
        }
    }
}
