package org.example.modelo.entorno;

public interface Bloque {
    boolean bloqueaMovimiento();

    boolean bloqueaProyectiles();

    boolean esDestruible();

    boolean estaDestruido();

    default boolean ocutlaVision() { return false; }

}
