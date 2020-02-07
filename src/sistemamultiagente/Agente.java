package sistemamultiagente;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Agente {

    /**
     * ATRIBUTOS
     */

    private final int distanciaMaxSensor = 5;
    private final int distanciaMaxMov = 5;

    private Integer id;
    private boolean perdido;
    /* EGOO esto en muchos sera Null al principio.*/
    private List<Integer> posicion;
    private List<Integer> listaTrilateraciones;

    /**
     * MÉTODOS
     */

    public Agente(Integer id) {
        posicion = null;
        perdido = true;
        this.id = id;
    }

    public Agente(boolean perdido, List<Integer> posicion, Integer id) {
        if (perdido) posicion = new ArrayList<>();
        this.posicion = posicion;
        this.perdido = perdido;
        this.listaTrilateraciones = new ArrayList<>();
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


    public List<Integer> calcularCoordenadasAgenteSinMovimiento(Tablero tablero) {
        if (!this.getPerdido()) {// pasa si posicion es False, es decir no esta perdido y las coordenadas no son null
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


}
