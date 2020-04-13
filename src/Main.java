import biblioteca.Biblioteca;
import productos.Producto;
import productos.ProductoPerecedero;
import biblioteca.Biblioteca;
import biblioteca.Libro;
import sistemamultiagente.Agente;
import sistemamultiagente.Tablero;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import sistemamultiagente.Figura;
import sistemamultiagente.Point;

import java.sql.Date;

public class Main {

    public static void main(String[] args) {

        Tablero tablero = Tablero.getInstance();
        double EjeXMaximo = tablero.getEjeXMaximo();
        double EjeYmaximo = tablero.getEjeYmaximo();
        /** Agentes no perdidos */
        for (int i = 0; i < 10; i++) {

            tablero.añadirAgente(false);
        }
        /** Agentes perdidos*/
        for (int i = 0; i <  100; i++) {
            tablero.añadirAgente(true);
        }

        for (tablero.getEtapa(); tablero.getEtapa() < 100; tablero.aumentarEtapa()) {
            tablero.getTablero().keySet().forEach(agente -> {

                agente.consensoDeCoordenadas();
                System.out.println("Calcular las coordenadas");
                agente.movimiento();
                System.out.println("Calcular el vector de mov");
                tablero.actualizarPosiciones(agente);
                System.out.println("actualiza el movimiento");

            });
            System.out.println(tablero.getEtapa());
            List<Agente> listaAgentes= tablero.getTablero().keySet().stream()
                    .filter(agente -> tablero.isDentro(tablero.getTablero().get(agente))) .collect(Collectors.toList());
            System.out.println("Numero de agentes que estan dentro segun el tablero: " + listaAgentes.size());

            List<Agente> listaAgentesPerdidosSegunEllos= tablero.getTablero().keySet().stream()
                    .filter(agente ->  agente.getPerdido()).collect(Collectors.toList());
            System.out.println("Numero de agentes que estan perdidos segun ellos: " + listaAgentesPerdidosSegunEllos.size());

            List<Agente> listaAgentes2= tablero.getTablero().keySet().stream()
                    .filter(agente -> agente.isDentroFigura()).collect(Collectors.toList());
            System.out.println("Numero de agentes que estan dentro de la figura segun el tablero: " + listaAgentes.size());
        }


            /*System.out.println("Hello World!");
            Main objeto = new Main();
            objeto.primeraAccion(10, "palabra");




            Producto producto1 = new Producto("Salchichitas");
            ProductoPerecedero productoPerecedero1 = new ProductoPerecedero("Queso", Date.valueOf("2020-06-20"));
            System.out.println(producto1.getNombre());
            System.out.println(productoPerecedero1.getNombre());
            */

            /*
            Biblioteca bibliotecaMunicipal = Biblioteca.getInstance();
            bibliotecaMunicipal.añadirLibro("Miguel de Cervantes", "Don Quijote de la Mancha");
            bibliotecaMunicipal.añadirLibro("Charles Dickens", "Historia de dos ciudades");
            bibliotecaMunicipal.añadirLibro("J. R. R. Tolkien", "El Señor de los Anillos");
            bibliotecaMunicipal.enseñarLibros();

           */

    }
        /*
        public void primeraAccion(int numero, String s){
            System.out.println("Este es el numero: " + numero);
            System.out.println("Este es el string: " + s);
        }

        */
}
