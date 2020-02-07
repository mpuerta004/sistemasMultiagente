package productos;

import java.util.Date;

public class ProductoPerecedero extends Producto implements Comparable<ProductoPerecedero>{

    private Date expiredDate;

    public ProductoPerecedero(String nombre, Date expiredDate) {
        super(nombre);
        this.expiredDate = expiredDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getNombre() {
        return super.getNombre() + " - " + expiredDate.toString();
    }

    @Override
    public int compareTo(ProductoPerecedero o) {
        return getNombre().compareTo(o.getNombre());
    }
}
