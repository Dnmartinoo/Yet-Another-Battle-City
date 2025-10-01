package org.example.modelo.entorno;

import javax.xml.transform.Result;

public class Acero implements Bloque {
    @Override public boolean bloqueaMovimiento() {
        return true;
    }

    @Override public boolean bloqueaProyectiles() {
        return true;
    }

    @Override public boolean esDestruible() {
        return false;
    }

    @Override public boolean estaDestruido() {
        return false;
    }

    @Override
    public ResultadoImpacto recibirImpacto(int dano) {
        return ResultadoImpacto.balaSeDetiene();
    }


}
