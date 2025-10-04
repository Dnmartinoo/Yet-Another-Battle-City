package org.example.modelo.entorno;

public final class ResultadoImpacto {
    private final boolean proyectilAtraviesa;

    public static final ResultadoImpacto DETENIDO   = new ResultadoImpacto(false);
    public static final ResultadoImpacto ATRAVIESA  = new ResultadoImpacto(true);

    public ResultadoImpacto(boolean proyectilAtraviesa) {
        this.proyectilAtraviesa = proyectilAtraviesa;
    }

    public boolean detener() {
        return !proyectilAtraviesa;
    }

    @Override
    public String toString() {
        return "ResultadoImpacto{" +
                "proyectilAtraviesa=" + proyectilAtraviesa;
    }
}

