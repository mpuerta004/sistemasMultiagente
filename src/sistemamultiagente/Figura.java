package sistemamultiagente;

public class Figura {

    /**
     * Atributos
     */

    private final double radio = 3.0;
    private final Point center = new Point(5.0, 5.0);
    private final Circle figura;

    /**
     * Métodos
     */

    public Figura() {
        figura = new Circle(center, radio);
    }

    public boolean isDentroFigura(Point point) {
        return figura.isDentro(point);
    }

}
