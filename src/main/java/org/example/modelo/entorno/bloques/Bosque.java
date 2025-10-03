package org.example.modelo.entorno.bloques;

import org.example.modelo.entorno.BloqueBase;
import org.example.modelo.entorno.ResultadoImpacto;
import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.config.JuegoConfig;
import org.example.modelo.juego.Spriteeable;

public class Bosque extends BloqueBase implements Spriteeable {
    public Bosque(Vector posicion, int tileSize) { super(posicion, tileSize); }

    @Override public boolean bloqueaMovimiento() { return false; }
    @Override public boolean bloqueaProyectiles() { return false; }
    @Override public boolean esDestruible() { return false; }

    @Override
    public ResultadoImpacto recibirImpacto(int dano) {
        return ResultadoImpacto.balaAtraviesa();
    }

    @Override public boolean ocultaVisual() { return true; }

    @Override public String spriteId() { return JuegoConfig.SPRITE_FOREST; }
}
