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
import javafx.stage.Stage;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.JavaFXUtils;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class SeleccionEstatusPedidoController implements Initializable {

    private ObservableList<Pedido.EstatusPedido> estatus = FXCollections.observableArrayList();
    
    private Pedido.EstatusPedido estatusSeleccionado = null;
    
    @FXML
    private ComboBox<Pedido.EstatusPedido> cbEstatus;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        estatus.addAll(Pedido.EstatusPedido.values());
        estatus.remove(Pedido.EstatusPedido.EnPreparacion);
        cbEstatus.setItems(estatus);
    }    
    
    public Pedido.EstatusPedido getEstatus(){
        return estatusSeleccionado;
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        ((Stage) cbEstatus.getScene().getWindow()).close();
    }

    @FXML
    private void clicBtnConfirmar(ActionEvent event) {
        if(cbEstatus.getSelectionModel().getSelectedItem() != null){
            estatusSeleccionado = cbEstatus.getSelectionModel().getSelectedItem();
            ((Stage) cbEstatus.getScene().getWindow()).close();
        } else{
            JavaFXUtils.mostrarAdvertencia("Sin selección", 
                    "Por favor seleccione un estatus\npara el pedido", false);
        }
    } 
}
