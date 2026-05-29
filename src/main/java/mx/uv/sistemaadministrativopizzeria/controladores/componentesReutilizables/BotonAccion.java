/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables;

import java.util.function.Consumer;
import org.kordamp.ikonli.Ikon;

/**
 *
 * @author hp
 */
public class BotonAccion<T> {

    private final String texto;
    private Ikon icono;
    private final Consumer<T> accion;

    public BotonAccion(
            String texto,
            Ikon icono,
            Consumer<T> accion
    ) {
        this.texto = texto;
        this.icono = icono;
        this.accion = accion;
    }

    public BotonAccion(String texto, Consumer<T> accion) {
        this.texto = texto;
        this.icono = null;
        this.accion = accion;
    }

    public String getTexto() {
        return texto;
    }

    public Ikon getIcono() {
        return icono;
    }

    public Consumer<T> getAccion() {
        return accion;
    }
}
