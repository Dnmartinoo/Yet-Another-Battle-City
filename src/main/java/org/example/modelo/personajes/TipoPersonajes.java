package org.example.modelo.personajes;

public enum TipoPersonajes {
    JUGADOR(1.0, 3, 1.0),
    BASICO(1.0, 1, 1.0),
    RAPIDO(2.0, 1, 1.0),
    POTENTE(1.2, 1, 2.0),
    BLINDADO(0.8, 3, 1.0);

    private double velocidad;
    private int vida;
    private double cadencia;

    TipoPersonajes(double velocidad, int vida, double cadencia) {
        this.velocidad = velocidad;
        this.vida = vida;
        this.cadencia = cadencia;
    }
    public double obtenerVelocidad() {
        return velocidad;
    }
    public int obtenerVida() {
        return vida;
    }
    public double obtenerCadencia() {
        return cadencia;
    }

    public boolean esJugador() { return this == JUGADOR; }
    public boolean esEnemigo() { return this != JUGADOR; }

}
