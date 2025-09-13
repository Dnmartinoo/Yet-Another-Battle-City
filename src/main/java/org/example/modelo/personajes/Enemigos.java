package org.example.modelo.personajes;

public class Enemigos {
    private TipoPersonajes enemigo;

    public Enemigos(TipoPersonajes enemigo){
        if (!enemigo.esEnemigo())
            throw new IllegalArgumentException("Tipo no enemigo: " + enemigo);
        this.enemigo = enemigo;
    }

    public double Velocidad(){
        return enemigo.obtenerVelocidad();
    }
    public double Cadencia() {
        return  enemigo.obtenerCadencia();
    }
    public int Vida(){
        return enemigo.obtenerVida();
    }
    public boolean estaVivo() {
        return Vida() > 0;
    }
    public void recibirImpacto() {
        if (estaVivo()) enemigo.reducirVida();
    }
}
