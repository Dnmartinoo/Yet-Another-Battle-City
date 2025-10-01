package org.example.modelo.entorno;

public class Agua implements Bloque{
    @Override public boolean bloqueaMovimiento() {
        return true;
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

    @Override
    public ResultadoImpacto recibirImpacto(int dano) {
        return ResultadoImpacto.balaAtraviesa();
    }
}
