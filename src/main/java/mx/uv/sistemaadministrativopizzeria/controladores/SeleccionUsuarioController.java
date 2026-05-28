/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Usuario;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.UsuarioDAO;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class SeleccionUsuarioController implements Initializable {

    private ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    
    private Pedido pedido;
    
    @FXML
    private ComboBox<Usuario> cbUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Usuario> lista = UsuarioDAO.obtenerUsuarios();
        usuarios.addAll(lista);
        cbUsuario.setItems(usuarios);
    }    
    
    public void setPedido(Pedido pedido){
        this.pedido = pedido;
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        pedido.setIdUsuario(-1);
        ((Stage) cbUsuario.getScene().getWindow()).close();
    }

    @FXML
    private void clicBtnConfirmar(ActionEvent event) {
        Usuario usuario = cbUsuario.getSelectionModel().getSelectedItem();
        if(usuario != null){
            pedido.setIdUsuario(usuario.getIdUsuario());
            pedido.setNombreUsuario(usuario.getString());
            ((Stage) cbUsuario.getScene().getWindow()).close();
        }
    }
}
