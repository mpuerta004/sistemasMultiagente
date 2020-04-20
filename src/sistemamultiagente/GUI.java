package sistemamultiagente;
import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;


public class GUI extends JFrame {

    // set window's title bar String and dimensions
    public GUI(double width, double height) {
        setSize((int) width*100, (int)height*100);




    }

    // draw shapes with Java2D API
    public void paint(Graphics g, Tablero tablero) {

        super.paint(g); // call superclass's paint method
        Graphics2D g2d = (Graphics2D) g; // cast g to Graphics2D
        // draw 2D ellipse filled with a blue-yellow gradient
        g2d.setPaint(Color.BLUE);
        g2d.fill(new Ellipse2D.Double(0,0, 65, 65));

        // draw 2D rectangle in red
        g2d.setPaint(Color.RED);
        g2d.setStroke(new BasicStroke(10.0f));
        g2d.draw(new Rectangle2D.Double(80, 30, 65, 100));

        // draw 2D rounded rectangle with a buffered background
        BufferedImage buffImage = new BufferedImage(10, 10,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D gg = buffImage.createGraphics();
        gg.setColor(Color.YELLOW); // draw in yellow
        gg.fillRect(0, 0, 10, 10); // draw a filled rectangle
        gg.setColor(Color.BLACK); // draw in black
        gg.drawRect(1, 1, 6, 6); // draw a rectangle
        gg.setColor(Color.BLUE); // draw in blue
        gg.fillRect(1, 1, 3, 3); // draw a filled rectangle
        gg.setColor(Color.RED); // draw in red
        gg.fillRect(4, 4, 3, 3); // draw a filled rectangle

        // paint buffImage onto the JFrame
        g2d.setPaint( new TexturePaint( buffImage,
                new Rectangle( 10, 10 ) ) );
        g2d.fill( new RoundRectangle2D.Double( 155, 30, 75, 100, 50, 50 ) );
         // draw 2D pie-shaped arc in white
         g2d.setPaint( Color.WHITE );
         g2d.setStroke( new BasicStroke( 6.0f ) );
         g2d.draw( new Arc2D.Double( 240, 30, 75, 100, 0, 270, Arc2D.PIE ) );

         // draw 2D lines in green and yellow
         g2d.setPaint( Color.GREEN );
         g2d.draw( new Line2D.Double( 395, 30, 320, 150 ) );

         float dashes[] = { 10 };

         g2d.setPaint( Color.YELLOW );
         g2d.setStroke( new BasicStroke( 4, BasicStroke.CAP_ROUND,
         BasicStroke.JOIN_ROUND, 10, dashes, 0 ) );
         g2d.draw( new Line2D.Double( 320, 30, 395, 150 ) );

         } // end method paint

}
