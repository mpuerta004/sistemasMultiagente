package sistemamultiagente;

public class Point {

    /**         Atributos       */
    public double x;
    public double y;

    /**         Métodos         */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double distance(Point other) {
        return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y));
    }

    public Point sub(Point other) {
        return new Point(x - other.x, y - other.y);
    }

    public Point scale(double s) {
        return new Point(x * s, y * s);
    }

    public Point add(Point p2) {
        return new Point(x + p2.x, y + p2.y);
    }

    public String toString() {
        return String.format("X=%f, Y=%f", this.x, this.y);
    }

}