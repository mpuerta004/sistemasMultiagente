package sistemamultiagente;

public class FiguraCirculoDoble implements FiguraInterface{




//    private final Circle figura1;
//    private final Circle figura2;
//
//    public FiguraCirculoDoble() {
//        figura1 = new Circle(Constants.CENTER_FIGURA1, Constants.RADIO_FIGURA1);
//        figura2= new Circle(Constants.CENTER_FIGURA2, Constants.RADIO_FIGURA2);
//    }


    public boolean isDentroFigura(Point point) {
        Circle figura1 = new Circle(Constants.CENTER_FIGURA1, Constants.RADIO_FIGURA1);
        Circle figura2= new Circle(Constants.CENTER_FIGURA2, Constants.RADIO_FIGURA2);
        if (figura1.isDentro(point) || figura2.isDentro(point)) {return true;}
        else{
        return false;}
    }

}
