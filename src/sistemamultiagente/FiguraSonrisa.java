package sistemamultiagente;

public class FiguraSonrisa implements FiguraInterface{

    public boolean isDentroFigura(Point point) {
        Circle figura1 = new Circle(Constants.CENTER_FIGURA1_SONRISA, Constants.RADIO_FIGURA1_SONRISA);
        Circle figura2= new Circle(Constants.CENTER_FIGURA2_SONRISA, Constants.RADIO_FIGURA2_SONRISA);
        if (figura1.isDentro(point) && !figura2.isDentro(point)) {return true;}
        else{
            return false;}
    }

}
