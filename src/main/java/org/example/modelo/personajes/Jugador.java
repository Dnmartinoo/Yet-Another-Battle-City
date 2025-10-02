package org.example.modelo.personajes;

import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.Spriteeable;
import org.example.modelo.powerup.ComandoPowerUp;
import org.example.modelo.powerup.PowerUp;
import org.example.modelo.controlador.Control;
import org.example.modelo.juego.JuegoConfig;

import java.util.ArrayList;
import java.util.List;

public class Jugador extends Tanque implements Control, Spriteeable {


    private int vidasRestantes = (JuegoConfig.VIDAS_INICIALES > 0)
            ? JuegoConfig.VIDAS_INICIALES
            : 3;

    public int vidasRestantes() { return vidasRestantes; }
    public void setVidasRestantes(int v) { vidasRestantes = Math.max(0, v); }
    public void ganarUnaVida() { vidasRestantes++; }

    private final int jugadorId;
    public int jugadorId() { return jugadorId; }

    // Poderes / estados
    private final List<PowerUp> poderes = new ArrayList<>();

    private static boolean invulnerable = false;
    private static long invulnerableHasta = 0L;

    private boolean disparoPotenciado = false;
    private boolean disparoPendiente = false;

    private int frameAnimacion = 0;
    private long proximoFrameMs = 0L;
    public Jugador(Vector posicion, int jugadorId) {
        super(TipoPersonaje.JUGADOR, posicion);
        this.jugadorId = jugadorId;
    }

    public Jugador(TipoPersonaje tipo, Vector posicion) {
        super(tipo, posicion);
        this.jugadorId = 1;
    }

    public Jugador(Vector posicion) {
        super(TipoPersonaje.JUGADOR, posicion);
        this.jugadorId = 1;
    }

    @Override
    public void recibirImpacto(int dano) {
        if (invulnerable || !estaVivo()) return;

        super.recibirImpacto(dano);

        if (!estaVivo()) {
            morir();
        }
    }

    public void morir() {
        invulnerable = false;
        disparoPotenciado = false;
        poderes.clear();

        if (vidasRestantes > 0) vidasRestantes--;  // cuenta una vida

        vidaActual = tipo.vidaBase();

    }

    public void agregarPoder(PowerUp p) { poderes.add(p); }
    public void eliminarPoder(PowerUp p) { poderes.remove(p); }
    public ComandoPowerUp aplicarPowerUp(PowerUp p) { return p.aplicar(this); }

    public static void activarInvulnerabilidadPor(long duracionMs, long ahoraMs) {
        invulnerable = true;
        invulnerableHasta = Math.max(invulnerableHasta, ahoraMs + duracionMs);
    }

    public void setInvulnerableHasta(long instanteMs) {
        invulnerable = true;
        invulnerableHasta = instanteMs;
    }

    public void setInvulnerable(boolean v) { invulnerable = v; }
    public boolean esInvulnerable() { return invulnerable; }
    public long getInvulnerableHasta() { return invulnerableHasta; }

    public void actualizarEstado(long ahoraMs) {
        // Fin de invulnerabilidad
        if (invulnerable && ahoraMs >= invulnerableHasta) {
            invulnerable = false;
        }

        // Animación simple (toggle cada N ms)
        final long periodo = (JuegoConfig.PLAYER_ANIM_FRAME_MS > 0)
                ? JuegoConfig.PLAYER_ANIM_FRAME_MS
                : 200L; // fallback 200ms
        if (ahoraMs >= proximoFrameMs) {
            frameAnimacion = 1 - frameAnimacion;
            proximoFrameMs = ahoraMs + periodo;
        }
    }

    @Override
    public String spriteId() {
        if (jugadorId == 1) {
            return (frameAnimacion == 0)
                    ? JuegoConfig.SPRITE_PLAYER1_0
                    : JuegoConfig.SPRITE_PLAYER1_1;
        } else {
            return (frameAnimacion == 0)
                    ? JuegoConfig.SPRITE_PLAYER2_0
                    : JuegoConfig.SPRITE_PLAYER2_1;
        }
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
}
