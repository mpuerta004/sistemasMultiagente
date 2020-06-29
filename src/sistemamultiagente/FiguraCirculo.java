package sistemamultiagente;

public class FiguraCirculo {

    /**
     * Atributos
     */
    private final Circle figura;

    /**
     * Métodos
     */

    public FiguraCirculo() {
        figura = new Circle(Constants.CENTER, Constants.RADIO);
    }


    public boolean isDentroFigura(Point point) {
        return figura.isDentro(point);
    }

}
