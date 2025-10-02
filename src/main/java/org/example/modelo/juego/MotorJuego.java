// src/main/java/org/example/modelo/juego/MotorJuego.java
package org.example.modelo.juego;

import java.util.List;

public class MotorJuego {
    private enum Fase {JUGANDO, VICTORIA, DERROTA}

    private Nivel nivelActual;

    private List<NivelData> campaña = null;
    private int idxNivel = -1;

    public interface NivelFactory {
        Nivel crear(NivelData data);
    }

    private NivelFactory factory = null;

    private Fase fase = Fase.JUGANDO;
    private long faseHastaMs = 0L;

    public void configurarCampaña(List<NivelData> niveles, NivelFactory factory) {
        if (niveles == null || niveles.isEmpty())
            throw new IllegalArgumentException("La campaña no puede estar vacía");
        if (factory == null)
            throw new IllegalArgumentException("La factory de niveles no puede ser null");

        this.campaña = List.copyOf(niveles);
        this.factory = factory;
        this.idxNivel = 0;
        cargarNivelDeCampañaActual();
    }

    private void cargarNivelDeCampañaActual() {
        NivelData data = campaña.get(idxNivel);
        this.nivelActual = factory.crear(data);
        this.nivelActual.setNumeroDeNivel(idxNivel + 1);
        this.nivelActual.crearMundo(data);

        this.fase = Fase.JUGANDO;
        this.faseHastaMs = 0L;
    }

    private boolean haySiguienteNivel() {
        return campaña != null && (idxNivel + 1) < campaña.size();
    }

    private void avanzarASiguienteNivel() {
        if (campaña == null) return;
        if (!haySiguienteNivel()) return;
        idxNivel++;
        cargarNivelDeCampañaActual();
    }

    private void reiniciarNivelActual() {
        if (campaña != null && idxNivel >= 0) {
            cargarNivelDeCampañaActual();
        } else if (nivelActual != null) {
            // Modo "manual": si alguien usa cargarNivel(Nivel) sin campaña,
            // no hacemos nada especial (quedás libre de decidir en tu UI).
        }
    }

    public void tick(long ahoraMs, InputEstado j1, InputEstado j2) {
        if (nivelActual == null) return;

        switch (fase) {
            case JUGANDO -> {
                nivelActual.tick(ahoraMs, j1, j2);
                var est = nivelActual.estado();
                if (est.derrota()) {
                    fase = Fase.DERROTA;
                    faseHastaMs = ahoraMs + JuegoConfig.DEFEAT_SCREEN_MS;
                } else if (est.victoria()) {
                    fase = Fase.VICTORIA;
                    faseHastaMs = ahoraMs + JuegoConfig.VICTORY_SCREEN_MS;
                }
            }
            case VICTORIA -> {
                if (ahoraMs >= faseHastaMs) {
                    if (haySiguienteNivel()) {
                        avanzarASiguienteNivel();
                    }
                }
            }
            case DERROTA -> {
                if (ahoraMs >= faseHastaMs) {
                    reiniciarNivelActual();
                }
            }
        }
    }
    public EstadoNivel estado() {
        return (nivelActual != null) ? nivelActual.estado() : EstadoNivel.empty();
    }

    public Nivel nivel() { return nivelActual; }}

