package sistemamultiagente;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Agente {

    /**
     * ATRIBUTOS
     */

    //ALERT Estos numero luego lees el paper y los pones todos bien.
    private final double distanciaMaxSensor = 1.5;
    private final double distanciaMaxMov = 1.5;
    private final int numDePasosParaMediarLasTrilateraciones = 10;
    private final int numTrilateracionesGuardo = 10;
    private final double tamañoAgente = 0.25;
    private final double radioDeRepulsion = 1.0;

    private final Figura figura = new Figura();

    private Integer id;
    private boolean perdido;
    private Point posicion;
    private Stack<Point> listaTrilateraciones;
    private Vector vectorMovimiento;

    /**
     * MÉTODOS
     */


    //Constructor 2 --- Para crear agentes no perdidos o perdidos. -----------------------------------------------------
    public Agente(boolean perdido, Point posicion, Integer id) {
        this.posicion = posicion;
        this.perdido = perdido;
        if (perdido) this.posicion = null;
        this.listaTrilateraciones = new Stack<>();
        this.vectorMovimiento = new Vector(0.0, 0.0);
        this.id = id;
    }

    // Funciones  get atributos del agente.-----------------------------------------------------------------------------
    public double getTamanoAgente() {
        return tamañoAgente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Vector getVectorMovimiento() {
        return vectorMovimiento;
    }

    public void setVectorMovimiento(Vector vectorMovimiento) {
        this.vectorMovimiento = vectorMovimiento;
    }

    public boolean getPerdido() {
        return perdido;
    }

    public Figura getFigura() {
        return figura;
    }

    public Point getPosicion() {
        return posicion;
    }

    public Stack<Point> getListaTrilateraciones() {
        return listaTrilateraciones;
    }

    public double getDistanciaMaxSensor() {
        return distanciaMaxSensor;
    }

    public double getDistanciaMaxMov() {
        return distanciaMaxMov;
    }
    //------------------------------------------------------------------------------------------------------------------

    @Override
    //Esto te permite comparar dos objetos Agente.----------------------------------------------------------------------
    public boolean equals(Object o) {
        /*Si el objeto del que llamas es igual al agente del parentesis de la funcion.*/
        if (this == o) return true;
        /* Si el objerto o es vacio o no es de la clase objeto devuelve False*/
        if (o == null || getClass() != o.getClass()) return false;
        //todo EGO: ¿Porque no pongo en la funcion Agente o y asi no necesito esto?
        //La linea de aqui abajo es porque auqnue yo sepa que el objeto o es un agente java no,
        // por lo se necesita un cast.
        Agente agente = (Agente) o;
        return Objects.equals(id, agente.id);
    }

    /** Funciones esenciales del programa:
     * Categoria Consenso de coordenadas! ----------------------------------------------------------------------------*/

    //PrimeraCoordenadaAgentePerdido ---> le permite saber una posicion aproximada por primera vez al egente que esta
    // perdido, se su posicion.

    /**
     * Teoria de la realizacon:
     * 1- Con las posiciones de los tres agentes no perdidos, creamos tres circulos de radios aleatorios cuyos
     * centros sean estas tres posiciones, que el agente conocera por la trnasmision inalambrica.
     * 2- Cada par de circulo da un punto intersante --> al final vamos a eleguir uno de estos tres puntos para
     * la coordenada del agente.
     * 3- Realizacion del calculo:
     * 3.1- interseccion de dos circunferencias (P y Q), en los puntos PQa, PQb.
     * 3.2- Obtenemos la linea que pasa por estos dos puntos L1.
     * 3.3- Interseccion de la circunferencias Q y R, en los puntos PRa y PRb.
     * 3.4- Obtenemos la linea que pasa por estos dos puntos L2.
     * 3.5- Obtenemos el punto de interseccion de las lineas L1 y L2.
     * 3.6- Eleguimos el punto de PQa y PQb que este mas cerca del punto calculado en el apartado 3.5.
     */
    private Point primeraCoordenadaAgentePerdido(List<Agente> tresAgentesCercanosNoPerdidos) {
        //Se realiza con el baricentro del triangulo, compuesto por las posiciones de los tres agentes cercanos
        // no perdidos. Cuidado: la posicion del agente la debe obtener por la red inalambrica que se estabrece entre
        // ellos, ya que la el tablero es la real.
        return new Point(
                tresAgentesCercanosNoPerdidos.stream().mapToDouble(a ->
                        Tablero.getInstance().redInalambrica(a).getX()).sum() / 3,
                tresAgentesCercanosNoPerdidos.stream().mapToDouble(a ->
                        Tablero.getInstance().redInalambrica(a).getX()).sum() / 3
        );
    }

    //Trilateracion:
    //Sabes la posiciones de tres agentes no perdidos, y mides la distancia que tienes a ellos con tu sensor.
    //Las coordenadas del otro agente las va a proporcional el tablero porque el agente no puedo saber mucho del
    //otro agente.
    //Calculas con esos datos tu posicion, que como tienes los radios y los circulos es un sistema de 3
    //ecuaciones, basado en encontrar el punto donde se cortan las tres circunferencias que has definido con todos los
    //datos.
    private Point trilateracion(List<Agente> tresAgentesCercanosNoPerdidos) {
        double distanciaSensorAgente1 = Tablero.getInstance().sensorAgente(this, tresAgentesCercanosNoPerdidos.get(0));
        double distanciaSensorAgente2 = Tablero.getInstance().sensorAgente(this, tresAgentesCercanosNoPerdidos.get(1));
        double distanciaSensorAgente3 = Tablero.getInstance().sensorAgente(this, tresAgentesCercanosNoPerdidos.get(2));
        //Este calculo lo hace el agente y la informacion que tiene el agente de la posiciones de los otros es
        //la posicion a la que el otro agente cree estar.
        //(xi, yi) posicion a la que cree estar el agente cercano i-esimo, donde i puede ser 1, 2, 3.
        double x1 = Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(0)).getX();
        double y1 = Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(0)).getY();
        double x2 = Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(1)).getX();
        double y2 = Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(1)).getY();
        double x3 = Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(2)).getX();
        double y3 = Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(2)).getY();

        double a = -2 * x1 + 2 * x2;
        double b = -2 * y1 + 2 * y2;
        double c = distanciaSensorAgente1 * distanciaSensorAgente1 - distanciaSensorAgente2 * distanciaSensorAgente2
                - x1 * x1 + x2 * x2 - y1 * y1 + y2 * y2;
        double d = -2 * x2 + 2 * x3;
        double e = -2 * y2 + 2 * y3;
        double f = distanciaSensorAgente2 * distanciaSensorAgente2 - distanciaSensorAgente3 * distanciaSensorAgente3
                - x2 * x2 + x3 * x3 - y2 * y2 + y3 * y3;
        double y = (d * c - a * f) / (d * b - a * e);
        double x = (c - b * y) / a;
        return new Point(x, y);
    }

    //todo MAITE: cuando funcione complementamente bien sin errores, e intruduzcas el error, deben emplementarlo.
    public Point gradientDescent(Point posicionAgente, Point solTrilateraciones) {
        boolean iterar = true;
        double beta = 0.9;
        int i = 0;
        while (iterar && i < 20) {
            Line l1 = new Line(posicionAgente, solTrilateraciones);
            //pendiente
            double triangulo = l1.getDireccion().getY() / l1.getDireccion().getX();
            Point nuevo = new Point(solTrilateraciones.getX() - beta * triangulo * solTrilateraciones.getX(),
                    solTrilateraciones.getY() - beta * triangulo * solTrilateraciones.getY());
            posicionAgente = solTrilateraciones;
            solTrilateraciones = nuevo;
            i++;
            // condiciones, si la distancia entre lso putnos es manor que el tamaño del agente o la pendiente ya es enanisima.
            if (solTrilateraciones.distance(posicionAgente) < this.tamañoAgente || triangulo < 0.1) iterar = false;
        }
        return solTrilateraciones;
    }

    //todo MAITE: ver que va bien y que guarda las que tiene que guardar, no mas!
    private Point mediaTrilateracion() {
        if (listaTrilateraciones.size() <= numTrilateracionesGuardo) {
            return this.posicion;
        } else {
            double sumX = 0.0;
            double sumY = 0.0;
            for (int i = 0; i < numTrilateracionesGuardo; i++) {
                Point nuevo = listaTrilateraciones.pop();
                sumX += nuevo.getX();
                sumY += nuevo.getY();
            }
            return new Point(sumX / numTrilateracionesGuardo, sumY / numTrilateracionesGuardo);
        }
    }

    //consensoDeCoordenadas:
    // Si el agente estaba perdido y hay al menos tres agentes no perdidos cerca ---> calculo el baricentro y realizo
    //to-do lo demas:
    // si el agente no estaba perdido o acaba de conseguir las coordenadas, ajunto las coordendas con la trilateracion
    //y el descenso del gradiente. esta coordenada la almaceno en la lista de trilateracion y cuando tenga un numero
    //especificado hare la media de estas para obtener un refinamiento.
    public void consensoDeCoordenadas() {
        List<Agente> agentesCercanosNoPerdidos = Tablero.getInstance().agentesCercanosNoPerdidos(this);
        if (agentesCercanosNoPerdidos.size() >= 3) {
            List<Agente> tresAgentesCercanosNoPerdidos = tresAgentesDeUnaLista(agentesCercanosNoPerdidos);
            if (this.perdido) {
                // si esta perdido, entonces calcula la primera coordena ---> Baricentro.
                this.posicion = this.primeraCoordenadaAgentePerdido(tresAgentesCercanosNoPerdidos);
                this.perdido = false;

                //Ahora que tengo coordenadas, las ajunto, con oros tres agentes.
                tresAgentesCercanosNoPerdidos = tresAgentesDeUnaLista(agentesCercanosNoPerdidos);
                Point solTrilateracion = this.trilateracion(tresAgentesCercanosNoPerdidos);
                //todo Maite: cuando arregles el descenso del gradiente porque halla errores.
                // solTrilateracion = this.gradientDescent(this.posicion, solTrilateracion );
                this.posicion = solTrilateracion;
                //todo EGO: esta bien usar add o debria ser push ¿?
                this.listaTrilateraciones.push(solTrilateracion);
                if (Tablero.getInstance().getEtapa() % numDePasosParaMediarLasTrilateraciones == 0) {
                    this.posicion = mediaTrilateracion();
                }
            } else {
                //todo MAITE, cuando no estan perdidos tambien deberian realizar este ajuste, porque hay fallos,
                // aqui al no haber fallos pues claro.... no puedo hacerlo porque da errores...
            }

        }
    }

    //tresAgentesDeUnaLista:
    //Voy a coger de la lista de agentes cercanos no perdidos, 3 cualquira de ellos.
    private List<Agente> tresAgentesDeUnaLista(List<Agente> agentesCercanosNoPerdidos) {
        Collections.shuffle(agentesCercanosNoPerdidos); // desordeno la lista!
        return agentesCercanosNoPerdidos.subList(0, 3);
    }

    /**
     * Funciones esenciales del programa:
     * Categoria Calcular el movimiento| ----------------------------------------------------------------------------
     */

    //movFuera:
    //Si el agente esta fuera de la figura, debe moverse hacia una direccion aleatoria, dentro del rango de movimiento
    //que tiene.
    private Vector movFuera() {
        double r1;
        double r2;
        if (Math.random() < 0.5) {
            r1 = -Math.random() * this.getDistanciaMaxMov();
        } else {
            r1 = Math.random() * this.getDistanciaMaxMov();
        }
        if (Math.random() < 0.5) {
            r2 = -Math.random() * this.getDistanciaMaxMov();
        } else {
            r2 = Math.random() * this.getDistanciaMaxMov();
        }
        return new Vector(r1, r2);
    }


    //movDentro:
    //La calcula la repulsion que cada agente ejerce sobre otro agente, cuando estan dentro de la figura, el resultado de
    //ña suma de las respulsiones es lo que el agente se va a mover.
    private Vector movDentro() {
        AtomicReference<Point> solucion1 = new AtomicReference<>(new Point(0.0, 0.0));
        Tablero.getInstance().agentesCercanosNoPerdidos(this).forEach(agente -> {
            double distanciaAgente = Tablero.getInstance().sensorAgente(this, agente);
            Point diferenciaPosiciones = this.posicion.sub(agente.getPosicion());
            Point repulsionPorEseAgente = diferenciaPosiciones.div(distanciaAgente).scale(radioDeRepulsion - distanciaAgente);
            //todo EGO: java me obligo a hacerlo atomico es correcto?
            solucion1.set(repulsionPorEseAgente.add(solucion1.get()));
        });

        return new Vector(solucion1.get().getX(), solucion1.get().getY());

    }

    //CalcularVectorMovimiento:
    //calculo lo que tengo que moverme pero no realizo el movimiento.
    //Este caclulo se hace siguientdo las siguientes normal:
    //Si estoy perdido o figuera de la figura ---> movFuera().
    //si estoy dentro de la figura ----> movDentro().
    // Si este movimiento sumado a mi opision ocasiona que me falga d ela figura, hago una porbabilidad si esta p
    //probabilidad es menor del 0.75 entonces me quedo quiero no me muevo, en caso contrario (con posibilidad pequeña
    //creo un movumiento aleatoria (MovFuera) asegurandome que este me deja dentro de la figura.
    public void calcularVectorMovimiento() {
        if (this.perdido) { // esta perdido.
            this.vectorMovimiento = this.movFuera();
        } else { // no esta perdido
            if (this.figura.isDentroFigura(this.posicion)) { // esta dentro de la figura
                this.vectorMovimiento = this.movDentro();
                if (this.figura.isDentroFigura(this.posicion.add(this.vectorMovimiento))) {
                    if (Math.random() < 0.75) {
                        this.vectorMovimiento = new Vector(0.0, 0.0);
                    } else {
                        this.vectorMovimiento = movFuera();
                        while (!this.figura.isDentroFigura(this.posicion.add(this.vectorMovimiento))) {
                            this.vectorMovimiento = movFuera();
                        }
                    }
                }
            } else {
                this.vectorMovimiento = this.movFuera();
            }
        }
    }

    //actualizarPosicion:
    //si el agente no esta perdido, es decir tiene posicion, sumo su posicion y el vector movimiento para definir su
    //nueva posicion.
    //Esta funcione s llamada en el tablero.
    public void actualizarPosicion() {
        if (this.posicion != null) {
            this.posicion = this.posicion.
                    add(this.vectorMovimiento).
                    add(
                            new Point(Tablero.getInstance().errorUniforme(this.distanciaMaxMov),
                                    Tablero.getInstance().errorUniforme(this.distanciaMaxMov)));
            this.posicion = Tablero.getInstance().posicionModuloTablero(this.posicion);
        }
    }

    //agenteisDentroFigura:
    //Si el agente no esta perdido y su posicion (la que el cree) esta dentro d ela figura, entonces devuelve True
    //en caso contrario, es decir la posicion sea nula o el agente este fuera de la figura, devuelve False.
    public boolean agenteisDentroFigura() {
        return (this.posicion != null && this.figura.isDentroFigura(this.posicion));
    }
}
