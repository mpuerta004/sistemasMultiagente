package sistemamultiagente;

public class FiguraCuadrado {


    /**
     * Atributos
     */



    /**
     * Métodos
     */

    public FiguraCuadrado() {
        //FiguraCuadrado figura = new FiguraCuadrado();
    }


    public boolean isDentroFigura(Point point) {
        if (point.getX() < Constants.EJE_X_MAXIMO_FIGURA && point.getX() > Constants.EJE_X_MINIMO_FIGURA
                && point.getY() < Constants.EJE_Y_MAXIMO_FIGURA && point.getY() > Constants.EJE_Y_MINIMO_FIGURA) {
            return true;
        } else {
            return false;
        }
    }
}
