package org.example.modelo.personajes;

import org.example.modelo.fisica.MundoFisico;
import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.JuegoConfig;
import org.example.modelo.juego.Spriteeable;

import java.util.Random;

public class Enemigo extends Tanque implements Spriteeable {
    private static final Random rng = new Random();

    private Vector direccionActual = Vector.CERO;
    private long tiempoConductaHasta = 0L;
    private Vector ultimaPosicion = Vector.CERO;
    private long ultimoCambioPosicion = 0L;
    private long cooldownDisparoHasta = 0L;

    public Enemigo(TipoPersonaje tipo, Vector posicion) {
        super(tipo, posicion);
        if (!tipo.esEnemigo()) {
            throw new IllegalArgumentException("Tipo no enemigo: " + tipo);
        }
    }

    @Override public boolean solido() { return true; }

    @Override
    public Vector velocidad() {
        return direccionActual.por(velocidadEscalar);
    }


    public void actualizarIA(long ahoraMs, MundoFisico mundo) {

        if (ahoraMs >= tiempoConductaHasta || direccionActual == Vector.CERO) {
            direccionActual = direccionAleatoria();
            tiempoConductaHasta = ahoraMs + (1000L * (1 + rng.nextInt(5)));
        }


        if (posicion.equals(ultimaPosicion)) {
            if (ahoraMs - ultimoCambioPosicion > 2000) {
                direccionActual = direccionAleatoria();
                tiempoConductaHasta = ahoraMs + (1000L * (1 + rng.nextInt(5)));
                ultimoCambioPosicion = ahoraMs;
            }
        } else {
            ultimaPosicion = posicion;
            ultimoCambioPosicion = ahoraMs;
        }


        Vector delta = direccionActual.por(velocidadEscalar * 0.016);
        setVelocidad(direccionActual.por(velocidadEscalar));
        mover(delta, mundo);


        if (ahoraMs >= cooldownDisparoHasta) {
            disparar();
            cooldownDisparoHasta = ahoraMs + 2000;
        }
    }

    private Vector direccionAleatoria() {
        return switch (rng.nextInt(4)) {
            case 0 -> new Vector(0, -1);
            case 1 -> new Vector(0, +1);
            case 2 -> new Vector(-1, 0);
            default -> new Vector(+1, 0);
        };
    }

    private void disparar() {
        // TODO: sistema de proyectiles
        //System.out.println("Enemigo dispara!");
    }

    @Override
    public String spriteId() {
        return switch (tipo) {
            case fastEnemy  -> frameAnimacion == 0 ? JuegoConfig.SPRITE_ENEMY_FAST_0    : JuegoConfig.SPRITE_ENEMY_FAST_1;
            case heavyEnemy   -> frameAnimacion == 0 ? JuegoConfig.SPRITE_ENEMY_HEAVY_0   : JuegoConfig.SPRITE_ENEMY_HEAVY_1;
            case powerfulEnemy   -> frameAnimacion == 0 ? JuegoConfig.SPRITE_ENEMY_POWER_0   : JuegoConfig.SPRITE_ENEMY_POWER_1;
            case regularEnemy -> frameAnimacion == 0 ? JuegoConfig.SPRITE_ENEMY_REGULAR_0 : JuegoConfig.SPRITE_ENEMY_REGULAR_1;
            case JUGADOR -> throw new IllegalStateException("No debe ser un jugador");
        };
    }


}
