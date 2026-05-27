/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import mx.uv.sistemaadministrativopizzeria.App;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Badge;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.BotonAccion;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ItemTextoBoton;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ModoFormulario;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Ventana;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Usuario;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.PedidoDAO;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.UsuarioDAO;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class ConsultaPedidosController implements Initializable {

    private ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    private ObservableList<Pedido.EstatusPedido> estatus = FXCollections.observableArrayList();
    
    @FXML
    private ComboBox<Usuario> cbBusUsuario;
    @FXML
    private DatePicker dpBusFecha;
    @FXML
    private ComboBox<Pedido.EstatusPedido> cbBusEstatus;
    @FXML
    private ListView<Pedido> lvPedidos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarLista();
        llenarComboBox();
        llenarEstatus();
        dpBusFecha.setValue(LocalDate.now());
        llenarLista();
    }
    
    private void configurarLista() {
        lvPedidos.setCellFactory(param -> new ItemTextoBoton<>(
                pedido -> {
                    List<Badge> badges = new ArrayList<>();

                    if(pedido.getEstatus().equals(Pedido.EstatusPedido.Entregado)){
                        badges.add(new Badge(Pedido.EstatusPedido.Entregado.toString(), "#7FFC64"));
                    } else if(pedido.getEstatus().equals(Pedido.EstatusPedido.EnPreparacion)){
                        badges.add(new Badge(Pedido.EstatusPedido.EnPreparacion.toString(), "#FFFB29"));
                    } else{
                        badges.add(new Badge(Pedido.EstatusPedido.Cancelado.toString(), "#FF7474"));
                    }
                    
                    return badges;
                },
                new BotonAccion<>(
                        "Editar",
                        "/imagenes/editar.png",
                        pedido -> {
                            //cargarVistaEdicion((Usuario) usuario);
                }),
                new BotonAccion<>(
                        "Eliminar",
                        "/imagenes/eliminar.png",
                        pedido -> {
                            //eliminarUsuario((Usuario) usuario);
                })
        ));
    }
    
    private void llenarEstatus() {
        estatus.add(null);
        estatus.addAll(Pedido.EstatusPedido.values());
        cbBusEstatus.setItems(estatus);
        cbBusEstatus.setCellFactory(lv -> new ListCell<Pedido.EstatusPedido>() {
            @Override
            protected void updateItem(Pedido.EstatusPedido item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else if (item == null) {
                    setText("Todos");
                } else {
                    setText(item.toString());
                }
            }
        });
        cbBusEstatus.setValue(null);
    }
    
    private void llenarComboBox(){
        List<Usuario> lista = UsuarioDAO.obtenerUsuarios();
        if(lista == null) return;
        usuarios.clear();
        usuarios.add(0, new Usuario(-1, "Todos", "", ""));
        usuarios.addAll(lista);
        cbBusUsuario.setItems(usuarios);
    }

    
    private void llenarLista() {
        List<Pedido> pedidos = PedidoDAO.obtenerPedidos();

        Usuario usuario = cbBusUsuario.getValue();
        LocalDate fecha = dpBusFecha.getValue();
        Pedido.EstatusPedido estatusSeleccionado = cbBusEstatus.getValue();

        if (usuario != null && usuario.getIdUsuario() != -1) {
            pedidos.removeIf(p -> p.getIdUsuario() != usuario.getIdUsuario());
        }

        if (fecha != null) {
            pedidos.removeIf(p -> !p.getFechaPedido().equals(fecha));
        }

        if (estatusSeleccionado != null) {
            pedidos.removeIf(p -> p.getEstatus() != estatusSeleccionado);
        }

        lvPedidos.setItems(FXCollections.observableArrayList(pedidos));
    }
    
    @FXML
    private void btnRealizarPedido(ActionEvent event) {
        try {
            Ventana<DatosPedidoController> ventana = App.setRootVentana("datosPedido");
            
            ventana.getController().configurar(ModoFormulario.REGISTRO, null);
        } catch (IOException ex) {
            System.getLogger(ConsultaPedidosController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void btnExportar(ActionEvent event) {
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
    private void clicCbSeleccionUsuario(ActionEvent event) {
        llenarLista();
    }

    @FXML
    private void clicDpSeleccionFecha(ActionEvent event) {
        llenarLista();
    }

    @FXML
    private void clicCbSeleccionEstatus(ActionEvent event) {
        llenarLista();
    }
}
