/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import mx.uv.sistemaadministrativopizzeria.App;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Badge;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.BotonAccion;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ItemTextoBoton;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.JavaFXUtils;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.ProductoDAO;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class ConsultaProductosController implements Initializable {

    private ObservableList<Producto> productos =
        FXCollections.observableArrayList();
    
    @FXML
    private TextField tfBusqueda;
    @FXML
    private CheckBox cbPorNombre;
    @FXML
    private CheckBox cbPorCodigo;
    @FXML
    private ListView<Producto> lvProductos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        lvProductos.setCellFactory(param ->
            new ItemTextoBoton<>(

                producto -> {

                    List<Badge> badges = new ArrayList<>();

                    if (producto.getEsPreparado()) {

                        badges.add(
                            new Badge(
                                "Preparado",
                                "#f59e0b"
                            )
                        );
                    }

                    if (producto.getEsInsumo()) {

                        badges.add(
                            new Badge(
                                "Insumo",
                                "#10b981"
                            )
                        );
                    }

                    return badges;
                },

                new BotonAccion<>(

                    "Editar",
                    "/imagenes/editar.png",

                    producto -> {
                        //cargarVistaEdicion(producto);
                    }
                ),

                new BotonAccion<>(

                    "Eliminar",
                    "/imagenes/eliminar.png",

                    producto -> {
                        eliminarProducto(producto);
                    }
                )
            )
        );

        cargarDatos();
    }    

    @FXML
    private void btnNuevoProducto(ActionEvent event) {
        
    }

    @FXML
    private void btnGenerarReporte(ActionEvent event) {
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
    
    private void cargarDatos() {

        List<Producto> listaProductos =
                ProductoDAO.obtenerProductos();

        productos.clear();

        productos.addAll(listaProductos);

        lvProductos.setItems(productos);
    }
    
    private void eliminarProducto(Producto producto) {

        boolean confirmado =
                JavaFXUtils.mostrarConfirmacion(
                        "Eliminar producto",
                        "¿Seguro que desea eliminar este producto?"
                );

        if (confirmado) {

            boolean resultado =
                    ProductoDAO.eliminarProducto(producto);

            if (resultado) {

                cargarDatos();

                JavaFXUtils.mostrarMensaje(
                        "Producto eliminado",
                        "El producto fue eliminado correctamente",
                        false
                );
            }
        }
}
}
