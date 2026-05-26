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
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class ConsultaPedidosController implements Initializable {

    @FXML
    private ComboBox<?> cbBusUsuario;
    @FXML
    private DatePicker dpBusFecha;
    @FXML
    private ComboBox<?> cbBusEstatus;
    @FXML
    private ListView<?> lvPedidos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnRealizarPedido(ActionEvent event) {
    }

    @FXML
    private void btnExportar(ActionEvent event) {
    }

    @FXML
    private void btnVolver(ActionEvent event) {
    }
    
}
