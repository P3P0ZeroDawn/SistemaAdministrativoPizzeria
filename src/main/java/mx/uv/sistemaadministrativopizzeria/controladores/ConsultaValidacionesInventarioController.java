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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.uv.sistemaadministrativopizzeria.App;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Badge;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.CeldaEstadoTabla;
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
    private TableColumn<ProductoHistorial, String> tcProducto;
    @FXML
    private TableColumn<ProductoHistorial, String> tcExistenciaSistema;
    @FXML
    private TableColumn<ProductoHistorial, String> tcExistenciaReal;
    @FXML
    private TableColumn<ProductoHistorial, ProductoHistorial.EstatusExistencia> tcEstatusExistencia;

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
    
    private void configurarTabla(){
        tcProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        tcExistenciaSistema.setCellValueFactory(new PropertyValueFactory<>("cantidadProducto"));
        tcExistenciaReal.setCellValueFactory(new PropertyValueFactory<>("cantidadReal"));
        tcEstatusExistencia.setCellValueFactory(new PropertyValueFactory<>("estatusExistencia"));
        tcEstatusExistencia.setCellFactory(param -> new CeldaEstadoTabla<>(estatus -> {
            if(estatus == null)
                return null;
            switch(estatus){
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
            App.setRoot("menuEmpleadoAdministrador");
        } catch (IOException ex) {
            System.getLogger(ConsultaUsuariosController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void btnRealizarValidacion(ActionEvent event) {
        try {
            App.setRoot("realizarValidacionInventario");
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
