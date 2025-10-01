package org.example.modelo.personajes;

import org.example.modelo.fisica.*;
import org.example.modelo.powerup.*;

import java.util.ArrayList;
import java.util.List;

public class Jugador extends Tanque {
    public Jugador(TipoPersonaje tipo, Vector posicion) {
        super(tipo, posicion);
    }
    private final List<PowerUp> poderes = new ArrayList<>();

    private static boolean invulnerable = false;
    private static long invulnerableHasta = 0L;
    private boolean disparoPotenciado = false;

    public Jugador(Vector posicion) {
        super(TipoPersonaje.JUGADOR, posicion);
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



}