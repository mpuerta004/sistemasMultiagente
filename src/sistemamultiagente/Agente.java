package sistemamultiagente;

import java.util.*;
import java.util.stream.Collectors;


public class Agente {

    /**
     * ATRIBUTOS
     */

    //ALERT Estos numero luego lees el paper y los pones todos bien.
    private final double distanciaMaxSensor = 1.5;
    private final double distanciaMaxMov = 1.5;
    private final int numDePasosParaMediarLasTrilateraciones = 10;
    private final int numTrilateracionesGuardo = 10;
    private final double tamañoAgente = 0.5;
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

    //Constructor --- Para crear a un nuevo agente que esta perdido.
    public Agente(Integer id) {
        posicion = null;
        perdido = true;
        this.id = id;
        this.vectorMovimiento = new Vector(0.0, 0.0);
        this.listaTrilateraciones = new Stack<>();
    }

    public Agente(boolean perdido, Point posicion, Integer id) {
        this.posicion = posicion;
        this.perdido = perdido;
        if (perdido) this.posicion = null;
        this.listaTrilateraciones = new Stack<>();
        this.vectorMovimiento = new Vector(0.0, 0.0);
        this.id = id;
    }

    public double getTamañoAgente() {
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
        /** Si esta distancia es 5, tu mueves 5 casillas ya sea arriba, abajo, derecha o izquierda. en el orden que quieras,
         * primero puedes hacer para arriba luego para abajo.
         * */
        return distanciaMaxMov;
    }

    @Override
    /*Esto te permite comparar dos objetos Agente. */
    public boolean equals(Object o) {
        /*Si el objeto del que llamas es igual al agente del parentesis de la funcion.*/
        if (this == o) return true;
        /* Si el objerto o es vacio o no es dela clase objeto devuelve False*/
        if (o == null || getClass() != o.getClass()) return false;
        /**La linea de aqui abajo es porque auqnue yo sepa que el o es un agente java no, por lo se necesita un cast. */
        Agente agente = (Agente) o;
        return Objects.equals(id, agente.id);
    }

    public Point primeraCoordenada(List<Agente> tresAgentesCercanosNoPerdidos) {
        //Sabemos que hay tres agentes cercanos no perdidos.
        /** Datos que necesita para realizar este calculo*/

//        List<Point> posiciones = new ArrayList();
//        posiciones.add(Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(0)));
//        posiciones.add(Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(1)));
//        posiciones.add(Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(2)));
//        Point posAgenteCercano1;
//        Point posAgenteCercano2;
//        Point posAgenteCercano3;
//        posAgenteCercano1 = posiciones.stream().sorted(Comparator.comparingDouble(a -> a.getX())).collect(Collectors.toList()).get(1);
//        posAgenteCercano2 = posiciones.stream().sorted(Comparator.comparingDouble(a -> a.getX())).collect(Collectors.toList()).get(0);
//        posAgenteCercano3 = posiciones.stream().sorted(Comparator.comparingDouble(a -> a.getX())).collect(Collectors.toList()).get(2);


        Point punto = new Point(tresAgentesCercanosNoPerdidos.stream().mapToDouble(a->a.getPosicion().getX()).sum()/3,
                tresAgentesCercanosNoPerdidos.stream().mapToDouble(a->a.getPosicion().getY()).sum()/3);

        /**       distancia1a2 = posAgenteCercano1.distance(posAgenteCercano2);
         distancia1a3 = posAgenteCercano1.distance(posAgenteCercano3);
         distancia2a3 = posAgenteCercano2.distance(posAgenteCercano3);
         boolean intentar = true;
         int iterador = 0;
         List<Point> listaIntersecciones = null;

         while (intentar) {
         try {
         iterador++;
         //System.out.println("Intento el try");
         //System.out.println(posAgenteCercano1 + "  "+ posAgenteCercano2 +" " + posAgenteCercano3);
         //System.out.println("distancia de 1 a 2 " + distancia1a2 + " distancia de 1 a 3 "+ distancia1a3 +"  distancia de 1 a 3 "+ distancia1a3);
         double radio2 = distancia1a2 / 2 + distancia1a2 / 2 * Math.random();
         double radio3 = distancia1a3 / 2 + distancia1a3 / 2 * Math.random();
         double CotaMinima = Math.max(distancia1a2 + radio2, distancia1a3 + radio3);
         double radioMaximo = Math.max(radio2, radio3);
         double radio1 = CotaMinima + radioMaximo * Math.random();

         Circle c1 = new Circle(posAgenteCercano1, radio1);

         Circle c2 = new Circle(posAgenteCercano2, radio2);


         Circle c3 = new Circle(posAgenteCercano3, radio3);


         listaIntersecciones = new ArrayList<>();
         // Si en alguno no son dospuntos saldra un nuevo
         listaIntersecciones.addAll(c1.intersection(c2));
         //System.out.println("CONSEGUIDO 1");

         listaIntersecciones.addAll(c1.intersection(c3));
         //System.out.println("CONSEGUIDO 2");
         //listaIntersecciones.addAll(c2.intersection(c3));
         //System.out.println("CONSEGUIDO 3");
         intentar = false;


         } catch (ArithmeticException e) {
         intentar = true;
         System.out.println("Los tres radios no se intersecan");
         if (iterador>50){intentar=false; return null;}

         }
         }

         Line l1 = new Line(listaIntersecciones.get(0), listaIntersecciones.get(1));
         Line l2 = new Line(listaIntersecciones.get(2), listaIntersecciones.get(3));
         Point puntoInterseccion = l1.interseccion(l2);
         double distanciaAlPrimero = puntoInterseccion.distance(listaIntersecciones.get(0));
         double distanciaAlSegundo = puntoInterseccion.distance(listaIntersecciones.get(1));
         if (distanciaAlPrimero > distanciaAlSegundo) {
         return listaIntersecciones.get(0);
         } else {
         return listaIntersecciones.get(1);
         }
         */
        return punto;
        //return Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(0));
    }


