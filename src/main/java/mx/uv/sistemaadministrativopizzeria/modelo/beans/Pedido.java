/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.modelo.beans;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author pedro
 */
public class Pedido {
    private int idPedido;
    private LocalDate fechaPedido;
    private Double totalAPagar;
    private EstatusPedido estatus;
    private ArrayList<ProductoPedido> productos;
            
    public enum EstatusPedido {EnPreparacion, Entregado, Cancelado};

    public Pedido() {
    }

    public Pedido(int idPedido, LocalDate fechaPedido, Double totalAPagar, EstatusPedido estatus, ArrayList<ProductoPedido> productos) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.totalAPagar = totalAPagar;
        this.estatus = estatus;
        this.productos = productos;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Double getTotalAPagar() {
        return totalAPagar;
    }

    public void setTotalAPagar(Double totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public EstatusPedido getEstatus() {
        return estatus;
    }

    public void setEstatus(EstatusPedido estatus) {
        this.estatus = estatus;
    }

    public ArrayList<ProductoPedido> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<ProductoPedido> productos) {
        this.productos = productos;
    }
}
