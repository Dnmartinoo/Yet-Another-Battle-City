package org.example.modelo.entorno.bloques;

import org.example.modelo.entorno.BloqueBase;
import org.example.modelo.entorno.ResultadoImpacto;
import org.example.modelo.fisica.Vector;

public class Base extends BloqueBase {
    public Base(Vector posicion, int tileSize) { super(posicion, tileSize); }

    @Override public boolean bloqueaMovimiento() { return !destruido; }
    @Override public boolean bloqueaProyectiles() { return !destruido; }
    @Override public boolean esDestruible() { return true; }

    @Override
    public ResultadoImpacto recibirImpacto(int dano) {
        destruido = true;
        return ResultadoImpacto.DETENIDO;
    }

    @Override public boolean esBase() { return true; }
}
