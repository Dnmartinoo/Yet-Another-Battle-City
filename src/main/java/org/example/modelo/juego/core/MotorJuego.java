package org.example.modelo.juego.core;
import org.example.modelo.juego.config.JuegoConfig;
import org.example.modelo.juego.estado.EstadoNivel;
import org.example.modelo.juego.input.InputEstado;

import java.util.List;

public final class MotorJuego {
    private enum Fase { JUGANDO, VICTORIA, DERROTA }

    private Nivel nivelActual;
    private List<NivelData> campaña;
    private int idxNivel;

    private NivelFactory factory;
    private Fase fase;
    private long faseHastaMs;
    private boolean finPartida;

    public interface NivelFactory {
        Nivel crear(NivelData data);
    }

    public MotorJuego() {
        this.idxNivel = JuegoConfig.NO_NIVEL;
        this.fase = Fase.JUGANDO;
        this.faseHastaMs = JuegoConfig.TIEMPO_INICIAL_MS;
        this.finPartida = JuegoConfig.PARTIDA_NO_FINALIZADA;
    }

    public void configurarCampaña(List<NivelData> niveles, NivelFactory factory) {
        this.campaña = List.copyOf(niveles);
        this.factory = factory;
        this.idxNivel = 0;
        this.finPartida = JuegoConfig.PARTIDA_NO_FINALIZADA;
        cargarNivelDeCampañaActual();
    }

    private void cargarNivelDeCampañaActual() {
        if (idxNivel < 0 || idxNivel >= campaña.size()) {
            nivelActual = null;
            finPartida = true;
            fase = Fase.DERROTA;
            return;
        }

        NivelData data = campaña.get(idxNivel);
        this.nivelActual = factory.crear(data);
        this.nivelActual.setNumeroDeNivel(idxNivel + 1);
        this.nivelActual.crearMundo(data);

        this.fase = Fase.JUGANDO;
        this.finPartida = JuegoConfig.PARTIDA_NO_FINALIZADA;
    }

    public void tick(long ahoraMs, InputEstado j1, InputEstado j2) {
        if (nivelActual == null) return;
        switch (fase) {
            case JUGANDO -> tickJugando(ahoraMs, j1, j2);
            case VICTORIA -> tickVictoria(ahoraMs);
            case DERROTA -> tickDerrota(ahoraMs);
        }
    }

    private void tickJugando(long ahoraMs, InputEstado j1, InputEstado j2) {
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

    private void tickVictoria(long ahoraMs) {
        if (ahoraMs < faseHastaMs) return;
        if ((idxNivel + 1) < campaña.size()) {
            idxNivel++;
            cargarNivelDeCampañaActual();
        } else {
            finPartida = JuegoConfig.PARTIDA_FINALIZADA;
            nivelActual = null;
        }
    }

    private void tickDerrota(long ahoraMs) {
        if (ahoraMs < faseHastaMs) return;
        finPartida = JuegoConfig.PARTIDA_FINALIZADA;
        nivelActual = null;
    }

    public boolean partidaFinalizada() { return finPartida; }
    public EstadoNivel estado() { return nivelActual != null ? nivelActual.estado() : EstadoNivel.empty(); }
    public Nivel nivel() { return nivelActual; }
    public boolean enVictoria() { return fase == Fase.VICTORIA; }
    public boolean enDerrota()  { return fase == Fase.DERROTA; }
}
