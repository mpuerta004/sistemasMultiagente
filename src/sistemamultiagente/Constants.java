package sistemamultiagente;

import java.util.ArrayList;

public class Constants {

    public static int AGENTES_NO_PERDIDOS=8;
    public static int AGENTES_PERDIDOS=30;

    /**AGENTE**/
    public static final double DISCANCIA_MAX_SENSOR = 2.0;
    public static final double DISTANCIA_MAX_MOV = 1.5;
    public static final int NUM_DE_PASOS_PARa_MEDIAR_LASTRILATERACIONES = 9;
    public static final double TAMAÑO_AGENTE= 0.25;
    public static final double RADIO_DE_REPULSION = 0.25;

    /** TABLERO **/
    public static final double EJE_Y_MAXIMO = 20.0;
    public static final double EJE_X_MAXIMO= 20.0;
    public static final double ERROR = 0.2;
    public static final double ERROR_MOV = 0.2;
    /**FIGURA --> CIRCULO**/
    public static final double RADIO = 7.0;
    public static final Point CENTER = new Point(10.0, 15.0);

    public static double COUNTBIEN = 0.0;
    public static double COUNTMAL=0.0;



    public static Point MEDIA_SIN_DIVIDIR=new Point(0.0,0.0);
    public static double VARIANZA =0.0;
    public static int AGENTES_CON_COORDENADAS=0;

}
