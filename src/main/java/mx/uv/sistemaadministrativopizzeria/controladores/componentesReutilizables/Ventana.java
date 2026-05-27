/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables;

import javafx.stage.Stage;

/**
 *
 * @author pedro
 */
public class Ventana<T> {

    private Stage stage;
    private T controller;

    public Ventana(Stage stage, T controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public Stage getStage() {
        return stage;
    }

    public T getController() {
        return controller;
    }
}
