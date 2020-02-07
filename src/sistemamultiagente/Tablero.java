package sistemamultiagente;

import java.util.*;
import java.util.stream.Collectors;

public class Tablero {

    /**
     * ATRIBUTOS
     */

    private static Tablero myInstance;
    private HashMap<Agente, List<Integer>> tablero;
    private int etapa;

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

    public double distanciaEuclideaPosicionesAgente(Agente agente1, Agente agente2) {
        List<Integer> posAgente1 = tablero.get(agente1);
        List<Integer> posAgente2 = tablero.get(agente2);

        return Math.sqrt(
                Math.pow(posAgente1.get(0) - posAgente2.get(0), 2) +
                        Math.pow(posAgente1.get(1) - posAgente2.get(1), 2));
    }

    public boolean añadirAgente(List<Integer> posicionTablero) {
        /* El agente siempre esta perdido en este caso, y desconoce su posicion.*/
        return añadirAgente(true, posicionTablero);
    }

    public int getEtapa() {
        return etapa;
    }

    public boolean añadirAgente(boolean perdido, List<Integer> posicionTablero) {
        /* Comprueba que si sale False ese nuevo agente ya no puede estar en el tablero porque
        ya hay uno en esa posicion del tablero.*/

        int idAgente = tablero.size();
        Agente agente = new Agente(perdido, posicionTablero, idAgente);
        // aqui se le mete posicionTablero pero si perdido =false entonces el agente no tiene acceso a su posicion.

        Optional<Agente> agenteConflicto = tablero.keySet().stream()
                .filter(agenteTablero -> {
                    return distanciaEuclideaPosicionesAgente(agente, agenteTablero) == 0.0;
                })
                .findAny();

        if (agenteConflicto.isPresent())
            tablero.put(agente, posicionTablero);

        return agenteConflicto.isPresent();
    }

    public List<Agente> agentesCercanos(Agente agente) {
        // Devuelve una LSIT de objeros agente que estan cerca del agente que tiene como atributo esta funcion.
        /** CUIDADO que este resultado es una lista.*/
        int distanciaMaxSensor = agente.getDistanciaMaxSensor();

        List<Agente> listaAgentesCercanos = tablero.keySet().stream()
                .filter(agenteCercano ->
                        (distanciaEuclideaPosicionesAgente(agenteCercano, agente) < distanciaMaxSensor && !agenteCercano.equals(agente)))
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
        /** Hacer*/
        return 0.0;
    }

    public List<Integer> redInalambrica(Agente agente1, Agente agente2) {
        /** Se supone que se la da y es siempre correcta, el agente1 pide y el otro se la da.*/
        return agente2.getPosicion();
    }

    public boolean movimientoPosible(Agente agente, LinkedList<Integer> posicionDeseada) {
        return false;

    }

    /** Luego posiblemente tengas que implementar una rutina para visualizar los agente en este tablero*/
}