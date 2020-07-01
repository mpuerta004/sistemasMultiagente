package sistemamultiagente;

public class FiguraCara implements FiguraInterface {

    public boolean isDentroFigura(Point point) {
        FiguraInterface FIGURA_ojos = new FiguraCirculoDoble();
        FiguraInterface FIGURA_sonrisa = new FiguraSonrisa();
        return (FIGURA_ojos.isDentroFigura(point) || FIGURA_sonrisa.isDentroFigura(point));
    }

}
