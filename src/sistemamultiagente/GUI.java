package sistemamultiagente;

import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;


public class GUI extends JFrame {

    public GUI(double width, double height) {
        super("Sistema Multiagente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize((int) width *15+100, (int) height * 15+100);
        getContentPane().setBackground(Color.darkGray);
        setVisible(true);

    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        //dibujo bordes para que se vea el cuadrado donde se mueven los agentes.
        g2d.setPaint(Color.GRAY);
        g2d.fill(new Rectangle2D.Double(0.0,0.0,  50,1000));
        g2d.setPaint(Color.GRAY);
        g2d.fill(new Rectangle2D.Double(0.0,0.0,  10000,50));
        g2d.setPaint(Color.GRAY);
        g2d.fill(new Rectangle2D.Double(50+(Constants.EJE_X_MAXIMO)*15.0,0.0,  50,500000));
        g2d.setPaint(Color.GRAY);
        g2d.fill(new Rectangle2D.Double(0.0,50+(Constants.EJE_Y_MAXIMO)*15.0,  5000000,50));
        //dibujo la figura
        FiguraCuadrado figura = new FiguraCuadrado();
//        Point centroFigura = redimensionarizar(Constants.CENTER, 2 * Constants.RADIO);
        g2d.setPaint(Color.BLACK);
        g2d.fill(new Rectangle2D.Double(Constants.EJE_X_MINIMO_FIGURA*15+50, 50+Constants.EJE_X_MINIMO_FIGURA*15,
                2 * ((Constants.EJE_Y_MAXIMO_FIGURA-Constants.EJE_X_MINIMO_FIGURA)/2)* 15,
                2 * ((Constants.EJE_Y_MAXIMO_FIGURA-Constants.EJE_X_MINIMO_FIGURA)/2)* 15));
        //Dibujar los agentes en la posicion en la que estan.
        Tablero.getInstance().getTablero().keySet().forEach(agente -> {
                    if (agente.getPerdido()) {
                        g2d.setPaint(Color.RED);
                    } else if (figura.isDentroFigura(agente.getPosicion())){
                            //Constants.CENTER.distance(agente.getPosicion()) <= Constants.RADIO) {
                        g2d.setPaint(Color.WHITE);
                    } else {
                        g2d.setPaint(Color.green);
                    }

                    Point centroAgente;
                    centroAgente = redimensionarizar(
                            Tablero.getInstance().getTablero().get(agente), Constants.TAMAÑO_AGENTE);
                    g2d.fill(new Ellipse2D.Double(
                            centroAgente.getX()*15+50,
                            centroAgente.getY()*15+50,
                            (Constants.TAMAÑO_AGENTE * 15),
                            (Constants.TAMAÑO_AGENTE * 15)));
//                    g2d.drawString(agente.getId() + "", Float.parseFloat(centroAgente.getX()*15+50 + ""),
//                            Float.parseFloat(centroAgente.getY()*15+50+ ""));
                }
        );
    }
    // redimensionar:
    // Le das el tamaño (DIAMETRO) y ek centro te calcula el punto de arriba a la izquierza para que sepa dibujarlo.
    private Point redimensionarizar(Point point, double tamano) {
        return point.sub(new Point(tamano / 2, tamano / 2));
    }
}