    public Point trilateracion(List<Agente> tresAgentesCercanosNoPerdidos) {
        //System.out.println("TRILATERACION");
        double distanciaSensorAgente1 = Tablero.getInstance().sensorAgente(this, tresAgentesCercanosNoPerdidos.get(0));
        double distanciaSensorAgente2 = Tablero.getInstance().sensorAgente(this, tresAgentesCercanosNoPerdidos.get(1));
        double distanciaSensorAgente3 = Tablero.getInstance().sensorAgente(this, tresAgentesCercanosNoPerdidos.get(2));

        // Este calculo lo hace el agente y la informacion que tiene el agente de la posiciones de los otros es
        // es la posicion a la que el otro agente cree estar.
        // (x1, y1) posicion a la cree estar el agente cercano primero.
        double x1 = Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(0)).getX();
        double y1 = Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(0)).getY();
        double x2 = Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(1)).getX();
        double y2 = Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(1)).getY();
        double x3 = Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(2)).getX();
        double y3 = Tablero.getInstance().redInalambrica(tresAgentesCercanosNoPerdidos.get(2)).getY();
        /**    el sistema de eciaciones que tengo que resolver es el siguiente en realizada:
         * (x-x1)^2 +(y-y1)^2 = distanciaSensorAgente1^2
         * (x-x2)^2 +(y-y2)^2 = distanciaSensorAgente2^2
         * (x-x3)^2 +(y-y3)^2 = distanciaSensorAgente3^2
         * Operando tenemos:
         * (-2x1+2x2)x+(-2y1+2y2)y= distanciaSensorAgente1^2 - distanciSensorAgente2^2 - x1^2+x2^2-y1^2+y2^2
         * (-2x2+2x3)x+(-2y2+2y3)y = distanciaSensorAgente2^2 -distanciaSensorAgente3^2 -x2^2+x3^2-y2^2+y3^2
         * Por tanto
         * Ax+By=C;
         * https://www.101computing.net/cell-phone-trilateration-algorithm/
         */
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
        Point solTrilateracion = new Point(x, y);
        /** llegados a este punto tienes la solucion en (x,y) del nuevo punto de ajunte pero ahora tienes que hacer
         * algortimo de descenso del gradiente. */

        return gradientDescent(this.posicion, solTrilateracion);
    }

    public Point gradientDescent(Point posicionAgente, Point solTrilateraciones) {
        //System.out.println("DESCESNSO DEL GRADIENTE");
        boolean iterar = true;
        double beta = 0.9;
        int i = 0;
        // CUIDado NO SE SI PUEDE DAR Fallos
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
        //System.out.println("He salido del descenso del gradiente");
        return solTrilateraciones;
    }

    public Point mediaTrilateracion() {
        if (listaTrilateraciones.size() < numTrilateracionesGuardo) {
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

    public List<Agente> tresAgentesDeUnaLista(List<Agente> agentesCercanosNoPerdidos) {
        // FUNCIONA BIEN
        int r1;
        int r2;
        int r3;
        Random r = new Random();
        List<Agente> tresAgentesCercanosNoPerdidos = new ArrayList<>();

        r1 = r.nextInt(agentesCercanosNoPerdidos.size());
        tresAgentesCercanosNoPerdidos.add(agentesCercanosNoPerdidos.get(r1));
        agentesCercanosNoPerdidos.remove(r1);

        r2 = r.nextInt(agentesCercanosNoPerdidos.size());
        tresAgentesCercanosNoPerdidos.add(agentesCercanosNoPerdidos.get(r2));
        agentesCercanosNoPerdidos.remove(r2);


        r3 = r.nextInt(agentesCercanosNoPerdidos.size());
        tresAgentesCercanosNoPerdidos.add(agentesCercanosNoPerdidos.get(r3));
        agentesCercanosNoPerdidos.remove(r3);
        return tresAgentesCercanosNoPerdidos;
    }

    public void consensoDeCoordenadas() {

        List<Agente> agentesCercanosNoPerdidos = Tablero.getInstance().agentesCercanosNoPerdidos(this);
        List<Agente> copiaAgentesCercanosNoPerdidos = Tablero.getInstance().agentesCercanosNoPerdidos(this);
        // Quiero quedarme con tres de ellos que sean aleatorios.

        if (agentesCercanosNoPerdidos.size() >= 3) {
            List<Agente> tresAgentesCercanosNoPerdidos = tresAgentesDeUnaLista(agentesCercanosNoPerdidos);
            if (this.perdido) {
                // si esta perdido, entonces calcula la primera coordena
                //System.out.println("CAso: estoy perdido y voy a calcular las coordenadas.");
                this.posicion = this.primeraCoordenada(tresAgentesCercanosNoPerdidos);
                if (this.posicion == null) {
                    tresAgentesCercanosNoPerdidos = tresAgentesDeUnaLista(copiaAgentesCercanosNoPerdidos);
                    this.posicion = this.primeraCoordenada(tresAgentesCercanosNoPerdidos);
                }
                //ystem.out.println("El problema no es calcular la primera coordenada");
                // ALERT la trilateracion la haces con los mismos agentes que con los que haces la posicion inical,
                //mirar si esto esta bien.


//                if (this.posicion != null) {
//
//                    this.perdido = false;
//                    this.posicion = this.trilateracion(tresAgentesCercanosNoPerdidos);
//
//
//                    listaTrilateraciones.push(this.trilateracion(tresAgentesCercanosNoPerdidos));
//                }
                //System.out.println("he acabado los calculos de este caso ");
            } else {
                //this.perdido = false;
                //System.out.println("Si no estaba perdido ");
//                if (Tablero.getInstance().getEtapa() % numDePasosParaMediarLasTrilateraciones == 0) {
//                    this.posicion = mediaTrilateracion();
//                }
//                listaTrilateraciones.push(this.trilateracion(tresAgentesCercanosNoPerdidos));
                //System.out.println("he acabado los calculos de este caso ");
            }
        }
    }

    public Vector movFuera() {
        /* Va hacia una direccion aleatoria.*/
        Tablero tablero = Tablero.getInstance();
        double r1;
        double r2;
        double r3;
        double r4;
        r4 = Math.random();
        r3 = Math.random();
        Vector vector = null;

        if (r3 < 0.5) {
            r1 = -Math.random() * this.getDistanciaMaxMov();
        } else {
            r1 = Math.random() * this.getDistanciaMaxMov();
        }
        if (r4 < 0.5) {
            r2 = -Math.random() * this.getDistanciaMaxMov();
        } else {
            r2 = Math.random() * this.getDistanciaMaxMov();
        }

        vector = new Vector(r1, r2);

        return vector;
    }


    /**
     * METODOS DE MOVIMIENTO.
     */

    public Vector movDentro() {
        List agentesCerca = Tablero.getInstance().agentesCercanosNoPerdidos(this);
        Point solucion = new Point(0.0, 0.0);
        /* Ten cuidado que aqui dentro juegas con poiont porque las posiciones son puntos
         * pero bueno tu quieres un vector. ten cuidado. */
        Iterator<Agente> iterator = agentesCerca.iterator();
        while (iterator.hasNext()) {
            Agente agenteI = iterator.next();
            double distanciaAgente = Tablero.getInstance().sensorAgente(this, agenteI);
            Point vector = this.posicion.sub(agenteI.getPosicion());
            Point vectorI = vector.div(distanciaAgente);
            Point vectorII = vectorI.scale(radioDeRepulsion - distanciaAgente);
            solucion = solucion.add(vectorII);
        }
        Vector vector = new Vector(solucion.getX(), solucion.getY());
        // si se va a salir queremos que con una probabilidad muy alta se quede quiete o se mueva aleatoriamente dentro
        //de la figura.
        if (this.figura.isDentroFigura(this.posicion.add(vector))) {
            double r;
            r = Math.random();
            if (r > 0.75) {
                vector = new Vector(0.0, 0.0);
            } else {
                Boolean movimientoNoPosible = true;
                while (movimientoNoPosible) {
                    vector = movFuera();
                    if (this.figura.isDentroFigura(this.posicion.add(vector))) ;
                    {
                        movimientoNoPosible = false;
                    }
                }
            }
        }
        return vector;
    }

    public void movimiento() {
        /** calcular el vector de movimiento pero no actualizas posicion,
         * esto es porque necesitas que todos los agentes sepan su vector de posicion primero. */

        if (this.perdido) {
            this.vectorMovimiento = this.movFuera();
        } else {
            if (this.figura.isDentroFigura(this.posicion)) {
                this.vectorMovimiento = this.movDentro();
            } else {
                this.vectorMovimiento = this.movFuera();
            }
        }

    }

    public void actualizarPosicion() {
        if (this.posicion != null) {
            this.posicion = this.posicion.add(this.vectorMovimiento);

            Point posicionNueva = Tablero.getInstance().posicionModeloTablero(this.posicion);
            // si el agente se sale por el tablero por un lado regresara por el contrario.
            this.posicion = posicionNueva;
        }
        /** ALERT Tendras que hacerlo con el error en la version final*/
    }

    public Point posicionModulo(Point posicionNueva) {
        Tablero tablero = Tablero.getInstance();
        double posicionX = posicionNueva.getX();
        double posicionY = posicionNueva.getY();

        double ejeXMaximo = tablero.getEjeXMaximo();
        double ejeYmaximo = tablero.getEjeYmaximo();
        if (!tablero.isDentro(posicionNueva)) {
            if (posicionX < 0.0) {
                posicionX = tablero.getEjeXMaximo() + posicionX;
            }
            if (posicionX > ejeXMaximo) {
                posicionX = posicionX - ejeXMaximo;
            }
            if (posicionY < 0.0) {
                posicionY = ejeYmaximo + posicionY;
            }
            if (posicionY > ejeYmaximo) {
                posicionY = posicionY - ejeYmaximo;
            }
        }
        return new Point(posicionX, posicionY);
    }

    public boolean isDentroFigura() {
        if (this.getPosicion() == null) {
            return false;
        }
        if (this.getFigura().isDentroFigura(this.getPosicion())) {
            return true;
        } else {
            return false;
        }
    }
}
