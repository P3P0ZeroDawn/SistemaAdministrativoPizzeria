/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.modelo.beans;

import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 *
 * @author pedro
 */
public class Producto {
    private int idProducto;
    private String nombre;
    private String codigo;
    private String descripcion;
    private Double precio;
    private String restricciones;
    private Image foto;
    private Double cantidad;
    private String unidadMedida;
    private Boolean esPreparado;
    private Boolean esInsumo;
    private Boolean activo;
    private ArrayList componentes;

    public Producto() {
    }

    public Producto(int idProducto, String nombre, String codigo, String descripcion, Double precio, String restricciones, Image foto, Double cantidad, String unidadMedida, Boolean esPreparado, Boolean esInsumo, Boolean activo, ArrayList componentes) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.restricciones = restricciones;
        this.foto = foto;
        this.cantidad = cantidad;
        this.unidadMedida = unidadMedida;
        this.esPreparado = esPreparado;
        this.esInsumo = esInsumo;
        this.activo = activo;
        this.componentes = componentes;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getRestricciones() {
        return restricciones;
    }

    public void setRestricciones(String restricciones) {
        this.restricciones = restricciones;
    }

    public Image getFoto() {
        return foto;
    }

    public void setFoto(Image foto) {
        this.foto = foto;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Boolean getEsPreparado() {
        return esPreparado;
    }

    public void setEsPreparado(Boolean esPreparado) {
        this.esPreparado = esPreparado;
    }

    public Boolean getEsInsumo() {
        return esInsumo;
    }

    public void setEsInsumo(Boolean esInsumo) {
        this.esInsumo = esInsumo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public ArrayList getComponentes() {
        return componentes;
    }

    public void setComponentes(ArrayList componentes) {
        this.componentes = componentes;
    }

    
}
