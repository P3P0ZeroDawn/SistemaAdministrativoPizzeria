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
    private ArrayList<ProductoHistorial> productosHistorial;

    public HistorialInventario() {
    }

    public HistorialInventario(int idHistorialInventario, LocalDateTime fecha, ArrayList<ProductoHistorial> productosHistorial) {
        this.idHistorialInventario = idHistorialInventario;
        this.fecha = fecha;
        this.productosHistorial = productosHistorial;
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

    public ArrayList getProductosHistorial() {
        return productosHistorial;
    }

    public void setProductosHistorial(ArrayList<ProductoHistorial> productosHistorial) {
        this.productosHistorial = productosHistorial;
    }
    
    @Override
    public String toString(){
        String fechaString = fecha.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return fechaString.replace("T", " ");
    }
}
