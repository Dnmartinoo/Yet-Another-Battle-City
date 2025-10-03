package org.example.modelo.juego.core;

import org.example.modelo.audio.ManagerSonido;
import org.example.modelo.disparo.Equipo;
import org.example.modelo.disparo.Proyectil;
import org.example.modelo.entorno.Bloque;
import org.example.modelo.entorno.BloqueFactory;
import org.example.modelo.entorno.ResultadoImpacto;
import org.example.modelo.fisica.MundoFisico;
import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;
import org.example.modelo.juego.config.JuegoConfig;
import org.example.modelo.personajes.Enemigo;
import org.example.modelo.personajes.Jugador;
import org.example.modelo.powerup.PowerUp;

import java.util.*;

public class GestorBalas {

    private final Map<Jugador, Proyectil> balaActivaPorJugador = new IdentityHashMap<>();
    private final Map<Proyectil, Jugador> duenioDeBala = new IdentityHashMap<>();
    private final ManagerSonido sonido = ManagerSonido.get();

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

        sonido.playEfecto(JuegoConfig.SND_DISPARAR);
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
        sonido.playEfecto(JuegoConfig.SND_DISPARAR);
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
        moverBalas(dt, proyectiles);
        colisionesConBloques(proyectiles, mundo);
        colisionesConTanques(proyectiles, jugadores, enemigos);
        colisionesEntreBalas(proyectiles);
        limpiarBalasFuera(proyectiles, limites);
        limpiarEnemigosMuertos(enemigos, poderes);
        actualizarGrid(mundo, bloques);
        limpiarReferencias(proyectiles);
    }

    private void moverBalas(double dt, List<Proyectil> proyectiles) {
        for (var b : proyectiles) {
            if (b.vivo()) b.setPosicion(b.posicion().mas(b.velocidad().por(dt)));
        }
    }

    private void colisionesConBloques(List<Proyectil> proyectiles, MundoFisico mundo) {
        for (var bala : proyectiles) {
            if (!bala.vivo()) continue;
            var hb = bala.hitbox();

            for (Bloque bl : mundo.bloquesEn(hb)) {
                if (!bl.bloqueaProyectiles()) continue;
                if (!bl.hitbox().intersecta(hb)) continue;

                ResultadoImpacto ri = bl.recibirImpacto(JuegoConfig.BULLET_DAMAGE);
                if (ri.detener()) {
                    bala.destruir();

                    if (bl.esAcero() && !bl.estaDestruido()) sonido.playEfecto(JuegoConfig.SND_IMPACTO_ACERO);
                    if (bl.esLadrillo() && bl.estaDestruido()) sonido.playEfecto(JuegoConfig.SND_LADRILLO_ROTO);
                    break;
                }
            }
        }
    }

    private void colisionesConTanques(List<Proyectil> proyectiles, List<Jugador> jugadores, List<Enemigo> enemigos) {
        for (var bala : proyectiles) {
            if (!bala.vivo()) continue;
            var hb = bala.hitbox();

            if (bala.equipo() == Equipo.JUGADOR) {
                for (var e : enemigos) {
                    if (!e.estaVivo()) continue;
                    if (e.hitbox().intersecta(hb)) {
                        int dano = bala.dano();
                        if (bala.esPotenciada()) dano = JuegoConfig.DANO_BALAS_POTENCIADAS;
                        e.recibirImpacto(dano);
                        bala.destruir();
                        break;
                    }
                }
                Jugador duenio = duenioDeBala.get(bala);
                if (duenio != null) {
                    for (var j : jugadores) {
                        if (j == duenio) continue;
                        if (j.hitbox().intersecta(hb)) {
                            long ahora = System.currentTimeMillis();
                            j.inmovilizarPorMs(JuegoConfig.PLAYER_STUN_MS, ahora);
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
    }

    private void colisionesEntreBalas(List<Proyectil> proyectiles) {
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
    }

    private void limpiarBalasFuera(List<Proyectil> proyectiles, Rectangulo limites) {
        final double MARGEN = BloqueFactory.TILE;
        proyectiles.removeIf(p ->
                !p.vivo() ||
                        p.posicion().x() < limites.x() - MARGEN ||
                        p.posicion().x() > limites.x() + limites.w() + MARGEN ||
                        p.posicion().y() < limites.y() - MARGEN ||
                        p.posicion().y() > limites.y() + limites.h() + MARGEN
        );
    }

    private void limpiarEnemigosMuertos(List<Enemigo> enemigos, List<PowerUp> poderes) {
        enemigos.removeIf(e -> {
            if (!e.estaVivo()) {
                if (Math.random() < JuegoConfig.PROB_DROP_POWERUP) {
                    poderes.add(GestorPowerUps.crearPoderRandom(e.posicion()));
                }
                return true;
            }
            return false;
        });
    }

    private void actualizarGrid(MundoFisico mundo, List<Bloque> bloques) {
        if (mundo == null) return;
        List<Bloque> destruidos = new ArrayList<>();
        for (var b : bloques) {
            if (b.esDestruible() && b.estaDestruido()) destruidos.add(b);
        }
        for (var b : destruidos) {
            int tx = (int)(b.posicion().x() / BloqueFactory.TILE);
            int ty = (int)(b.posicion().y() / BloqueFactory.TILE);
            mundo.setBloque(ty, tx, null);
        }
        bloques.removeAll(destruidos);
    }

    private void limpiarReferencias(List<Proyectil> proyectiles) {
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

    private Vector origenBalaDesdeCentro(org.example.modelo.fisica.Cuerpo tanque, Vector dir, double bulletW, double bulletH) {
        var hb = tanque.hitbox();
        double cx = hb.x() + hb.w() / 2.0;
        double cy = hb.y() + hb.h() / 2.0;

        double offset = Math.max(hb.w(), hb.h()) / 2.0;
        double ox = (cx - bulletW / 2.0) + dir.x() * offset;
        double oy = (cy - bulletH / 2.0) + dir.y() * offset;

        return new Vector(ox, oy);
    }
}
