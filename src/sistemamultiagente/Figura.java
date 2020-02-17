package sistemamultiagente;

import java.util.List;

public class Figura {

    /**
     * Atributos
     */

    private List<Point> figura;

    /**
     * Métodos
     */

    public Figura(List lista){
        figura = lista;
    }

    public boolean dentroFuera(Agente agente){
        return figura.contains(agente.getPosicion());
    }


}
