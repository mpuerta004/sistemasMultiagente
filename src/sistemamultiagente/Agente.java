package sistemamultiagente;


import java.util.*;


public class Agente {

    /**
     * ATRIBUTOS
     */
    //ALERT estos numero luego lees el paper y los pones todos bien.
    private final int distanciaMaxSensor = 5;
    private final int distanciaMaxMov = 5;
    private final int numDePasosParaMediarLasTrilateraciones = 10;
    private final int numTrilateracionesGuardo = 10;
    private final int tamañoAgente = 1;


    private Integer id;
    private boolean perdido;
    /* EGOO esto en muchos sera Null al principio.*/
    private Point posicion;
    private Stack<Point> listaTrilateraciones;

    /**
     * MÉTODOS
     */

    public Agente(Integer id) {
        posicion = null;
        perdido = true;
        this.id = id;
        this.listaTrilateraciones = new Stack<Point>();
    }

    public Agente(boolean perdido, Point posicion, Integer id) {
        if (perdido) posicion = null;
        this.posicion = posicion;
        this.perdido = perdido;
        this.listaTrilateraciones = new Stack<Point>();
        this.id = id;
    }

    public boolean getPerdido() {
        return perdido;
    }

    public Point getPosicion() {
        return posicion;
    }

    public Stack<Point> getListaTrilateraciones() {
        return listaTrilateraciones;
    }

    public int getDistanciaMaxSensor() {
        return distanciaMaxSensor;
    }

    public int getDistanciaMaxMov() {
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
        /**ALERT   ¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿?????????????????????????????????????????????????????????????????????????? */
        Agente agente = (Agente) o;
        return Objects.equals(id, agente.id);
    }

    public Point primeraCoordenada(List<Agente> tresAgentesCercanosNoPerdidos, Tablero tablero) {
        //Sabemos que hay tres agentes cercanos no perdidos.
        /** Datos que necesita para realizar este calculo*/
        Point posAgenteCercano1 = tablero.redInalambrica(this, tresAgentesCercanosNoPerdidos.get(0));
        Point posAgenteCercano2 = tablero.redInalambrica(this, tresAgentesCercanosNoPerdidos.get(1));
        Point posAgenteCercano3 = tablero.redInalambrica(this, tresAgentesCercanosNoPerdidos.get(2));
        boolean intentar = true;

        /** Calculo */
        while (intentar) {
            try {
                Random r1 = new Random();
                double radio1 = this.tamañoAgente + (distanciaMaxSensor - tamañoAgente) * r1.nextDouble();
                Random r2 = new Random();
                double radio2 = this.tamañoAgente + (distanciaMaxSensor - tamañoAgente) * r2.nextDouble();
                Random r3 = new Random();
                double radio3 = this.tamañoAgente + (distanciaMaxSensor - tamañoAgente) * r3.nextDouble();

                Circle c1 = new Circle(posAgenteCercano1, radio1);
                Circle c2 = new Circle(posAgenteCercano2, radio2);
                Circle c3 = new Circle(posAgenteCercano3, radio3);

                List<Point> listaIntersecciones = new ArrayList<>();
                // Si en alguno no son dospuntos saldra un nuevo

                listaIntersecciones.addAll(c1.intersection(c2));
                listaIntersecciones.addAll(c1.intersection(c3));
                listaIntersecciones.addAll(c2.intersection(c3));

                intentar = false;
                Line l1 = new Line(listaIntersecciones.get(0), listaIntersecciones.get(1));
                Line l2 = new Line(listaIntersecciones.get(2), listaIntersecciones.get(4));
                Point puntoInterseccion = l1.interseccion(l2);
                double distanciaAlPrimero = puntoInterseccion.distance(listaIntersecciones.get(0));
                double distanciaAlSegundo = puntoInterseccion.distance(listaIntersecciones.get(1));
                if (distanciaAlPrimero > distanciaAlSegundo) {
                    return listaIntersecciones.get(0);
                } else {
                    return listaIntersecciones.get(1);
                }
            } catch (ArithmeticException e) {
                intentar = true;


            }
        }
        /** ALERT EGO ??????????????????????????????????????????????????????????????????????????????????????????????*/
        return null;
    }


