package org.example.modelo.juego;

import org.example.modelo.audio.ManagerSonido;
import org.example.modelo.disparo.Proyectil;
import org.example.modelo.entorno.*;
import org.example.modelo.fisica.MundoFisico;
import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;
import org.example.modelo.personajes.Enemigo;
import org.example.modelo.personajes.Jugador;
import org.example.modelo.powerup.PowerUp;

import java.util.*;

public class Nivel {

    private final List<Bloque> bloques = new ArrayList<>();
    private final List<Enemigo> enemigos = new ArrayList<>();
    private final List<Jugador> jugadores = new ArrayList<>(2);
    private final List<Proyectil> proyectiles = new ArrayList<>();
    private final List<PowerUp> poderes = new ArrayList<>();
    private final Spawner spawner;

    // Gestores
    private final GestorBalas gestorBalas = new GestorBalas();
    private final GestorPowerUps gestorPowerUps = new GestorPowerUps();

    private Bloque base;
    private MundoFisico mundo;
    private Rectangulo limites;

    private boolean victoria = false, derrota = false;

    private final Map<Enemigo, Long> proximoDisparoEnemigoMs = new IdentityHashMap<>();
    private final Map<Jugador, Long> proximoDisparoJugadorMs = new IdentityHashMap<>();

    private long lastMs = 0L;

    public List<Jugador> jugadores()  { return List.copyOf(jugadores); }
    public List<Enemigo> enemigos()   { return List.copyOf(enemigos); }
    public List<Bloque> bloques()     { return List.copyOf(bloques); }
    public List<Proyectil> proyectiles() { return List.copyOf(proyectiles); }
    public List<PowerUp> poderes() { return List.copyOf(poderes); }
    public Bloque base() { return base; }

    // Mapeos bala<->dueño (friendly fire)
    private final Map<Jugador, Proyectil> balaActivaPorJugador = new IdentityHashMap<>();
    private final Map<Proyectil, Jugador> duenioDeBala = new IdentityHashMap<>();

    private int numeroDeNivel = 1;
    public void setNumeroDeNivel(int n) { this.numeroDeNivel = n; }
    public int numeroDeNivel() { return numeroDeNivel; }

    public int enemigosVivos() { return enemigos.size(); }
    public int enemigosPendientes() { return spawner.cantidadPendiente(); }
    public int enemigosRestantesTotales() { return enemigosVivos() + enemigosPendientes(); }
    public int vidasJugador1() { return jugadores.isEmpty() ? 0 : jugadores.get(0).vidasRestantes(); }
    public int vidasJugador2() { return (jugadores.size() > 1) ? jugadores.get(1).vidasRestantes() : 0; }

    public Nivel(Rectangulo rectangulo, Spawner spawner) {
        this.spawner = spawner;
        this.limites = rectangulo;
    }

    // ===================== crearMundo  =====================
    public void crearMundo(NivelData data) {
        // reset de colecciones/estado (igual que antes)
        balaActivaPorJugador.clear();
        duenioDeBala.clear();
        bloques.clear();
        jugadores.clear();
        enemigos.clear();
        proyectiles.clear();
        poderes.clear();
        proximoDisparoEnemigoMs.clear();
        proximoDisparoJugadorMs.clear();
        victoria = derrota = false;
        lastMs = 0L;

        // reinicio de estructuras internas de gestores
        gestorBalas.reset();

        // construimos el mundo mediante el creador
        CreadorDeMundo creador = new CreadorDeMundo();
        CreadorDeMundo.MundoConstruido mc = creador.construir(data);

        // aplicamos resultado
        this.limites = mc.limites();
        this.base    = mc.base();
        this.mundo   = mc.mundo();
        this.bloques.addAll(mc.bloques());
        this.jugadores.addAll(mc.jugadores());

        // cooldown inicial de disparo de jugadores (igual que el original)
        for (var j : jugadores) proximoDisparoJugadorMs.put(j, 0L);

        // enemigos pendientes
        spawner.cargarPendientes(data.enemigos());
    }
    // =====================================================================================

