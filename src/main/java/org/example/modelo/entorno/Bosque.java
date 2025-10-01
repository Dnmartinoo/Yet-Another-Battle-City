package org.example.modelo.entorno;

public class Bosque implements Bloque {
    @Override public boolean bloqueaMovimiento() {
        return false;
    }

    @Override public boolean bloqueaProyectiles() {
        return false;
    }

    @Override public boolean esDestruible() {
        return false;
    }

    @Override public boolean estaDestruido() {
        return false;
    }

    @Override public boolean ocutlaVision() {
        return true;
    }

    @Override
    public ResultadoImpacto recibirImpacto(int dano) {
        return ResultadoImpacto.balaAtraviesa();
    }
}
