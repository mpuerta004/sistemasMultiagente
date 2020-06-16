package sistemamultiagente;

import sim.field.continuous.Continuous2D;

public class Figura {

    /**
     * Atributos
     */


    private final Circle figura;

    /**
     * Métodos
     */

    public Figura() {
        figura = new Circle(Constants.CENTER, Constants.RADIO);
    }

    public boolean isDentroFigura(Point point) {
        return figura.isDentro(point);
    }

}
