package sistemamultiagente;

public class FiguraCirculo implements FiguraInterface {

    /**
     * Atributos
     */
    //private final Circle figura;

    /**
     * Métodos
     */

//    public FiguraCirculo() {
//        figura = new Circle(Constants.CENTER, Constants.RADIO);
//    }


    public boolean isDentroFigura(Point point) {
        Circle figura = new Circle(Constants.CENTER, Constants.RADIO);
        return figura.isDentro(point);
    }

}
