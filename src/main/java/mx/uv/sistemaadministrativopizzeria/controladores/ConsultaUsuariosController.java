/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores;

import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ItemTextoBoton;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.BotonAccion;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import mx.uv.sistemaadministrativopizzeria.App;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Badge;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.JavaFXUtils;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ModoFormulario;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Ventana;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Usuario;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.UsuarioDAO;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class ConsultaUsuariosController implements Initializable {
    
    private ObservableList<Usuario> usuarios = FXCollections.observableArrayList();

    @FXML
    private TextField tfBusqueda;
    @FXML
    private CheckBox cbPorNombre;
    @FXML
    private CheckBox cbPorDireccion;
    @FXML
    private CheckBox cbPorTelefono;
    @FXML
    private ListView<Usuario> lvUsuarios;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lvUsuarios.setCellFactory(param -> new ItemTextoBoton<>(
                usuario -> {
                    List<Badge> badges = new ArrayList<>();
                    
                    if(usuario.getTipoUsuario().equals(Usuario.tipoUsuario.Empleado)){
                        badges.add(
                                new Badge(
                                        "Empleado",
                                        "#FFFB29"
                                )
                        );
                        if(usuario.getRolEmpleado().equals(Usuario.rolEmpleado.Administrador)){
                           badges.add(
                                new Badge(
                                        "Administrador",
                                        "#375cf050"
                                )
                            ); 
                        }else{
                            badges.add(
                                new Badge(
                                        "Cajero",
                                        "#f037bb50"
                                )
                            ); 
                        }
                    }else{
                        badges.add(
                                new Badge(
                                        "Cliente",
                                        "#7FFC6446"
                                )
                        );
                    }
                    
                    return badges;
                },
                new BotonAccion<>(
                        "Editar",
                        "/imagenes/editar.png",
                        usuario -> {
                        cargarVistaEdicion((Usuario) usuario);
                }),
                
                new BotonAccion<>(
                        "Eliminar",
                        "/imagenes/eliminar.png",
                        usuario -> {
                        eliminarUsuario((Usuario) usuario);
                })
        ));
        cargarDatos();
    }    

    @FXML
    private void btnNuevoUsuario(ActionEvent event) {
        try {
            Ventana<DatosUsuarioController> ventana =
                    App.abrirVentanaEmergente(
                            "datosUsuario",
                            "Alta de usuario",
                            800,
                            600,
                            true
                    );

            ventana.getController().configurar(
                    ModoFormulario.REGISTRO,
                    null
            );

            ventana.getStage().showAndWait();
            cargarDatos();
        } catch (IOException ex) {
            System.getLogger(ConsultaProductosController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void btnVolver(ActionEvent event) {
        try {
            App.setRoot("menuEmpleadoAdministrador");
        } catch (IOException ex) {
            System.getLogger(ConsultaUsuariosController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void btnBuscar(ActionEvent event) {
    }
    
    private void cargarDatos(){
        List<Usuario> listaUsuarios = UsuarioDAO.obtenerUsuarios();
        usuarios.clear();
        usuarios.addAll(listaUsuarios);
        lvUsuarios.setItems(usuarios);
    }
    
    private void cargarVistaEdicion(Usuario usuario){
        try {
            Ventana<DatosUsuarioController> ventana =
                    App.abrirVentanaEmergente(
                            "datosUsuario",
                            "Editar usuario",
                            800,
                            600,
                            true
                    );

            ventana.getController().configurar(
                    ModoFormulario.EDICION,
                    usuario
            );  
            ventana.getStage().showAndWait();
            cargarDatos();
        } catch (IOException ex) {
            System.getLogger(ConsultaUsuariosController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    private void eliminarUsuario(Usuario usuario){
        Usuario usuarioActivo = (Usuario) App.getMetadato("usuario");
        if(usuario.getIdUsuario() != usuarioActivo.getIdUsuario()){
            boolean confirmado = 
                JavaFXUtils.mostrarConfirmacion(
                        "Eliminación de usuario",
                        "¿Seguro que desea eliminar este usuario?"
                );
        
            if(confirmado){
                boolean resultado = UsuarioDAO.eliminarUsuario(usuario);
                if(resultado){
                    cargarDatos();
                    JavaFXUtils.mostrarMensaje("Eliminación de usuario", "Usuario eliminado correctamente", false);
                }  
            }  
        }else{
            JavaFXUtils.mostrarError("No se puede eliminar", "El usuario seleccionado es el usuario activo", false);
        }  
    }
}
