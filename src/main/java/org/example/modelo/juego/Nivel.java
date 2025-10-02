package org.example.modelo.juego;

import org.example.modelo.disparo.Equipo;
import org.example.modelo.disparo.Proyectil;
import org.example.modelo.entorno.*;
import org.example.modelo.fisica.MundoFisico;
import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;
import org.example.modelo.personajes.Enemigo;
import org.example.modelo.personajes.Jugador;

import java.util.*;

public class Nivel {

    private final List<Bloque> bloques = new ArrayList<>();
    private final List<Enemigo> enemigos = new ArrayList<>();
    private final List<Jugador> jugadores = new ArrayList<>(2);
    private final List<Proyectil> proyectiles = new ArrayList<>();
    private final Spawner spawner;

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
    public Bloque base()              { return base; }  // <- devuelve Bloque
    private int numeroDeNivel = 1;
    public void setNumeroDeNivel(int n) { this.numeroDeNivel = n; }
    public int numeroDeNivel() { return numeroDeNivel; }

    public int enemigosVivos() { return enemigos().size(); }
    public int enemigosPendientes() { return spawner.cantidadPendiente(); }
    public int enemigosRestantesTotales() { return enemigosVivos() + enemigosPendientes(); }

    public int vidasJugador1() {
        return jugadores().isEmpty() ? 0 : jugadores().get(0).vidasRestantes();
    }
    public int vidasJugador2() {
        return (jugadores().size() > 1) ? jugadores().get(1).vidasRestantes() : 0;
    }


    public Nivel(Rectangulo rectangulo, Spawner spawner) {
        this.spawner = spawner;
        this.limites = rectangulo;
    }

    public void crearMundo(NivelData data) {
        // Limpiar todo
        bloques.clear();
        jugadores.clear();
        enemigos.clear();
        proyectiles.clear();
        proximoDisparoEnemigoMs.clear();
        proximoDisparoJugadorMs.clear();
        victoria = derrota = false;
        lastMs = 0L;

        this.limites = new Rectangulo(0, 0, data.ancho(), data.alto());

        Bloque baseRef = null;
        for (var bd : data.bloques()) {
            Bloque b = BloqueFactory.crear(bd.tipo, bd.x, bd.y);
            bloques.add(b);
            if (b.esBase()) baseRef = b; // polimorfismo
        }
        if (baseRef == null) {
            double bx = (data.ancho() - BloqueFactory.TILE) / 2.0;
            double by = data.alto() - BloqueFactory.TILE - 20.0;
            baseRef = new Base(new Vector(bx, by), BloqueFactory.TILE);
            bloques.add(baseRef);
        }
        this.base = baseRef;

        jugadores.add(new Jugador(new Vector(data.jugador1X(), data.jugador1Y()), 1));
        if (data.coop()) {
            jugadores.add(new Jugador(new Vector(data.jugador2X(), data.jugador2Y()),2));
        }
        for (var j : jugadores) proximoDisparoJugadorMs.put(j, 0L);

        spawner.cargarPendientes(data.enemigos());

        // Mundo físico (grid) – sin instanceof (Bloque extiende Cuerpo)
        int anchoTiles = data.ancho() / BloqueFactory.TILE;
        int altoTiles  = data.alto() / BloqueFactory.TILE;
        Bloque[][] grid = new Bloque[altoTiles][anchoTiles];
        for (Bloque b : bloques) {
            int x = (int)(b.posicion().x() / BloqueFactory.TILE);
            int y = (int)(b.posicion().y() / BloqueFactory.TILE);
            if (x >= 0 && x < anchoTiles && y >= 0 && y < altoTiles) {
                grid[y][x] = b;
            }
        }
        this.mundo = new MundoFisico(BloqueFactory.TILE, anchoTiles, altoTiles, grid);
    }

