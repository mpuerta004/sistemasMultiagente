package sistemamultiagente;

public class FiguraOjos implements FiguraInterface  {

    public boolean isDentroFigura(Point point) {
        FiguraInterface FIGURA_ojos = new FiguraCirculoDoble();

         Circle circulo1= new Circle( Constants.CENTER_FIGURA1_IRIS, Constants.RADIO_FIGURA1_IRIS);
        Circle circulo2= new Circle( Constants.CENTER_FIGURA2_IRIS, Constants.RADIO_FIGURA2_IRIS);
        return ( FIGURA_ojos.isDentroFigura(point) && !circulo1.isDentro(point) && !circulo2.isDentro(point));
    }
}
