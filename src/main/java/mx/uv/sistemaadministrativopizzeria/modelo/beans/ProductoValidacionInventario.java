package mx.uv.sistemaadministrativopizzeria.modelo.beans;

public class ProductoValidacionInventario {

    private Producto producto;

    private int cantidadReal;

    private String razon;

    public ProductoValidacionInventario() {
    }

    public ProductoValidacionInventario(Producto producto) {
        this.producto = producto;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidadReal() {
        return cantidadReal;
    }

    public void setCantidadReal(int cantidadReal) {
        this.cantidadReal = cantidadReal;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }
}