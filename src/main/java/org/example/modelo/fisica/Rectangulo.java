package org.example.modelo.fisica;

public record Rectangulo(double x, double y, double w, double h) {
    public boolean intersecta(Rectangulo o){
        return x < o.x + o.w && x + w > o.x && y < o.y + o.h && y + h > o.y;
    }

    public Vector correccion(Rectangulo o){
        if(!intersecta(o)) {
            return Vector.CERO;
        }

        double dx1 = (o.x + o.w) - x;
        double dx2 = (x + w) - o.x;
        double dy1 = (o.y + o.h) - y;
        double dy2 = (y + h) - o.y;

        double cx = (dx1  < dx2) ? dx1 : -dx2;
        double cy = (dy1  < dy2) ? dy1 : -dy2;

        if (Math.abs(cx) < Math.abs(cy)) {
            return new Vector(cx, 0);
        }
        else {
            return new Vector(0, cy);
        }
    }

    public Rectangulo trasladado(Vector d) {
        return new Rectangulo(x + d.x(), y + d.y(), w, h);
    }
}
