package org.example.vista.core;

import org.example.modelo.audio.ManagerSonido;
import org.example.vista.config.ConstantesUI;

final class AudioInitializer {
    private AudioInitializer() {}

    static void inicializar() {
        // SFX
        ManagerSonido.cargarEfecto("muerteTanque",     ConstantesUI.SFX_MUERTE_TANQUE);
        ManagerSonido.cargarEfecto("derrota",          ConstantesUI.SFX_DERROTA);
        ManagerSonido.cargarEfecto("impactoBlindado",  ConstantesUI.SFX_IMPACTO_BLIND);
        ManagerSonido.cargarEfecto("impactoAcero",     ConstantesUI.SFX_IMPACTO_ACERO);
        ManagerSonido.cargarEfecto("disparar",         ConstantesUI.SFX_DISPARAR);
        ManagerSonido.cargarEfecto("ladrilloRoto",     ConstantesUI.SFX_LADRILLO_ROTO);

        // Música
        ManagerSonido.cargarMusica(ConstantesUI.MUSICA_LOOP);
        ManagerSonido.playMusica();
    }
}
