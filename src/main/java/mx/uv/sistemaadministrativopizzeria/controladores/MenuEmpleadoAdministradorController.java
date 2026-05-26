/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import mx.uv.sistemaadministrativopizzeria.App;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Usuario;

/**
 * FXML Controller class
 *
 * @author pedro
 */
public class MenuEmpleadoAdministradorController implements Initializable {

    @FXML
    private Label lbNombreEmpleado;
    
    Usuario usuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuario = (Usuario) App.getMetadato("usuario");
        lbNombreEmpleado.setText(usuario.getNombre() +
                " " + usuario.getApellidoPaterno() +
                " " + usuario.getApellidoMaterno());
    }    

    @FXML
    private void clicBtnCerrarSesion(ActionEvent event) {
        App.setMetadato("usuario", null);
        try {
            App.setRoot("inicioSesion");
        } catch (IOException ex) {
            System.getLogger(MenuEmpleadoAdministradorController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void clicBtnUsuarios(ActionEvent event) {
        try {
            App.setRoot("consultaUsuarios");
        } catch (IOException ex) {
            System.getLogger(MenuEmpleadoAdministradorController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void clicBtnAcercaDe(ActionEvent event) {
        try {
            App.setRoot("acercaDe");
        } catch (IOException ex) {
            System.getLogger(MenuEmpleadoAdministradorController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void clicBtnProductos(ActionEvent event) {
        try {
            App.setRoot("consultaProductos");
        } catch (IOException ex) {
            System.getLogger(MenuEmpleadoAdministradorController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void clicBtnPedidos(ActionEvent event) {
        try {
            App.setRoot("consultaPedidos");
        } catch (IOException ex) {
            System.getLogger(MenuEmpleadoAdministradorController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
}
