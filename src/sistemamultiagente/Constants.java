package sistemamultiagente;

import java.util.ArrayList;

public class Constants {

    public static final int AGENTES_NO_PERDIDOS=12;
    public static final int AGENTES_PERDIDOS=150;
    public static final int NUMERO_ETAPAS=1001;
    public static final int NUMERO_ESPERA=2*10;

    /**AGENTE**/
    public static final double DISCANCIA_MAX_SENSOR = 5.0;
    public static final double DISTANCIA_MAX_MOV =3.0;
    public static final int NUM_DE_PASOS_PARa_MEDIAR_LASTRILATERACIONES = 10;
    public static final double TAMAÑO_AGENTE= 0.25;
    public static final double RADIO_DE_REPULSION = 2.0;
    public static final int NUMERO_AGENTES_CERCANOS_PARA_PENSAR_QUE_ESTAS_EN_LA_FIGURA= 15;

    /**Trilateracion**/
    public static final double LEARNING_RATE=0.001;
    public static final int NUMERO_MAXIMA_ITERACIONES=  800;
    public static final double CRITERIO=  0.00001;

    /** TABLERO **/
    public static final double EJE_Y_MAXIMO = 30.0;
    public static final double EJE_X_MAXIMO= 30.0;
    public static final double ERROR = 0.2;
    public static final double ERROR_MOV = 0.2;


//    public static final FiguraInterface FIGURA = new FiguraCirculo();
//    public static final FiguraInterface FIGURA = new FiguraCuadrado();
//    public static final FiguraInterface FIGURA = new FiguraCirculoDoble();
 //  public static final FiguraInterface FIGURA = new FiguraSonrisa();
    public static final FiguraInterface FIGURA = new FiguraCara();

    /**FIGURA --> CIRCULO**/
    public static final double RADIO = 10.0;
    public static final Point CENTER = new Point(25.0, 25.0);


    /**FIGURA --> CUADRADP**/
    public static final double EJE_X_MINIMO_FIGURA=5.0;
    public static final double EJE_Y_MINIMO_FIGURA=5.0;
    public static final double EJE_X_MAXIMO_FIGURA=15.0;
    public static final double EJE_Y_MAXIMO_FIGURA=15.0;

    /** FIGURA -> CIRCULO DOBLE**/
    public static final  Point  CENTER_FIGURA1= new Point(21.0,13.0);
    public static final  double RADIO_FIGURA1= 5.0;
    public static final  Point CENTER_FIGURA2= new Point(13.0,13.0);
    public static final  double RADIO_FIGURA2=5.0;

    /** FIGURA -> CIRCULO DOBLE SUPUESPUESTOS **/
    public static final  Point  CENTER_FIGURA1_SONRISA= new Point(15.0,23.0);
    public static final  double RADIO_FIGURA1_SONRISA= 5.0;
    public static final  Point CENTER_FIGURA2_SONRISA= new Point(15.0,18.0);
    public static final  double RADIO_FIGURA2_SONRISA=5.0;


}
