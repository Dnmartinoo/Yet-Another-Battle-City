package org.example.modelo.personajes;

import org.example.audio.ManagerSonido;
import org.example.modelo.fisica.Cuerpo;
import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;
import org.example.modelo.fisica.utils.ColisionUtils;
import org.example.modelo.fisica.MundoFisico;

public abstract class Tanque implements Cuerpo {
    protected TipoPersonaje tipo;
    protected int vidaActual;
    protected Vector posicion;
    protected Rectangulo hitboxLocal;
    protected double velocidadEscalar;
    protected Vector velocidadActual = Vector.CERO;
    protected double ultimaRotacion = 0.0;
    protected Vector ultimaDireccion = new Vector(0, -1);
    protected static final int DEFAULT_TILE = 20;
    protected static final double DEFAULT_SPEED = 60.0;


    protected int frameAnimacion = 0;



    public Tanque(Vector posicion, Rectangulo hitboxLocal, double velocidadEscalar, TipoPersonaje tipo) {
        this.posicion = posicion;
        this.hitboxLocal = hitboxLocal;
        this.velocidadEscalar = velocidadEscalar;
        this.tipo = tipo;
        this.vidaActual = (tipo != null) ? tipo.vidaBase() : 1;
    }


    public Tanque(TipoPersonaje tipo, Vector posicion) {
        this(
                posicion,
                new Rectangulo(0, 0, DEFAULT_TILE, DEFAULT_TILE),
                (tieneVelBase(tipo) ? tipo.obtenerVelocidad() : DEFAULT_SPEED),
                tipo
        );
    }


    public Tanque(Vector posicion) {
        this(posicion, new Rectangulo(0, 0, DEFAULT_TILE, DEFAULT_TILE), DEFAULT_SPEED, null);
    }

    private static boolean tieneVelBase(TipoPersonaje t) {
        try { t.obtenerVelocidad(); return true; } catch (Throwable __) { return false; }
    }


    @Override public Vector posicion() { return posicion; }
    @Override public void setPosicion(Vector nuevaPosicion) { this.posicion = nuevaPosicion; }
    @Override public Rectangulo hitbox() { return hitboxLocal.trasladado(posicion); }
    @Override public boolean solido() { return true; }


    @Override public Vector velocidad() { return velocidadActual; }


    public void setVelocidad(Vector v) {
        this.velocidadActual = v;
        if (!v.esCero()) {
            ultimaDireccion = v.normalizado();
        }
    }


    public boolean estaVivo() { return vidaActual > 0; }

    public void recibirImpacto(int dano) {
        vidaActual -= dano;
        if (vidaActual <= 0) {
            ManagerSonido.playEfecto("muerteTanque");
        }
        if(this.tipo == TipoPersonaje.heavyEnemy && estaVivo()) {
            ManagerSonido.playEfecto("impactoBlindado");
        }
    }


    public void mover(Vector delta, MundoFisico mundo) {
        if (delta.x() == 0 && delta.y() == 0) return;


        if (delta.x() != 0) {
            double nx = posicion.x() + delta.x();
            Rectangulo hX = hitboxLocal.trasladado(new Vector(nx - posicion.x(), 0));
            if (ColisionUtils.colisionaConBloqueSolido(hX, mundo)) {
                nx = ColisionUtils.ajustarX(posicion.x(), posicion.y(), delta.x(), hitbox(), mundo);
            }
            posicion = new Vector(nx, posicion.y());
        }


        if (delta.y() != 0) {
            double ny = posicion.y() + delta.y();
            Rectangulo hY = hitboxLocal.trasladado(new Vector(0, ny - posicion.y()));
            if (ColisionUtils.colisionaConBloqueSolido(hY, mundo)) {
                ny = ColisionUtils.ajustarY(posicion.x(), posicion.y(), delta.y(), hitbox(), mundo);
            }
            posicion = new Vector(posicion.x(), ny);
        }
    }

    public double rotacion() {
        if (velocidadActual.x() > 0) {
            ultimaRotacion = 90;
        } else if (velocidadActual.x() < 0) {
            ultimaRotacion = 270;
        } else if (velocidadActual.y() > 0) {
            ultimaRotacion = 180;
        } else if (velocidadActual.y() < 0) {
            ultimaRotacion = 0;
        }
        return ultimaRotacion;
    }

    public Vector ultimaDireccion() {
        return ultimaDireccion;
    }


}
