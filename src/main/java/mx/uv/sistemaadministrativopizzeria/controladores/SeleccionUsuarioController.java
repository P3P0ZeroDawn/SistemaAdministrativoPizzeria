/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Usuario;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class SeleccionUsuarioController implements Initializable {

    private ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    
    @FXML
    private ComboBox<Usuario> cbUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
    }

    @FXML
    private void clicBtnConfirmar(ActionEvent event) {
    }
    
}
