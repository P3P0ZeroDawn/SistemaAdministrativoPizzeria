/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.modelo.beans;

/**
 *
 * @author pedro
 */
public class ProductoHistorial {
    private int idProductoHistorial;
    private Double cantidadReal;
    private String razon;
    private EstatusExistencia estatusExistencia;
    private Producto producto;
    private Double cantidadSistema;
    
    public enum EstatusExistencia {Correcta, Faltante, Sobrante};

    public ProductoHistorial() {
    }

    public ProductoHistorial(int idProductoHistorial, Double cantidadReal, String razon, EstatusExistencia estatusExistencia, Producto producto, Double cantidadSistema) {
        this.idProductoHistorial = idProductoHistorial;
        this.cantidadReal = cantidadReal;
        this.razon = razon;
        this.estatusExistencia = estatusExistencia;
        this.producto = producto;
        this.cantidadSistema = cantidadSistema;
    }

    public int getIdProductoHistorial() {
        return idProductoHistorial;
    }

    public void setIdProductoHistorial(int idProductoHistorial) {
        this.idProductoHistorial = idProductoHistorial;
    }

    public Double getCantidadReal() {
        return cantidadReal;
    }

    public void setCantidadReal(Double cantidadReal) {
        this.cantidadReal = cantidadReal;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public EstatusExistencia getEstatusExistencia() {
        return estatusExistencia;
    }

    public void setEstatusExistencia(EstatusExistencia estatusExistencia) {
        this.estatusExistencia = estatusExistencia;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public String getNombreProducto(){
        return producto.getNombre();
    }

    public Double getCantidadSistema() {
        return cantidadSistema;
    }

    public void setCantidadSistema(Double cantidadSistema) {
        this.cantidadSistema = cantidadSistema;
    }
}
