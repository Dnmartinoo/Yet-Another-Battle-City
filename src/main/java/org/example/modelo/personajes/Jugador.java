package org.example.modelo.personajes;

import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.Spriteeable;
import org.example.modelo.powerup.PowerUp;
import org.example.modelo.controlador.Control;
import org.example.modelo.juego.JuegoConfig;

import java.util.ArrayList;
import java.util.List;

public class Jugador extends Tanque implements Control, Spriteeable {

    private int vidasRestantes = (JuegoConfig.VIDAS_INICIALES > 0)
            ? JuegoConfig.VIDAS_INICIALES
            : 3; // fallback

    private Vector respawnPos = null;

    private final int jugadorId;

    public int vidasRestantes() { return vidasRestantes; }
    public void setVidasRestantes(int v) { vidasRestantes = Math.max(0, v); }
    public void ganarUnaVida() { vidasRestantes++; }

    public void setRespawn(Vector p) { this.respawnPos = p; }


    private final List<PowerUp> poderes = new ArrayList<>();

    private static boolean invulnerable = false;
    private static long invulnerableHasta = 0L;

    private boolean disparoPotenciado = false;
    private boolean disparoPendiente = false;

    public Jugador(Vector posicion, int jugadorId) {
        super(TipoPersonaje.JUGADOR, posicion);
        this.jugadorId = jugadorId;
    }
    @Override
    public void recibirImpacto(int dano) {
        if (invulnerable) return;

        if (vidasRestantes > 0) {
            vidasRestantes--;
            if (vidasRestantes > 0) {
                respawnear();
            } else {
                this.vidaActual = 0;
                this.detener();
            }
        } else {
            this.vidaActual = 0;
            this.detener();
        }
    }

    private void respawnear() {
        if (respawnPos != null) {
            this.posicion = respawnPos;
        }

        this.detener();

        setInvulnerableHasta(System.currentTimeMillis() + JuegoConfig.RESPAWN_INVULN_MS);
    }

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

    public boolean esInvulnerable() { return invulnerable; }

    public boolean sinVidas() {
        return vidasRestantes == 0 && !estaVivo();
    }

    public void setDisparoPotenciado(boolean v) { disparoPotenciado = v; }
    public boolean tieneDisparoPotenciado() { return disparoPotenciado; }

    @Override public void moverArriba()    { setVelocidad(new Vector(0, -tipo.obtenerVelocidad() * 50)); }
    @Override public void moverAbajo()     { setVelocidad(new Vector(0, +tipo.obtenerVelocidad() * 50)); }
    @Override public void moverDerecha()   { setVelocidad(new Vector(+tipo.obtenerVelocidad() * 50, 0)); }
    @Override public void moverIzquierda() { setVelocidad(new Vector(-tipo.obtenerVelocidad() * 50, 0)); }
    @Override public void detener()        { setVelocidad(Vector.CERO); }

    @Override public void disparar() { this.disparoPendiente = true; }
    public boolean hayDisparoPendiente() { return disparoPendiente; }
    public void consumirDisparoPendiente() { disparoPendiente = false; }

    @Override
    public String spriteId() {
        return (jugadorId == 1) ? JuegoConfig.SPRITE_PLAYER1_0 : JuegoConfig.SPRITE_PLAYER2_0;
    }
}