    public void tick(long ahoraMs, InputEstado inJ1, InputEstado inJ2) {
        if (derrota || victoria) return;

        double dt = calcularDt(ahoraMs);

        if (!jugadores.isEmpty() && inJ1 != null && inJ1.disparar()) jugadores.get(0).disparar();
        if (jugadores.size() > 1 && inJ2 != null && inJ2.disparar()) jugadores.get(1).disparar();

        for (Jugador j : jugadores) j.actualizarEstado(ahoraMs);

        for (Enemigo e : enemigos) e.actualizarIA(ahoraMs, mundo);

        List<Enemigo> nuevos = spawner.talVezSpawnear(ahoraMs);
        if (!nuevos.isEmpty()) {
            enemigos.addAll(nuevos);
            for (var e : nuevos) proximoDisparoEnemigoMs.put(e, ahoraMs + JuegoConfig.ENEMY_SHOOT_COOLDOWN_MS);
        }

        for (Jugador j : jugadores) {
            if (!j.hayDisparoPendiente()) continue;
            long next = proximoDisparoJugadorMs.getOrDefault(j, 0L);
            if (ahoraMs >= next) {
                spawnBalaJugador(j);
                proximoDisparoJugadorMs.put(j, ahoraMs + JuegoConfig.PLAYER_SHOOT_COOLDOWN_MS);
            }
            j.consumirDisparoPendiente();
        }

        for (Enemigo e : enemigos) {
            long next = proximoDisparoEnemigoMs.getOrDefault(e, 0L);
            if (ahoraMs >= next) {
                spawnBalaEnemigo(e);
                proximoDisparoEnemigoMs.put(e, ahoraMs + JuegoConfig.ENEMY_SHOOT_COOLDOWN_MS);
            }
        }

        actualizarBalas(dt);

        if (base != null && base.estaDestruido()) derrota = true;
        if (enemigos.isEmpty() && spawner.yaTermino()) victoria = true;
    }

    private double calcularDt(long ahoraMs) {
        if (lastMs == 0L) { lastMs = ahoraMs; return 0.0; }
        double dt = (ahoraMs - lastMs) / 1000.0;
        lastMs = ahoraMs;
        return dt;
    }

