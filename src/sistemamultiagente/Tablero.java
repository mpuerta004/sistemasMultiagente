package sistemamultiagente;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class Tablero {

    /**
     * ATRIBUTOS
     */

    private static Tablero myInstance;
    private HashMap<Agente, Point> tablero;
    private int etapa;

    /**
     * MÉTODOS
     */

    //Contructores, en las dos funciones creamos el contructor.---------------------------------------------------------
    private Tablero() {
        /* Esto es el contructor y esta pruvado porque solo tengo un tablero.*/
        tablero = new HashMap<>();
        this.etapa = 0;
    }

    public static Tablero getInstance() {
        /* Como el contructor esta vacio y necesito poner acceder a la intancia
         creada hago un metodo que coga la instancia*/
        if (myInstance == null) myInstance = new Tablero();
        return myInstance;
    }

    //Funciones get atributos del tablero. -----------------------------------------------------------------------------


    public int getEtapa() {
        return etapa;
    }

    public HashMap<Agente, Point> getTablero() {
        return tablero;
    }
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Funciones esenciales del programa: ---------------------------------------------------------------------------
     */


    //distanciaRealEuclideaPosicionesAgente:
    public double distanciaRealEuclideaPosicionesAgente(Agente agente1, Agente agente2) {
        Point posAgente1 = tablero.get(agente1);
        Point posAgente2 = tablero.get(agente2);
        return posAgente1.distance(posAgente2);
    }

    public void aumentarEtapa() {
        this.etapa = this.etapa + 1;
    }

    //primeraCoordenada AgenteTablero:
    //Si el agente esta perdido, le da una coordenada aletoria en calquier posicion el tablero.
    //si al egente no esta perdido le da una posicion dentro del cuadrante central de la diviison en 9 cuadrantes del
    // tablero
    private Point primeraCoordenadaAgenteTablero(Boolean perdido) {
        double posicionX;
        double posicionY;
        double ejeYMaximo = Constants.EJE_Y_MAXIMO;
        double ejeXMaximo = Constants.EJE_X_MAXIMO;
        if (perdido) {
            posicionX = Math.random() * ejeXMaximo;
            posicionY = Math.random() * ejeYMaximo;
        } else {
            if (Math.random() < 0.5) {
                posicionX = ejeXMaximo / 2 + Math.random() * (ejeYMaximo / 3) / 2;
            } else {

                posicionX = ejeXMaximo / 2 - Math.random() * (ejeYMaximo / 3) / 2;
            }
            if (Math.random() < 0.5) {
                posicionY = ejeXMaximo / 2 + (Math.random() * (ejeYMaximo / 3) / 2);
            } else {

                posicionY = ejeYMaximo / 2 - Math.random() * ((ejeYMaximo / 3) / 2);
            }
        }
        return new Point(posicionX, posicionY);
    }

    //conflicto:
    //Da un true si la posicion que se quiere meter el agente proboca que un agente tenga que estar encima de otro.
    private boolean conflictos(Point point) {
        Optional<Agente> agenteConflicto = tablero.keySet().stream()
                .filter(agenteTablero -> tablero.get(agenteTablero).distance(point) <= Constants.TAMAÑO_AGENTE)
                .findAny();
        return agenteConflicto.isPresent();
    }

    //añadirAgente:
    //Se calcula el id del agente, posteriormente se obtiene su posicion en el tablero (el agente no tiene porque
    // saberla se mira si hay conflicto y en caso de haberlo se vuelve a calcular otra posicion, sino se crea el agente
    // y se mete dicho agente en el tablero.
    public void anadirAgente(Boolean perdido) {
        int idAgente = tablero.size();
        Point punto1 = primeraCoordenadaAgenteTablero(perdido);
        while (conflictos(punto1)) punto1 = primeraCoordenadaAgenteTablero(perdido);
        Agente agente = new Agente(perdido, punto1, idAgente);
        tablero.put(agente, punto1);
    }

    //agentesCercanos:
    //Te devuelve los agente que estan desde tu posicion a una distancia manor o igual a la del
    // sensor(distanciaMaxSensor).
    private List<Agente> agentesCercanos(Agente agente) {
        double distanciaMaxSensor;
        distanciaMaxSensor = Constants.DISCANCIA_MAX_SENSOR;
        return tablero.keySet().stream().filter(agenteCercano ->
                distanciaRealEuclideaPosicionesAgente(agenteCercano, agente) <
                        distanciaMaxSensor && !agenteCercano.equals(agente)).collect(Collectors.toList());
    }

    //agentesCercanosNoPerdidos:
    //Te devuelve de los agentes cercanos aquellos qu eno estan perdidos, esta funcion se usa basicamente en los agente.
    public List<Agente> agentesCercanosNoPerdidos(Agente agente) {
        List<Agente> agentesCercanos = agentesCercanos(agente);
        return agentesCercanos.stream()
                .filter(agenteCercano -> !agenteCercano.getPerdido())
                .collect(Collectors.toList());

    }

    //sensorAgente:
    //Esto simula el sensor de porximidad, que le da al agente la distacia REAL a la que esta el otro agente,
    //con un error que debo añadir cuando. CUIDADO como es la real lo hhago con la del tablero y le doy la sol
    //con el error al agente.
    public double sensorAgente(Agente agente1, Agente agente2) {
        double error = errorUniforme(Constants.DISCANCIA_MAX_SENSOR);
        return (distanciaRealEuclideaPosicionesAgente(agente1, agente2) + Constants.ERROR * error);
    }

    //todo MAITE:
    public double errorUniforme(double distanciaMaxMovoSensor) {
        if (Math.random() < 0.5) {
            return Math.random() * distanciaMaxMovoSensor;
        } else {
            return -Math.random() * distanciaMaxMovoSensor;
        }
    }


    //redInalambrica:
    //Simula la red inalambrica que tienen los agentes, por lo que devuelve la poscion
    // que el agente le enviaria mediante esa red.
    public Point redInalambrica(Agente agente2) {
        return agente2.getPosicion();
    }

    //actualizarPosicion:
    //La idea es sumar a la posicion el vector de movimiento del agente. PAra ello debo asegurar que el vector de
    // movimiento no tiene error todavia. Ademas debo hacer el modulo para que no se salga del espacio y además si esta
    // nueva posicion provoco que dos agentes estan en la mism posicion (incluido el tamaño que tienene) calculare otra
    // posicion, para lo cual realizare otro calculo del vector de movimiento.
    //Por ultimo le dice al agente que tambien actualice su posicion.
    public void actualizarPosiciones(Agente agente) {
        Point posicionAntigua = tablero.get(agente);
        Point nuevaPosicion = posicionModuloTablero(tablero.get(agente).add(agente.getVectorMovimiento()));
        //todo EGO: creo que hay formas mejores de hacer esto
        tablero.put(agente, new Point(Constants.EJE_X_MAXIMO * 1000, Constants.EJE_Y_MAXIMO * 1000));
        while (conflictos(nuevaPosicion)) {
            agente.calcularVectorMovimiento();
            nuevaPosicion = posicionAntigua.add(agente.getVectorMovimiento());
        }
        tablero.put(agente, nuevaPosicion);
        agente.actualizarPosicion();
    }

    //posicionModuloTablero:
    public Point posicionModuloTablero(Point nuevaPosicion) {
        double posicionX = nuevaPosicion.getX();
        double posicionY = nuevaPosicion.getY();
        double ejeXMaximo = Constants.EJE_X_MAXIMO;
        double ejeYMaximo = Constants.EJE_Y_MAXIMO;
        if (!this.isDentro(nuevaPosicion)) {
            while (posicionX < 0.0) {
                posicionX = 0.0;
                //this.ejeXMaximo + posicionX;
            }
            while (posicionX > ejeXMaximo) {
                posicionX = ejeXMaximo;
                //posicionX - this.ejeXMaximo;
            }
            while (posicionY < 0.0) {
                posicionY = 0.0;
                // this.ejeYmaximo + posicionY;
            }
            while (posicionY > ejeYMaximo) {
                posicionY = ejeYMaximo;
                // posicionY - this.ejeYMaximo;
            }
            nuevaPosicion = new Point(posicionX, posicionY);
        }
        return nuevaPosicion;
    }

    //isDentro:
    //true si el punto esta dentro del tablero, false en caso contrario.
    public boolean isDentro(Point point) {
        double posicionX = point.getX();
        double posicionY = point.getY();
        if (0.0 <= posicionX && posicionX <= Constants.EJE_X_MAXIMO && 0.0 <= posicionY && posicionY <= Constants.EJE_Y_MAXIMO) {
            return true;
        } else {
            return false;
        }
    }


}