    public void tick(long ahoraMs, InputEstado inJ1, InputEstado inJ2) {
        if (derrota || victoria) return;

        double dt = calcularDt(ahoraMs);

        // input de disparo
        if (!jugadores.isEmpty() && inJ1 != null && inJ1.disparar()) jugadores.get(0).disparar();
        if (jugadores.size() > 1 && inJ2 != null && inJ2.disparar()) jugadores.get(1).disparar();

        // actualizar estados
        for (Jugador j : jugadores) j.actualizarEstado(ahoraMs);
        for (Enemigo e : enemigos)  e.actualizarIA(ahoraMs, mundo);

        // disparos jugadores (cooldown + spawn)
        for (Jugador j : jugadores) {
            if (!j.hayDisparoPendiente()) continue;
            long next = proximoDisparoJugadorMs.getOrDefault(j, 0L);
            if (ahoraMs >= next) {
                // delega en GestorBalas pero mantiene listas en Nivel
                gestorBalas.spawnBalaJugador(j, proyectiles);
                proximoDisparoJugadorMs.put(j, ahoraMs + JuegoConfig.PLAYER_SHOOT_COOLDOWN_MS);
            }
            j.consumirDisparoPendiente();
        }

        gestorPowerUps.tick(poderes, jugadores, enemigos, spawner);

        for (Enemigo e : enemigos) {
            long next = proximoDisparoEnemigoMs.getOrDefault(e, 0L);
            if (ahoraMs >= next) {
                gestorBalas.spawnBalaEnemigo(e, proyectiles);
                proximoDisparoEnemigoMs.put(e, ahoraMs + JuegoConfig.ENEMY_SHOOT_COOLDOWN_MS);
            }
        }

        // balas: mover, colisionar, limpiar + drops y mantenimiento de grid
        gestorBalas.actualizar(
                dt,
                proyectiles,
                enemigos,
                jugadores,
                bloques,
                poderes,
                limites,
                mundo
        );

        // spawn de enemigos
        List<Enemigo> nuevosEnemigos = spawner.spawnearHastaCompletar(
                enemigos.size(),
                ahoraMs,
                JuegoConfig.MAX_ENEMIGOS_CONCURRENTES
        );
        if (!nuevosEnemigos.isEmpty()) {
            enemigos.addAll(nuevosEnemigos);
            for (var e : nuevosEnemigos) {
                proximoDisparoEnemigoMs.put(e, ahoraMs + JuegoConfig.ENEMY_SHOOT_COOLDOWN_MS);
            }
        }

        // condiciones de fin
        if (todosJugadoresAgotados()) {
            derrota = true;
            try { ManagerSonido.playEfecto("derrota");  } catch (Throwable __) {}
        }
        if (base != null && base.estaDestruido()) {
            derrota = true;
            try { ManagerSonido.playEfecto("derrota");  } catch (Throwable __) {}
        }
        if (enemigos.isEmpty() && spawner.yaTermino()) {
            victoria = true;
            try { ManagerSonido.playEfecto("victoria"); } catch (Throwable __) {}
        }
    }

    private double calcularDt(long ahoraMs) {
        if (lastMs == 0L) { lastMs = ahoraMs; return 0.0; }
        double dt = (ahoraMs - lastMs) / 1000.0;
        lastMs = ahoraMs;
        return dt;
    }

    private boolean todosJugadoresAgotados() {
        if (jugadores.isEmpty()) return true;
        for (Jugador j : jugadores) {
            if (!j.sinVidas()) return false;
        }
        return true;
    }

    public boolean colisionaConBloqueSolido(Rectangulo area) {
        for (var b : mundo.bloquesEn(area)) {
            if (!b.bloqueaMovimiento()) continue;
            if (b.hitbox().intersecta(area)) return true;
        }
        return false;
    }

    public EstadoNivel estado() {
        List<EstadoEntidad> entidades = new ArrayList<>(
                bloques.size() + proyectiles.size() + enemigos.size() + jugadores.size() + poderes.size()
        );

        ConstructorEntidades.agregarBloques(entidades, bloques);
        ConstructorEntidades.agregarProyectiles(entidades, proyectiles);
        ConstructorEntidades.agregarEnemigos(entidades, enemigos);
        ConstructorEntidades.agregarJugadores(entidades, jugadores);
        ConstructorEntidades.agregarPowerUps(entidades, poderes);

        return new EstadoNivel(
                victoria,
                derrota,
                false,
                jugadores.size(),
                enemigosVivos(),
                enemigosPendientes(),
                numeroDeNivel(),
                vidasJugador1(),
                vidasJugador2(),
                entidades
        );
    }

}
