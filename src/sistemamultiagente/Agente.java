package sistemamultiagente;

import java.util.*;


public class Agente {

    /**
     * ATRIBUTOS
     */
    private  FiguraInterface figura =Constants.FIGURA;

    private Integer id;
    private boolean perdido;
    private Point posicion;
    private List<Point> listaTrilateraciones;
    private Vector vectorMovimiento;
    private HashMap<Agente, Double> listaDistanciaSensor = new HashMap<>();
    private Point posicionAntigua;
    private List<Point> posicionesDentroFigura = new ArrayList<>();
    private int contadorMovimiento;


    /**
     * MÉTODOS
     */


    //Constructor 2 --- Para crear agentes no perdidos o perdidos. -----------------------------------------------------
    public Agente(boolean perdido, Point posicion, Integer id) {
        this.posicion = posicion;
        this.perdido = perdido;
        if (perdido) {
            this.posicion = null;
            this.posicionAntigua = null;
            this.posicionesDentroFigura = new ArrayList<>();
        }

        this.listaTrilateraciones = new ArrayList<>();
        this.vectorMovimiento = new Vector(0.0, 0.0);
        this.id = id;

        this.contadorMovimiento = 0;

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
    private Point primeraCoordenadaAgentePerdido(HashMap<Agente, Double> tresAgentesCercanosNoPerdidos) {
        //System.out.println("............................................................................");
        //Se realiza con el baricentro del triangulo compuesto por las posiciones de los tres agentes cercanos
        // no perdidos que estan en la lista: tresAgentesCercanosNoPerdido.
        // Cuidado: la posicion de cualquiera de los agentes de esta lista se debe obtener por la red inalambrica
        // que se estabrece entre ellos ---> funcion del Tablero.
        List<Agente> listaAgentes = new ArrayList<>();
        for (Agente agente : tresAgentesCercanosNoPerdidos.keySet()) {
            listaAgentes.add(agente);
        }

        Circle circulo1 = new Circle(listaAgentes.get(0).getPosicion(),
                tresAgentesCercanosNoPerdidos.get(listaAgentes.get(0)));
        Circle circulo2 = new Circle(listaAgentes.get(1).getPosicion(),
                tresAgentesCercanosNoPerdidos.get(listaAgentes.get(1)));
        Circle circulo3 = new Circle(listaAgentes.get(2).getPosicion(),
                tresAgentesCercanosNoPerdidos.get(listaAgentes.get(2)));

        List<Point> interseccion_1_2 = circulo1.intersection(circulo2);
        List<Point> interseccion_1_3 = circulo1.intersection(circulo3);
        List<Point> interseccion_2_3 = circulo2.intersection(circulo3);

        //tem.out.println(interseccion_1_2);
        if (interseccion_1_2.size() == 0 || interseccion_1_3.size() == 0 || interseccion_2_3.size() == 0) {
            // System.out.println("*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*//*/**/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/");
        }
        if (interseccion_1_2.size() == 1) {
            //System.out.println(interseccion_1_2.get(0));
            //System.out.println("Distancia al punto real:" + interseccion_1_2.get(0).distance(Tablero.getInstance().getTablero().get(this)));
            return interseccion_1_2.get(0);
        } else if (interseccion_1_3.size() == 1) {
            //System.out.println(interseccion_1_3.get(0));
            //System.out.println("Distancia al punto real:" + interseccion_1_3.get(0).distance(Tablero.getInstance().getTablero().get(this)));
            return interseccion_1_3.get(0);
        } else if (interseccion_2_3.size() == 1) {
            //System.out.println(interseccion_2_3.get(0));
            //System.out.println("Distancia al punto real:" + interseccion_2_3.get(0).distance(Tablero.getInstance().getTablero().get(this)));
            return interseccion_2_3.get(0);
        } else {
            Line L1 = new Line(interseccion_1_2.get(0), interseccion_1_2.get(1));
            Line L2 = new Line(interseccion_1_3.get(0), interseccion_1_3.get(1));
            Point interseccion = L1.interseccion(L2);
            if (interseccion.distance(interseccion_1_2.get(0)) < interseccion.distance(interseccion_1_2.get(1))) {
                //  System.out.println("Distancia al punto real:" + interseccion_1_2.get(0).distance(Tablero.getInstance().getTablero().get(this)));
                return interseccion_1_2.get(0);
            } else {
                //System.out.println("Distancia al punto real:" + interseccion_1_2.get(1).distance(Tablero.getInstance().getTablero().get(this)));
                return interseccion_1_2.get(1);
            }
        }
    }

    private HashMap<Agente, Double> DiccionarioAgenteSensor(List<Agente> agentesCercanosNoPerdidos) {
        HashMap<Agente, Double> listaDistanciaSensor = new HashMap<>();
        agentesCercanosNoPerdidos.forEach(a -> listaDistanciaSensor.put(a, Tablero.getInstance().sensorAgente(this, a)));
        return listaDistanciaSensor;
    }

    //Trilateracion:
    // Con las posiciones de los agentes de la lista agenterCercanosNoPerdidos y la distancia a ellos(---> Funciones del
    // tablero) vamos a calcular la posicion que minimice los datos, (la interseccion de los circulos de centro las
    //posiciones de los agentes y radio la distancia a ellos).
    private Point trilateracion() {
        ArrayList<Double> PuntosMinimos = new ArrayList<>();
        ArrayList<Point> listaDeTresPosiciones = new ArrayList<>();
        ArrayList<Double> listaDeTresUltimasEvaluaciones = new ArrayList<>();
        List<Double> gradientes;
        listaDeTresPosiciones.add(this.posicion);
        listaDeTresPosiciones.add(this.posicion);
        listaDeTresPosiciones.add(this.posicion);
        double insertar = evaluarFuncionParaTrilateracion(this.posicion);
        listaDeTresUltimasEvaluaciones.add(insertar);
        listaDeTresUltimasEvaluaciones.add(insertar);
        listaDeTresUltimasEvaluaciones.add(insertar);
        //int i = 0;
        Point result = this.posicion;
        PuntosMinimos.add(insertar);
        double mulx = Constants.LEARNING_RATE;
        int numeroIteracionesMaximas = Constants.NUMERO_MAXIMA_ITERACIONES;
        double criterio = Constants.CRITERIO;
        for (int i = 0; i < numeroIteracionesMaximas; i++) {
            listaDeTresUltimasEvaluaciones.remove(0);
            listaDeTresPosiciones.remove(0);
            gradientes = gradiente(listaDeTresPosiciones.get(1));
            //if(Math.abs(gradientes.get(0))>Math.abs(gradientes.get(1))) {
            listaDeTresPosiciones.add(Tablero.getInstance().posicionModuloTablero(
                    new Point(
                            listaDeTresPosiciones.get(1).getX() - mulx * gradientes.get(0),
                            listaDeTresPosiciones.get(1).getY() - mulx * gradientes.get(1)
                    )));
//            }else{ listaDeTresPosiciones.add(Tablero.getInstance().posicionModuloTablero(
//                    new Point(
//                    listaDeTresPosiciones.get(1).getX() ,
//                    listaDeTresPosiciones.get(1).getY() - mulx * gradientes.get(1)
//            )));}

            listaDeTresUltimasEvaluaciones.add(
                    evaluarFuncionParaTrilateracion(
                            listaDeTresPosiciones.get(2)));
            if (Math.abs(mulx * gradientes.get(0)) < criterio || Math.abs(mulx * gradientes.get(1)) < criterio) {
                // System.out.println("HE SALIDO POR SQUI");
                result = listaDeTresPosiciones.get(2);
                break;
            }
            //if (listaDeTresUltimasEvaluaciones.get(1) < listaDeTresUltimasEvaluaciones.get(0) && (listaDeTresUltimasEvaluaciones.get(1) < listaDeTresUltimasEvaluaciones.get(2))) {

            if (listaDeTresUltimasEvaluaciones.get(2) < PuntosMinimos.get(0)) {
                result = listaDeTresPosiciones.get(2);
                PuntosMinimos.remove(0);
                PuntosMinimos.add(listaDeTresUltimasEvaluaciones.get(2));
            }
        }
//        if (result.distance(Tablero.getInstance().getTablero().get(this)) > 1) {
////            System.out.println(result.distance(Tablero.getInstance().getTablero().get(this)) + "--------------------" +
////                    Tablero.getInstance().agentesCercanosNoPerdidos(this).size());
//        }
        return result;

    }
//        System.out.println(listaDeTresUltimasEvaluaciones.get(0)+ "---"+listaDeTresUltimasEvaluaciones.get(1)+ "---"+listaDeTresUltimasEvaluaciones.get(2)+ "---" );
//        System.out.println("--------------------------------------------------------------------------------------------");
    //return Tablero.getInstance().posicionModuloTablero(listaDeTresPosiciones.get(1));
    //return PuntosMinimos.


    private double evaluarFuncionParaTrilateracion(Point posicionAgenteCalculando) {
        double resultado = 0.0;
        Point posicionAgente;
        double distanciaAgente;

        for (Agente agente : this.listaDistanciaSensor.keySet()) {
            //distanciaAgente = this.listaDistanciaSensor.get(agente);
            distanciaAgente = Tablero.getInstance().sensorAgente(this, agente);
            //posicionAgente = Tablero.getInstance().redInalambrica(agente);
            resultado = resultado + Math.abs(posicionAgenteCalculando.distance(agente.getPosicion()) - distanciaAgente);
        }
        return resultado;
    }

    private List<Double> gradiente(Point posicionAgenteCalculando) {
        Point posicionAgente;
        ArrayList<Double> derivadasParciales = new ArrayList<>();
        double distanciaAgente;
        double calculo;
        derivadasParciales.add(0.0); // La primera sera la deriavada del eje x.
        derivadasParciales.add(0.0);
        for (Agente agente : this.listaDistanciaSensor.keySet()) {
            //distanciaAgente = this.listaDistanciaSensor.get(agente);
            distanciaAgente = Tablero.getInstance().sensorAgente(this, agente);
            posicionAgente = Tablero.getInstance().redInalambrica(agente);
            calculo = posicionAgente.distance(posicionAgenteCalculando) - distanciaAgente;
            if (calculo != 0.0) {
                derivadasParciales.set(0,
                        derivadasParciales.get(0) + (
                                (calculo / Math.abs(calculo)) * (
                                        (posicionAgenteCalculando.getX() - posicionAgente.getX())
                                                / posicionAgente.distance(posicionAgenteCalculando))
                        )
                );
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
            for (int i = 0; i < this.listaTrilateraciones.size(); i++) {
                Point nuevo = listaTrilateraciones.get(listaTrilateraciones.size() - 1 - i);
                sumX += nuevo.getX();
                sumY += nuevo.getY();
            }
            sol = new Point(sumX / this.listaTrilateraciones.size(),
                    sumY / this.listaTrilateraciones.size());
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
        this.listaDistanciaSensor = DiccionarioAgenteSensor(agentesCercanosNoPerdidos);
        if (agentesCercanosNoPerdidos.size() >= 3) {
            //List<Agente> tresAgentesCercanosNoPerdidos = tresAgentesDeUnaLista(agentesCercanosNoPerdidos);
            if (this.perdido) {
                // si esta perdido, entonces calcula la primera coordena ---> Baricentro.
                this.posicion = this.primeraCoordenadaAgentePerdido(tresAgentesDiccionario(agentesCercanosNoPerdidos));
                this.perdido = false;

            }
            //System.out.println(this.posicion);
            if (figura.isDentroFigura(this.posicion)) {
                //System.out.println(this.posicionesDentroFigura);
                this.posicionesDentroFigura.add(this.posicion);
            }
            this.posicionAntigua = this.posicion;
            Point solTrilateracion = this.trilateracion();
            this.posicion = solTrilateracion;
            this.listaTrilateraciones.add(solTrilateracion);
        }
//        if (Tablero.getInstance().getEtapa() % Constants.NUM_DE_PASOS_PARa_MEDIAR_LASTRILATERACIONES == 0) {
//            if (listaTrilateraciones.size() >= 1) {
//                this.posicionAntigua = this.posicion;
//                this.posicion = mediaTrilateracion();
//                this.listaTrilateraciones = new ArrayList<>();
//            }
//        }
    }

    //tresAgentesDeUnaLista:
    //Voy a coger de la lista de agentes cercanos no perdidos, 3 cualquira de ellos.
    private List<Agente> tresAgentesDeUnaLista(List<Agente> agentesCercanosNoPerdidos) {
        Collections.shuffle(agentesCercanosNoPerdidos); // desordeno la lista!
        return agentesCercanosNoPerdidos.subList(0, 3);
    }

    private HashMap<Agente, Double> tresAgentesDiccionario(List<Agente> agentesCercanosNoPerdidos) {
        Collections.shuffle(agentesCercanosNoPerdidos); // desordeno la lista!
        HashMap<Agente, Double> listaResult = new HashMap<>();
        HashMap<Agente, Double> lista = DiccionarioAgenteSensor(agentesCercanosNoPerdidos);
        for (Agente agente : lista.keySet()) {
            listaResult.put(agente, lista.get(agente));
            if (listaResult.size() == 3) {
                continue;
            }

        }
        return listaResult;
    }

    /**
     * Funciones esenciales del programa:
     * Categoria Calcular el movimiento| ----------------------------------------------------------------------------
     */

    //movFuera:
    //Si el agente esta fuera de la figura, debe moverse hacia una direccion aleatoria, dentro del rango de movimiento
    //que tiene.
    private Vector movFuera() {

        double r1 = 0.0;
        double r2 = 0.0;
        double distanciaMaxMov = Constants.DISTANCIA_MAX_MOV;
        List<Agente> listaAgentes = Tablero.getInstance().agentesCercanosNoPerdidos(this);
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
//

        return new Vector(r1, r2);
    }


    //movDentro:
    //La calcula la repulsion que cada agente ejerce sobre otro agente, cuando estan dentro de la figura, el resultado de
    //ña suma de las respulsiones es lo que el agente se va a mover.
    private Vector movDentro() {
        double distanciaAgente;
        Point repulsionPorEseAgente;
        double solx = 0.0;
        double soly = 0.0;
        Point diferenciaPosiciones;
        List<Agente> agenteCercanosNoPerdidos = Tablero.getInstance().agentesCercanosNoPerdidos(this);
        for (Agente agente : agenteCercanosNoPerdidos) {
            diferenciaPosiciones = agente.posicion.sub(this.getPosicion());
            double distance = Math.sqrt(diferenciaPosiciones.getX() * diferenciaPosiciones.getX() + diferenciaPosiciones.getY() * diferenciaPosiciones.getY());
            if (distance < Constants.RADIO_DE_REPULSION) {
                solx -= diferenciaPosiciones.getX() * (Constants.RADIO_DE_REPULSION - distance) / distance;
                soly -= diferenciaPosiciones.getY() * (Constants.RADIO_DE_REPULSION - distance) / distance;
            }
        }

        Vector vectorMovimiento = new Vector(solx, soly);

        //Double modulo = vectorMovimiento.distance(new Vector(0.0, 0.0));

        while (!figura.isDentroFigura(this.posicion.add(vectorMovimiento))) {
            double r1 = Math.random();
            if (r1 < 0.8) {
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
            this.contadorMovimiento = 0;
        } else { // no esta perdido
//            /**todo this*/
            if (this.posicionesDentroFigura.size() >= 1 && !figura.isDentroFigura(this.posicion)) {
                this.contadorMovimiento = this.contadorMovimiento + 1;
                if (this.contadorMovimiento > 10) {
                    System.out.println("HE ECHI ESTO CAMBIOOOOOOOOOOOO");
                    this.posicionesDentroFigura = new ArrayList<>();
                    this.contadorMovimiento = 0;
                    this.vectorMovimiento = this.movFuera();
                } else {
                    for (Point punto : this.posicionesDentroFigura) {
                        Double distance = this.posicion.distance(punto);
                        if (distance < Constants.DISCANCIA_MAX_SENSOR) {
                            Point posicion = punto.sub(this.posicion);
                            double modulo = posicion.distance(new Point(0.0, 0.0));
                            posicion = posicion.div(modulo);
                            posicion = posicion.scale(Constants.DISTANCIA_MAX_MOV);
                            this.vectorMovimiento = new Vector(posicion.getX(), posicion.getY());
                            break;
                        } else {
                            this.vectorMovimiento = this.movFuera();
                            this.contadorMovimiento = 0;
                        }
                    }
                }
//                /*todo this*/

           } else
                if (figura.isDentroFigura(this.posicion)) {
                    //Tablero.getInstance().getTablero().get(this))) { // esta dentro de la figura
                    this.vectorMovimiento = this.movDentro();
                    this.contadorMovimiento = 0;
//                /**todo this*/
                    List<Agente> agenteDentroRadioDeRepulsion = new ArrayList<>();
                    for (Agente agente : Tablero.getInstance().agentesCercanosNoPerdidos(this)) {
                        if (this.getPosicion().distance(agente.getPosicion()) < Constants.DISCANCIA_MAX_SENSOR) {
                            agenteDentroRadioDeRepulsion.add(agente);
                        }
                    }
                    if (agenteDentroRadioDeRepulsion.size() < Constants.NUMERO_AGENTES_CERCANOS_PARA_PENSAR_QUE_ESTAS_EN_LA_FIGURA) {
                        //System.out.println("-----------------------");
                        this.vectorMovimiento = this.movFuera();
                    }
//                /**todo this*/
                } else {
                    this.vectorMovimiento = this.movFuera();
                    this.contadorMovimiento = 0;
                }
            }
        }


    //actualizarPosicion:
    //si el agente no esta perdido, es decir tiene posicion, sumo su posicion y el vector movimiento para definir su
    //nueva posicion.
    //Esta funcione s llamada en el tablero.
    public void actualizarPosicion() {
        if (this.posicion != null) {
            Point nueva = this.posicion.
                    add(this.vectorMovimiento);
            nueva = nueva.add(new Point(
                    Constants.ERROR_MOV * Tablero.getInstance().errorUniforme(Constants.DISTANCIA_MAX_MOV),
                    Constants.ERROR_MOV * Tablero.getInstance().errorUniforme(Constants.DISTANCIA_MAX_MOV)));

            this.posicion = Tablero.getInstance().posicionModuloTablero(nueva);
//            /** todo this:*/
            if (this.listaDistanciaSensor.keySet().size() > 3) {
                for (Agente agente : this.listaDistanciaSensor.keySet()) {
                    this.listaDistanciaSensor.put(agente, agente.getPosicion().distance(this.posicion));
                }
                Point solTrilateracion = this.trilateracion();
                this.posicion = solTrilateracion;
                this.listaTrilateraciones.add(solTrilateracion);
            }
//            /** todo this:*/
        }
    }

}
