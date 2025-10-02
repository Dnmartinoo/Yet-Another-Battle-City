package org.example.modelo.fisica;

import org.example.modelo.entorno.Bloque;

import java.util.ArrayList;
import java.util.List;

public class MundoFisico {

    private final int TILE;
    private final int anchoTiles, altoTiles;
    private final Bloque[][] grid; // [y][x]

    public MundoFisico(int tile, int anchoTiles, int altoTiles, Bloque[][] grid) {
        this.TILE = tile;
        this.anchoTiles = anchoTiles;
        this.altoTiles = altoTiles;
        this.grid = grid;
    }

    public List<Bloque> bloquesEn(Rectangulo area) {
        int x0 = Math.max(0, (int)Math.floor(area.x() / TILE));
        int y0 = Math.max(0, (int)Math.floor(area.y() / TILE));
        int x1 = Math.min(anchoTiles - 1, (int)Math.floor((area.x() + area.w() - 0.0001) / TILE));
        int y1 = Math.min(altoTiles - 1, (int)Math.floor((area.y() + area.h() - 0.0001) / TILE));


        List<Bloque> res = new ArrayList<>();
        for (int y = y0; y <= y1; y++) {
            for (int x = x0; x <= x1; x++) {
                Bloque b = grid[y][x];
                if (b != null && !b.estaDestruido()) {
                    res.add(b);
                }
            }
        }
        return res;
    }
    public void setBloque(int fila, int col, Bloque b) { grid[fila][col] = b; }


    public int tileSize(){ return TILE; }
}
