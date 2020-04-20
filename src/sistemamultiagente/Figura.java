package sistemamultiagente;

public class Figura {

    /**
     * Atributos
     */

    private final double radio = 5.0;
    private final Point center = new Point(6.5, 6.5);
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
