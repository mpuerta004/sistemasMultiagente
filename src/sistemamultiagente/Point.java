package sistemamultiagente;


public class Point {

    /**         Atributos       */
    private double x;
    private double y;

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
        return Math.sqrt(
                Math.pow(this.getX() - other.getX(), 2)  + Math.pow(this.getY() - other.getY(),2)
        );
    }

    public Point div(double s){
        return new Point(this.getX() / s, this.getY()/s);
    }
    public Point sub(Point other) {
        return new Point(this.getX() - other.getX(), this.getY() - other.getY());
    }

    public Point sub(Vector other) {
        return new Point(this.getX()- other.getX(), this.getY() - other.getY());
    }

    public Point scale(double s) {
        return new Point(this.getX() * s, this.getY() * s);
    }

    public Point add(Point p2) {
        return new Point(this.getX() + p2.getX(), this.getY()+ p2.getY());
    }

    public Point add(Vector p2) {
        return new Point(this.getX() + p2.getX(), this.getY() + p2.getY());
    }

    public String toString() {
        return String.format(" X= %f, Y = %f ", this.getX(), this.getY());
    }

}