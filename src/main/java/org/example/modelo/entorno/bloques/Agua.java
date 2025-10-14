package org.example.modelo.entorno.bloques;

import org.example.modelo.entorno.BloqueBase;
import org.example.modelo.entorno.ResultadoImpacto;
import org.example.modelo.fisica.Vector;

public class Agua extends BloqueBase {
    public Agua(Vector posicion, int tileSize) { super(posicion, tileSize); }

    @Override public boolean bloqueaMovimiento() { return true; }
    @Override public boolean bloqueaProyectiles() { return false; }
    @Override public boolean esDestruible() { return false; }

    @Override
    public ResultadoImpacto recibirImpacto(int dano) {
        return ResultadoImpacto.ATRAVIESA;
    }
}
