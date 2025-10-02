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
    private boolean finPartida = false;
    public void configurarCampaña(List<NivelData> niveles, NivelFactory factory) {
        if (niveles == null || niveles.isEmpty())
            throw new IllegalArgumentException("La campaña no puede estar vacía");
        if (factory == null)
            throw new IllegalArgumentException("La factory de niveles no puede ser null");

        this.campaña = List.copyOf(niveles);
        this.factory = factory;
        this.idxNivel = 0;
        this.finPartida = false;
        cargarNivelDeCampañaActual();
    }

    private void cargarNivelDeCampañaActual() {
        NivelData data = campaña.get(idxNivel);
        this.nivelActual = factory.crear(data);
        this.nivelActual.setNumeroDeNivel(idxNivel + 1);
        this.nivelActual.crearMundo(data);

        this.fase = Fase.JUGANDO;
        this.faseHastaMs = 0L;
        this.finPartida = false;
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

    public void tick(long ahoraMs, InputEstado j1, InputEstado j2) {
        if (nivelActual == null) return;

        switch (fase) {
            case JUGANDO -> {
                nivelActual.tick(ahoraMs, j1, j2);
                var est = nivelActual.estado();

                if (est.derrota()) {
                    // Derrota del jugador
                    fase = Fase.DERROTA;
                    faseHastaMs = ahoraMs + JuegoConfig.DEFEAT_SCREEN_MS;
                } else if (est.victoria()) {
                    // Victoria de este nivel
                    fase = Fase.VICTORIA;
                    faseHastaMs = ahoraMs + JuegoConfig.VICTORY_SCREEN_MS;
                }
            }

            case VICTORIA -> {
                // Esperamos que pase el tiempo del overlay de victoria
                if (ahoraMs >= faseHastaMs) {
                    if (haySiguienteNivel()) {
                        avanzarASiguienteNivel();
                    } else {
                        // ✅ No hay más niveles → Fin de campaña → Victoria final
                        finPartida = true;
                        nivelActual = null;
                        fase = Fase.VICTORIA; // se queda en victoria para que UI dibuje overlay final
                    }
                }
            }

            case DERROTA -> {
                // Esperamos el tiempo de overlay de derrota
                if (ahoraMs >= faseHastaMs) {
                    // ✅ Fin de partida por derrota → volver al menú
                    finPartida = true;
                    nivelActual = null;
                    fase = Fase.DERROTA; // se queda en derrota para que UI dibuje overlay
                }
            }
        }
    }

    public boolean partidaFinalizada() { return finPartida; }

    public boolean enVictoria() { return fase == Fase.VICTORIA; }
    public boolean enDerrota()  { return fase == Fase.DERROTA;  }
    public boolean enJuego()    { return fase == Fase.JUGANDO;  }

    public EstadoNivel estado() {
        return (nivelActual != null) ? nivelActual.estado() : EstadoNivel.empty();
    }

    public Nivel nivel() { return nivelActual; }}

