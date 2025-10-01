package org.example.modelo.personajes;

public enum TipoPersonaje {
    JUGADOR(1.0, 3, 1.0),
    BASICO(1.0, 1, 1.0),
    RAPIDO(2.0, 1, 1.0),
    POTENTE(1.2, 1, 2.0),
    BLINDADO(0.8, 3, 1.0);

    private double velocidad;
    private int vidaBase;
    private double cadencia;

    TipoPersonaje(double velocidad, int vidaBase, double cadencia) {
        this.velocidad = velocidad;
        this.vidaBase = vidaBase;
        this.cadencia = cadencia;
    }
    public double obtenerVelocidad() {
        return velocidad;
    }
    public int vidaBase() {
        return vidaBase;
    }
    public double obtenerCadencia() {
        return cadencia;
    }

    public void reducirVida() {vidaBase--;}

    public boolean esJugador() { return this == JUGADOR; }
    public boolean esEnemigo() { return this != JUGADOR; }

}
