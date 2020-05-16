package sistemamultiagente;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Agente {

    /**
     * ATRIBUTOS
     */

    //ALERT Estos numero luego lees el paper y los pones todos bien.
    private final double distanciaMaxSensor = 4.0;
    private final double distanciaMaxMov = 1.0;
    private final int numDePasosParaMediarLasTrilateraciones = 10;
    private final int numTrilateracionesGuardo = 10;
    private final double tamañoAgente = 1.0;
    private final double radioDeRepulsion = 1.5;

    private final Figura figura = new Figura();

    private Integer id;
    private boolean perdido;
    private Point posicion;
    private List<Point> listaTrilateraciones;
    private Vector vectorMovimiento;

    /**
     * MÉTODOS
     */


    //Constructor 2 --- Para crear agentes no perdidos o perdidos. -----------------------------------------------------
    public Agente(boolean perdido, Point posicion, Integer id) {
        this.posicion = posicion;
        this.perdido = perdido;
        if (perdido) this.posicion = null;
        this.listaTrilateraciones = new ArrayList<>();
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

    public List<Point> getListaTrilateraciones() {
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
    private Point trilateracion(List<Agente> agentesCercanosNoPerdidos) {
        //double distanciaSensorAgente1 = Tablero.getInstance().sensorAgente(this, tresAgentesCercanosNoPerdidos.get(0))
        //double x1 = Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(0)).getX();
        ArrayList<Point> listaDeTresPosiciones= new ArrayList<>();
        ArrayList<Double> listaDeTresUltimasEvaluaciones = new ArrayList<>();
        listaDeTresPosiciones.add(this.posicion);
        listaDeTresPosiciones.add(this.posicion);
        listaDeTresPosiciones.add(this.posicion);
        double insertar = evaluarFuncionParaTrilateracion(agentesCercanosNoPerdidos, this.posicion);
        listaDeTresUltimasEvaluaciones.add(insertar);
        listaDeTresUltimasEvaluaciones.add(insertar);
        listaDeTresUltimasEvaluaciones.add(insertar);
        double grad;
        while (  !( listaDeTresUltimasEvaluaciones.get(1)  < listaDeTresUltimasEvaluaciones.get(0)  &&
              listaDeTresUltimasEvaluaciones.get(1) <  listaDeTresUltimasEvaluaciones.get(2)) ) {


           listaDeTresUltimasEvaluaciones.remove(0);
                 listaDeTresPosiciones.remove(0);

                grad = gradiente(agentesCercanosNoPerdidos, listaDeTresPosiciones.get(1));
                listaDeTresPosiciones.add( Tablero.getInstance().posicionModuloTablero(listaDeTresPosiciones.get(1).scale(1-0.01*grad)));

                listaDeTresUltimasEvaluaciones.add(
                        evaluarFuncionParaTrilateracion(agentesCercanosNoPerdidos,
                        listaDeTresPosiciones.get(2)));
                System.out.println("-----------------------------------------------------------------------------------");
            System.out.println("PRimera posicion del vecotr" + listaDeTresPosiciones.get(0).toString()+
                    "Evaluacion"+ listaDeTresUltimasEvaluaciones.get(0));
            System.out.println("Segunda posicion del vecotr" + listaDeTresPosiciones.get(1).toString()+
                    "Evaluacion"+ listaDeTresUltimasEvaluaciones.get(1));
            System.out.println("Tercera posicion del vecotr" + listaDeTresPosiciones.get(2).toString()+
                    "Evaluacion"+ listaDeTresUltimasEvaluaciones.get(2));


        }
        return listaDeTresPosiciones.get(2);
    }

    private double evaluarFuncionParaTrilateracion(List<Agente> agentesCercanosNoPerdidos,Point posicionAgenteCalculando ){
        double resultado = 0.0;
        Point posicionAgente;
        double distanciaAgente;

        for (Agente agente : agentesCercanosNoPerdidos) {
            distanciaAgente = Tablero.getInstance().sensorAgente(this, agente);
            posicionAgente = Tablero.getInstance().redInalambrica(agente);
            resultado = resultado +
                    Math.abs(Math.sqrt(
                            Math.pow(posicionAgenteCalculando.getX()-posicionAgente.getX(), 2) +
                                    Math.pow(posicionAgenteCalculando.getY()-posicionAgente.getY(), 2)
                    ) - distanciaAgente);
        }
        return resultado;
    }
    private double gradiente(List<Agente> agentesCercanosNoPerdidos, Point posicionAgenteCalculando){
        Point posicionAgente;
        double distanciaAgente=0.0;
        double calculo;
        double gradiente=0.0;
        for (Agente agente : agentesCercanosNoPerdidos) {
            distanciaAgente = Tablero.getInstance().sensorAgente(this, agente);
            posicionAgente = Tablero.getInstance().redInalambrica(agente);
            calculo = (posicionAgente.distance(this.posicion) - distanciaAgente);

            gradiente = gradiente +
                    (calculo / Math.abs(calculo)) * (
                            (posicionAgenteCalculando.getX() - posicionAgente.getX()) + (posicionAgenteCalculando.getY() - posicionAgente.getY()
                            ) / posicionAgente.distance(posicionAgenteCalculando)
                    );
        }

        return gradiente;
    }


    //todo MAITE: ver que va bien y que guarda las que tiene que guardar, no mas!
    private Point mediaTrilateracion() {
        double sumX = 0.0;
        double sumY = 0.0;
        Point sol;
        //if (listaTrilateraciones.size()==0) {return new Point (sumX,sumY);}else{
        if (listaTrilateraciones.size()>=5){
        for (int i = 0; i < 5; i++) {
            Point nuevo = listaTrilateraciones.get(listaTrilateraciones.size()-1-i);
            sumX += nuevo.getX();
            sumY += nuevo.getY();
        }
            sol = new Point(sumX / 5, sumY / 5);
        }else{
            for (int i = 0; i < listaTrilateraciones.size(); i++) {
                Point nuevo = listaTrilateraciones.get(i);
                sumX += nuevo.getX();
                sumY += nuevo.getY();
        }
            sol = new Point(sumX / this.listaTrilateraciones.size(), sumY / this.listaTrilateraciones.size());
        }


        sol = Tablero.getInstance().posicionModuloTablero(sol);
        return sol;
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
            }

            System.out.println("Consenso de coordenadas.");
            Point solTrilateracion = this.trilateracion(agentesCercanosNoPerdidos);
            this.posicion = solTrilateracion;
            //todo EGO: esta bien usar add o debria ser push ¿?
            this.listaTrilateraciones.add(solTrilateracion);
        }else if(!this.perdido){listaTrilateraciones.add(this.posicion);}
        if( Tablero.getInstance().getEtapa()%5==0){
            if (listaTrilateraciones.size()>=1){ this.posicion= mediaTrilateracion();};
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
        Vector vectorMovimiento = new Vector(solucion1.get().getX(), solucion1.get().getY());
        if (!this.figura.isDentroFigura(this.posicion.add(vectorMovimiento))) {
            if (Math.random() < 0.75) {
                vectorMovimiento = new Vector(0.0, 0.0);
            } else {
                vectorMovimiento = movFuera();
                while (!this.figura.isDentroFigura(this.posicion.add(vectorMovimiento))) {
                    vectorMovimiento = movFuera();
                }
            }
        }

        return vectorMovimiento;

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
//                    if (!this.figura.isDentroFigura(this.posicion.add(this.vectorMovimiento))) {
//                        if (Math.random() < 0.75) {
//                            this.vectorMovimiento = new Vector(0.0, 0.0);
//                        } else {
//                            this.vectorMovimiento = movFuera();
//                            while (!this.figura.isDentroFigura(this.posicion.add(this.vectorMovimiento))) {
//                                this.vectorMovimiento = movFuera();
//                            }
//                        }
//                    }
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
