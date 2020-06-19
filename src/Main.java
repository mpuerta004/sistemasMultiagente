import sistemamultiagente.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.execute();
    }

    private void execute() {
        ArrayList<Double> ListaPorCentajeAgentesDentroReal = new ArrayList<>();
        ArrayList<Double> ListaPorCentajeAgentesDentro = new ArrayList<>();
        ArrayList<Double> SincronizacionAgentes = new ArrayList<>();
        Tablero tablero = Tablero.getInstance();
        double EjeXMaximo = Constants.EJE_X_MAXIMO;
        double EjeYmaximo = Constants.EJE_Y_MAXIMO;
        GUI application = new GUI(EjeXMaximo, EjeYmaximo);

        //application.setDefaultCloseperation(JFrame.EXIT_ON_CLOSE);
        //Agentes no perdidos
        for (int i = 0; i < Constants.AGENTES_NO_PERDIDOS; i++) {
            tablero.anadirAgente(false);
        }
        // Agentes perdidos
        for (int i = 0; i < Constants.AGENTES_PERDIDOS; i++) {
            tablero.anadirAgente(true);
        }

        application.paint(application.getGraphics());

        for (tablero.getEtapa(); tablero.getEtapa() < 1001; tablero.aumentarEtapa()) {
            Constants.MEDIA_SIN_DIVIDIR = new Point(0.0, 0.0);
            Constants.AGENTES_CON_COORDENADAS = 0;
            Constants.VARIANZA = 0.0;
            tablero.getTablero().keySet().forEach(agente ->

                        agente.consensoDeCoordenadas());
            tablero.getTablero().keySet().forEach(agente -> {

                agente.consensoDeCoordenadas();
                agente.calcularVectorMovimiento();
                tablero.actualizarPosiciones(agente);

//                if (!agente.getPerdido()) {
//                    Constants.AGENTES_CON_COORDENADAS = Constants.AGENTES_CON_COORDENADAS + 1;
//                    Constants.MEDIA_SIN_DIVIDIR = Constants.MEDIA_SIN_DIVIDIR.add(agente.getPosicion());
//                    //System.out.println(Constants.MEDIA_SIN_DIVIDIR);
//                }

            });
//            Constants.MEDIA_SIN_DIVIDIR = Constants.MEDIA_SIN_DIVIDIR.div(Constants.AGENTES_CON_COORDENADAS);
//            System.out.println("MEdia " + Constants.MEDIA_SIN_DIVIDIR);
//            //System.out.println(Constants.AGENTES_CON_COORDENADAS);

//            for (Agente agente : tablero.getTablero().keySet()) {
//                if (!agente.getPerdido()) {
//                    Double nuevo = Math.pow(agente.getPosicion().distance(Constants.MEDIA_SIN_DIVIDIR), 2);
//
//
//                    Constants.VARIANZA = Constants.VARIANZA + nuevo;
//                }
//            }
//            Constants.VARIANZA = Constants.VARIANZA / (Constants.AGENTES_CON_COORDENADAS);
//            System.out.println(Constants.AGENTES_CON_COORDENADAS);
//            System.out.println("VArianza de la etapa " + tablero.getEtapa() + " es: " + Constants.VARIANZA);

            //System.out.println("Etapa" + tablero.getEtapa());
            application.update(application.getGraphics());
            try {
                Thread.sleep(3 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (tablero.getEtapa() % 100 == 0) {
                ListaPorCentajeAgentesDentroReal.add(AgentesDentroDeLaFiguraREAL());
                ListaPorCentajeAgentesDentro.add(AgentesDentroDeLaFiguraSegunEllos());
                SincronizacionAgentes.add(SincornizacionGLOBAL());
            }
        }
//
//
        /**HAY QUE CERRAR LA PESTAÑA DE FRAME**/
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println("Agentes dentro real  ||  Agentes dentro segun ellos || Sincronicacion Gloabl");
        for (int i = 0; i < ListaPorCentajeAgentesDentroReal.size(); i++) {
            System.out.println(ListaPorCentajeAgentesDentroReal.get(i) + "                    " +
                    ListaPorCentajeAgentesDentro.get(i)+ "                    " + SincronizacionAgentes.get(i));
        }

    }


    public double AgentesDentroDeLaFiguraREAL() {
        Tablero tablero = Tablero.getInstance();
        Figura figura = new Figura();
        Double numeroTotalAgentes = Double.valueOf(Constants.AGENTES_NO_PERDIDOS + Constants.AGENTES_PERDIDOS);
        Double agentesDentroFigura = 0.0;
        for (Agente agente : tablero.getTablero().keySet()) {
            if (figura.isDentroFigura(tablero.getTablero().get(agente))) {
                agentesDentroFigura = agentesDentroFigura + 1;
            }
        }
        return (agentesDentroFigura / numeroTotalAgentes) * 100;
    }

    public double AgentesDentroDeLaFiguraSegunEllos() {
        Tablero tablero = Tablero.getInstance();
        Figura figura = new Figura();
        Double numeroTotalAgentes = 0.0;
        Double agentesDentroFiguraSegunELLOS = 0.0;
        for (Agente agente : tablero.getTablero().keySet()) {
            if (!agente.getPerdido()) {
                numeroTotalAgentes = numeroTotalAgentes + 1;
                //System.out.println(numeroTotalAgentes);
                if (figura.isDentroFigura(agente.getPosicion())) {

                    agentesDentroFiguraSegunELLOS = agentesDentroFiguraSegunELLOS + 1;
                }
            }
        }
        return (agentesDentroFiguraSegunELLOS / numeroTotalAgentes) * 100;
    }

    public double SincronizacionGLOABLUnAgente(Agente agenteSujeto) {
        Tablero tablero = Tablero.getInstance();
        Double error = 0.0;
        for (Agente agente : tablero.getTablero().keySet()) {
            if(!agente.getPerdido()){
            error = error +
                    Math.abs((agenteSujeto.getPosicion().distance(agente.getPosicion()) -
                            tablero.getTablero().get(agenteSujeto).distance(tablero.getTablero().get(agente))
                    ));
        }}
        return error;
    }

    public double SincornizacionGLOBAL(){
        Tablero tablero = Tablero.getInstance();
        Double error = 0.0;
        Double numeroDeAgentesNoPErdidos=0.0;
        for (Agente agente: tablero.getTablero().keySet()){
            if(!agente.getPerdido()){
                numeroDeAgentesNoPErdidos=numeroDeAgentesNoPErdidos+1;
                error = error + SincronizacionGLOABLUnAgente(agente);
            }
        }
        return (error/numeroDeAgentesNoPErdidos);
    }

}
//        for (Agente agente : tablero.getTablero().keySet()) {
//            if (!agente.getPerdido()) {
//                if (agente.getPosicion().sub(tablero.getTablero().get(agente)).getX() != 0.0 ||
//                        agente.getPosicion().sub(tablero.getTablero().get(agente)).getY() != 0.0) {
//                    System.out.println(" Error en agente con id " + agente.getId() + " ERROR:----------->>>>> " + agente.getPosicion().sub(Tablero.getInstance().getTablero().get(agente)));
//                    System.out.println("Posicion del agente:" + agente.getPosicion());
//                    System.out.println("Posicion real del agente: " + Tablero.getInstance().getTablero().get(agente));
//                    if (agente.getId() <= 3) {
//                        System.out.println("Inicialmente: NO PERDIDO");
//                    } else {
//                        System.out.println("Inicialmente:  PERDIDO");
//                    }
//                }
//            } else {
//                System.out.println("El agente no ha encontrado coordenadas: " + agente.getId());
//            }
//        }
//        System.out.println("TRILATERACIONES BIEN ECHAS: " + Constants.COUNTBIEN);
//        System.out.println("Trilateraciones MALL echas: " + Constants.COUNTMAL);
//        System.out.println("___________________________________");


//
//            AtomicInteger contador44 = new AtomicInteger();
//            tablero.getTablero().keySet().forEach(agente -> {
//                        System.out.println("Agente ......................................................................");
//                        //System.out.println("Posicion del agente:");
//                        System.out.println(agente.getListaTrilateraciones());
//                        if (agente.getPosicion() != null) {
//                            System.out.println("Diferencia entre las posion real y la del agente:");
//                            System.out.println(agente.getPosicion().sub(tablero.getTablero().get(agente)));
//
//                            System.out.println("Posicion x: " + agente.getPosicion().getX());
//                            System.out.println("Posicion y: " + agente.getPosicion().getY());
//                            System.out.println("Si esta dentro con la posicion que cree el agente: " + tablero.isDentro(agente.getPosicion()));
//                        } else {
//                            contador44.set(contador44.get() + 1);
//                            System.out.println("Posicion:  NULL");
//
//                        }
//                        System.out.println("Posicion Del agente real");
//                        System.out.println("Posicion real:" + tablero.getTablero().get(agente));
//
//                        System.out.println("Si esta dentro con la posicion del tablero: " + tablero.isDentro(tablero.getTablero().get(agente)));
//                    }
//            );
//            System.out.println("Cueston estas perdidos:" + contador44);
//
//
//            System.out.println(tablero.getEtapa());
//            List<Agente> listaAgentes = tablero.getTablero().keySet().stream()
//                    .filter(agente -> tablero.isDentro(tablero.getTablero().get(agente))).collect(Collectors.toList());
//            System.out.println("Numero de agentes que estan dentro segun el tablero: " + listaAgentes.size());
//
//            List<Agente> listaAgentesPerdidosSegunEllos2 = tablero.getTablero().keySet().stream()
//                    .filter(agente -> agente.getPerdido()).collect(Collectors.toList());
//            System.out.println("Numero de agentes que estan perdidos segun ellos: " + listaAgentesPerdidosSegunEllos2.size());
//
//            List<Agente> listaAgentes4 = tablero.getTablero().keySet().stream()
//                    .filter(agente -> agente.agenteisDentroFigura()).collect(Collectors.toList());
//            System.out.println("Numero de agentes que estan dentro de la figura segun el tablero: " + listaAgentes4.size());