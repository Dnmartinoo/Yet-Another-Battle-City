package org.example.modelo.fisica;

public record Rectangulo(double x, double y, double w, double h) {

    public boolean intersecta(Rectangulo o) {
        return x < o.x + o.w && x + w > o.x &&
                y < o.y + o.h && y + h > o.y;
    }

    public Rectangulo trasladado(Vector d) {
        return new Rectangulo(x + d.x(), y + d.y(), w, h);
    }
}
