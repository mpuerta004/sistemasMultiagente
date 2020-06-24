package sistemamultiagente;

import java.util.ArrayList;
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
// throw new ArithmeticException("The circles have no intersections");
        double d = getCenter().distance(other.getCenter());
        Point direccion;
        ArrayList<Point> result =new ArrayList<>();
        if (d > this.getRadio() + other.getRadio()) {
            direccion= this.center.sub(other.getCenter()); // direccion de this a other.
            //Double moduloDireccion = direccion.distance(new Point(0.0, 0.0));
            direccion = direccion.div(d);
            double d2  = (d - this.getRadio() - other.getRadio())/2;
            direccion = direccion.scale(d2+this.getRadio());
            result.add(Tablero.getInstance().posicionModuloTablero(this.getCenter().add(direccion)));

        }
//        else if ( this.getRadio() > d + other.getRadio() ) {
//            //ystem.out.println("22222222222222222");
//            direccion= this.center.sub(other.getCenter());
//            //Double moduloDireccion = direccion.distance(new Point(0.0, 0.0));
//            direccion = direccion.div(d);
//            direccion = direccion.scale(this.getRadio());
//            result.add(Tablero.getInstance().posicionModuloTablero(this.getCenter().add(direccion)));
//            System.out.println("Distancia del punto: "
//                    +result.get(0).distance(Tablero.getInstance().getTablero().get(this)));
//        }
        else if (other.getRadio() > d + this.getRadio()) {
            //System.out.println("33333333333333");
            direccion= other.center.sub(this.getCenter());
            //Double moduloDireccion = direccion.distance(new Point(0.0, 0.0));
            direccion = direccion.div(d);
            direccion = direccion.scale(other.getRadio());
            result.add(Tablero.getInstance().posicionModuloTablero(other.getCenter().add(direccion)));

        }
        else if (d == this.getRadio() + other.getRadio() || this.getRadio() > d + other.getRadio()) {
            //todo no se si esta direccion esta bien.
            direccion = this.center.sub(other.getCenter());//direccion que this-->Other
            direccion = direccion.div(d);
            direccion = direccion.scale(this.getRadio());
            result.add(Tablero.getInstance().posicionModuloTablero(this.getCenter().add(direccion)));

        }

        else if (d < this.getRadio() + other.getRadio()) { //SECANTES.
            //System.out.println("55555555555555555");
            //https://stackoverflow.com/questions/3349125/circle-circle-intersection-points/60130568#60130568
            double a = (radio * radio - other.radio * other.radio + d * d) / (2 * d);
            double h = Math.sqrt(radio * radio - a * a);
            Point P2 = other.center.sub(this.getCenter()).scale(a / d).add(this.center);
            double x3 = P2.getX() + h * (other.center.getY() - this.center.getY()) / d;
            double y3 = P2.getY() - h * (other.center.getX() - this.center.getX()) / d;
            double x4 = P2.getX() - h * (other.center.getY() - this.center.getY()) / d;
            double y4 = P2.getY() + h * (other.center.getX() - this.center.getX()) / d;
            result.add(Tablero.getInstance().posicionModuloTablero(new Point(x3,y3)));
            result.add(Tablero.getInstance().posicionModuloTablero(new Point(x4,y4)));
            //return Arrays.asList(new Point(x3, y3), new Point(x4, y4));
        }

        return result;
    }


    public boolean isDentro(Point point) {
        Boolean dentro = false;
        double distancia = point.distance(this.center);
        if (distancia <= this.radio) {
            dentro = true;
        }
        return dentro;
    }


}