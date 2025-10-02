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

    // -------------------------
    // VIDAS & RESPAWN
    // -------------------------
    private int vidasRestantes = (JuegoConfig.VIDAS_INICIALES > 0)
            ? JuegoConfig.VIDAS_INICIALES
            : 3; // fallback

    /** Dónde reaparece el jugador al morir (se setea desde Nivel) */
    private Vector respawnPos = null;

    /** 1 = P1, 2 = P2 (para HUD / sprites) */
    private final int jugadorId;

    public int vidasRestantes() { return vidasRestantes; }
    public void setVidasRestantes(int v) { vidasRestantes = Math.max(0, v); }
    public void ganarUnaVida() { vidasRestantes++; }

    public void setRespawn(Vector p) { this.respawnPos = p; }

    // -------------------------
    // POWERUPS & ESTADO
    // -------------------------
    private final List<PowerUp> poderes = new ArrayList<>();

    // (tu diseño original los hacía estáticos)
    private static boolean invulnerable = false;
    private static long invulnerableHasta = 0L;

    private boolean disparoPotenciado = false;
    private boolean disparoPendiente = false;

    // ----- Constructores -----
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

    public int jugadorId() { return jugadorId; }

    @Override
    public void recibirImpacto(int dano) {
        if (invulnerable) return;

        if (vidasRestantes > 0) {
            vidasRestantes--;                   // Descuento UNA vida
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


    public void morir() {
        disparoPotenciado = false;
        poderes.clear();

        // ¿Quedan vidas para respawn?
        if (vidasRestantes > 0) {
            vidasRestantes--;          // consumir 1 vida
            respawnear();              // reaparecer con vida llena e invulnerabilidad
        } else {
            // Sin vidas ⇒ quedamos muertos (vidaActual ya es 0).
            // El Nivel detectará "todos sin vidas" y marcará derrota.
        }
    }

    private void respawnear() {
        if (respawnPos != null) {
            this.posicion = respawnPos;
        }

        this.detener();

        setInvulnerableHasta(System.currentTimeMillis() + JuegoConfig.RESPAWN_INVULN_MS);
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

    public void actualizarEstado(long ahoraMs) {
        if (invulnerable && ahoraMs >= invulnerableHasta) {
            invulnerable = false;
        }
    }

    public void setInvulnerable(boolean v) { invulnerable = v; }
    public boolean esInvulnerable() { return invulnerable; }
    public long getInvulnerableHasta() { return invulnerableHasta; }

    public boolean sinVidas() {
        return vidasRestantes == 0 && !estaVivo();
    }

    // ----- DISPARO POTENCIADO -----
    public void setDisparoPotenciado(boolean v) { disparoPotenciado = v; }
    public boolean tieneDisparoPotenciado() { return disparoPotenciado; }

    // ----- Movimiento -----
    @Override public void moverArriba()    { setVelocidad(new Vector(0, -tipo.obtenerVelocidad() * 50)); }
    @Override public void moverAbajo()     { setVelocidad(new Vector(0, +tipo.obtenerVelocidad() * 50)); }
    @Override public void moverDerecha()   { setVelocidad(new Vector(+tipo.obtenerVelocidad() * 50, 0)); }
    @Override public void moverIzquierda() { setVelocidad(new Vector(-tipo.obtenerVelocidad() * 50, 0)); }
    @Override public void detener()        { setVelocidad(Vector.CERO); }

    // ----- Disparo -----
    @Override public void disparar() { this.disparoPendiente = true; }
    public boolean hayDisparoPendiente() { return disparoPendiente; }
    public void consumirDisparoPendiente() { disparoPendiente = false; }

    // ----- Sprite -----
    @Override
    public String spriteId() {
        return (jugadorId == 1) ? JuegoConfig.SPRITE_PLAYER1_0 : JuegoConfig.SPRITE_PLAYER2_0;
    }
}
