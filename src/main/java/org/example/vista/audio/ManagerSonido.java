package org.example.vista.audio;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

public final class ManagerSonido {

    private static final ManagerSonido INSTANCE = new ManagerSonido();

    private final Map<String, AudioClip> sonidos = new HashMap<>();
    private MediaPlayer musicaLoop;
    private ManagerSonido() {}
    public static ManagerSonido get() {
        return INSTANCE;
    }

    public void cargarEfecto(String id, String ruta) {
        try {
            var url = getClass().getResource(ruta);
            if (url == null) {
                System.err.println("No encontré efecto: " + ruta);
                return;
            }
            sonidos.put(id, new AudioClip(url.toString()));
        } catch (Exception e) {
            System.err.println("Error cargando efecto " + ruta + ": " + e.getMessage());
        }
    }

    public void cargarMusica(String ruta) {
        try {
            var url = getClass().getResource(ruta);
            if (url == null) {
                System.err.println("No encontré música: " + ruta);
                return;
            }
            Media media = new Media(url.toString());
            musicaLoop = new MediaPlayer(media);
            musicaLoop.setCycleCount(MediaPlayer.INDEFINITE);
            musicaLoop.setVolume(0.5);
        } catch (Exception e) {
            System.err.println("Error cargando música " + ruta + ": " + e.getMessage());
        }
    }

    public void playEfecto(String id) {
        AudioClip clip = sonidos.get(id);
        if (clip != null) {
            clip.play();
        }
    }

    public void playMusica() {
        if (musicaLoop != null) musicaLoop.play();
    }
}
