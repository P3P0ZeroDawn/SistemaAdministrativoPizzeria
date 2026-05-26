/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.modelo.beans;

import java.time.LocalDateTime;

/**
 *
 * @author pedro
 */
public class HistorialInventario {
    private int idHistorialInventario;
    private LocalDateTime fecha;

    public HistorialInventario() {
    }

    public HistorialInventario(int idHistorialInventario, LocalDateTime fecha) {
        this.idHistorialInventario = idHistorialInventario;
        this.fecha = fecha;
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
}
