package sistemamultiagente;

import java.util.*;


public class Agente {

    /**
     * ATRIBUTOS
     */
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

    public Point getPosicion() {
        return posicion;
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

    /**
     * Funciones esenciales del programa:
     * Categoria Consenso de coordenadas! ----------------------------------------------------------------------------
     */

    //PrimeraCoordenadaAgentePerdido ---> le permite saber una posicion aproximada por primera vez al egente que esta
    // perdido, se su posicion.
    private Point primeraCoordenadaAgentePerdido(List<Agente> tresAgentesCercanosNoPerdidos) {
        //Se realiza con el baricentro del triangulo compuesto por las posiciones de los tres agentes cercanos
        // no perdidos que estan en la lista: tresAgentesCercanosNoPerdido.
        // Cuidado: la posicion de cualquiera de los agentes de esta lista se debe obtener por la red inalambrica
        // que se estabrece entre ellos ---> funcion del Tablero.
        return new Point(
                tresAgentesCercanosNoPerdidos.stream().mapToDouble(a ->
                        Tablero.getInstance().redInalambrica(a).getX()).sum() / 3,
                tresAgentesCercanosNoPerdidos.stream().mapToDouble(a ->
                        Tablero.getInstance().redInalambrica(a).getX()).sum() / 3
        );
    }

    //Trilateracion:
    // Con las posiciones de los agentes de la lista agenterCercanosNoPerdidos y la distancia a ellos(---> Funciones del
    // tablero) vamos a calcular la posicion que minimice los datos, (la interseccion de los circulos de centro las
    //posiciones de los agentes y radio la distancia a ellos).
    private Point trilateracion(List<Agente> agentesCercanosNoPerdidos) {
        ArrayList<Point> listaDeTresPosiciones = new ArrayList<>();
        ArrayList<Double> listaDeTresUltimasEvaluaciones = new ArrayList<>();
        HashMap<Agente, Double> listaDistanciaSensor = new HashMap<>();
        List<Double> gradientes;
        agentesCercanosNoPerdidos.forEach(a -> listaDistanciaSensor.put(a, Tablero.getInstance().sensorAgente(this, a)));
        listaDeTresPosiciones.add(this.posicion);
        listaDeTresPosiciones.add(this.posicion);
        listaDeTresPosiciones.add(this.posicion);
        double insertar = evaluarFuncionParaTrilateracion(agentesCercanosNoPerdidos, this.posicion, listaDistanciaSensor);
        listaDeTresUltimasEvaluaciones.add(insertar);
        listaDeTresUltimasEvaluaciones.add(insertar);
        listaDeTresUltimasEvaluaciones.add(insertar);
        int i = 0;
        double mulx = 0.00001;
        double muly = 0.00001;
        while (
                listaDeTresUltimasEvaluaciones.get(0) <= listaDeTresUltimasEvaluaciones.get(1) ||
                listaDeTresUltimasEvaluaciones.get(2) <= listaDeTresUltimasEvaluaciones.get(1)
        ) {
            i += 1;
            listaDeTresUltimasEvaluaciones.remove(0);
            listaDeTresPosiciones.remove(0);
            gradientes = gradiente(agentesCercanosNoPerdidos, listaDeTresPosiciones.get(1));
//            if (i > 2 && Math.abs(gradientes.get(0)) < 0.5) {
//                mulx = 0.02;
//                Constants.COUNT_MULX_PRIMERO =Constants.COUNT_MULX_PRIMERO+1;
//            } else if (i > 2 && Math.abs(gradientes.get(0)) < 0.01) {
//                mulx = 0.005;
//                Constants.COUNT_MULX_SEGUNDO =Constants.COUNT_MULX_SEGUNDO+1;
//            }else if (i > 2 && Math.abs(gradientes.get(0)) < 0.001) {
//                mulx = 0.0001;
//                Constants.COUNT_MULX_TERCERO =Constants.COUNT_MULX_TERCERO+1;
//            }
//
//            if (i > 2 && Math.abs(gradientes.get(1)) < 0.5) {
//                muly = 0.02;
//                Constants.COUNT_MULY_PRIMERO=Constants.COUNT_MULY_PRIMERO+1;
//            } else if (i > 2 && Math.abs(gradientes.get(1)) < 0.01) {
//                muly = 0.005;
//                Constants.COUNT_MULY_SEGUNDO=Constants.COUNT_MULY_SEGUNDO+1;
//            }else if (i > 2 && Math.abs(gradientes.get(1)) < 0.001) {
//                muly = 0.0001;
//                Constants.COUNT_MULY_TERCERO=Constants.COUNT_MULY_TERCERO+1;
//            }
            if (Math.abs(gradientes.get(0)) > Math.abs(gradientes.get(1))) {
                listaDeTresPosiciones.add(Tablero.getInstance().posicionModuloTablero(
                        new Point(
                                listaDeTresPosiciones.get(1).getX() * (1 - mulx * gradientes.get(0)),
                                listaDeTresPosiciones.get(1).getY()
                        )));
            } else {
                listaDeTresPosiciones.add(Tablero.getInstance().posicionModuloTablero(
                        new Point(
                                listaDeTresPosiciones.get(1).getX(),
                                listaDeTresPosiciones.get(1).getY() * (1 - muly * gradientes.get(1))
                        )));
            }
            listaDeTresUltimasEvaluaciones.add(
                    evaluarFuncionParaTrilateracion(agentesCercanosNoPerdidos,
                            listaDeTresPosiciones.get(2), listaDistanciaSensor));
//            System.out.println("Distancia entre distintas triletaraciones: ---> "+ listaDeTresPosiciones.get(1).distance(listaDeTresPosiciones.get(2))
////                    + "---"
////            + listaDeTresPosiciones.get(1).distance(this.posicion));
            if ((Math.abs(gradientes.get(0)) < 0.0001 && Math.abs(gradientes.get(1)) < 0.0001) || i > 1000) {
                //System.out.println("HE SALIDO POR SQUI");
                break;
            }
        }
//        System.out.println(listaDeTresUltimasEvaluaciones.get(0)+ "---"+listaDeTresUltimasEvaluaciones.get(1)+ "---"+listaDeTresUltimasEvaluaciones.get(2)+ "---" );
//        System.out.println("--------------------------------------------------------------------------------------------");
        return listaDeTresPosiciones.get(1);
    }

    private double evaluarFuncionParaTrilateracion(List<Agente> agentesCercanosNoPerdidos, Point
            posicionAgenteCalculando, HashMap<Agente, Double> listaDistanciaSensor) {
        double resultado = 0.0;
        Point posicionAgente;
        double distanciaAgente;

        for (Agente agente : agentesCercanosNoPerdidos) {

            distanciaAgente = listaDistanciaSensor.get(agente);
            //Tablero.getInstance().sensorAgente(this, agente);
            posicionAgente = Tablero.getInstance().redInalambrica(agente);
            resultado = resultado +
                    Math.abs(Math.sqrt(
                            Math.pow(posicionAgenteCalculando.getX() - posicionAgente.getX(), 2) +
                                    Math.pow(posicionAgenteCalculando.getY() - posicionAgente.getY(), 2)
                    ) - distanciaAgente);
        }
        return resultado;
    }

    private List<Double> gradiente(List<Agente> agentesCercanosNoPerdidos, Point posicionAgenteCalculando) {
        Point posicionAgente;
        ArrayList<Double> derivadasParciales = new ArrayList<>();
        double distanciaAgente;
        double calculo;
        derivadasParciales.add(0.0); // La primera sera la deriavada del eje x.
        derivadasParciales.add(0.0);
        for (Agente agente : agentesCercanosNoPerdidos) {
            distanciaAgente = Tablero.getInstance().sensorAgente(this, agente);
            posicionAgente = Tablero.getInstance().redInalambrica(agente);
            calculo = posicionAgente.distance(posicionAgenteCalculando) - distanciaAgente;
            if (calculo != 0.0) {
                derivadasParciales.set(0,
                        derivadasParciales.get(0) + ((calculo / Math.abs(calculo)) * (
                                (posicionAgenteCalculando.getX() - posicionAgente.getX())
                                        / posicionAgente.distance(posicionAgenteCalculando))));
                derivadasParciales.set(1,
                        derivadasParciales.get(1) +
                                (
                                        (calculo / Math.abs(calculo)) * (
                                                (posicionAgenteCalculando.getY() - posicionAgente.getY())
                                                        / posicionAgente.distance(posicionAgenteCalculando))));
            }
        }
        return derivadasParciales;
    }


    //todo MAITE: ver que va bien y que guarda las que tiene que guardar, no mas!
    private Point mediaTrilateracion() {
        double sumX = 0.0;
        double sumY = 0.0;
        Point sol;
        //if (listaTrilateraciones.size()==0) {return new Point (sumX,sumY);}
        int numDePasosParaMediarLasTrilateraciones = Constants.NUM_DE_PASOS_PARa_MEDIAR_LASTRILATERACIONES;
        if (listaTrilateraciones.size() >= numDePasosParaMediarLasTrilateraciones) {
            for (int i = 0; i < numDePasosParaMediarLasTrilateraciones; i++) {
                Point nuevo = listaTrilateraciones.get(listaTrilateraciones.size() - 1 - i);
                sumX += nuevo.getX();
                sumY += nuevo.getY();
            }
            sol = new Point(sumX / numDePasosParaMediarLasTrilateraciones, sumY / numDePasosParaMediarLasTrilateraciones);
            sol = Tablero.getInstance().posicionModuloTablero(sol);
            return sol;
        } else {
            return this.posicion;
        }
    }

    //consensoDeCoordenadas:
    // Si el agente estaba perdido y hay al menos tres agentes no perdidos cerca ---> calculo el baricentro
    // Realizo el ajunte de las coordendas con la trilateracion y el descenso del gradiente. --> obtengo: La posicion del agente.
    // Esta coordenada la almaceno en la lista de trilateracion.
    //Se calcula la media de las trilateraciones cuando la etapa sea multiplo de Constants.NUM_DE_PASOS_PARa_MEDIAR_LASTRILATERACIONES
    // solo se realiza la media si esta lista tiene Constants.NUM_DE_PASOS_PARa_MEDIAR_LASTRILATERACIONES elementos.
    // Si no se hace asi va peor.
    public void consensoDeCoordenadas() {
        List<Agente> agentesCercanosNoPerdidos = Tablero.getInstance().agentesCercanosNoPerdidos(this);
        if (agentesCercanosNoPerdidos.size() >= 3) {
            List<Agente> tresAgentesCercanosNoPerdidos = tresAgentesDeUnaLista(agentesCercanosNoPerdidos);
            if (this.perdido) {
                // si esta perdido, entonces calcula la primera coordena ---> Baricentro.
                this.posicion = this.primeraCoordenadaAgentePerdido(tresAgentesCercanosNoPerdidos);
                this.perdido = false;
            }
            double num1=this.posicion.distance(Tablero.getInstance().getTablero().get(this));
            //System.out.println(" Inicialmente: " +this.posicion.distance(Tablero.getInstance().getTablero().get(this)));
            Point solTrilateracion = this.trilateracion(agentesCercanosNoPerdidos);
            this.posicion = solTrilateracion;

            double num2=this.posicion.distance(Tablero.getInstance().getTablero().get(this));
            if (num1>num2){Constants.COUNTBIEN = Constants.COUNTBIEN+1; }else{
                Constants.COUNTMAL = Constants.COUNTMAL+1;
            }
           // System.out.println("Trilateracion" + this.posicion.distance(Tablero.getInstance().getTablero().get(this)));

            //System.out.println("------------------------------------------------------------------------");
            this.listaTrilateraciones.add(solTrilateracion);
        }
        if (Tablero.getInstance().getEtapa() % Constants.NUM_DE_PASOS_PARa_MEDIAR_LASTRILATERACIONES == 0) {
            if (listaTrilateraciones.size() >= 1) {
                this.posicion = mediaTrilateracion();
                this.listaTrilateraciones = new ArrayList<>();
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
        double distanciaMaxMov = Constants.DISTANCIA_MAX_MOV;
        if (Math.random() < 0.5) {
            r1 = -Math.random() * distanciaMaxMov;
        } else {
            r1 = Math.random() * distanciaMaxMov;
        }
        if (Math.random() < 0.5) {
            r2 = -Math.random() * distanciaMaxMov;
        } else {
            r2 = Math.random() * distanciaMaxMov;
        }
        return new Vector(r1, r2);
    }


    //movDentro:
    //La calcula la repulsion que cada agente ejerce sobre otro agente, cuando estan dentro de la figura, el resultado de
    //ña suma de las respulsiones es lo que el agente se va a mover.
    private Vector movDentro() {
        double distanciaAgente;
        Point repulsionPorEseAgente;
        Point diferenciaPosiciones;
        double solx = 0.0;
        double soly = 0.0;
        List<Agente> agenteCercanosNoPerdidos = Tablero.getInstance().agentesCercanosNoPerdidos(this);
        for (Agente agente : agenteCercanosNoPerdidos) {
            //todo esto no se si debe estar el if.
            //if (figura.isDentroFigura(agente.getPosicion())) {
                distanciaAgente = Tablero.getInstance().sensorAgente(this, agente);
                diferenciaPosiciones = this.posicion.sub(agente.getPosicion());
                repulsionPorEseAgente = diferenciaPosiciones.div(distanciaAgente);
                //todo
                if (-distanciaAgente + Constants.RADIO_DE_REPULSION > 0.0) {
                    repulsionPorEseAgente = repulsionPorEseAgente.scale(-distanciaAgente + Constants.RADIO_DE_REPULSION);
                    //System.out.println("Repulsion por agente"+ solx + " Y :"+ soly)
                    solx = solx + repulsionPorEseAgente.getX();
                    soly = soly + repulsionPorEseAgente.getY();
                } else {
                    solx = solx + 0.0;
                    soly = soly + 0.0;
                }
//            }
        }
        Vector vectorMovimiento = new Vector(solx, soly);

        Double modulo = vectorMovimiento.distance(new Vector(0.0, 0.0));
        if (modulo != 0.0) {
            vectorMovimiento = vectorMovimiento.div(modulo);
            vectorMovimiento = vectorMovimiento.scale(Constants.DISTANCIA_MAX_MOV);
        }
        //System.out.println("Vector de movimeinto: "+ vectorMovimiento);
        //System.out.println("Vector mov dentro:" + vectorMovimiento.toString() + " ID: " + this.getId()+ " modulo "+ modulo);
        //todo no se cuanto de correcto es añadir este margen.
        while (!this.figura.isDentroFigura(this.posicion.add(vectorMovimiento))) {
              //  .add(new Point ( Constants.ERROR_MOV, Constants.ERROR_MOV)))) {
            double r1 = Math.random();
            if (r1 < 0.75) {
                vectorMovimiento = new Vector(0.0, 0.0);

            } else {
                vectorMovimiento = movFuera();

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
            if (this.figura.isDentroFigura(this.posicion)) {


                //Tablero.getInstance().getTablero().get(this))) { // esta dentro de la figura
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
        //System.out.println("Error de movimiento "+ error.toString());
        if (this.posicion != null) {
            Point nueva = this.posicion.
                    add(this.vectorMovimiento);
            nueva = nueva.add(new Point(
                    Constants.ERROR_MOV * Tablero.getInstance().errorUniforme(Constants.DISTANCIA_MAX_MOV),
                    Constants.ERROR_MOV * Tablero.getInstance().errorUniforme(Constants.DISTANCIA_MAX_MOV)));

            this.posicion = Tablero.getInstance().posicionModuloTablero(nueva);
        }
    }

    //agenteisDentroFigura:
    //Si el agente no esta perdido y su posicion (la que el cree) esta dentro d ela figura, entonces devuelve True
    //en caso contrario, es decir la posicion sea nula o el agente este fuera de la figura, devuelve False.
    public boolean agenteisDentroFigura() {
        return (this.posicion != null && this.figura.isDentroFigura(this.posicion));
        // Tablero.getInstance().getTablero().get(this)));
    }
}
