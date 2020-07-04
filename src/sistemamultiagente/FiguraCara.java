package sistemamultiagente;

public class FiguraCara implements FiguraInterface {

    public boolean isDentroFigura(Point point) {
        FiguraInterface FIGURA_ojos = new FiguraOjos();
        FiguraInterface FIGURA_sonrisa = new FiguraSonrisa();
        return (  FIGURA_sonrisa.isDentroFigura(point) ||FIGURA_ojos.isDentroFigura(point));
    }

}
