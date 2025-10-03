package org.example.vista.core;

import org.example.modelo.audio.ManagerSonido;
import org.example.vista.config.ConstantesUI;

final class AudioInitializer {
    private AudioInitializer() {}

    static void inicializar() {
        ManagerSonido.get().cargarEfecto("muerteTanque",     ConstantesUI.SFX_MUERTE_TANQUE);
        ManagerSonido.get().cargarEfecto("derrota",          ConstantesUI.SFX_DERROTA);
        ManagerSonido.get().cargarEfecto("impactoBlindado",  ConstantesUI.SFX_IMPACTO_BLIND);
        ManagerSonido.get().cargarEfecto("impactoAcero",     ConstantesUI.SFX_IMPACTO_ACERO);
        ManagerSonido.get().cargarEfecto("disparar",         ConstantesUI.SFX_DISPARAR);
        ManagerSonido.get().cargarEfecto("ladrilloRoto",     ConstantesUI.SFX_LADRILLO_ROTO);

        // Música
        ManagerSonido.get().cargarMusica(ConstantesUI.MUSICA_LOOP);
        ManagerSonido.get().playMusica();
    }
}
