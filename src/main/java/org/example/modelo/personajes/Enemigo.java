package org.example.modelo.personajes;

import org.example.modelo.fisica.MundoFisico;
import org.example.modelo.fisica.Vector;

import java.util.Random;

public class Enemigo extends Tanque {
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

    // ahora recibe MundoFisico
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
        mover(delta, mundo);


        if (ahoraMs >= cooldownDisparoHasta) {
            disparar();
            cooldownDisparoHasta = ahoraMs + 2000; // 2s
        }
    }

    private Vector direccionAleatoria() {
        return switch (rng.nextInt(4)) {
            case 0 -> new Vector(0, -1); // arriba
            case 1 -> new Vector(0, +1);
            case 2 -> new Vector(-1, 0);
            default -> new Vector(+1, 0);
        };
    }

    private void disparar() {
        // TODO: sistema de proyectiles
        System.out.println("Enemigo dispara!");
    }
}
