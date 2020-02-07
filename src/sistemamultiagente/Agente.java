package sistemamultiagente;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class Agente {

    /**
     * ATRIBUTOS
     */
    //ALERT estos numero luego lees el paper y los pones todos bien.
    private final int distanciaMaxSensor = 5;
    private final int distanciaMaxMov = 5;
    private final int numDePasosParaMediarLasTrilateraciones= 15;
    private final int numTrilateracionesGuardo = 10;


    private Integer id;
    private boolean perdido;
    /* EGOO esto en muchos sera Null al principio.*/
    private List<Integer> posicion;
    private Stack<List<Integer>> listaTrilateraciones;

    /**
     * MÉTODOS
     */

    public Agente(Integer id) {
        posicion = null;
        perdido = true;
        this.id = id;
        this.listaTrilateraciones = new Stack<List<Integer>>;
    }

    public Agente(boolean perdido, List<Integer> posicion, Integer id) {
        if (perdido) posicion = new ArrayList<>();
        this.posicion = posicion;
        this.perdido = perdido;
        this.listaTrilateraciones = new Stack<List<Integer>>;
        this.id = id;
    }

    public boolean getPerdido() {
        return perdido;
    }

    public List<Integer> getPosicion() {
        return posicion;
    }

    public List<Integer> getListaTrilateraciones() {
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
        /* ¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿??????????????????????? */
        Agente agente = (Agente) o;
        return Objects.equals(id, agente.id);
    }

    /*
public List<Integer> calcularCoordenadasAgenteSinMovimiento(Tablero tablero) {
    if (!this.getPerdido()) { // pasa si posicion es False, es decir no esta perdido y las coordenadas no son null
        return this.getPosicion();
    } else {
        List<Agente> agentesCercanosNoPerdidos = tablero.agentesCercanosNoPerdidos(this);
        if (agentesCercanosNoPerdidos.size() < 3) {
            return ;
        } else {
            List<Agente> tresAgentesCercanos
        }
    }


    }
    */

    public List<Integer> primeraCoordenada(List<Agente> tresAgentesCercanosNoPerdidos) {
        /**     HACER   */
        return this.posicion;
    }

    public List<Integer> trilateracion(List<Agente> tresAgentesCercanosNoPerdidos) {
        /**     HACER   */
        return this.posicion;
    }
    public List<Integer> mediaTrilateracion(){
        /**     HACER */
        return this.posicion;
    }

    public void consensoDeCoordenadas(Tablero tablero) {
        List<Agente> agentesCercanosNoPerdidos = tablero.agentesCercanosNoPerdidos(this);
        if (agentesCercanosNoPerdidos.size() > 3) {
            /**ALET  PARA que no los de siempre en el mismo orden esto lo hace porque utilice stream ?????????????*/
            List<Agente> tresAgentesCercanosNoPerdidos = agentesCercanosNoPerdidos.subList(0, 3);
            if (this.perdido) {
                // si esta perdido, entonces calcula la primera coordena
                /** ALET No se si ahi tiene que haber un this o no ???????????????????????????????????????????????*/
                this.posicion = this.primeraCoordenada(tresAgentesCercanosNoPerdidos);
                this.posicion = this.trilateracion(tresAgentesCercanosNoPerdidos);
                listaTrilateraciones.add(this.trilateracion(tresAgentesCercanosNoPerdidos));
                // ALERT esto lo estas haciendo con los mismo que calculas la posicion inicial esto se podria cambiar.
            } else {
                if (tablero.getEtapa()% numDePasosParaMediarLasTrilateraciones ==0) {
                    this.posicion = mediaTrilateracion();
                }
                listaTrilateraciones.add(this.trilateracion(tresAgentesCercanosNoPerdidos));
            }
        }
    }
}
