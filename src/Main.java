import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;

import sistemamultiagente.*;

public class Main {

    public static void main(String[] args) {
        //try {
           // String ruta = "C:/Users/Maite/Desktop\Sistemas Multiagente\Pruebas_Finales\prueba_BORRAR.csv";
           // PrintWriter writer = new PrintWriter(ruta, "UTF-8");
        Main main = new Main();
            //for (int j = 0; j <10; j++) {
        main.execute();
        Tablero.getInstance().reset();

           // }
           // writer.close();
        //} catch (IOException e) {
        //    e.printStackTrace();
        
    //}
    }

    private void execute() {

        String ListaPorCentajeAgentesDentroReal = "";
        String ListaPorCentajeAgentesDentro = "";
        String SincronizacionAgentes = "";
        String SincronizacionLOCALAgentes = "";
        String SincronizacionDentroFigura = "";
        String EtapasParaEstadisticas = "";
        String CentroFguraX = "";
        String CentroFguraY = "";

        Tablero tablero = Tablero.getInstance();
        double EjeXMaximo = Constants.EJE_X_MAXIMO;
        double EjeYMaximo = Constants.EJE_Y_MAXIMO;
        GUI application = new GUI(EjeXMaximo, EjeYMaximo);


        //Agentes no perdidos
        for (int i = 0; i < Constants.AGENTES_NO_PERDIDOS; i++) {
            tablero.anadirAgente(false);
        }
        // Agentes perdidos
        for (int i = 0; i < Constants.AGENTES_PERDIDOS; i++) {
            tablero.anadirAgente(true);
        }
        application.paint(application.getGraphics());

        for (tablero.getEtapa(); tablero.getEtapa() < Constants.NUMERO_ETAPAS; tablero.aumentarEtapa()) {
            if (tablero.getEtapa() % 100 == 0) {
                System.out.println("Empieza");
                for (int j = 0; j < 5; j++) {
                    tablero.getTablero().keySet().forEach(agente -> {
                        agente.consensoDeCoordenadas();
                    });
                }
            }
            tablero.getTablero().keySet().forEach(agente -> {

                agente.consensoDeCoordenadas();
                agente.calcularVectorMovimiento();
                tablero.actualizarPosiciones(agente);

            });

            application.update(application.getGraphics());
            try {
//                if (Tablero.getInstance().getEtapa() % 100 == 0) {
//                GUI application2 = new GUI(EjeXMaximo, EjeYMaximo);
//                application2.update(application.getGraphics());
//                } else {
                    Thread.sleep(Constants.NUMERO_ESPERA);
              //  }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (tablero.getEtapa() % 100 == 0) {
                EtapasParaEstadisticas = EtapasParaEstadisticas + " ; " + tablero.getEtapa();
                ListaPorCentajeAgentesDentroReal = ListaPorCentajeAgentesDentroReal + " ; " + String.format("%.2f", AgentesDentroDeLaFiguraREAL());
                ListaPorCentajeAgentesDentro = ListaPorCentajeAgentesDentro + " ; " + String.format("%.2f", AgentesDentroDeLaFiguraSegunEllos());
                SincronizacionAgentes = SincronizacionAgentes + " ; " + String.format("%.2f", SincornizacionGLOBAL());
                SincronizacionLOCALAgentes = SincronizacionLOCALAgentes + " ; " + String.format("%.2f", SincornizacionLOCAL());
                SincronizacionDentroFigura = SincronizacionDentroFigura + " ; " + String.format("%.2f", SincornizacionGLOBALDentroFigura());
                Point centroFigura = centroFigura();
                CentroFguraX = CentroFguraX + " ; " + String.format("%.2f", centroFigura.getX());
                CentroFguraY = CentroFguraY + " ; " + String.format("%.2f", centroFigura.getY());
            }
        }
        //writer.println("Etapa" + EtapasParaEstadisticas);
        //writer.println("%realAgentesDentroFigura" + ListaPorCentajeAgentesDentroReal);
        System.out.println(ListaPorCentajeAgentesDentroReal);
        //writer.println("%AgenteCreenDentroFigura" + ListaPorCentajeAgentesDentro);
        System.out.println(ListaPorCentajeAgentesDentro);
        //writer.println("Sincronicacion entre agentes" + SincronizacionAgentes);
        System.out.println(SincronizacionAgentes);
        //writer.println("Sincronicacion LOCAL entre agentes" + SincronizacionLOCALAgentes);
        System.out.println(SincronizacionLOCALAgentes);
        //writer.println("Sincronicacion GLOABL dentro figura" + SincronizacionDentroFigura);
        System.out.println(SincronizacionDentroFigura);
        //writer.println("Centro figura X" + CentroFguraX);
        System.out.println(CentroFguraX);
        //writer.println("Centro figura Y" + CentroFguraY);
        System.out.println(CentroFguraY);


//
        /* HAY QUE CERRAR LA PESTAÑA DE FRAME */
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public double AgentesDentroDeLaFiguraREAL() {
        Tablero tablero = Tablero.getInstance();
        FiguraInterface figura = Constants.FIGURA;
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
        FiguraInterface figura = Constants.FIGURA;
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


    public double SincronizacionGLOABLUnAgenteDentroFigura(Agente agenteSujeto) {
        Tablero tablero = Tablero.getInstance();
        FiguraInterface figura = Constants.FIGURA;
        Double error = 0.0;
        for (Agente agente : tablero.getTablero().keySet()) {
            if (!agente.getPerdido() && figura.isDentroFigura(agente.getPosicion())) {
                error = error + Math.abs((agenteSujeto.getPosicion().distance(agente.getPosicion()) - tablero.getTablero().get(agenteSujeto).distance(tablero.getTablero().get(agente))));
            }
        }
        if (tablero.getTablero().keySet().size() >= 1) {
            error = error / tablero.getTablero().keySet().size();
        }
        return error;
    }

    public double SincornizacionGLOBALDentroFigura() {
        Tablero tablero = Tablero.getInstance();
        Double error = 0.0;
        FiguraInterface figura = Constants.FIGURA;
        Double numeroDeAgentesNoPErdidos = 0.0;
        for (Agente agente : tablero.getTablero().keySet()) {
            if (!agente.getPerdido() && figura.isDentroFigura(agente.getPosicion())) {
                numeroDeAgentesNoPErdidos = numeroDeAgentesNoPErdidos + 1;
                error = error + SincronizacionGLOABLUnAgenteDentroFigura(agente);
            }
        }
        return (error / numeroDeAgentesNoPErdidos);
    }

    public double SincronizacionGLOABLUnAgente(Agente agenteSujeto) {
        Tablero tablero = Tablero.getInstance();
        Double error = 0.0;
        for (Agente agente : tablero.getTablero().keySet()) {
            if (!agente.getPerdido()) {
                error = error + Math.abs((agenteSujeto.getPosicion().distance(agente.getPosicion()) - tablero.getTablero().get(agenteSujeto).distance(tablero.getTablero().get(agente))));
            }
        }
        if (tablero.getTablero().keySet().size() >= 1) {
            error = error / tablero.getTablero().keySet().size();
        }
        return error;
    }

    public double SincornizacionGLOBAL() {
        Tablero tablero = Tablero.getInstance();
        Double error = 0.0;
        Double numeroDeAgentesNoPErdidos = 0.0;
        for (Agente agente : tablero.getTablero().keySet()) {
            if (!agente.getPerdido()) {
                numeroDeAgentesNoPErdidos = numeroDeAgentesNoPErdidos + 1;
                error = error + SincronizacionGLOABLUnAgente(agente);
            }
        }
        return (error / numeroDeAgentesNoPErdidos);
    }

    public double SincronizacionLOCALUnAgente(Agente agenteSujeto) {
        Tablero tablero = Tablero.getInstance();
        Double error = 0.0;
        for (Agente agente : tablero.agentesCercanosNoPerdidos(agenteSujeto)) {
            if (!agente.getPerdido() && tablero.agentesCercanosNoPerdidos(agenteSujeto).size() > 1) {
                error = error + Math.abs((agenteSujeto.getPosicion().distance(agente.getPosicion()) - tablero.getTablero().get(agenteSujeto).distance(tablero.getTablero().get(agente))));
            }
        }
        if (tablero.agentesCercanosNoPerdidos(agenteSujeto).size() >= 1) {
            error = error / tablero.agentesCercanosNoPerdidos(agenteSujeto).size();
        }
        return error;
    }


    public double SincornizacionLOCAL() {
        Tablero tablero = Tablero.getInstance();
        Double error = 0.0;
        Double numeroDeAgentesNoPErdidos = 0.0;
        for (Agente agente : tablero.getTablero().keySet()) {
            if (!agente.getPerdido()) {
                numeroDeAgentesNoPErdidos = numeroDeAgentesNoPErdidos + 1;
                error = error + SincronizacionLOCALUnAgente(agente);
            }
        }
        return (error / numeroDeAgentesNoPErdidos);
    }

    public Point centroFigura() {
        Tablero tablero = Tablero.getInstance();
        FiguraInterface figura = Constants.FIGURA;
        double numeroDeAgentes = 0.0;
        Point result = new Point(0.0, 0.0);
        for (Agente agente : tablero.getTablero().keySet()) {
            if (!agente.getPerdido() && figura.isDentroFigura(agente.getPosicion())) {
                numeroDeAgentes = numeroDeAgentes + 1;
                result = result.add(Tablero.getInstance().getTablero().get(agente));
            }
        }
        return new Point(result.getX() / numeroDeAgentes, result.getY() / numeroDeAgentes);
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