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
    private int cantidadReal;
    private String razon;
    private EstatusExistencia estatusExistencia;
    private Producto producto;
    
    public enum EstatusExistencia {Correcta, Faltante, Sobrante};

    public ProductoHistorial() {
    }

    public ProductoHistorial(int idProductoHistorial, int cantidadReal, String razon, EstatusExistencia estatusExistencia, Producto producto) {
        this.idProductoHistorial = idProductoHistorial;
        this.cantidadReal = cantidadReal;
        this.razon = razon;
        this.estatusExistencia = estatusExistencia;
        this.producto = producto;
    }

    public int getIdProductoHistorial() {
        return idProductoHistorial;
    }

    public void setIdProductoHistorial(int idProductoHistorial) {
        this.idProductoHistorial = idProductoHistorial;
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
}
