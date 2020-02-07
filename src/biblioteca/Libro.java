package biblioteca;

public class Libro  implements Comparable<Libro>{

    private String autor;
    private String titulo;

    public Libro(String autor, String titulo) {
        this.autor=autor;
        this.titulo=titulo;

        }

    public String getAutor() {
        return autor;
    }

    public String getTitulo() {
        return titulo;
    }
    public void getInformation(){
        System.out.println("Autor: "+ this.getAutor()+ " - Titulo: "+ getTitulo());

    }


    @Override
    public int compareTo(Libro o) {
        return this.getTitulo().compareTo(o.getTitulo());
    }
}
