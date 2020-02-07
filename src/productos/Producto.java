package productos;

public class Producto {

    private String nombre;
    private final String procedencia = "ES";


    public Producto(String nombre){
        this.setNombre(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if(nombre != null && nombre.length() > 1)
        this.nombre = nombre;
    }

}
