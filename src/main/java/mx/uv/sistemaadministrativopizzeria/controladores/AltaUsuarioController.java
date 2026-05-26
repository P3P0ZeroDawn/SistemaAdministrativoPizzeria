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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.JavaFXUtils;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Validador;
import mx.uv.sistemaadministrativopizzeria.excepciones.DatosFaltantesException;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Usuario;

/**
 * FXML Controller class
 *
 * @author pedro
 */
public class AltaUsuarioController implements Initializable {
    
    @FXML
    private ComboBox<Usuario.tipoUsuario> cbTipousuario;
    @FXML
    private ComboBox<Usuario.rolEmpleado> cbRolEmpleado;
    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField pfContrasenia;
    @FXML
    private PasswordField pfConfirmacionContrasenia;
    
    private final Tooltip tooltipContrasenia = new Tooltip("Las contraseñas no coinciden");
    @FXML
    private VBox vbRol;
    @FXML
    private VBox vbUsuario;
    @FXML
    private VBox vbContrasenia;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField tfCp;
    @FXML
    private TextField tfCalle;
    @FXML
    private TextField tfNumero;
    @FXML
    private TextField tfCiudad;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbTipousuario.getItems().addAll(Usuario.tipoUsuario.values());
        
        pfContrasenia.setOnKeyReleased(event -> validarContrasenias());
        pfConfirmacionContrasenia.setOnKeyReleased(event -> validarContrasenias());
    }    

    @FXML
    private void clicCbTipoUsuario(ActionEvent event) {
        if(cbTipousuario.getSelectionModel().getSelectedItem().equals(Usuario.tipoUsuario.Empleado)){
            cbRolEmpleado.setDisable(false);
            cbRolEmpleado.getItems().addAll(Usuario.rolEmpleado.values());
            vbRol.setVisible(true);
            tfUsuario.setDisable(false);
            vbUsuario.setVisible(true);
            pfContrasenia.setDisable(false);
            pfConfirmacionContrasenia.setDisable(false);
            pfConfirmacionContrasenia.setVisible(true);
            vbContrasenia.setVisible(true);
        }else{
            cbRolEmpleado.setDisable(true);
            cbRolEmpleado.getItems().clear();
            tfUsuario.setText(null);
            pfContrasenia.setText(null);
            pfConfirmacionContrasenia.setText(null);
            tfUsuario.setDisable(true);
            pfContrasenia.setDisable(true);
            pfConfirmacionContrasenia.setDisable(true);
            vbUsuario.setVisible(false);
            vbContrasenia.setVisible(false);
            vbRol.setVisible(false);
        }
    }    

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        ((Stage) cbTipousuario.getScene().getWindow()).close();
    }

    @FXML
    private void clicBtnDarDeAlta(ActionEvent event) {
        try{
            Usuario usuarioRecuperado = recuperarUsuario();
        }catch (DatosFaltantesException e){
            JavaFXUtils.mostrarAdvertencia("Datos Faltantes", e.getMessage(), false);
        }  
    }
    
    private Usuario recuperarUsuario() throws DatosFaltantesException{
        
        Validador.validarCampo(tfNombre, "El nombre es obligatorio");
        Validador.validarCampo(tfApellidoPaterno, "El apellido paterno es obligatorio");
        Validador.validarCampo(tfTelefono, "El teléfono es obligatorio");
        Validador.validarCampo(tfCorreo, "El correo es obligatorio");
        Validador.validarCampo(tfCp, "El código postal es obligatorio");
        Validador.validarCampo(tfCalle, "La calle es obligatoria");
        Validador.validarCampo(tfNumero, "El número es obligatorio");
        Validador.validarCampo(tfCiudad, "La ciudad es obligatoria");
        Validador.validarCampoComboBox(cbTipousuario, "Debes seleccionar un tipo de usuario");
        
        if(cbTipousuario.getValue() == Usuario.tipoUsuario.Empleado){
            Validador.validarCampoComboBox(cbRolEmpleado, "Debes seleccionar un rol de empleado");
            Validador.validarCampo(tfUsuario, "El usuario es obligatorio");
            Validador.validarCampoPassword(pfContrasenia, "La contraseña es obligatoria");
            Validador.validarComprobacionContrasenia(pfContrasenia,
                    pfConfirmacionContrasenia,
                    "Las contraseñas no coinciden");
        }
        Usuario usuario = new Usuario();
        
        usuario.setNombre(tfNombre.getText().trim());
        usuario.setApellidoPaterno(tfApellidoPaterno.getText().trim());
        usuario.setApellidoMaterno(tfApellidoMaterno.getText().trim());
        usuario.setTelefono(tfTelefono.getText().trim());
        usuario.setEmail(tfCorreo.getText().trim());
        Usuario.Direccion direccion = usuario.new Direccion();
        direccion.setCodigoPostal(tfCp.getText().trim());
        direccion.setCalle(tfCalle.getText().trim());
        direccion.setNumero(tfNumero.getText().trim());
        direccion.setCiudad(tfCiudad.getText().trim());
        usuario.setDireccion(direccion);
        usuario.setTipoUsuario(cbTipousuario.getValue());
        
        if(cbTipousuario.getValue() == Usuario.tipoUsuario.Empleado){
            usuario.setUsuario(tfUsuario.getText().trim());
            usuario.setPassword(pfContrasenia.getText());
            usuario.setRolEmpleado(cbRolEmpleado.getValue());
        }
        return usuario;
    }
    
    private void validarContrasenias() {

        String contrasenia = pfContrasenia.getText() == null
                ? ""
                : pfContrasenia.getText();

        String confirmacion = pfConfirmacionContrasenia.getText() == null
                ? ""
                : pfConfirmacionContrasenia.getText();

        if (!confirmacion.equals(contrasenia)) {

            pfConfirmacionContrasenia.setStyle(
                "-fx-border-color: red; " +
                "-fx-border-width: 2px;"
            );

            pfConfirmacionContrasenia.setTooltip(tooltipContrasenia);

        } else {

            pfConfirmacionContrasenia.setStyle("");
            pfConfirmacionContrasenia.setTooltip(null);
        }
    }
}
