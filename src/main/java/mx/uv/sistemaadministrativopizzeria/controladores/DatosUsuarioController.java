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
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.JavaFXUtils;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ModoFormulario;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Validador;
import mx.uv.sistemaadministrativopizzeria.excepciones.DatosFaltantesException;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Usuario;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.UsuarioDAO;

/**
 * FXML Controller class
 *
 * @author pedro
 */
public class DatosUsuarioController implements Initializable {
    
    private ModoFormulario modo;
    private Usuario usuarioEdicion;
    
    private final Tooltip tooltipContrasenia = new Tooltip("Las contraseñas no coinciden");
    
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
    @FXML
    private CheckBox chbModifContrasenia;
    private VBox vbModifContrasenia;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbTipousuario.getItems().addAll(Usuario.tipoUsuario.values());
        
        pfContrasenia.setOnKeyReleased(event -> validarContrasenias());
        pfConfirmacionContrasenia.setOnKeyReleased(event -> validarContrasenias());
        chbModifContrasenia.setOnAction(event -> alternarVisualizacionCamposContrasenia());
        Validador.permitirSoloTexto(tfNombre, 45);
        Validador.permitirSoloTexto(tfApellidoPaterno, 45);
        Validador.permitirSoloTexto(tfApellidoMaterno, 45);
        Validador.permitirTelefono(tfTelefono, 10);
        Validador.permitirSoloNumeros(tfCp, 45);
        Validador.permitirDireccion(tfCalle, 45);
        Validador.permitirTextoNumerico(tfNumero, 45);
        Validador.permitirSoloTexto(tfCiudad, 45);
        Validador.permitirTextoNumerico(tfUsuario, 45);
        Validador.permitirPassword(pfContrasenia, 45);
        Validador.permitirPassword(pfConfirmacionContrasenia, 45);
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
    private void clicBtnGuardar(ActionEvent event) {
        try{
            Usuario usuarioRecuperado = recuperarUsuario();
            boolean resultado = false;
            if(modo == ModoFormulario.REGISTRO){

                resultado = UsuarioDAO.registrarUsuario(usuarioRecuperado);

            }else if(modo == ModoFormulario.EDICION){

                usuarioRecuperado.setIdUsuario(usuarioEdicion.getIdUsuario());

                resultado = UsuarioDAO.actualizarUsuario(usuarioRecuperado, chbModifContrasenia.isSelected());
            }
            if(resultado){
                JavaFXUtils.mostrarMensaje("Datos guardados",
                        "Se guardaron correctamente los datos del usuario", false);
            }
            ((Stage) tfNombre.getScene().getWindow()).close();
        }catch (DatosFaltantesException e){
            JavaFXUtils.mostrarAdvertencia("Datos Faltantes", e.getMessage(), false);
        }  
    }
    
    public void configurar(ModoFormulario modo, Usuario usuario) {

        this.modo = modo;
        this.usuarioEdicion = usuario;

        if (modo == ModoFormulario.EDICION && usuario != null) {
            cargarDatosUsuario(usuario);
            vbContrasenia.setVisible(false);
            chbModifContrasenia.setVisible(true);
        }
    }
    
    private void cargarDatosUsuario(Usuario usuario) {

        tfNombre.setText(usuario.getNombre());
        tfApellidoPaterno.setText(usuario.getApellidoPaterno());
        tfApellidoMaterno.setText(usuario.getApellidoMaterno());

        tfTelefono.setText(usuario.getTelefono());
        tfCorreo.setText(usuario.getEmail());

        tfCp.setText(usuario.getDireccion().getCodigoPostal());
        tfCalle.setText(usuario.getDireccion().getCalle());
        tfNumero.setText(usuario.getDireccion().getNumero());
        tfCiudad.setText(usuario.getDireccion().getCiudad());

        cbTipousuario.setValue(usuario.getTipoUsuario());

        clicCbTipoUsuario(null);

        if(usuario.getTipoUsuario() == Usuario.tipoUsuario.Empleado){

            tfUsuario.setText(usuario.getUsuario());

            cbRolEmpleado.setValue(usuario.getRolEmpleado());

            // no se recupera de la bd
            pfContrasenia.setText("");
            pfConfirmacionContrasenia.setText("");
        }
    }
    
    private Usuario recuperarUsuario() throws DatosFaltantesException{
        
        Validador.requerido(tfNombre,
                "El nombre es obligatorio");

        Validador.requerido(tfApellidoPaterno,
                "El apellido paterno es obligatorio");

        Validador.requerido(tfTelefono,
                "El teléfono es obligatorio");

        Validador.requerido(tfCorreo,
                "El correo es obligatorio");
        
        Validador.email(tfCorreo, "Ingrese una dirección de correo válida");

        Validador.requerido(tfCp,
                "El código postal es obligatorio");

        Validador.requerido(tfCalle,
                "La calle es obligatoria");

        Validador.requerido(tfNumero,
                "El número es obligatorio");

        Validador.requerido(tfCiudad,
                "La ciudad es obligatoria");

        Validador.comboRequerido(cbTipousuario,
                "Debes seleccionar un tipo de usuario");

        if(cbTipousuario.getValue() == Usuario.tipoUsuario.Empleado){

            Validador.comboRequerido(cbRolEmpleado,
                    "Debes seleccionar un rol de empleado");

            Validador.requerido(tfUsuario,
                    "El usuario es obligatorio");
            if (modo == ModoFormulario.REGISTRO
                    || chbModifContrasenia.isSelected()){
                validarCamposContrasenia();
            }
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
            if (modo == ModoFormulario.REGISTRO || this.chbModifContrasenia.isSelected()){
                usuario.setPassword(pfContrasenia.getText());
            }
            usuario.setUsuario(tfUsuario.getText().trim());
            usuario.setRolEmpleado(cbRolEmpleado.getValue());
        }
        return usuario;
    }

    private void validarCamposContrasenia() throws DatosFaltantesException {
        Validador.requerido(pfContrasenia,
                "La contraseña es obligatoria");

        Validador.requerido(pfConfirmacionContrasenia,
                "La confirmación de la contraseña es obligatoria");

        Validador.passwordSegura(
                pfContrasenia,
                "La contraseña debe tener mínimo 8 caracteres y contener mayúsculas, minúsculas, números y símbolos"
        );

        Validador.passwordsIguales(
                pfContrasenia,
                pfConfirmacionContrasenia,
                "Las contraseñas no coinciden");
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

    private void alternarVisualizacionCamposContrasenia() {
        if(chbModifContrasenia.isSelected()){
            vbContrasenia.setVisible(true);
            pfContrasenia.setDisable(false);
            pfConfirmacionContrasenia.setDisable(false);
        }else{
            vbContrasenia.setVisible(false);
            pfContrasenia.setDisable(true);
            pfConfirmacionContrasenia.setDisable(true);
        }
    }
}
