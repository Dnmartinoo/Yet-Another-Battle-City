package org.example.modelo.entorno.bloques;

import org.example.modelo.entorno.BloqueBase;
import org.example.modelo.entorno.ResultadoImpacto;
import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.config.JuegoConfig;
import org.example.modelo.juego.Spriteeable;

public class Base extends BloqueBase implements Spriteeable {
    public Base(Vector posicion, int tileSize) { super(posicion, tileSize); }

    @Override public boolean bloqueaMovimiento() { return !destruido; }
    @Override public boolean bloqueaProyectiles() { return !destruido; }
    @Override public boolean esDestruible() { return true; }

    @Override
    public ResultadoImpacto recibirImpacto(int dano) {
        destruido = true;
        return ResultadoImpacto.balaSeDetiene();
    }

    @Override public boolean esBase() { return true; }

    @Override public String spriteId() { return JuegoConfig.SPRITE_BASE; }
}
