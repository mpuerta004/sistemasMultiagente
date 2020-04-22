package sistemamultiagente;

import java.util.Arrays;
import java.util.List;

public class Circle {

    /**
     * Atributos
     */

    private Point center;
    private double radio;

    /**
     * Métodos
     */

    public Circle(Point center, double radio) {
        this.center = center;
        this.radio = radio;
    }

    public double getRadio() {
        return radio;
    }

    public Point getCenter() {
        return center;
    }

    public List<Point> intersection(Circle other) {

        double d = getCenter().distance(other.getCenter());

        if (d > this.getRadio() + other.getRadio()) throw new ArithmeticException("The circles have no intersections");
        if (d >= this.getRadio() + other.getRadio()) throw new ArithmeticException("The circles have one intersection");
        if (this.getRadio() >= d + other.getRadio()) throw new ArithmeticException("Un circulo esta dentro de otro");
        if (other.getRadio() >= d + this.getRadio()) throw new ArithmeticException("Un circulo esta dentro de otro");


        //https://stackoverflow.com/questions/3349125/circle-circle-intersection-points/60130568#60130568
        double a = (radio * radio - other.radio * other.radio + d * d) / (2 * d);
        double h = Math.sqrt(radio * radio - a * a);
        Point P2 = other.center.sub(this.getCenter()).scale(a / d).add(this.center);
        double x3 = P2.getX() + h * (other.center.getY() - this.center.getY()) / d;
        double y3 = P2.getY() - h * (other.center.getX() - this.center.getX()) / d;
        double x4 = P2.getX() - h * (other.center.getY() - this.center.getY()) / d;
        double y4 = P2.getY() + h * (other.center.getX() - this.center.getX()) / d;

        return Arrays.asList(new Point(x3, y3), new Point(x4, y4));
    }


    public boolean isDentro(Point point) {
        Boolean dentro;
        double distancia = point.distance(this.center);
        if (distancia <= this.radio) {
            dentro = true;
        } else {
            dentro = false;
        }
        return dentro;
    }


}