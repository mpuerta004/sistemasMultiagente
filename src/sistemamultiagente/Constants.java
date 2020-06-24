package sistemamultiagente;

import java.util.ArrayList;

public class Constants {

    public static final int AGENTES_NO_PERDIDOS=8;
    public static final int AGENTES_PERDIDOS=60;
    public static final int NUMERO_ETAPAS=1001;
    public static final int NUMERO_ESPERA=3*10;

    /**AGENTE**/
    public static final double DISCANCIA_MAX_SENSOR = 5.0;
    public static final double DISTANCIA_MAX_MOV =3.0;
    public static final int NUM_DE_PASOS_PARa_MEDIAR_LASTRILATERACIONES = 10;
    public static final double TAMAÑO_AGENTE= 0.25;
    public static final double RADIO_DE_REPULSION = 3.0;


    /** TABLERO **/
    public static final double EJE_Y_MAXIMO = 20.0;
    public static final double EJE_X_MAXIMO= 20.0;
    public static final double ERROR = 0.2;
    public static final double ERROR_MOV = 0.2;

    /**FIGURA --> CIRCULO**/
    public static final double RADIO = 7.0;
    public static final Point CENTER = new Point(10.0, 15.0);

    /**FIGURA --> CUADRADP**/
    public static final double EJE_X_MINIMO_FIGURA=5.0;
    public static final double EJE_Y_MINIMO_FIGURA=5.0;
    public static final double EJE_X_MAXIMO_FIGURA=15.0;
    public static final double EJE_Y_MAXIMO_FIGURA=15.0;

    public static final double LEARNING_RATE=0.001;
    public static final int NUMERO_MAXIMA_ITERACIONES=  1000;
    public static final double CRITERIO=  0.00001;
}
