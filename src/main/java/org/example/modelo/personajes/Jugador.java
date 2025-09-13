package org.example.modelo.personajes;

import org.example.modelo.Poderes;
import org.example.modelo.powerup.ComandoPowerUp;
import org.example.modelo.powerup.PowerUp;
import org.example.modelo.powerup.ComandoPowerUp;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private final org.example.modelo.personajes.TipoPersonajes jugador = TipoPersonajes.JUGADOR;
    private int vida;
    private List<PowerUp> poderes = new ArrayList<>();
    private boolean esInvulnerable = false;
    private boolean disparoPotenciado = false;


    public Jugador(){
        this.vida = jugador.obtenerVida();
    }

    public int obtenerVida() {
        return this.vida;
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    public void recibirDisparo() {
        if (!esInvulnerable) {
            this.vida--;
        }
    }

    /**GESTION DE PowerUps**/
    public void agregarPoder(PowerUp p){
        this.poderes.add(p);
    }
    public void eliminarPoder(PowerUp p){
        this.poderes.remove(p);
    }

    public ComandoPowerUp aplicarPowerUp(PowerUp p){
        return p.aplicar(this);
    }

    public void setInvulnerable(boolean invulnerable){
        this.esInvulnerable = invulnerable;
    }

    public boolean isInvulnerable(){
        return esInvulnerable;
    }

    public void setDisparoPotenciado(boolean encendido){
        this.disparoPotenciado = encendido;
    }

    public boolean tieneDisparoPotenciado() {
        return disparoPotenciado;
    }
}
