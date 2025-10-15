package org.example.modelo.juego.core;

import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.config.JuegoConfig;
import org.example.modelo.personajes.Enemigo;
import org.example.modelo.personajes.Jugador;
import org.example.modelo.powerup.*;
import org.example.modelo.puertos.SoundPort;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GestorPowerUps {

    private final Random rng = new Random();
    private final SoundPort sound;

    public GestorPowerUps(SoundPort sound) {
        this.sound = sound;
    }

    public PowerUp crearPoderRandom(Vector posicion) {
        int r = rng.nextInt(3);
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
                    sound.playEffect(JuegoConfig.SND_POWERUP);
                    it.remove();
                    break;
                }
            }
        }
    }
}
