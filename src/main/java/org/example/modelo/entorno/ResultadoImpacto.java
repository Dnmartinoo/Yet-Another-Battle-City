package org.example.modelo.entorno;


public class ResultadoImpacto {

    public enum Evento {
        NINGUNO,
        BLOQUE_DESTRUIDO,
        BASE_DESTRUIDA
    }


    public final boolean proyectilAtraviesa;


    public final Evento evento;

    private ResultadoImpacto(boolean proyectilAtraviesa, Evento evento) {
        this.proyectilAtraviesa = proyectilAtraviesa;
        this.evento = evento;
    }


    public boolean detener() {
        return !proyectilAtraviesa;
    }

    public static ResultadoImpacto balaSeDetiene() {
        return new ResultadoImpacto(false, Evento.NINGUNO);
    }

    public static ResultadoImpacto balaAtraviesa() {
        return new ResultadoImpacto(true, Evento.NINGUNO);
    }


    @Override
    public String toString() {
        return "ResultadoImpacto{" +
                "proyectilAtraviesa=" + proyectilAtraviesa +
                ", evento=" + evento +
                '}';
    }
}
