/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables;

import java.util.function.Consumer;

/**
 *
 * @author hp
 */
public class BotonAccion<T> {

    private final String texto;
    private final String rutaIcono;
    private final Consumer<T> accion;

    public BotonAccion(String texto,
                       String rutaIcono,
                       Consumer<T> accion) {

        this.texto = texto;
        this.rutaIcono = rutaIcono;
        this.accion = accion;
    }

    public String getTexto() {
        return texto;
    }

    public String getRutaIcono() {
        return rutaIcono;
    }

    public Consumer<T> getAccion() {
        return accion;
    }
}
