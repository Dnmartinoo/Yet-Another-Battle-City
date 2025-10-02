// utils/ColisionUtils.java
package org.example.modelo.fisica.utils;

import org.example.modelo.fisica.Rectangulo;
import org.example.modelo.fisica.Vector;
import org.example.modelo.entorno.Bloque;
import org.example.modelo.mundo.MundoFisico;

public final class ColisionUtils {

    private ColisionUtils(){}

    // Devuelve true si el bbox toca al menos un bloque que bloquea movimiento
    public static boolean colisionaConBloqueSolido(Rectangulo bbox, MundoFisico mundo) {
        for (Bloque b : mundo.bloquesEn(bbox)) {
            if (b.bloqueaMovimiento() && b.hitbox().intersecta(bbox)) {
                return true;
            }
        }
        return false;
    }

    // Ajusta posición en X contra el primer bloque sólido que choca
    public static double ajustarX(double xActual, double yActual, double dx,
                                  Rectangulo bbox, MundoFisico mundo) {
        double x = xActual;
        double paso = Math.signum(dx); // +1 o -1
        double restante = Math.abs(dx);

        while (restante > 0) {
            double intento = x + paso;
            // delta relativo al bbox original
            Vector delta = new Vector(intento - xActual, 0);
            Rectangulo next = bbox.trasladado(delta);
            if (colisionaConBloqueSolido(next, mundo)) break;
            x = intento;
            restante -= 1.0;
        }
        return x;
    }

    // Ajusta posición en Y contra el primer bloque sólido que choca
    public static double ajustarY(double xActual, double yActual, double dy,
                                  Rectangulo bbox, MundoFisico mundo) {
        double y = yActual;
        double paso = Math.signum(dy);
        double restante = Math.abs(dy);

        while (restante > 0) {
            double intento = y + paso;
            Vector delta = new Vector(0, intento - yActual);
            Rectangulo next = bbox.trasladado(delta);
            if (colisionaConBloqueSolido(next, mundo)) break;
            y = intento;
            restante -= 1.0;
        }
        return y;
    }
}
