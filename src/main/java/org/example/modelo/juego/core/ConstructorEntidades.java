package org.example.modelo.juego.core;

import org.example.modelo.entorno.Bloque;
import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.disparo.Proyectil;
import org.example.modelo.juego.Spriteeable;
import org.example.modelo.juego.config.JuegoConfig;
import org.example.modelo.juego.estado.EstadoEntidad;
import org.example.modelo.personajes.Enemigo;
import org.example.modelo.personajes.Jugador;
import org.example.modelo.powerup.PowerUp;

import java.util.List;

public final class ConstructorEntidades {

    private static EstadoEntidad entidadFija(String spriteId, Rectangulo hb) {
        return new EstadoEntidad(
                spriteId, hb.x(), hb.y(), hb.w(), hb.h(),
                false,
                JuegoConfig.ROTACION_FIJA
        );
    }

    private static EstadoEntidad entidadConRot(String spriteId, Rectangulo hb, double rot) {
        return new EstadoEntidad(
                spriteId, hb.x(), hb.y(), hb.w(), hb.h(),
                false,
                rot
        );
    }

    private static EstadoEntidad entidadJugador(String spriteId, Rectangulo hb, boolean casco, double rot) {
        return new EstadoEntidad(
                spriteId, hb.x(), hb.y(), hb.w(), hb.h(),
                casco,
                rot
        );
    }

    public static void agregarBloques(List<EstadoEntidad> out, List<Bloque> bloques) {
        for (var bloque : bloques) {
            var hb = bloque.hitbox();
            var id = ((Spriteeable) bloque).spriteId();
            out.add(entidadFija(id, hb));
        }
    }

    public static void agregarProyectiles(List<EstadoEntidad> out, List<Proyectil> proyectiles) {
        for (var p : proyectiles) {
            var hb = p.hitbox();
            out.add(entidadFija(p.spriteId(), hb));
        }
    }

    public static void agregarEnemigos(List<EstadoEntidad> out, List<Enemigo> enemigos) {
        for (var e : enemigos) {
            var hb = e.hitbox();
            out.add(entidadConRot(e.spriteId(), hb, e.rotacion()));
        }
    }

    public static void agregarJugadores(List<EstadoEntidad> out, List<Jugador> jugadores) {
        for (var j : jugadores) {
            if (!j.estaVisible()) continue;
            var hb = j.hitbox();
            out.add(entidadJugador(j.spriteId(), hb, j.esInvulnerable(), j.rotacion()));
        }
    }

    public static void agregarPowerUps(List<EstadoEntidad> out, List<PowerUp> poderes) {
        for (var p : poderes) {
            var hb = p.hitbox();
            var id = ((Spriteeable) p).spriteId();
            out.add(entidadFija(id, hb));
        }
    }
}
