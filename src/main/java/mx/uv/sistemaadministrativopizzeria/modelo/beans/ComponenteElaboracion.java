/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.modelo.beans;

/**
 *
 * @author pedro
 */
public class ComponenteElaboracion {
    private int idComponenteElaboración;
    private Producto producto;
    private int cantidad;

    public ComponenteElaboracion() {
    }

    public ComponenteElaboracion(int idComponenteElaboración, Producto producto, int cantidad) {
        this.idComponenteElaboración = idComponenteElaboración;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public int getIdComponenteElaboración() {
        return idComponenteElaboración;
    }

    public void setIdComponenteElaboración(int idComponenteElaboración) {
        this.idComponenteElaboración = idComponenteElaboración;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    
}
