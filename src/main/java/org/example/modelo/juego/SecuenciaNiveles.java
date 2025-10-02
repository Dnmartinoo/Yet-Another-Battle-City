package org.example.modelo.juego;

import java.util.List;

public final class SecuenciaNiveles {
    private final List<NivelData> niveles;
    private int indice = 0;

    public SecuenciaNiveles(List<NivelData> niveles) {
        if (niveles == null || niveles.isEmpty())
            throw new IllegalArgumentException("La secuencia de niveles no puede estar vacía");
        this.niveles = List.copyOf(niveles);
    }

    public NivelData actual() { return niveles.get(indice); }
    public int numeroActual() { return indice + 1; }
    public boolean haySiguiente() { return indice + 1 < niveles.size(); }

    public NivelData avanzar() {
        if (!haySiguiente()) return null;
        indice++;
        return niveles.get(indice);
    }

    public void reiniciar() { indice = 0; }
}