    public Point trilateracion(List<Agente> tresAgentesCercanosNoPerdidos, Tablero tablero) {

        double distanciaSensorAgente1 = tablero.sensorAgente(this, tresAgentesCercanosNoPerdidos.get(0));
        double distanciaSensorAgente2 = tablero.sensorAgente(this, tresAgentesCercanosNoPerdidos.get(1));
        double distanciaSensorAgente3 = tablero.sensorAgente(this, tresAgentesCercanosNoPerdidos.get(2));

        // Este calculo lo hace el agente y la informacion que tiene el agente de la posiciones de los otros es
        // es la posicion a la que el otro agente cree estar.
        // (x1, y1) posicion a la cree estar el agente cercano primero.
        double x1 = tablero.redInalambrica(this, tresAgentesCercanosNoPerdidos.get(0)).getX();
        double y1 = tablero.redInalambrica(this, tresAgentesCercanosNoPerdidos.get(0)).getY();
        double x2 = tablero.redInalambrica(this, tresAgentesCercanosNoPerdidos.get(1)).getX();
        double y2 = tablero.redInalambrica(this, tresAgentesCercanosNoPerdidos.get(1)).getY();
        double x3 = tablero.redInalambrica(this, tresAgentesCercanosNoPerdidos.get(2)).getX();
        double y3 = tablero.redInalambrica(this, tresAgentesCercanosNoPerdidos.get(2)).getY();
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


    public Point gradientDescent(Point posicionAgente, Point soltrilateraciones) {
        boolean iterar = true;
        double beta = 0.9;
        /** ALERT ...... no estoy segura de que esto este bien plantado la vd.*/
        while (iterar) {
            Line l1 = new Line(posicionAgente, soltrilateraciones);
            double triangulo = l1.getDireccion().getY() / l1.getDireccion().getX();
            Point nuevo = new Point(soltrilateraciones.getX() - beta * triangulo * soltrilateraciones.getX(), soltrilateraciones.getY() - beta * triangulo * soltrilateraciones.getY());
            posicionAgente = soltrilateraciones;
            soltrilateraciones = nuevo;
            if (nuevo.distance(posicionAgente) < this.tamañoAgente || triangulo < 0.001) iterar = false;
        }
        return soltrilateraciones;
    }

    public Point mediaTrilateracion() {
        if (listaTrilateraciones.size() < numTrilateracionesGuardo) {
            return this.posicion;
        } else {
            double sumX = 0.0;
            double sumY = 0.0;
            for (int i = 0; i < numTrilateracionesGuardo; i++) {
                /** ALERT no se si lo el pop estaria bien...????????????????????????????????????????????????????*/
                Point nuevo = listaTrilateraciones.pop();
                sumX += nuevo.getX();
                sumY += nuevo.getY();
            }

            return new Point(sumX / numTrilateracionesGuardo, sumY / numTrilateracionesGuardo);
        }
    }

    public void consensoDeCoordenadas(Tablero tablero) {
        List<Agente> agentesCercanosNoPerdidos = tablero.agentesCercanosNoPerdidos(this);
        if (agentesCercanosNoPerdidos.size() > 3) {
            /**ALET  PARA que no los de siempre en el mismo orden esto lo hace porque utilice stream ????????????????*/
            List<Agente> tresAgentesCercanosNoPerdidos = agentesCercanosNoPerdidos.subList(0, 3);
            if (this.perdido) {
                // si esta perdido, entonces calcula la primera coordena
                /** ALET No se si ahi tiene que haber un this o no ??????????????????????????????????????????????????*/
                /** ALERT no se si verdadermaente tengo que hacer un push.....???????????????????????????????????????*/
                this.posicion = this.primeraCoordenada(tresAgentesCercanosNoPerdidos, tablero);
                this.posicion = this.trilateracion(tresAgentesCercanosNoPerdidos, tablero);
                listaTrilateraciones.push(this.trilateracion(tresAgentesCercanosNoPerdidos, tablero));
                // ALERT esto lo estas haciendo con los mismos agentes que calculas la posicion inicial esto se podria
                // cambiar.
            } else {
                if (tablero.getEtapa() % numDePasosParaMediarLasTrilateraciones == 0) {
                    this.posicion = mediaTrilateracion();
                }
                listaTrilateraciones.push(this.trilateracion(tresAgentesCercanosNoPerdidos, tablero));
            }
        }
    }
}
