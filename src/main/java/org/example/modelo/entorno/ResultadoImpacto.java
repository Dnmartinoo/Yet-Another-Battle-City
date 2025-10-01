package org.example.modelo.entorno;

public class ResultadoImpacto {
    public enum Evento {
        NINGUNO,
        BLOQUE_DESTRUIDO,
        BASE_DESTRUIDA
    }

    public boolean proyectilAtraviesa;

    public Evento evento;

    private ResultadoImpacto(boolean proyectilAtraviesa, Evento evento) {
        this.proyectilAtraviesa = proyectilAtraviesa;
        this.evento = evento;
    }

    public static ResultadoImpacto balaSeDetiene() {
        return new ResultadoImpacto(false, Evento.NINGUNO);
    }

    public static ResultadoImpacto balaAtraviesa() {
        return new ResultadoImpacto(true, Evento.NINGUNO);
    }

    public static ResultadoImpacto bloqueDestruido(boolean balaAtraviesa) {
        return new ResultadoImpacto(balaAtraviesa, Evento.BLOQUE_DESTRUIDO);
    }

    public static ResultadoImpacto baseDestruida() {
        return new ResultadoImpacto(false, Evento.BASE_DESTRUIDA);
    }
}
