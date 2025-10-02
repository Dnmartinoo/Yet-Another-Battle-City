package org.example.modelo.entorno;

import org.example.modelo.fisica.Vector;

public abstract class BloqueBase implements Bloque {
    protected boolean destruido = false;

    @Override
    public boolean estaDestruido() {
        return destruido;
    }

    @Override
    public boolean solido() {
        return true; // por default, casi todos lo son
    }

    @Override
    public Vector velocidad() {
        return new Vector(0, 0); // los bloques no se mueven
    }
}
