/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables;

/**
 *
 * @author pedro
 */
public class Badge {

    private final String texto;
    private final String color;

    public Badge(String texto, String color) {
        this.texto = texto;
        this.color = color;
    }

    public String getTexto() {
        return texto;
    }

    public String getColor() {
        return color;
    }
}
