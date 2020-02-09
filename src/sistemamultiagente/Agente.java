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
        /**ALERT   ¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿??????????????????????? */
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
        return null;
    }


    public Point trilateracion(List<Agente> tresAgentesCercanosNoPerdidos, Tablero tablero) {
        double distanciaSensorAgente1 = tablero.sensorAgente(this, tresAgentesCercanosNoPerdidos.get(0));
        double distanciaSensorAgente2 = tablero.sensorAgente(this, tresAgentesCercanosNoPerdidos.get(1));
        double distanciaSensorAgente3 = tablero.sensorAgente(this, tresAgentesCercanosNoPerdidos.get(2));
        /**    ALERT todo  */


        return this.posicion;
    }

    public Point mediaTrilateracion() {
/**        if (listaTrilateraciones.size()>= numTrilateracionesGuardo) {

 }else{ */
        return this.posicion;
        //     }
    }

    public void consensoDeCoordenadas(Tablero tablero) {
        List<Agente> agentesCercanosNoPerdidos = tablero.agentesCercanosNoPerdidos(this);
        if (agentesCercanosNoPerdidos.size() > 3) {
            /**ALET  PARA que no los de siempre en el mismo orden esto lo hace porque utilice stream ?????????????*/
            List<Agente> tresAgentesCercanosNoPerdidos = agentesCercanosNoPerdidos.subList(0, 3);
            if (this.perdido) {
                // si esta perdido, entonces calcula la primera coordena
                /** ALET No se si ahi tiene que haber un this o no ???????????????????????????????????????????????*/
                this.posicion = this.primeraCoordenada(tresAgentesCercanosNoPerdidos, tablero);
                this.posicion = this.trilateracion(tresAgentesCercanosNoPerdidos, tablero);
                listaTrilateraciones.add(this.trilateracion(tresAgentesCercanosNoPerdidos, tablero));
                // ALERT esto lo estas haciendo con los mismo que calculas la posicion inicial esto se podria cambiar.
            } else {
                if (tablero.getEtapa() % numDePasosParaMediarLasTrilateraciones == 0) {
                    this.posicion = mediaTrilateracion();
                }
                listaTrilateraciones.add(this.trilateracion(tresAgentesCercanosNoPerdidos, tablero));
            }
        }
    }


}
