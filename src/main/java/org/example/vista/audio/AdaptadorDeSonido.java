package org.example.vista.audio;

import org.example.modelo.puertos.SoundPort;

public final class AdaptadorDeSonido implements SoundPort {
    private final ManagerSonido ms;

    public AdaptadorDeSonido(ManagerSonido ms) {
        this.ms = ms;
    }

    @Override
    public void playEffect(String soundId) {
        ms.playEfecto(soundId);
    }
}
