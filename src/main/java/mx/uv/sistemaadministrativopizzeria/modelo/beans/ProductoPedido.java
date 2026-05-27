/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.modelo.beans;

import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ItemObservableList;

/**
 *
 * @author pedro
 */
public class ProductoPedido implements ItemObservableList{
    private int idProductoPedido;
    private Producto producto;
    private int cantidad;

    public ProductoPedido() {
    }

    public ProductoPedido(int idProductoPedido, Producto producto, int cantidad) {
        this.idProductoPedido = idProductoPedido;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public int getIdProductoPedido() {
        return idProductoPedido;
    }

    public void setIdProductoPedido(int idProductoPedido) {
        this.idProductoPedido = idProductoPedido;
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

    @Override
    public String getString() {
        return producto.getNombre();
    }
}
