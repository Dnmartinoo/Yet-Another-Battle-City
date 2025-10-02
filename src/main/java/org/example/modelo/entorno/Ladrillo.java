package org.example.modelo.entorno;

public class Ladrillo implements Bloque{
    private int hp = 3;
    private boolean destruido = false;

    @Override public boolean bloqueaMovimiento() {
        return !destruido;
    }

    @Override public boolean bloqueaProyectiles() {
        return !destruido;
    }

    @Override public boolean esDestruible() {
        return true;
    }

    @Override public boolean estaDestruido() {
        return destruido;
    }

    @Override
    public ResultadoImpacto recibirImpacto(int dano) {
        if (destruido) return ResultadoImpacto.balaAtraviesa();
        hp -= dano;

        if (hp <= 0) {
            destruido = true;
            return ResultadoImpacto.bloqueDestruido(true);
        }
        return ResultadoImpacto.balaSeDetiene();
    }
}
