package org.example.modelo.entorno;

import org.example.modelo.fisica.Cuerpo;
import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;

public final class BloqueCuerpo implements Bloque, Cuerpo {
    private final Bloque delegado;
    private Vector pos;
    private final double w, h;

    public BloqueCuerpo(Bloque delegado, Vector pos, double w, double h) {
        this.delegado = delegado;
        this.pos = pos;
        this.w = w;
        this.h = h;
    }

    // --- Cuerpo ---
    @Override public Rectangulo hitbox() { return new Rectangulo(pos.x(), pos.y(), w, h); }
    @Override public Vector posicion()   { return pos; }
    @Override public void setPosicion(Vector p) { this.pos = p; }
    @Override public Vector velocidad()  { return Vector.CERO; }
    @Override public boolean solido()    { return delegado.bloqueaMovimiento(); }

    // --- Delego Bloque ---
    @Override public boolean bloqueaMovimiento()     { return delegado.bloqueaMovimiento(); }
    @Override public boolean bloqueaProyectiles()    { return delegado.bloqueaProyectiles(); }
    @Override public boolean esDestruible()          { return delegado.esDestruible(); }
    @Override public boolean estaDestruido()         { return delegado.estaDestruido(); }
    @Override public ResultadoImpacto recibirImpacto(int dano) { return delegado.recibirImpacto(dano); }
    @Override public boolean ocultaVisual()          { return delegado.ocultaVisual(); }

    // Por si necesitás detectar la BASE luego
    public Bloque delegado() { return delegado; }
}
