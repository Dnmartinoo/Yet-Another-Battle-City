package org.example.modelo.juego;

import org.example.modelo.audio.ManagerSonido;
import org.example.modelo.disparo.Equipo;
import org.example.modelo.disparo.Proyectil;
import org.example.modelo.entorno.Bloque;
import org.example.modelo.entorno.BloqueFactory;
import org.example.modelo.entorno.ResultadoImpacto;
import org.example.modelo.fisica.MundoFisico;
import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;
import org.example.modelo.personajes.Enemigo;
import org.example.modelo.personajes.Jugador;
import org.example.modelo.powerup.PowerUp;

import java.util.*;

public class GestorBalas {

    // Relación dueño <-> bala para friendly fire (stun)
    private final Map<Jugador, Proyectil> balaActivaPorJugador = new IdentityHashMap<>();
    private final Map<Proyectil, Jugador> duenioDeBala = new IdentityHashMap<>();

    public void reset() {
        balaActivaPorJugador.clear();
        duenioDeBala.clear();
    }

    public void spawnBalaJugador(Jugador j, List<Proyectil> proyectiles) {
        Proyectil balaActiva = balaActivaPorJugador.get(j);
        if (balaActiva != null && balaActiva.vivo()) return;

        Vector dir = j.velocidad().esCero() ? j.ultimaDireccion() : j.velocidad().normalizado();
        Vector origen = origenBalaDesdeCentro(j, dir, JuegoConfig.BULLET_SIZE, JuegoConfig.BULLET_SIZE);
        boolean esPotenciada = j.tieneDisparoPotenciado();

        Proyectil nueva = new Proyectil(
                origen, dir,
                JuegoConfig.PLAYER_BULLET_SPEED,
                JuegoConfig.BULLET_DAMAGE,
                Equipo.JUGADOR,
                esPotenciada
        );
        proyectiles.add(nueva);
        balaActivaPorJugador.put(j, nueva);
        duenioDeBala.put(nueva, j);

        ManagerSonido.playEfecto(JuegoConfig.SND_DISPARAR);
    }

    public void spawnBalaEnemigo(Enemigo e, List<Proyectil> proyectiles) {
        Vector dir = e.velocidad().esCero() ? e.ultimaDireccion() : e.velocidad().normalizado();
        Vector origen = origenBalaDesdeCentro(e, dir, JuegoConfig.BULLET_SIZE, JuegoConfig.BULLET_SIZE);
        proyectiles.add(new Proyectil(
                origen, dir,
                JuegoConfig.ENEMY_BULLET_SPEED,
                JuegoConfig.BULLET_DAMAGE,
                Equipo.ENEMIGO,
                false
        ));
        ManagerSonido.playEfecto(JuegoConfig.SND_DISPARAR);
    }

    public void actualizar(
            double dt,
            List<Proyectil> proyectiles,
            List<Enemigo> enemigos,
            List<Jugador> jugadores,
            List<Bloque> bloques,
            List<PowerUp> poderes,
            Rectangulo limites,
            MundoFisico mundo
    ) {
        // 1) Mover
        for (var b : proyectiles) {
            if (b.vivo()) b.setPosicion(b.posicion().mas(b.velocidad().por(dt)));
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

                    if (bl.esAcero() && !bl.estaDestruido()) ManagerSonido.playEfecto(JuegoConfig.SND_IMPACTO_ACERO);
                    if (bl.esLadrillo() && bl.estaDestruido()) ManagerSonido.playEfecto(JuegoConfig.SND_LADRILLO_ROTO);
                    break;
                }
            }
        }

        // 3) Colisiones bala-entidad
        for (var bala : proyectiles) {
            if (!bala.vivo()) continue;
            var hb = bala.hitbox();

            if (bala.equipo() == Equipo.JUGADOR) {
                // Jugador -> enemigo (daño) o friendly fire -> jugador (stun)
                boolean impacto = false;

                for (var e : enemigos) {
                    if (!e.estaVivo()) continue;
                    if (e.hitbox().intersecta(hb)) {
                        int dano = bala.dano();
                        if (bala.esPotenciada()) dano *= 999; // respeta tu lógica original
                        e.recibirImpacto(dano);
                        bala.destruir();
                        impacto = true;
                        break;
                    }
                }
                if (impacto) continue;

                Jugador duenio = duenioDeBala.get(bala);
                if (duenio != null) {
                    for (var j : jugadores) {
                        if (j == duenio) continue;
                        if (j.hitbox().intersecta(hb)) {
                            long ahora = System.currentTimeMillis();
                            long duracion = (JuegoConfig.PLAYER_STUN_MS > 0)
                                    ? JuegoConfig.PLAYER_STUN_MS
                                    : 1500;
                            j.inmovilizarPorMs(duracion, ahora);
                            try { ManagerSonido.playEfecto(JuegoConfig.SND_STUN); } catch (Throwable __) {}
                            bala.destruir();
                            break;
                        }
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

        final double MARGEN = BloqueFactory.TILE; // en el original era 32
        proyectiles.removeIf(p ->
                !p.vivo() ||
                        p.posicion().x() < limites.x() - MARGEN ||
                        p.posicion().x() > limites.x() + limites.w() + MARGEN ||
                        p.posicion().y() < limites.y() - MARGEN ||
                        p.posicion().y() > limites.y() + limites.h() + MARGEN
        );

        enemigos.removeIf(e -> {
            if (!e.estaVivo()) {
                if (Math.random() < 0.2) {
                    poderes.add(GestorPowerUps.crearPoderRandom(e.posicion()));
                }
                return true;
            }
            return false;
        });

        if (mundo != null) {
            List<Bloque> destruidos = new ArrayList<>();
            for (var b : bloques) {
                if (b.esDestruible() && b.estaDestruido()) destruidos.add(b);
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

        // 8) Limpiar referencias de dueño <-> bala
        Set<Proyectil> aEliminar = new HashSet<>();
        duenioDeBala.forEach((proj, owner) -> {
            if (!proj.vivo() || !proyectiles.contains(proj)) {
                Proyectil actual = balaActivaPorJugador.get(owner);
                if (actual == proj) balaActivaPorJugador.remove(owner);
                aEliminar.add(proj);
            }
        });
        for (Proyectil p : aEliminar) duenioDeBala.remove(p);
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
}
