package org.example.modelo.entorno.bloques;

import org.example.modelo.entorno.BloqueBase;
import org.example.modelo.entorno.ResultadoImpacto;
import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.config.JuegoConfig;
import org.example.modelo.juego.Spriteeable;

public class Acero extends BloqueBase implements Spriteeable {
    public Acero(Vector posicion, int tileSize) { super(posicion, tileSize); }

    @Override public boolean bloqueaMovimiento() { return true; }
    @Override public boolean bloqueaProyectiles() { return true; }
    @Override public boolean esDestruible() { return false; }

    @Override
    public ResultadoImpacto recibirImpacto(int dano) {
        return ResultadoImpacto.DETENIDO;
    }

    @Override public boolean esAcero() { return true; }

    @Override public String spriteId() { return JuegoConfig.SPRITE_STEEL; }
}
