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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import mx.uv.sistemaadministrativopizzeria.App;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Badge;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.BotonAccion;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ItemTextoBoton;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ModoFormulario;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoPedido;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.PedidoDAO;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.ProductoDAO;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class DatosPedidoController implements Initializable {

    private ObservableList<Producto> productos = FXCollections.observableArrayList();
    private ObservableList<ProductoPedido> proPedidos = FXCollections.observableArrayList();
     
    private ModoFormulario modo;
    private Pedido pedido;
    
    @FXML
    private Label lbTitulo;
    @FXML
    private Label lbUsuario;
    @FXML
    private Label lbTotal;
    @FXML
    private TextField tfProducto;
    @FXML
    private ListView<Producto> lvProducto;
    @FXML
    private TextField tfPedido;
    @FXML
    private ListView<ProductoPedido> lvPedido;
    @FXML
    private Button btnSeleccionUsuario;
    @FXML
    private Label lbFecha;
    @FXML
    private Button btnConfirmacion;
    @FXML
    private Label lbFechaPedido;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarListaProductos();
        llenarProductos();
        configurarListaPedidos();
        lvPedido.setItems(proPedidos);
    }
    
    private void configurarListaProductos(){
        lvProducto.setCellFactory(param -> new ItemTextoBoton<>(
                producto -> {
                    List<Badge> badges = new ArrayList<>();

                    badges.add(new Badge("$" + producto.getPrecio(), "#E6E6E6"));
                    
                    return badges;
                },
                new BotonAccion<>(
                        "Agregar",
                        pedido -> {
                            agregarProducto(pedido, 2);
                })
        ));
    }
    
    private void configurarListaPedidos(){
        lvPedido.setCellFactory(param -> new ItemTextoBoton<>(
                pedido -> {
                    List<Badge> badges = new ArrayList<>();

                    badges.add(new Badge("X " + pedido.getCantidad(), "#E6E6E6"));
                    
                    return badges;
                },
                new BotonAccion<>(
                        "Eliminar",
                        "/imagenes/uno.png",
                        pedido -> {
                            disminuirPedido(pedido);
                })
        ));
    }
    
    private void llenarProductos(){
        List<Producto> lista = ProductoDAO.obtenerProductos();
        productos.addAll(lista);
        lvProducto.setItems(productos);
    }

    public void configurar(ModoFormulario modo, Pedido pedido){
        this.modo = modo;
        this.pedido = pedido;
        
        if(modo == ModoFormulario.EDICION && pedido != null){
            lbTitulo.setText("Edición de pedido");
            btnSeleccionUsuario.setVisible(false);
            btnConfirmacion.setText("Confirmar");
        } else{
            lbFecha.setVisible(false);
            lbFechaPedido.setVisible(false);
        }
    }
    
    private void agregarProducto(Producto producto, int cantidad){
        Boolean existe = false;
        for(ProductoPedido p: proPedidos){
            if(p.getProducto().getIdProducto() == producto.getIdProducto()){
                p.setCantidad(p.getCantidad() + cantidad);
                int index = proPedidos.indexOf(p);
                proPedidos.set(index, p);
                existe = true;
                break;
            }
        }
        if (!existe) {
            ProductoPedido proPedido = new ProductoPedido();
            proPedido.setProducto(producto);
            proPedido.setCantidad(cantidad);
            proPedidos.add(proPedido);
        }
    }
    
    private void disminuirPedido(ProductoPedido pedido){
        for(ProductoPedido p: proPedidos){
            if(p.getProducto().getIdProducto() == pedido.getProducto().getIdProducto()){
                p.setCantidad(p.getCantidad() - 1);
                int index = proPedidos.indexOf(p);
                proPedidos.set(index, p);
                break;
            }
        }
    }
    
    @FXML
    private void btnClicSeleccionUsuario(ActionEvent event) {
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        try {
            App.setRoot("consultaPedidos");
        } catch (IOException ex) {
            System.getLogger(DatosPedidoController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void clicConfirmar(ActionEvent event) {
    }
    
}
