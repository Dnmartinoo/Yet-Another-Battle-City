package org.example.modelo.juego;

import org.example.modelo.entorno.Base;
import org.example.modelo.entorno.Bloque;
import org.example.modelo.entorno.BloqueFactory;
import org.example.modelo.fisica.MundoFisico;
import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;
import org.example.modelo.personajes.Jugador;

import java.util.ArrayList;
import java.util.List;

public final class CreadorDeMundo {

    public record MundoConstruido(
            Rectangulo limites,
            List<Bloque> bloques,
            List<Jugador> jugadores,
            Bloque base,
            MundoFisico mundo
    ) {}

    public MundoConstruido construir(NivelData data) {
        // Limites del nivel
        Rectangulo limites = new Rectangulo(0, 0, data.ancho(), data.alto());

        // Bloques (normalizando a la grilla)
        List<Bloque> bloques = new ArrayList<>();
        Bloque baseRef = null;

        for (var bd : data.bloques()) {
            int gridX = (int) Math.floor(bd.x / BloqueFactory.TILE);
            int gridY = (int) Math.floor(bd.y / BloqueFactory.TILE);
            double bx = gridX * BloqueFactory.TILE;
            double by = gridY * BloqueFactory.TILE;

            Bloque b = BloqueFactory.crear(bd.tipo, bx, by);
            bloques.add(b);

            if (b.esBase()) baseRef = b;
        }

        // Si no vino la base en el data, la creamos por defecto (idéntico al original)
        if (baseRef == null) {
            double bx = (data.ancho() - BloqueFactory.TILE) / 2.0;
            double by = data.alto() - BloqueFactory.TILE - 20.0; // (lo pasamos a JuegoConfig en una próxima etapa)
            baseRef = new Base(new Vector(bx, by), BloqueFactory.TILE);
            bloques.add(baseRef);
        }

        // Jugadores y respawn
        List<Jugador> jugadores = new ArrayList<>(2);
        Jugador j1 = new Jugador(new Vector(data.jugador1X(), data.jugador1Y()), 1);
        j1.setRespawn(new Vector(data.jugador1X(), data.jugador1Y()));
        jugadores.add(j1);

        if (data.coop()) {
            Jugador j2 = new Jugador(new Vector(data.jugador2X(), data.jugador2Y()), 2);
            j2.setRespawn(new Vector(data.jugador2X(), data.jugador2Y()));
            jugadores.add(j2);
        }

        // Mundo físico (grilla)
        int anchoTiles = data.ancho() / BloqueFactory.TILE;
        int altoTiles  = data.alto()  / BloqueFactory.TILE;
        Bloque[][] grid = new Bloque[altoTiles][anchoTiles];
        for (Bloque b : bloques) {
            int gx = (int) (b.posicion().x() / BloqueFactory.TILE);
            int gy = (int) (b.posicion().y() / BloqueFactory.TILE);
            if (gx >= 0 && gx < anchoTiles && gy >= 0 && gy < altoTiles) {
                grid[gy][gx] = b;
            }
        }
        MundoFisico mundo = new MundoFisico(BloqueFactory.TILE, anchoTiles, altoTiles, grid);

        return new MundoConstruido(limites, bloques, jugadores, baseRef, mundo);
    }
}
