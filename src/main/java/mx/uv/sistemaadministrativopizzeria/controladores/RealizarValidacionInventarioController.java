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
import javafx.scene.control.ListView;
import mx.uv.sistemaadministrativopizzeria.App;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ItemValidacionInventario;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.JavaFXUtils;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoHistorial;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoValidacionInventario;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.HistorialInventarioDAO;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.ProductoDAO;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class RealizarValidacionInventarioController implements Initializable {

    private ObservableList<ProductoHistorial> productos;
    
    @FXML
    private ListView<ProductoHistorial> lvProductos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        productos = FXCollections.observableArrayList();

        List<Producto> listaProductos =
                ProductoDAO.obtenerProductosValidacionInventario();

        for (Producto producto : listaProductos) {

            ProductoHistorial ph =
                    new ProductoHistorial();

            ph.setProducto(producto);

            /*
             * EXISTENCIA SISTEMA
             */
            ph.setCantidadSistema(
                    producto.getCantidad()
            );

            /*
             * INICIALMENTE IGUAL
             */
            ph.setCantidadReal(
                    producto.getCantidad()
            );

            /*
             * SIN DIFERENCIAS
             */
            ph.setEstatusExistencia(
                    ProductoHistorial
                            .EstatusExistencia
                            .Correcta
            );

            ph.setRazon("");

            productos.add(ph);
        }

        lvProductos.setItems(productos);
        
        lvProductos.setCellFactory(
                param -> new ItemValidacionInventario()
        );
    }    

    @FXML
    private void btnVolver(ActionEvent event) {
        try {
            App.setRoot("consultaValidacionesInventario");
        } catch (IOException ex) {
            System.getLogger(RealizarValidacionInventarioController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {

        boolean confirmado =
                JavaFXUtils.mostrarConfirmacion(
                        "Cancelar",
                        "¿Desea cancelar la validación?"
                );

        if (confirmado) {

            try {

                App.setRoot(
                        "consultaValidacionesInventario"
                );

            } catch (IOException ex) {

                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void clicBtnGuardar(ActionEvent event) {
        
        for (ProductoHistorial p : productos) {

            boolean diferencia =
                    p.getEstatusExistencia()
                    != ProductoHistorial
                            .EstatusExistencia
                            .Correcta;

            boolean razonVacia =
                    p.getRazon() == null
                    || p.getRazon().isBlank();

            if (diferencia && razonVacia) {

                JavaFXUtils.mostrarAdvertencia(
                        "Razón requerida",
                        "Debe ingresar una razón para "
                        + p.getNombreProducto(),
                        false
                );

                return;
            }
        }

        boolean resultado =
                HistorialInventarioDAO
                        .guardarHistorialInventario(
                                productos
                        );

        if (resultado) {

            JavaFXUtils.mostrarMensaje(
                    "Validación guardada",
                    "La validación se guardó correctamente",
                    false
            );

            try {

                App.setRoot(
                        "consultaValidacionesInventario"
                );

            } catch (IOException ex) {

                ex.printStackTrace();
            }

        } else {

            JavaFXUtils.mostrarError(
                    "Error",
                    "No se pudo guardar la validación",
                    false
            );
        }
    }
    
}
