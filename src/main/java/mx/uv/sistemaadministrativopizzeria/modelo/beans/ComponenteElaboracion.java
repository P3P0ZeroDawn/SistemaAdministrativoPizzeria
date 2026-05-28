package mx.uv.sistemaadministrativopizzeria.modelo.beans;

import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ItemObservableList;

public class ComponenteElaboracion implements ItemObservableList {

    private Producto producto;
    private Double cantidad;

    public ComponenteElaboracion() {
    }

    public ComponenteElaboracion(
            Producto producto,
            Double cantidad) {

        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String getString() {

        if (producto != null) {
            return producto.getNombre();
        }

        return "";
    }
}