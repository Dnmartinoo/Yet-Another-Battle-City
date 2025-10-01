package org.example.modelo.juego;



import org.example.modelo.entorno.*;
import org.example.modelo.personajes.Enemigo;
import java.util.*;

public class NivelData {
    boolean coop = false;

    public List<Bloque> construirBloques() {
        return List.of(
                new Ladrillo(), new Ladrillo(), new Acero(),
                new Agua(), new Bosque()
        );
    }

    public List<Enemigo> construirEnemigosIniciales() {
        return new ArrayList<>();
    }


    public double Jugador1X() {
        return 100;
    }

    public double Jugador1Y() {
        return 500;
    }

    public double Jugador2X() {
        return 200;
    }

    public double Jugador2Y() {
        return 500;
    }



}
