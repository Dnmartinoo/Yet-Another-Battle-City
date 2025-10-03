package org.example.modelo.juego;

import java.util.List;

public class MotorJuego {
    private enum Fase { JUGANDO, VICTORIA, DERROTA }

    private Nivel nivelActual;
    private List<NivelData> campaña;
    private int idxNivel;

    public interface NivelFactory {
        Nivel crear(NivelData data);
    }

    private NivelFactory factory;
    private Fase fase;
    private long faseHastaMs;
    private boolean finPartida;

    // ==================== Constructor ====================

    public MotorJuego() {
        this.campaña = null;
        this.idxNivel = JuegoConfig.NO_NIVEL;
        this.factory = null;
        this.fase = Fase.JUGANDO;
        this.faseHastaMs = JuegoConfig.TIEMPO_INICIAL_MS;
        this.finPartida = JuegoConfig.PARTIDA_NO_FINALIZADA;
    }

    // ==================== Configuración de campaña ====================

    public void configurarCampaña(List<NivelData> niveles, NivelFactory factory) {
        if (niveles == null || niveles.isEmpty()) {
            throw new IllegalArgumentException("La campaña no puede estar vacía");
        }
        if (factory == null) {
            throw new IllegalArgumentException("La factory de niveles no puede ser null");
        }

        this.campaña = List.copyOf(niveles);
        this.factory = factory;
        this.idxNivel = 0;
        this.finPartida = JuegoConfig.PARTIDA_NO_FINALIZADA;
        cargarNivelDeCampañaActual();
    }

    private void cargarNivelDeCampañaActual() {
        if (campaña == null || factory == null || idxNivel < 0 || idxNivel >= campaña.size()) {
            nivelActual = null;
            finPartida = JuegoConfig.PARTIDA_FINALIZADA;
            fase = Fase.DERROTA;
            faseHastaMs = JuegoConfig.TIEMPO_INICIAL_MS;
            return;
        }

        NivelData data = campaña.get(idxNivel);
        this.nivelActual = factory.crear(data);
        this.nivelActual.setNumeroDeNivel(idxNivel + 1);
        this.nivelActual.crearMundo(data);

        this.fase = Fase.JUGANDO;
        this.faseHastaMs = JuegoConfig.TIEMPO_INICIAL_MS;
        this.finPartida = JuegoConfig.PARTIDA_NO_FINALIZADA;
    }

    private boolean haySiguienteNivel() {
        return campaña != null && (idxNivel + 1) < campaña.size();
    }

    private void avanzarASiguienteNivel() {
        if (!haySiguienteNivel()) return;
        idxNivel++;
        cargarNivelDeCampañaActual();
    }

    // ==================== Bucle principal ====================

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
                if (ahoraMs < faseHastaMs) return;
                if (haySiguienteNivel()) {
                    avanzarASiguienteNivel();
                } else {
                    finPartida = JuegoConfig.PARTIDA_FINALIZADA;
                    nivelActual = null;
                }
            }
            case DERROTA -> {
                if (ahoraMs < faseHastaMs) return;
                finPartida = JuegoConfig.PARTIDA_FINALIZADA;
                nivelActual = null;
            }
        }
    }

    // ==================== Consultas de estado ====================

    public boolean partidaFinalizada() { return finPartida; }
    public boolean enVictoria() { return fase == Fase.VICTORIA; }
    public boolean enDerrota()  { return fase == Fase.DERROTA; }

    public EstadoNivel estado() {
        if (nivelActual != null) return nivelActual.estado();
        return EstadoNivel.empty();
    }

    public Nivel nivel() { return nivelActual; }
}
