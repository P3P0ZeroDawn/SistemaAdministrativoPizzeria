/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores;

import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.JavaFXUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import mx.uv.sistemaadministrativopizzeria.App;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Validador;
import mx.uv.sistemaadministrativopizzeria.excepciones.DatosFaltantesException;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Usuario;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.UsuarioDAO;

/**
 * FXML Controller class
 *
 * @author pedro
 */
public class InicioSesionController implements Initializable {

    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField pfContrasenia;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void clicBtnIniciarSesion(ActionEvent event) {
        try{
            Validador.requerido(tfUsuario, "El nombre de usuario es requerido");
            Validador.requerido(pfContrasenia, "La contraseña es requerida");
        }catch (DatosFaltantesException e){
                JavaFXUtils.mostrarAdvertencia("Datos Faltantes", e.getMessage(), false);
                return;
        }
        String usuario = tfUsuario.getText();
        String contrasenia = pfContrasenia.getText();
        
        Usuario usuarioRecuperado = null;
        usuarioRecuperado = UsuarioDAO.validarLogin(usuario, contrasenia);
        
        if(usuarioRecuperado != null){
            String nombreCompleto = (usuarioRecuperado.getNombre() != null ? usuarioRecuperado.getNombre() : "")
                    + (usuarioRecuperado.getApellidoPaterno() != null ? " " + usuarioRecuperado.getApellidoPaterno() : "")
                    + (usuarioRecuperado.getApellidoMaterno() != null ? " " + usuarioRecuperado.getApellidoMaterno() : "");
            JavaFXUtils.mostrarMensaje("Bienvenido", "Bienvenido, " + nombreCompleto.trim(), false);
            App.setMetadato("usuario", usuarioRecuperado);
            try {
                App.setRoot("menuEmpleado");
            } catch (IOException ex) {
                System.getLogger(InicioSesionController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }else{
            JavaFXUtils.mostrarMensaje("No se pudo iniciar sesión", "Credenciales inválidas", false);
        }
    }
    
}
