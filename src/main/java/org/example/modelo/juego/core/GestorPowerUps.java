package org.example.modelo.juego.core;

import org.example.modelo.audio.ManagerSonido;
import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.config.JuegoConfig;
import org.example.modelo.personajes.Enemigo;
import org.example.modelo.personajes.Jugador;
import org.example.modelo.powerup.*;

import java.util.Iterator;
import java.util.List;

public class GestorPowerUps {
    public static PowerUp crearPoderRandom(Vector posicion) {
        int r = (int) (Math.random() * 3);
        return switch (r) {
            case 0 -> new Casco(posicion);
            case 1 -> new Estrella(posicion);
            case 2 -> new Granada(posicion);
            default -> new Estrella(posicion);
        };
    }


    public void tick(List<PowerUp> poderes, List<Jugador> jugadores, List<Enemigo> enemigos, Spawner spawner) {
        for (Jugador j : jugadores) {
            Iterator<PowerUp> it = poderes.iterator();
            while (it.hasNext()) {
                PowerUp p = it.next();
                if (j.hitbox().intersecta(p.hitbox())) {
                    ComandoPowerUp cmd = p.aplicar(j);
                    if (cmd == ComandoPowerUp.DESTUIR_TODOS_ENEMIGOS) {
                        enemigos.clear();
                        spawner.cancelarPendientes();
                    }
                    ManagerSonido.get().playEfecto(JuegoConfig.SND_POWERUP);
                    it.remove();
                    break;
                }
            }
        }
    }
}
