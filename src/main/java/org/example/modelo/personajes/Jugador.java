package org.example.modelo.personajes;

import org.example.modelo.Poderes;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private final TipoPersonajes jugador = TipoPersonajes.JUGADOR;
    private int vida;
    private List<Poderes>poderes;
    private boolean esInvulnerable = false;


    public Jugador(){
        this.vida = jugador.obtenerVida();
        this.poderes = new ArrayList<Poderes>();
    }

    public int obtenerVida() {
        return this.vida;
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    public void recibirDisparo(){
        if (!esInvulnerable){
        this.vida --;
        }
    }


    public void agregarPoder(Poderes p){
        this.poderes.add(p);
    }
    public void eliminarPoder(Poderes p){
        this.poderes.remove(p);
    }
}
