package org.example.modelo.juego;

import org.example.modelo.fisica.Rectangulo;
public class CrearNivel {
    public static Nivel crearNivelDemo() {
        var spawner = new Spawner();
        var nivel = new Nivel(new Rectangulo(0,0,800,600), spawner);

        var data = new NivelData();
        data.coop = false;

        nivel.crearMundo(data);

        return nivel;
    }
}
