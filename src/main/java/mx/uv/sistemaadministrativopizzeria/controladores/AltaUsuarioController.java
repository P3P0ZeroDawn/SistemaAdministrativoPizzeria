/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Usuario;

/**
 * FXML Controller class
 *
 * @author pedro
 */
public class AltaUsuarioController implements Initializable {
    
    @FXML
    private ComboBox<Usuario.tipoUsuario> cbTipousuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbTipousuario.getItems().addAll(Usuario.tipoUsuario.values());
    }    

    @FXML
    private void clicCbTipoUsuario(ActionEvent event) {
        
    }
    
}
