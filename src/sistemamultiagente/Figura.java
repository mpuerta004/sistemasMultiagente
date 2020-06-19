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


        if (point.getX() < 15&& point.getX()>5&& point.getY()<15&& point.getY()>5) {
            return true;
        } else {
            return false;
        }
    }
//        return figura.isDentro(point);
//    }

}
