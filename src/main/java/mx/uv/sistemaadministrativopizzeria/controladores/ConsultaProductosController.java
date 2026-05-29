/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
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
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Exportador;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ItemTextoBoton;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.JavaFXUtils;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ModoFormulario;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Ventana;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.ProductoDAO;
import java.awt.Desktop;
import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
                        cargarVistaEdicion(producto);
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
        try{
            Ventana<DatosProductoController> ventana =
                    App.abrirVentanaEmergente(
                            "datosProducto",
                            "Registro de producto",
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
        } catch (IOException ex){
            System.getLogger(ConsultaProductosController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void btnGenerarReporte(ActionEvent event) {

        List<Producto> listaProductos =
                ProductoDAO.obtenerProductos();

        if (listaProductos.isEmpty()) {

            JavaFXUtils.mostrarAdvertencia(
                    "Sin datos",
                    "No hay productos para exportar",
                    false
            );

            return;
        }

        try {

            Ventana<SeleccionReporteController>
                    ventana =
                    App.abrirVentanaEmergente(
                            "seleccionReporte",
                            "Exportar reporte",
                            350,
                            140,
                            true
                    );

            ventana.getStage()
                    .showAndWait();

            String formato =
                    ventana.getController()
                            .getFormatoSeleccionado();

            if (formato == null) {
                return;
            }

            FileChooser selector =
                    new FileChooser();

            Stage stageActual =
                    (Stage) lvProductos
                            .getScene()
                            .getWindow();

            File archivo;

            /*
             * PDF
             */
            if (formato.equals("PDF")) {

                selector.setTitle(
                        "Exportar inventario PDF"
                );

                selector.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter(
                                "Archivos PDF",
                                "*.pdf"
                        )
                );

                selector.setInitialFileName(
                        "InventarioProductos.pdf"
                );

                archivo =
                        selector.showSaveDialog(
                                stageActual
                        );

                if (archivo != null) {

                    Exportador.exportarInventarioPDF(
                            archivo.getAbsolutePath(),
                            listaProductos
                    );

                    if (archivo.exists()) {

                        Desktop.getDesktop()
                                .open(archivo);
                    }

                    JavaFXUtils.mostrarMensaje(
                            "Reporte generado",
                            "El PDF fue generado correctamente",
                            false
                    );
                }

            } else {

                /*
                 * CSV
                 */
                selector.setTitle(
                        "Exportar inventario CSV"
                );

                selector.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter(
                                "Archivos CSV",
                                "*.csv"
                        )
                );

                selector.setInitialFileName(
                        "InventarioProductos.csv"
                );

                archivo =
                        selector.showSaveDialog(
                                stageActual
                        );

                if (archivo != null) {

                    Exportador.exportarInventarioCSV(
                            archivo.getAbsolutePath(),
                            listaProductos
                    );

                    if (archivo.exists()) {

                        Desktop.getDesktop()
                                .open(archivo);
                    }

                    JavaFXUtils.mostrarMensaje(
                            "Reporte generado",
                            "El CSV fue generado correctamente",
                            false
                    );
                }
            }

        } catch (Exception ex) {

            System.getLogger(
                    ConsultaProductosController.class.getName()
            ).log(
                    System.Logger.Level.ERROR,
                    (String) null,
                    ex
            );

            JavaFXUtils.mostrarError(
                    "Error",
                    "No se pudo generar el reporte",
                    false
            );
        }
    }

    @FXML
    private void btnVolver(ActionEvent event) {
        try {
            App.setRoot("menuEmpleado");
        } catch (IOException ex) {
            System.getLogger(ConsultaUsuariosController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void btnBuscar(ActionEvent event) {
        String busqueda = tfBusqueda.getText().trim();
        
        boolean porNombre = cbPorNombre.isSelected();
        boolean porCodigo = cbPorCodigo.isSelected();
        
        List<Producto> resultado =
                ProductoDAO.buscarProductos(
                        busqueda,
                        porNombre,
                        porCodigo
                );
        
        productos.clear();
        productos.addAll(resultado);
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
                        try {
                                boolean resultado = ProductoDAO.eliminarProducto(producto);
                                if (resultado) {
                                        cargarDatos();
                                        JavaFXUtils.mostrarMensaje("Producto eliminado", "El producto fue eliminado correctamente", false);
                                }
                        } catch (mx.uv.sistemaadministrativopizzeria.excepciones.ProductoUsadoEnPedidoException ex) {
                                JavaFXUtils.mostrarError("Imposible eliminar", ex.getMessage(), false);
                        }
                }
    }
    
    private void cargarVistaEdicion(Producto producto){
        try{
            Ventana<DatosProductoController> ventana = App.abrirVentanaEmergente(
                    "datosProducto",
                    "Editar producto",
                    800,
                    600,
                    true
            );
            
            ventana.getController().configurar(
                    ModoFormulario.EDICION,
                    producto
            );
            ventana.getStage().showAndWait();
            cargarDatos();
        } catch (IOException ex) {
           System.getLogger(ConsultaUsuariosController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex); 
        }
    }
}
