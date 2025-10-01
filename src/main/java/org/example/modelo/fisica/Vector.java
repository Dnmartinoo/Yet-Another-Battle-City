package org.example.modelo.fisica;

public record Vector(double x, double y) {
    public static final Vector CERO = new Vector(0.0, 0.0);

    public Vector mas(Vector o) {
        return new Vector(x + o.x, y + o.y);
    }

    public Vector por(double k) {
        return new Vector(x * k, y * k);
    }

    public double modulo2() {
        return x*x + y*y;
    }

    public double modulo() {
        return Math.sqrt(modulo2());
    }

    public Vector normalizado() {
        double m = modulo();
        return (m == 0) ? this : new Vector(x / m, y / m);
    }
}
