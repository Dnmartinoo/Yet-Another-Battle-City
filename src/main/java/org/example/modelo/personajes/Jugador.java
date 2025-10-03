package org.example.modelo.personajes;

import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.Spriteeable;
import org.example.modelo.controlador.Control;
import org.example.modelo.juego.config.JuegoConfig;

public class Jugador extends Tanque implements Control, Spriteeable {

    private int vidasRestantes = JuegoConfig.VIDAS_INICIALES;
    private Vector respawnPos = null;
    private final int jugadorId;

    private static boolean invulnerable = false;
    private static long invulnerableHasta = 0L;

    private boolean disparoPotenciado = false;
    private boolean disparoPendiente = false;
    private boolean visible = true;

    private long inmovilizadoHastaMs = 0L;

    public Jugador(Vector posicion, int jugadorId) {
        super(TipoPersonaje.JUGADOR, posicion);
        this.jugadorId = jugadorId;
    }

    public int vidasRestantes() { return vidasRestantes; }
    public void setRespawn(Vector p) { this.respawnPos = p; }

    @Override
    public void recibirImpacto(int dano) {
        if (invulnerable) return;

        if (vidasRestantes > 0) {
            vidasRestantes--;
            if (vidasRestantes > 0) respawnear();
            else {
                this.vidaActual = 0;
                this.detener();
                this.setVisible(false);
            }
        }
    }

    private void respawnear() {
        if (respawnPos != null) this.posicion = respawnPos;
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
        if (invulnerable && ahoraMs >= invulnerableHasta) invulnerable = false;
    }

    public boolean esInvulnerable() { return invulnerable; }
    public boolean sinVidas() { return vidasRestantes == 0 && !estaVivo(); }

    public void setDisparoPotenciado(boolean v) { disparoPotenciado = v; }
    public boolean tieneDisparoPotenciado() { return disparoPotenciado; }

    public void setVisible(boolean v) { this.visible = v; }
    public boolean estaVisible() { return visible; }
    public void inmovilizarPorMs(long duracionMs, long ahoraMs) {
        this.inmovilizadoHastaMs = Math.max(this.inmovilizadoHastaMs, ahoraMs + duracionMs);
    }

    public boolean estaInmovilizado(long ahoraMs) {
        return ahoraMs < inmovilizadoHastaMs;
    }
    @Override public void moverArriba()    { if (visible && !estaInmovilizado(System.currentTimeMillis())) setVelocidad(new Vector(0, -tipo.obtenerVelocidad() * 50)); }
    @Override public void moverAbajo()     { if (visible && !estaInmovilizado(System.currentTimeMillis())) setVelocidad(new Vector(0, tipo.obtenerVelocidad() * 50)); }
    @Override public void moverDerecha()   { if (visible && !estaInmovilizado(System.currentTimeMillis())) setVelocidad(new Vector(tipo.obtenerVelocidad() * 50, 0)); }
    @Override public void moverIzquierda() { if (visible && !estaInmovilizado(System.currentTimeMillis())) setVelocidad(new Vector(-tipo.obtenerVelocidad() * 50, 0)); }
    @Override public void detener()        { setVelocidad(Vector.CERO); }

    @Override public void disparar() { if (visible) this.disparoPendiente = true; }
    public boolean hayDisparoPendiente() { return disparoPendiente; }
    public void consumirDisparoPendiente() { disparoPendiente = false; }

    @Override
    public String spriteId() {
        return (jugadorId == 1) ? JuegoConfig.SPRITE_PLAYER1_0 : JuegoConfig.SPRITE_PLAYER2_0;
    }
}
