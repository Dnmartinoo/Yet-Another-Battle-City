package org.example.modelo.juego;

import org.example.modelo.fisica.Rectangulo;
public class CrearNivel {

    public static Nivel crearNivelDemo(boolean coop) {
        var spawner = new Spawner();
        var nivel = new Nivel(new Rectangulo(0,0,800,600), spawner);

        var data = new NivelData();
        data.coop = coop;

        nivel.crearMundo(data);

        return nivel;
    }
}