    private void actualizarBalas(double dt) {
        for (var b : proyectiles) {
            if (!b.vivo()) continue;
            b.setPosicion(b.posicion().mas(b.velocidad().por(dt)));
        }

        for (var bala : proyectiles) {
            if (!bala.vivo()) continue;
            var hb = bala.hitbox();

            for (Bloque bl : mundo.bloquesEn(hb)) {
                if (!bl.bloqueaProyectiles()) continue;
                if (!bl.hitbox().intersecta(hb)) continue;

                ResultadoImpacto ri = bl.recibirImpacto(JuegoConfig.BULLET_DAMAGE);
                if (ri.detener()) {
                    bala.destruir();
                    break;
                }
            }
        }

        for (var bala : proyectiles) {
            if (!bala.vivo()) continue;
            var hb = bala.hitbox();

            if (bala.equipo() == Equipo.JUGADOR) {
                for (var e : enemigos) {
                    if (!e.estaVivo()) continue;
                    if (e.hitbox().intersecta(hb)) {
                        e.recibirImpacto(JuegoConfig.BULLET_DAMAGE);
                        bala.destruir();
                        break;
                    }
                }
            } else {
                for (var j : jugadores) {
                    if (j.hitbox().intersecta(hb)) {
                        j.recibirImpacto(JuegoConfig.BULLET_DAMAGE);
                        bala.destruir();
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < proyectiles.size(); i++) {
            var a = proyectiles.get(i);
            if (!a.vivo()) continue;
            for (int k = i + 1; k < proyectiles.size(); k++) {
                var b = proyectiles.get(k);
                if (!b.vivo()) continue;
                if (a.hitbox().intersecta(b.hitbox())) {
                    a.destruir();
                    b.destruir();
                }
            }
        }

        proyectiles.removeIf(p ->
                !p.vivo() ||
                        p.posicion().x() < limites.x() - 32 || p.posicion().x() > limites.x() + limites.w() + 32 ||
                        p.posicion().y() < limites.y() - 32 || p.posicion().y() > limites.y() + limites.h() + 32
        );

        {
            var it = enemigos.iterator();
            while (it.hasNext()) {
                if (!it.next().estaVivo()) it.remove();
            }
        }

        if (mundo != null) {
            List<Bloque> destruidos = new ArrayList<>();
            for (var b : bloques) {
                if (b.esDestruible() && b.estaDestruido()) {
                    destruidos.add(b);
                }
            }
            if (!destruidos.isEmpty()) {
                for (var b : destruidos) {
                    int tx = (int)(b.posicion().x() / BloqueFactory.TILE);
                    int ty = (int)(b.posicion().y() / BloqueFactory.TILE);
                    mundo.setBloque(ty, tx, null);
                }
                bloques.removeAll(destruidos);
            }
        }
    }


    private void spawnBalaJugador(Jugador j) {
        Vector dir = j.velocidad().esCero() ? JuegoConfig.PLAYER_DEFAULT_FACING
                : j.velocidad().normalizado();
        Vector origen = origenBalaDesdeCentro(j, dir, JuegoConfig.BULLET_SIZE, JuegoConfig.BULLET_SIZE);
        proyectiles.add(new Proyectil(origen, dir, JuegoConfig.PLAYER_BULLET_SPEED, JuegoConfig.BULLET_DAMAGE, Equipo.JUGADOR));
    }

    private void spawnBalaEnemigo(Enemigo e) {
        Vector dir = e.velocidad().esCero() ? JuegoConfig.ENEMY_DEFAULT_FACING
                : e.velocidad().normalizado();
        Vector origen = origenBalaDesdeCentro(e, dir, JuegoConfig.BULLET_SIZE, JuegoConfig.BULLET_SIZE);
        proyectiles.add(new Proyectil(origen, dir, JuegoConfig.ENEMY_BULLET_SPEED, JuegoConfig.BULLET_DAMAGE, Equipo.ENEMIGO));
    }

    private Vector origenBalaDesdeCentro(org.example.modelo.fisica.Cuerpo tanque, Vector dir,
                                         double bulletW, double bulletH) {
        var hb = tanque.hitbox();
        double cx = hb.x() + hb.w() / 2.0;
        double cy = hb.y() + hb.h() / 2.0;

        double offset = Math.max(hb.w(), hb.h()) / 2.0;
        double ox = (cx - bulletW / 2.0) + dir.x() * offset;
        double oy = (cy - bulletH / 2.0) + dir.y() * offset;

        return new Vector(ox, oy);
    }

    public boolean colisionaConBloqueSolido(Rectangulo area) {
        for (var b : mundo.bloquesEn(area)) {
            if (!b.bloqueaMovimiento()) continue;
            if (b.hitbox().intersecta(area)) return true;
        }
        return false;
    }

    public void eliminarTodosLosEnemigos() {
        enemigos.clear();
        proximoDisparoEnemigoMs.clear();
        spawner.cancelarPendientes();
    }

    public boolean estaTerminado() {
        return victoria || derrota;
    }


    public EstadoNivel estado() {
        List<EstadoEntidad> entidades = new ArrayList<>();

        for (var bloque : bloques()) {
            var hb = bloque.hitbox();
            var id = ((Spriteeable) bloque).spriteId();
            entidades.add(new EstadoEntidad(
                    id,
                    hb.x(), hb.y(),
                    hb.w(), hb.h(),
                    false
            ));
        }

        for (var proyectil : proyectiles()) {
            var hb = proyectil.hitbox();
            var id = ((Spriteeable) proyectil).spriteId();
            entidades.add(new EstadoEntidad(
                    id,
                    hb.x(), hb.y(),
                    hb.w(), hb.h(),
                    false
            ));
        }

        for (var enemigo : enemigos()) {
            var hb = enemigo.hitbox();
            var id = ((Spriteeable) enemigo).spriteId();
            entidades.add(new EstadoEntidad(
                    id,
                    hb.x(), hb.y(),
                    hb.w(), hb.h(),
                    false
            ));
        }

        for (var jugador : jugadores()) {
            var hb = jugador.hitbox();
            var id = ((Spriteeable) jugador).spriteId();
            boolean casco = jugador.esInvulnerable();
            entidades.add(new EstadoEntidad(
                    id,
                    hb.x(), hb.y(),
                    hb.w(), hb.h(),
                    casco
            ));
        }

        int cantJug = jugadores.size();
        int vP1 = (cantJug >= 1) ? jugadores.get(0).vidasRestantes() : 0;
        int vP2 = (cantJug >= 2) ? jugadores.get(1).vidasRestantes() : 0;

        int vivos = enemigosVivos();            // ya lo tenés
        int pend  = enemigosPendientes();       // usa spawner.cantidadPendiente()
        int nivelNro = numeroDeNivel();         // guardado en Nivel

        return new EstadoNivel(
                victoria,
                derrota,
                false,
                cantJug,
                vivos,
                pend,
                nivelNro,
                vP1,
                vP2,
                entidades
        );
    }



}
