/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.modelo.beans;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author pedro
 */
public class HistorialInventario {
    private int idHistorialInventario;
    private LocalDateTime fecha;
    private ArrayList ProductoHistorial;

    public HistorialInventario() {
    }

    public HistorialInventario(int idHistorialInventario, LocalDateTime fecha, ArrayList ProductoHistorial) {
        this.idHistorialInventario = idHistorialInventario;
        this.fecha = fecha;
        this.ProductoHistorial = ProductoHistorial;
    }
    
    public int getIdHistorialInventario() {
        return idHistorialInventario;
    }

    public void setIdHistorialInventario(int idHistorialInventario) {
        this.idHistorialInventario = idHistorialInventario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public ArrayList getProductoHistorial() {
        return ProductoHistorial;
    }

    public void setProductoHistorial(ArrayList ProductoHistorial) {
        this.ProductoHistorial = ProductoHistorial;
    }
    
    @Override
    public String toString(){
        String fechaString = fecha.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return fechaString.replace("T", " ");
    }
}
