package org.example.modelo.fisica;

public record Rectangulo(double x, double y, double w, double h) {

    public boolean intersecta(Rectangulo o) {
        return x < o.x + o.w && x + w > o.x &&
                y < o.y + o.h && y + h > o.y;
    }

    public Rectangulo trasladado(Vector d) {
        return new Rectangulo(x + d.x(), y + d.y(), w, h);
    }


    public Vector correccion(Rectangulo o) {
        if (!intersecta(o)) return Vector.CERO;

        double dx1 = o.x + o.w - x;
        double dx2 = x + w - o.x;
        double dy1 = o.y + o.h - y;
        double dy2 = y + h - o.y;

        double minX = Math.min(dx1, dx2);
        double minY = Math.min(dy1, dy2);

        if (minX < minY) {
            return (dx1 < dx2) ? new Vector(dx1, 0) : new Vector(-dx2, 0);
        } else {
            return (dy1 < dy2) ? new Vector(0, dy1) : new Vector(0, -dy2);
        }
    }
}
