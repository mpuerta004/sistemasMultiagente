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
    private final double ejeYmaximo = 10.0;
    private final double ejeXMaximo = 10.0;

    /**
     * MÉTODOS
     */

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

    public double getEjeYmaximo() {
        return ejeYmaximo;
    }

    public double getEjeXMaximo() {
        return ejeXMaximo;
    }

    public double distanciaRealEuclideaPosicionesAgente(Agente agente1, Agente agente2) {
        // Esta es la distancia REAL

        Point posAgente1 = tablero.get(agente1);
        Point posAgente2 = tablero.get(agente2);


        return posAgente1.distance(posAgente2);
    }


    public int getEtapa() {
        return etapa;
    }

    private Point primeraCoordenadaAgenteTablero(Boolean perdido) {
        double posicionX;
        double posicionY;

        if (perdido) {
            // si entra aqui significa que esta perdido
            posicionX = Math.random() * ejeXMaximo;
            posicionY = Math.random() * ejeYmaximo;

        } else {
            // sino esta perdido los creamos en una zona concentrica.
            double masMenos = Math.random();
            if ((masMenos) < 0.5) {
                posicionX = this.getEjeXMaximo() / 2 + Math.random() * (this.ejeYmaximo / 3) / 2;
            } else {
                posicionX = this.getEjeXMaximo() / 2 - Math.random() * (this.ejeYmaximo / 3) / 2;
            }
            double masMenos2 = Math.random();
            if ((masMenos2) < 0.5) {
                posicionY = this.getEjeXMaximo() / 2 + (Math.random() * (this.ejeYmaximo / 3) / 2);
            } else {

                posicionY = this.getEjeYmaximo() / 2 - Math.random() * ((this.ejeYmaximo / 3) / 2);
            }
        }
        Point punto = new Point(posicionX, posicionY);
        return punto;
    }

    public void añadirAgente(Boolean perdido) {
        /* Comprueba que si sale False ese nuevo agente ya no puede estar en el tablero porque
        ya hay uno en esa posicion del tablero.*/

        int idAgente = tablero.size();
        Point punto1 = primeraCoordenadaAgenteTablero(perdido);
        while (conflictos(punto1)) {
            System.out.println("No puedo meter ese agente en esa posicion");
            System.out.println(punto1.toString());
            punto1 = primeraCoordenadaAgenteTablero(perdido);
        }
        Agente agente = new Agente(perdido, punto1, idAgente);
        tablero.put(agente, punto1);
        // aqui se le mete posicionTablero pero si perdido =false entonces el agente no tiene acceso a su posicion.
    }

    public void aumentarEtapa() {
        this.etapa = this.etapa + 1;
    }

    public boolean conflictos(Point point) {
        Optional<Agente> agenteConflicto = tablero.keySet().stream()
                .filter(agenteTablero -> tablero.get(agenteTablero).distance(point) <= agenteTablero.getTamanoAgente())
                .findAny();
        return agenteConflicto.isPresent();
    }

    public List<Agente> agentesCercanos(Agente agente) {
        // Devuelve una LIST de objetos agente que estan cerca del agente que tiene como parametro esta funcion.
        double distanciaMaxSensor = agente.getDistanciaMaxSensor();

        List<Agente> listaAgentesCercanos = tablero.keySet().stream()
                .filter(agenteCercano -> distanciaRealEuclideaPosicionesAgente(agenteCercano, agente) < distanciaMaxSensor && !agenteCercano.equals(agente))
                .collect(Collectors.toList());

        return listaAgentesCercanos;
    }

    public List<Agente> agentesCercanosNoPerdidos(Agente agente) {
        List<Agente> agentesCercanos = agentesCercanos(agente);
        List<Agente> agentesCercanosNoPerdidos = agentesCercanos.stream()
                .filter(agenteCercano -> !agenteCercano.getPerdido())
                .collect(Collectors.toList());
        return agentesCercanosNoPerdidos;

    }

    public double sensorAgente(Agente agente1, Agente agente2) {
        // El agente 1 tiene un sensor que puede mirar una cierta distancia de su posicion
        // ha encontrado en esa cierta distancia al agente2, entonces este metodo lo que hace
        // es darle la infromacion que le daria ese sensor.
        double distanciaReal = distanciaRealEuclideaPosicionesAgente(agente1, agente2);
        //todo MAite: añadir error.
        double distanciaMaxSensor = agente1.getDistanciaMaxSensor();
        /** ALERT luego aqui le metes el error, de momento no*/
        // double error = errorUniforme(distanciaMaxSensor);
        return distanciaReal;
    }

    public HashMap<Agente, Point> getTablero() {
        return tablero;
    }


    /**
     * ALERT cuando
     * public double errorUniforme(int distanciaMaxSensor){
     * <p>
     * return 0.0;
     * }
     */
    public Point redInalambrica(Agente agente2) {
        /** no puedo darle el control de esta comunicacion inalambrica al agente porque sino tendrian mucho poder...*/
        return agente2.getPosicion();
    }

    public void actualizarPosiciones(Agente agente) {

        /** ALERT se puede quedar en un bucle infinito  si la posicion siempre sale la misma */
        Point nuevaPosicion = posicionModuloTablero(tablero.get(agente).add(agente.getVectorMovimiento()));
        tablero.put(agente, nuevaPosicion);

       /* while (conflictos(tablero.get(agente))) {
            System.out.println("Conflicto agente :" + tablero.get(agente) + ",  Etapa: " + getEtapa() + "  id del agente " + agente.getId());
            agente.calcularVectorMovimiento();
            nuevaPosicion = posicionModuloTablero(tablero.get(agente).add(agente.getVectorMovimiento()));
            tablero.replace(agente, nuevaPosicion);
        }*/
        // si el agente se sale por el tablero por un lado regresara por el contrario
        agente.actualizarPosicion();
    }

    public Point posicionModuloTablero(Point nuevaPosicion) {
        double posicionX = nuevaPosicion.getX();
        double posicionY = nuevaPosicion.getY();
        if (!this.isDentro(nuevaPosicion)) {
            while (posicionX < 0.0) {
                posicionX = this.ejeXMaximo + posicionX;
            }
            while (posicionX > this.ejeXMaximo) {
                posicionX = posicionX - this.ejeXMaximo;
            }
            while (posicionY < 0.0) {
                posicionY = this.ejeYmaximo + posicionY;
            }
            while (posicionY > this.ejeYmaximo) {
                posicionY = posicionY - this.ejeYmaximo;
            }
            nuevaPosicion = new Point(posicionX, posicionY);
        }
        return nuevaPosicion;
    }

    public boolean isDentro(Point point) {
        Boolean isDentro;
        double posicionX = point.getX();
        double posicionY = point.getY();

        if (0.0 <= posicionX && posicionX <= this.ejeXMaximo && 0.0 <= posicionY && posicionY <= this.ejeYmaximo) {
            isDentro = true;
        } else {
            isDentro = false;
        }
        return isDentro;
    }
    /** ALERT Luego posiblemente tengas que implementar una rutina para visualizar los agente en este tablero*/

}