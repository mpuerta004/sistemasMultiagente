import biblioteca.Biblioteca;
import productos.Producto;
import productos.ProductoPerecedero;
import biblioteca.Biblioteca;
import biblioteca.Libro;
import sistemamultiagente.Agente;
import sistemamultiagente.Tablero;
import sistemamultiagente.Figura;

import java.sql.Date;

public class Main {

    public static void main(String[] args) {
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
        Tablero tablero = Tablero.getInstance();

}

    public void primeraAccion(int numero, String s){
        System.out.println("Este es el numero: " + numero);
        System.out.println("Este es el string: " + s);
    }


}
