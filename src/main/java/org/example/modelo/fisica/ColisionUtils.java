package org.example.modelo.fisica;

import org.example.modelo.entorno.Bloque;

public final class ColisionUtils {

    public static boolean colisionaConBloqueSolido(Rectangulo bbox, MundoFisico mundo) {
        for (Bloque b : mundo.bloquesEn(bbox)) {
            if (b.bloqueaMovimiento() && b.hitbox().intersecta(bbox)) return true;
        }
        return false;
    }

    public static double ajustarX(double xActual, double yActual, double dx, Rectangulo bbox, MundoFisico mundo) {
        return ajustar(xActual, yActual, dx, 0, bbox, mundo).x();
    }

    public static double ajustarY(double xActual, double yActual, double dy, Rectangulo bbox, MundoFisico mundo) {
        return ajustar(xActual, yActual, 0, dy, bbox, mundo).y();
    }

    private static Vector ajustar(double xActual, double yActual, double dx, double dy, Rectangulo bbox, MundoFisico mundo) {
        double nx = xActual;
        double ny = yActual;

        double pasoX = Math.signum(dx);
        double pasoY = Math.signum(dy);
        double restanteX = Math.abs(dx);
        double restanteY = Math.abs(dy);

        while (restanteX > 0) {
            double intentoX = nx + pasoX;
            Vector delta = new Vector(intentoX - xActual, 0);
            if (colisionaConBloqueSolido(bbox.trasladado(delta), mundo)) break;
            nx = intentoX;
            restanteX -= 1.0;
        }

        while (restanteY > 0) {
            double intentoY = ny + pasoY;
            Vector delta = new Vector(0, intentoY - yActual);
            if (colisionaConBloqueSolido(bbox.trasladado(delta), mundo)) break;
            ny = intentoY;
            restanteY -= 1.0;
        }

        return new Vector(nx, ny);
    }
}
