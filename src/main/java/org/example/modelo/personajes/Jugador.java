package org.example.modelo.personajes;

import org.example.modelo.fisica.*;
import org.example.modelo.powerup.*;
import org.example.modelo.controlador.Control;
import org.example.modelo.juego.JuegoConfig;

import java.util.ArrayList;
import java.util.List;

public class Jugador extends Tanque implements Control {

    private final List<PowerUp> poderes = new ArrayList<>();

    // -------------------------
    // NUEVO: contador de vidas
    // -------------------------
    private int vidasRestantes = (JuegoConfig.VIDAS_INICIALES > 0)
            ? JuegoConfig.VIDAS_INICIALES
            : 3; // fallback

    public int vidasRestantes() { return vidasRestantes; }
    public void setVidasRestantes(int v) { vidasRestantes = Math.max(0, v); }
    public void ganarUnaVida() { vidasRestantes++; }

    // -------------------------

    private static boolean invulnerable = false;
    private static long invulnerableHasta = 0L;
    private boolean disparoPotenciado = false;
    private boolean disparoPendiente = false;

    public Jugador(TipoPersonaje tipo, Vector posicion) {
        super(tipo, posicion);
    }

    public Jugador(Vector posicion) {
        super(TipoPersonaje.JUGADOR, posicion);
    }

    @Override
    public void recibirImpacto(int dano) {
        if (invulnerable || !estaVivo()) return;

        super.recibirImpacto(dano);

        // Si llegó a 0 de vida → morir
        if (!estaVivo()) {
            morir();
        }
    }

    public void morir() {
        invulnerable = false;
        disparoPotenciado = false;
        poderes.clear();

        // ↓↓↓ NUEVO: descontar vidas ↓↓↓
        if (vidasRestantes > 0) vidasRestantes--;

        // Respawn "rápido": restaurar vida al máximo
        vidaActual = tipo.vidaBase();

        // (Opcional) dar invulnerabilidad de respawn:
        // setInvulnerableHasta(System.currentTimeMillis() + 1500);
    }

    // POWER UPS
    public void agregarPoder(PowerUp p) { poderes.add(p); }
    public void eliminarPoder(PowerUp p) { poderes.remove(p); }
    public ComandoPowerUp aplicarPowerUp(PowerUp p) { return p.aplicar(this); }

    // INVULNERABILIDAD
    public static void activarInvulnerabilidadPor(long duracionMs, long ahoraMs) {
        invulnerable = true;
        invulnerableHasta = Math.max(invulnerableHasta, ahoraMs + duracionMs);
    }

    public void setInvulnerableHasta(long instanteMs) {
        invulnerable = true;
        invulnerableHasta = instanteMs;
    }

    public void actualizarEstado(long ahoraMs) {
        if (invulnerable && ahoraMs >= invulnerableHasta) {
            invulnerable = false;
        }
    }

    public void setInvulnerable(boolean v) { invulnerable = v; }
    public boolean esInvulnerable() { return invulnerable; }
    public long getInvulnerableHasta() { return invulnerableHasta; }

    // DISPARO POTENCIADO
    public void setDisparoPotenciado(boolean v) { disparoPotenciado = v; }
    public boolean tieneDisparoPotenciado() { return disparoPotenciado; }

    // MOVIMIENTO
    @Override public void moverArriba()    { setVelocidad(new Vector(0, -tipo.obtenerVelocidad() * 50)); }
    @Override public void moverAbajo()     { setVelocidad(new Vector(0, +tipo.obtenerVelocidad() * 50)); }
    @Override public void moverDerecha()   { setVelocidad(new Vector(+tipo.obtenerVelocidad() * 50, 0)); }
    @Override public void moverIzquierda() { setVelocidad(new Vector(-tipo.obtenerVelocidad() * 50, 0)); }
    @Override public void detener()        { setVelocidad(Vector.CERO); }

    // DISPARO
    @Override
    public void disparar() { this.disparoPendiente = true; }

    public boolean hayDisparoPendiente() { return disparoPendiente; }
    public void consumirDisparoPendiente() { disparoPendiente = false; }
}
