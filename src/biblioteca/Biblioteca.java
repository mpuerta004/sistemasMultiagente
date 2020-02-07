package biblioteca;

import java.util.Iterator;
import java.util.LinkedList;

public class Biblioteca {


    private static Biblioteca myInstance;
    private LinkedList<Libro> listaLibros;

    public LinkedList<Libro> getListaLibros() {
        return listaLibros;
    }

    private Biblioteca() {
        listaLibros = new LinkedList<Libro>();
    }

    public static Biblioteca getInstance(){
        if(myInstance == null) myInstance = new Biblioteca();
        return myInstance;
    }

    public void añadirLibro(String autor, String titulo) {
        Libro libro1 = new Libro(autor, titulo);
        listaLibros.add(libro1);

    }

    public void enseñarLibros() {
        listaLibros.sort(Libro::compareTo);
        System.out.println("Esta es una lista de los libros disponibles ordenadas por titulo.");
        for (int i = 0; i < listaLibros.size(); ++i) listaLibros.get(i).getInformation();

        Iterator<Libro> iterator = listaLibros.iterator();
        while (iterator.hasNext()) iterator.next().getInformation();

        for (Libro libro : listaLibros) libro.getInformation();

        listaLibros.stream().forEach(libro -> libro.getInformation());
    }


}
