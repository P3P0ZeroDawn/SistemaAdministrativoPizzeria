/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.modelo.beans;

import java.time.LocalDate;
import java.util.ArrayList;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ItemObservableList;

/**
 *
 * @author pedro
 */
public class Pedido implements ItemObservableList{
    private int idPedido;
    private int idUsuario;
    private LocalDate fechaPedido;
    private Double totalAPagar;
    private EstatusPedido estatus;
    private ArrayList<ProductoPedido> productos;
    private String nombreUsuario;

    @Override
    public String getString() {
        return String.format("%-30s %s", nombreUsuario, fechaPedido);
    }
            
    public enum EstatusPedido {EnPreparacion, Entregado, Cancelado};

    public Pedido() {
        this.totalAPagar = 0.0;
    }

    public Pedido(int idPedido, int idUsuario, LocalDate fechaPedido, Double totalAPagar, EstatusPedido estatus, ArrayList<ProductoPedido> productos,String nombreUsuario) {
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.fechaPedido = fechaPedido;
        this.totalAPagar = totalAPagar;
        this.estatus = estatus;
        this.productos = productos;
        this.nombreUsuario = nombreUsuario;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    @Override
    public String toString(){
        return nombreUsuario;
    }
}
