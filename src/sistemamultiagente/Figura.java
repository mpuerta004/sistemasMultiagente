package sistemamultiagente;

public class Figura {

    /**
     * Atributos
     */

    private final double radio = 10.0;
    private final Point center = new Point(20.0, 20.0);
    private final Circle figura;

    /**
     * Métodos
     */

    public Figura() {
        figura = new Circle(center, radio);
    }

    public Point getCenter() {
        return this.center;
    }

    public double getRadio() {
        return this.radio;
    }

    public boolean isDentroFigura(Point point) {
        return figura.isDentro(point);
    }

}
