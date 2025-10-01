package org.example.modelo.entorno;

public class Base implements Bloque{
    private boolean destruida = false;

    @Override public boolean bloqueaMovimiento() {
        return destruida;
    }

    @Override public boolean bloqueaProyectiles() {
        return !destruida;
    }

    @Override public boolean esDestruible() {
        return true;
    }

    @Override public boolean estaDestruido() {
        return destruida;
    }

    @Override
    public ResultadoImpacto recibirImpacto(int dano) {
        if (destruida) return ResultadoImpacto.balaAtraviesa();
        destruida = true;
        return ResultadoImpacto.baseDestruida();
    }
}
