/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mx.uv.sistemaadministrativopizzeria.App;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Badge;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.CeldaEstadoTabla;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Ventana;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.HistorialInventario;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoHistorial;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.HistorialInventarioDAO;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class ConsultaValidacionesInventarioController implements Initializable {

    private ObservableList<ProductoHistorial> productosHis;
    private ObservableList<HistorialInventario> historial;
    
    @FXML
    private DatePicker dpBusFecha;
    @FXML
    private ComboBox<HistorialInventario> cbBusValidacion;
    @FXML
    private TableView<ProductoHistorial> tvValidaciones;
    @FXML 
    private TableColumn<ProductoHistorial, ProductoHistorial> tcImagen;
    @FXML
    private TableColumn<ProductoHistorial, String> tcProducto;
    @FXML
    private TableColumn<ProductoHistorial, Double> tcExistenciaSistema;
    @FXML
    private TableColumn<ProductoHistorial, Double> tcExistenciaReal;
    @FXML
    private TableColumn<ProductoHistorial, ProductoHistorial.EstatusExistencia> tcEstatusExistencia;
    @FXML
    private TableColumn<ProductoHistorial, String> tcUnidad;
    @FXML
    private TableColumn<ProductoHistorial, String> tcRazon;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarDatePicker();
        configurarTabla();
        dpBusFecha.setValue(LocalDate.now());
        llenarComboBox(dpBusFecha.getValue());
    }
    
    private void llenarComboBox(LocalDate fecha) {
        List<HistorialInventario> lista
                = HistorialInventarioDAO.obtenerHistorialesInventarioFecha(fecha);
        if (lista == null) {
            return;
        }

        historial = FXCollections.observableArrayList();

        historial.addAll(lista);
        cbBusValidacion.getItems().clear();
        cbBusValidacion.getItems().setAll(historial);

        if (lista.isEmpty()) {
            cbBusValidacion.setDisable(true);
            cbBusValidacion.setEditable(true);
            cbBusValidacion.getSelectionModel().clearSelection();
            cbBusValidacion.setValue(null);
            cbBusValidacion.setPromptText("Sin historiales");
            tvValidaciones.getItems().clear();
        } else {
            cbBusValidacion.setDisable(false);
            cbBusValidacion.setEditable(false);
            cbBusValidacion.getSelectionModel().select(0);
            llenarTabla(cbBusValidacion.getValue().getIdHistorialInventario());
        }
    }

    private void llenarTabla(int idHistorialInventario){
        List<ProductoHistorial> lista = HistorialInventarioDAO.obtenerProductosDeHistorial(idHistorialInventario);
        if(lista == null)
            return;
        productosHis = FXCollections.observableArrayList();
        
        productosHis.addAll(lista);
        tvValidaciones.setItems(productosHis);
    }
    
    private void configurarTabla() {
        // ================= IMAGEN =================
        tcImagen.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue())
        );

        tcImagen.setCellFactory(col -> new TableCell<ProductoHistorial, ProductoHistorial>() {

            private final ImageView iv = new ImageView();
            private final StackPane container = new StackPane();

            {
                iv.setFitWidth(45);
                iv.setFitHeight(45);
                iv.setPreserveRatio(true);

                container.setAlignment(Pos.CENTER);
                container.setStyle(
                        "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 5;"
                );

                container.getChildren().add(iv);
            }

            @Override
            protected void updateItem(ProductoHistorial item, boolean empty) {

                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }

                iv.setImage(
                        item.getProducto() != null
                                ? item.getProducto().getFoto()
                                : null
                );

                setGraphic(container);
            }
        });

        // ================= PRODUCTO =================
        tcProducto.setCellValueFactory(p ->
                new SimpleStringProperty(p.getValue().getNombreProducto())
        );

        // ================= SISTEMA =================
        tcExistenciaSistema.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getCantidadSistema())
        );

        // ================= REAL =================
        tcExistenciaReal.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getCantidadReal())
        );

        // ================= UNIDAD =================
        tcUnidad.setCellValueFactory(p ->
                new SimpleStringProperty(
                        p.getValue().getProducto() != null
                                ? p.getValue().getProducto().getUnidadMedida()
                                : ""
                )
        );

        // ================= RAZÓN =================
        tcRazon.setCellValueFactory(p ->
                new SimpleStringProperty(
                        p.getValue().getRazon() == null
                                ? ""
                                : p.getValue().getRazon()
                )
        );

        // ================= ESTADO =================
        tcEstatusExistencia.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getEstatusExistencia())
        );

        tcEstatusExistencia.setCellFactory(param -> new CeldaEstadoTabla<>(estatus -> {

            if (estatus == null) return null;

            switch (estatus) {
                case Correcta:
                    return new Badge("Correcta", "#7FFC6475");
                case Faltante:
                    return new Badge("Faltante", "#FF7474CC");
                case Sobrante:
                    return new Badge("Sobrante", "#FF8D2899");
                default:
                    return new Badge(estatus.toString(), "transparent");
            }
        }));

        // ================= ESTILO =================
        tcProducto.setStyle("-fx-alignment: CENTER-LEFT;");
        tcExistenciaSistema.setStyle("-fx-alignment: CENTER;");
        tcExistenciaReal.setStyle("-fx-alignment: CENTER;");
        tcUnidad.setStyle("-fx-alignment: CENTER;");
        tcRazon.setStyle("-fx-alignment: CENTER-LEFT;");
        tcEstatusExistencia.setStyle("-fx-alignment: CENTER;");
    }
    
    public void configurarDatePicker() {
        dpBusFecha.setEditable(false);
        dpBusFecha.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date != null && date.isAfter(LocalDate.now())) {
                    setDisable(true);

                }
            }
        });
    }

    @FXML
    private void btnVolver(ActionEvent event) {
        try {
            App.configurarVentana(
                ((Stage) dpBusFecha.getScene().getWindow()),
                "Sistema Administrativo Pizzeria Italia Pizza",
                700, 300,
                815, 650,
                900, 700,
                false
            );
            ((Stage) dpBusFecha.getScene().getWindow()).centerOnScreen();
            App.setRoot("menuEmpleado");
        } catch (IOException ex) {
            System.getLogger(ConsultaUsuariosController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void btnRealizarValidacion(ActionEvent event) {
        try {
            Ventana<RealizarValidacionInventarioController> ventana =
                    App.abrirVentanaEmergente(
                            "realizarValidacionInventario",
                            "Validación de inventario",
                            1200,
                            600,
                            true
                    );
            
            ventana.getStage().showAndWait();
            llenarComboBox(dpBusFecha.getValue());
        } catch (IOException ex) {
            System.getLogger(ConsultaValidacionesInventarioController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void clicDpSeleccionFecha(ActionEvent event) {
        LocalDate fecha = dpBusFecha.getValue();
        if(fecha != null){
            llenarComboBox(fecha);
        }
    }

    @FXML
    private void clicCbSeleccionValidacion(ActionEvent event) {
        HistorialInventario historial = cbBusValidacion.getValue();
        if(historial != null){
            llenarTabla(historial.getIdHistorialInventario());
        }
    }
    
}
