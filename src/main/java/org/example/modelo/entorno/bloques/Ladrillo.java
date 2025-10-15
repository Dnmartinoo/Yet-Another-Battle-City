package org.example.modelo.entorno.bloques;

import org.example.modelo.entorno.BloqueBase;
import org.example.modelo.entorno.ResultadoImpacto;
import org.example.modelo.fisica.Vector;

public class Ladrillo extends BloqueBase{
    private int vida = 3;

    public Ladrillo(Vector posicion, int tileSize) {
        super(posicion, tileSize);
    }

    @Override public boolean bloqueaMovimiento() { return !destruido; }
    @Override public boolean bloqueaProyectiles() { return !destruido; }
    @Override public boolean esDestruible() { return true; }

    @Override
    public ResultadoImpacto recibirImpacto(int dano) {
        vida -= dano;
        if (vida <= 0) destruido = true;
        return ResultadoImpacto.DETENIDO;
    }

    @Override public boolean esLadrillo() { return true; }
}
