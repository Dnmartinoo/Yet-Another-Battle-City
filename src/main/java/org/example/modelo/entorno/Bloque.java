package org.example.modelo.entorno;

import org.example.modelo.fisica.Cuerpo;

public interface Bloque extends Cuerpo {
    boolean bloqueaMovimiento();
    boolean bloqueaProyectiles();
    boolean esDestruible();
    boolean estaDestruido();
    ResultadoImpacto recibirImpacto(int dano);
    default boolean ocultaVisual() { return false; }
    default boolean esBase() { return false; }

}
