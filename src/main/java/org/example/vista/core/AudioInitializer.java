package org.example.vista.core;

import org.example.vista.audio.ManagerSonido;
import org.example.vista.config.ConstantesUI;

public class AudioInitializer {

    private final ManagerSonido sonido;

    public AudioInitializer(ManagerSonido sonido) {
        this.sonido = sonido;
    }

    public void inicializar() {
        //EFECTOS
        sonido.cargarEfecto("muerteTanque",     ConstantesUI.SFX_MUERTE_TANQUE);
        sonido.cargarEfecto("derrota",          ConstantesUI.SFX_DERROTA);
        sonido.cargarEfecto("impactoBlindado",  ConstantesUI.SFX_IMPACTO_BLIND);
        sonido.cargarEfecto("impactoAcero",     ConstantesUI.SFX_IMPACTO_ACERO);
        sonido.cargarEfecto("disparar",         ConstantesUI.SFX_DISPARAR);
        sonido.cargarEfecto("ladrilloRoto",     ConstantesUI.SFX_LADRILLO_ROTO);

        // MUSICA
        sonido.cargarMusica(ConstantesUI.MUSICA_LOOP);
        sonido.playMusica();
    }
}
