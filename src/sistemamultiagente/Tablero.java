package sistemamultiagente;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Tablero {

    /**
     * ATRIBUTOS
     */

    private static Tablero myInstance;
    private HashMap<Agente, Point> tablero;
    private int etapa;
    private final int ejeYmaximo= 13;
    private final int ejeXMaximo = 13;

    /**
     * MÉTODOS
     */

    private Tablero() {
        /* Esto es el contructor y esta pruvado porque solo tengo un tablero.*/
        tablero = new HashMap<>();
        /** No se si esto se pondria aqui ????????????????????????????????????????????????????????*/
        this.etapa = 0;
    }

    public static Tablero getInstance() {
        /* Como el contructor esta vacio y necesito poner acceder a la intancia
         creada hago un metodo que coga la instancia*/
        if (myInstance == null) myInstance = new Tablero();
        return myInstance;
    }

    public double distanciaRealEuclideaPosicionesAgente(Agente agente1, Agente agente2) {
        // Esta es la distancia REAL
        Point posAgente1 = tablero.get(agente1);
        Point posAgente2 = tablero.get(agente2);

        return posAgente1.distance(posAgente2);
    }

    public boolean añadirAgente(Point posicionTablero) {
        /* El agente siempre esta perdido en este caso, y desconoce su posicion.*/
        return añadirAgente(true, posicionTablero);
    }

    public int getEtapa() {
        return etapa;
    }

    public Point get(Agente agente){
        return tablero.get(agente);
    }

    public boolean añadirAgente(boolean perdido, Point posicionTablero) {
        /* Comprueba que si sale False ese nuevo agente ya no puede estar en el tablero porque
        ya hay uno en esa posicion del tablero.*/

        int idAgente = tablero.size();
        Agente agente = new Agente(perdido, posicionTablero, idAgente);
        // aqui se le mete posicionTablero pero si perdido =false entonces el agente no tiene acceso a su posicion.

        Optional<Agente> agenteConflicto = tablero.keySet().stream()
                .filter(agenteTablero -> {
                    return distanciaRealEuclideaPosicionesAgente(agente, agenteTablero) == 0.0;
                })
                .findAny();

        if (agenteConflicto.isPresent())
            tablero.put(agente, posicionTablero);

        return agenteConflicto.isPresent();
    }

    public List<Agente> agentesCercanos(Agente agente) {
        // Devuelve una LIST de objeros agente que estan cerca del agente que tiene como atributo esta funcion.
        int distanciaMaxSensor = agente.getDistanciaMaxSensor();

        List<Agente> listaAgentesCercanos = tablero.keySet().stream()
                .filter(agenteCercano ->
                        (distanciaRealEuclideaPosicionesAgente(agenteCercano, agente) < distanciaMaxSensor && !agenteCercano.equals(agente)))
                .collect(Collectors.toList());

        return listaAgentesCercanos;
    }

    public List<Agente> agentesCercanosNoPerdidos(Agente agente) {
        List<Agente> agentesCercanos = agentesCercanos(agente);
        List<Agente> agentesCercanosNoPerdidos = agentesCercanos.stream()
                .filter(agenteCercano ->
                        (!agenteCercano.getPerdido()))
                .collect(Collectors.toList());
        return agentesCercanosNoPerdidos;

    }

    public double sensorAgente(Agente agente1, Agente agente2) {
        // El agente 1 tiene un sensor que puede mirar una cierta distancia de su posicion
        // ha encontrado en esa cierta distancia al agente2, entonces este metodo lo que hace
        // es darle la infromacion que le daria ese sensor.
        double distanciaReal = distanciaRealEuclideaPosicionesAgente(agente1, agente2);
        int distanciaMaxSensor = agente1.getDistanciaMaxSensor();
        double error = errorUniforme(distanciaMaxSensor);
        return distanciaReal+error;
    }

    public double errorUniforme(int distanciaMaxSensor){
        /** ALERT cuando todo este bien hago que tenga mayor complejidad*/
        return 0.0;
    }

    public Point redInalambrica(Agente agente1, Agente agente2) {
        /** Se supone que se la da y es siempre correcta, el agente1 pide y el otro se la da.*/
        /** ALERT Mira esta funcion porque no me convence del todo ????????????????????? */
        return agente2.getPosicion();
    }

    /** ALERT Luego posiblemente tengas que implementar una rutina para visualizar los agente en este tablero*/
}