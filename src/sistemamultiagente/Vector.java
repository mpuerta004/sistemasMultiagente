package sistemamultiagente;

public class Vector {

    /**
     *      Atributos
     */
    private double x;
    private double y;

    /**
     *          Métodos
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double distance(Vector other) {
        return Math.sqrt(
                Math.pow(this.getX() - other.getX(), 2)  + Math.pow(this.getY() - other.getY(),2)
        );
    }

    public Vector sub(Vector other) {
        return new Vector(this.getX()- other.getX(), this.getY() - other.getY());
    }

    public Vector scale(double s) {
        return new Vector(this.getX() * s, this.getY() * s);
    }

    public Vector div(Vector vector,double s){
        return new Vector(vector.getX() / s, vector.getY()/s);
    }
    public Vector div(double s){
        return new Vector(this.getX() / s, this.getY()/s);
    }
    public Vector add(Vector p2) {
        return new Vector(this.getX() + p2.getX(), this.getY() + p2.getY());
    }
    public Point add(Point p2) {
        return new Point(this.getX() + p2.getX(), this.getY() + p2.getY());
    }

    public String toString() {
        return String.format("X=%f, Y=%f", this.getX(), this.getY());
    }


}
