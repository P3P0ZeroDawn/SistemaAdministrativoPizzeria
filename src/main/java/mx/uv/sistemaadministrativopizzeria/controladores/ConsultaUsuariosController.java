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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class ConsultaUsuariosController implements Initializable {

    @FXML
    private TextField tfBusqueda;
    @FXML
    private CheckBox cbPorNombre;
    @FXML
    private CheckBox cbPorDireccion;
    @FXML
    private CheckBox cbPorTelefono;
    @FXML
    private ListView<?> lvUsuarios;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnNuevoUsuario(ActionEvent event) {
    }

    @FXML
    private void btnVolver(ActionEvent event) {
    }

    @FXML
    private void btnBuscar(ActionEvent event) {
    }
    
}
