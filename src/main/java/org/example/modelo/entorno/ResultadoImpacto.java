package org.example.modelo.entorno;

/**
 * Representa el resultado de un impacto de proyectil contra un bloque o base.
 * Indica si la bala atraviesa, si debe detenerse y qué evento ocurrió.
 */
public class ResultadoImpacto {

    public enum Evento {
        NINGUNO,
        BLOQUE_DESTRUIDO,
        BASE_DESTRUIDA
    }

    /** True si el proyectil atraviesa el objeto y sigue su recorrido */
    public final boolean proyectilAtraviesa;

    /** Evento especial generado por el impacto */
    public final Evento evento;

    private ResultadoImpacto(boolean proyectilAtraviesa, Evento evento) {
        this.proyectilAtraviesa = proyectilAtraviesa;
        this.evento = evento;
    }

    /** Devuelve true si el proyectil debe detenerse (no atraviesa). */
    public boolean detener() {
        return !proyectilAtraviesa;
    }

    // ======================
    // Factory methods
    // ======================

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

    @Override
    public String toString() {
        return "ResultadoImpacto{" +
                "proyectilAtraviesa=" + proyectilAtraviesa +
                ", evento=" + evento +
                '}';
    }
}
