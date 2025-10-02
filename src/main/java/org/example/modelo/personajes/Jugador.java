package org.example.modelo.personajes;

import org.example.modelo.fisica.*;
import org.example.modelo.juego.JuegoConfig;
import org.example.modelo.juego.Spriteeable;
import org.example.modelo.powerup.*;
import org.example.modelo.controlador.Control;

import java.util.ArrayList;
import java.util.List;

public class Jugador extends Tanque implements Control, Spriteeable {
    private final int idJugador;
    public Jugador(TipoPersonaje tipo, Vector posicion, int idJugador) {
        super(tipo, posicion);
        this.idJugador = idJugador;
    }
    private final List<PowerUp> poderes = new ArrayList<>();

    private static boolean invulnerable = false;
    private static long invulnerableHasta = 0L;
    private boolean disparoPotenciado = false;
    private boolean disparoPendiente = false;

    public Jugador(Vector posicion, int idJugador) {

        super(TipoPersonaje.JUGADOR, posicion);
        this.idJugador = idJugador;
    }

    public int getIdJugador() {
        return idJugador;
    }

    @Override public void recibirImpacto(int dano) {
        if(invulnerable || !estaVivo()) {
            return;
        }
        super.recibirImpacto(dano);
    }

    public void morir() {
        invulnerable = false;
        disparoPotenciado = false;
        poderes.clear();
        vidaActual = tipo.vidaBase();
    }

    // POWER UPS

    public void agregarPoder(PowerUp p) {
        poderes.add(p);
    }

    public void eliminarPoder(PowerUp p) {
        poderes.remove(p);
    }

    public ComandoPowerUp aplicarPowerUp(PowerUp p) {
        return p.aplicar(this);
    }

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
        if(invulnerable && ahoraMs >= invulnerableHasta) {
            invulnerable = false;
        }
    }

    public void setInvulnerable(boolean v) {
        invulnerable = v;
    }

    public boolean esInvulnerable() {
        return invulnerable;
    }

    public long getInvulnerableHasta() {
        return invulnerableHasta;
    }

    // DISPARO POTENCIADO

    public void setDisparoPotenciado(boolean v) {
        disparoPotenciado = v;
    }

    public boolean tieneDisparoPotenciado() {
        return disparoPotenciado;
    }


    @Override public void moverArriba(){
        setVelocidad(new Vector(0, -tipo.obtenerVelocidad()*50));
    }

    @Override public void moverAbajo(){
        setVelocidad(new Vector(0, +tipo.obtenerVelocidad()*50));
    }

    @Override public void moverDerecha(){
        setVelocidad(new Vector(+tipo.obtenerVelocidad()*50, 0 ));
    }

    @Override public void moverIzquierda(){
        setVelocidad(new Vector(-tipo.obtenerVelocidad()*50, 0));
    }

    @Override public void detener() {
        setVelocidad(Vector.CERO);
    }

    @Override
    public void disparar() {
        this.disparoPendiente = true;
    }

    public boolean hayDisparoPendiente() {
        return disparoPendiente;
    }

    public void consumirDisparoPendiente() {
        disparoPendiente = false;
    }

    @Override
    public String spriteId() {
        return (idJugador == 1)
                ? (frameAnimacion == 0 ? JuegoConfig.SPRITE_PLAYER1_0 : JuegoConfig.SPRITE_PLAYER1_1)
                : (frameAnimacion == 0 ? JuegoConfig.SPRITE_PLAYER2_0 : JuegoConfig.SPRITE_PLAYER2_1);
    }


}