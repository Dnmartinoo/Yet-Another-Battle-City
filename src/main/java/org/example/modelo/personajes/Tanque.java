package org.example.modelo.personajes;

import org.example.modelo.fisica.Cuerpo;
import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;
import org.example.modelo.fisica.utils.ColisionUtils;
import org.example.modelo.mundo.MundoFisico;

public abstract class Tanque implements Cuerpo {
    // --- Estado base que Jugador/Enemigo esperan ---
    protected TipoPersonaje tipo;       // <- Jugador lo usa (tipo.vidaBase(), tipo.obtenerVelocidad())
    protected int vidaActual;           // <- Jugador lo usa (morir() lo resetea)
    protected Vector posicion;
    protected Rectangulo hitboxLocal;   // relativo al (0,0)
    protected double velocidadEscalar;  // px/s (magnitud base del tipo)
    protected Vector velocidadActual = Vector.CERO; // <- Jugador usa setVelocidad(...)

    // Defaults seguros
    protected static final int DEFAULT_TILE = 20;
    protected static final double DEFAULT_SPEED = 60.0;

    // 1) Ctor completo
    public Tanque(Vector posicion, Rectangulo hitboxLocal, double velocidadEscalar, TipoPersonaje tipo) {
        this.posicion = posicion;
        this.hitboxLocal = hitboxLocal;
        this.velocidadEscalar = velocidadEscalar;
        this.tipo = tipo;
        this.vidaActual = (tipo != null) ? tipo.vidaBase() : 1; // fallback
    }

    // 2) Ctor compat con TipoPersonaje (el que usás en Jugador/Enemigo)
    public Tanque(TipoPersonaje tipo, Vector posicion) {
        this(
                posicion,
                new Rectangulo(0, 0, DEFAULT_TILE, DEFAULT_TILE),
                // si tu TipoPersonaje no tiene velocidadBase(), podés mapear a DEFAULT_SPEED
                (tieneVelBase(tipo) ? tipo.obtenerVelocidad() : DEFAULT_SPEED),
                tipo
        );
    }

    // 3) Ctor sin tipo (por si alguna clase vieja lo usa)
    public Tanque(Vector posicion) {
        this(posicion, new Rectangulo(0, 0, DEFAULT_TILE, DEFAULT_TILE), DEFAULT_SPEED, null);
    }

    private static boolean tieneVelBase(TipoPersonaje t) {
        try { t.obtenerVelocidad(); return true; } catch (Throwable __) { return false; }
    }

    // --- Cuerpo ---
    @Override public Vector posicion() { return posicion; }
    @Override public void setPosicion(Vector nuevaPosicion) { this.posicion = nuevaPosicion; }
    @Override public Rectangulo hitbox() { return hitboxLocal.trasladado(posicion); }
    @Override public boolean solido() { return true; }

    // velocidad() del Cuerpo -> la velocidad INSTANTÁNEA (vector), no la escalar base
    @Override public Vector velocidad() { return velocidadActual; }

    // Setter que Jugador invoca desde Control.moverXxx()
    public void setVelocidad(Vector v) {
        this.velocidadActual = v;
    }

    // Vida / daño que Jugador espera
    public boolean estaVivo() { return vidaActual > 0; }

    public void recibirImpacto(int dano) {
        vidaActual = Math.max(0, vidaActual - Math.max(0, dano));
    }

    // Movimiento con resolución por ejes (colisión con bloques)
    public void mover(Vector delta, MundoFisico mundo) {
        if (delta.x() == 0 && delta.y() == 0) return;

        // Eje X
        if (delta.x() != 0) {
            double nx = posicion.x() + delta.x();
            Rectangulo hX = hitboxLocal.trasladado(new Vector(nx - posicion.x(), 0));
            if (ColisionUtils.colisionaConBloqueSolido(hX, mundo)) {
                nx = ColisionUtils.ajustarX(posicion.x(), posicion.y(), delta.x(), hitbox(), mundo);
            }
            posicion = new Vector(nx, posicion.y());
        }

        // Eje Y
        if (delta.y() != 0) {
            double ny = posicion.y() + delta.y();
            Rectangulo hY = hitboxLocal.trasladado(new Vector(0, ny - posicion.y()));
            if (ColisionUtils.colisionaConBloqueSolido(hY, mundo)) {
                ny = ColisionUtils.ajustarY(posicion.x(), posicion.y(), delta.y(), hitbox(), mundo);
            }
            posicion = new Vector(posicion.x(), ny);
        }
    }
}
