import sistemamultiagente.Agente;
import sistemamultiagente.Tablero;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import sistemamultiagente.GUI;

public class Main {

    public static void main(String[] args) {


        Tablero tablero = Tablero.getInstance();
        double EjeXMaximo = tablero.getEjeXMaximo();
        double EjeYmaximo = tablero.getEjeYmaximo();
        GUI application = new GUI(EjeXMaximo, EjeYmaximo);
        //Agentes no perdidos
        for (int i = 0; i < 10; i++) {

            tablero.añadirAgente(false);
        }
        // Agentes perdidos
        for (int i = 0; i < 10; i++) {
            tablero.añadirAgente(true);
        }
        List<Agente> listaAgentesPerdidosSegunEllos = tablero.getTablero().keySet().stream()
                .filter(agente -> agente.getPerdido()).collect(Collectors.toList());
        System.out.println("Numero de agentes que estan perdidos segun ellos: " + listaAgentesPerdidosSegunEllos.size());


        for (tablero.getEtapa(); tablero.getEtapa() < 250; tablero.aumentarEtapa()) {
            tablero.getTablero().keySet().forEach(agente -> {

                agente.consensoDeCoordenadas();
                // System.out.println("Calcular las coordenadas");
                agente.calcularVectorMovimiento();
                // System.out.println("Calcular el vector de mov");
                tablero.actualizarPosiciones(agente);
                //agente.actualizarPosicion();
                System.out.println("actualiza el movimiento");


            });
        }
        AtomicInteger contador44 = new AtomicInteger();
        tablero.getTablero().keySet().forEach(agente -> {
            System.out.println("Agente ......................................................................");
            System.out.println("Posicion del agente:");
            if (agente.getPosicion() != null) {
                System.out.println("Posicion x: " + agente.getPosicion().getX());
                System.out.println("Posicion y: " + agente.getPosicion().getY());
                System.out.println("Si esta dentro con la posicion que cree el agente: " + tablero.isDentro(agente.getPosicion()));
            } else {
                contador44.set(contador44.get() + 1);
                System.out.println("Posicion:  NULL");

            }

            System.out.println("Posicion Del agente real");
            System.out.println("Posicion real:" + tablero.getTablero().get(agente));

            System.out.println("Si esta dentro con la posicion del tablero: " + tablero.isDentro(tablero.getTablero().get(agente)));
        });
        System.out.println("Cueston estas perdidos:" + contador44);


            System.out.println(tablero.getEtapa());
            List<Agente> listaAgentes = tablero.getTablero().keySet().stream()
                    .filter(agente -> tablero.isDentro(tablero.getTablero().get(agente))).collect(Collectors.toList());
            System.out.println("Numero de agentes que estan dentro segun el tablero: " + listaAgentes.size());

            List<Agente> listaAgentesPerdidosSegunEllos2 = tablero.getTablero().keySet().stream()
                    .filter(agente -> agente.getPerdido()).collect(Collectors.toList());
            System.out.println("Numero de agentes que estan perdidos segun ellos: " + listaAgentesPerdidosSegunEllos2.size());

            List<Agente> listaAgentes4 = tablero.getTablero().keySet().stream()
                    .filter(agente -> agente.isDentroFigura()).collect(Collectors.toList());
            System.out.println("Numero de agentes que estan dentro de la figura segun el tablero: " + listaAgentes4.size());
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    //}
        /*
        public void primeraAccion(int numero, String s){
            System.out.println("Este es el numero: " + numero);
            System.out.println("Este es el string: " + s);
        }

        */
}
