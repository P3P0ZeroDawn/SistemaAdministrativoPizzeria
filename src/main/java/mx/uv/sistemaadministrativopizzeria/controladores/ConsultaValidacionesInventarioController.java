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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class ConsultaValidacionesInventarioController implements Initializable {

    @FXML
    private DatePicker dpBusFecha;
    @FXML
    private ComboBox<?> cbBusValidacion;
    @FXML
    private TableView<?> tvValidaciones;
    @FXML
    private TableColumn<?, ?> tcProducto;
    @FXML
    private TableColumn<?, ?> tcExistenciaSistema;
    @FXML
    private TableColumn<?, ?> tcExistenciaReal;
    @FXML
    private TableColumn<?, ?> tcEstatusExistencia;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnVolver(ActionEvent event) {
    }

    @FXML
    private void btnRealizarValidacion(ActionEvent event) {
    }
    
}
