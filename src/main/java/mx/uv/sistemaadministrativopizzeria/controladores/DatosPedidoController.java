/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import mx.uv.sistemaadministrativopizzeria.App;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ModoFormulario;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.PedidoDAO;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class DatosPedidoController implements Initializable {

    private ObservableList<Producto> pedidos = FXCollections.observableArrayList();
     
    private ModoFormulario modo;
    private Pedido pedido;
    
    @FXML
    private Label lbTitulo;
    @FXML
    private Label lbUsuario;
    @FXML
    private Label lbTotal;
    @FXML
    private TextField tfProducto;
    @FXML
    private ListView<Producto> lvProducto;
    @FXML
    private TextField tfPedido;
    @FXML
    private ListView<Producto> lvPedido;
    @FXML
    private Button btnSeleccionUsuario;
    @FXML
    private Label lbFecha;
    @FXML
    private Button btnConfirmacion;
    @FXML
    private Label lbFechaPedido;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    private void llenarProductos(){
        //pedidos.addAll(lista);
        //lvProducto.setItems(pedidos);
    }

    public void configurar(ModoFormulario modo, Pedido pedido){
        this.modo = modo;
        this.pedido = pedido;
        
        if(modo == ModoFormulario.EDICION && pedido != null){
            lbTitulo.setText("Edición de pedido");
            btnSeleccionUsuario.setVisible(false);
            btnConfirmacion.setText("Confirmar");
        } else{
            lbFecha.setVisible(false);
            lbFechaPedido.setVisible(false);
        }
    }
    
    @FXML
    private void btnClicSeleccionUsuario(ActionEvent event) {
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        try {
            App.setRoot("consultaPedidos");
        } catch (IOException ex) {
            System.getLogger(DatosPedidoController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void clicConfirmar(ActionEvent event) {
    }
    
}
