package sistemamultiagente;

public class Line {

    /**
     * Atributos
     */

    private Point punto1;
    private Point punto2;
    private Vector direccion;

    /** Metodos     */

    /** todo:
     *  - Interseccion de dos lineas. ------
     *  - Constructor ---------------------- Hecho.
     */

    public Line(Point punto1, Point punto2){
        this.punto1=punto1;
        this.punto2=punto2;
        this.direccion= new Vector(punto2.x - punto1.x, punto2.y-punto1.y);
    }

    public Line(Point punto1, Vector direccion){
        this.punto1=punto1;
        this.punto2 = punto1.add(direccion);
        this.direccion = direccion;
    }

    public Point interseccion(Line l2){
        /**
         * Ax+By=C
         * Dx+Ey=F
         * ALERT repite las cuentas por si acaso.
         * */
        double a = this.direccion.getX();
        double b = l2.direccion.getX();
        double c = l2.punto1.getX() - this.punto1.getX();

        double d=this.direccion.getY();
        double e = this.direccion.getY();
        double f = l2.punto1.getY() - this.punto1.getY();

        double y = ((f * a) - (d * c)) / ((e * a) - (d * b));
        double x = (c - (b * y)) / a;

        return new Point(x,y);
    }


}
