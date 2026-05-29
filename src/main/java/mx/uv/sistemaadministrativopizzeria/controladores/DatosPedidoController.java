/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Ventana;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoPedido;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.PedidoDAO;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.ProductoDAO;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.ProductoPedidoDAO;

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
                        "/imagenes/agregar.png",
                        producto -> {
                            seleccionarCantidad(producto);
                })
        ));
    }
    
    private void configurarListaPedidos(){
        lvPedido.setCellFactory(param -> new ItemTextoBoton<>(
                2, //posicion en el que se insertar el Badge
                pedido -> {
                    List<Badge> badges = new ArrayList<>();

                    badges.add(new Badge("" + pedido.getCantidad(), "#E6E6E6"));
                    
                    return badges;
                },
                new BotonAccion<>(
                        "Eliminar",
                        "/imagenes/restar.png",
                        pedido -> {
                            disminuirPedido(pedido);
                }),
                new BotonAccion<>(
                        "Aumentar",
                        "/imagenes/mas.png",
                        pedido -> {
                            aumentarPedido(pedido);
                })
        ));
    }
    
    private void llenarProductos(){
        List<Producto> lista = ProductoDAO.obtenerProdParaPedido();
        for(Producto p: lista){
            if(p.getEsPreparado()){
                p = ProductoDAO.obtenerProductosProducto(p);
            }
        }
        productos.addAll(lista);
        lvProducto.setItems(productos);
    }

    private void llenarPedido(){
        try {
            if(pedido == null) throw new NullPointerException("No existe ningun pedido");
            pedido = ProductoPedidoDAO.obtenerProPedidos(pedido);
            for(ProductoPedido p: pedido.getProductos()){
                p.setProducto(ProductoDAO.obtenerProducto(p.getProducto().getIdProducto()));
                if(p.getProducto().getEsPreparado()){
                    p.setProducto(ProductoDAO.obtenerProductosProducto(p.getProducto()));
                }
            }
            proPedidos.addAll(pedido.getProductos());
        } catch (SQLException | NullPointerException ex){
            System.getLogger(DatosPedidoController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public void configurar(ModoFormulario modo, Pedido pedido){
        this.modo = modo;
        this.pedido = pedido;
        
        if(modo == ModoFormulario.EDICION && pedido != null){
            lbTitulo.setText("Edición de pedido");
            btnSeleccionUsuario.setVisible(false);
            btnConfirmacion.setText("Confirmar");
            llenarPedido();
            lbUsuario.setText(pedido.getNombreUsuario());
            lbFechaPedido.setText("" + pedido.getFechaPedido());
            lbTotal.setText("" + pedido.getTotalAPagar());
        } else{
            this.pedido = new Pedido();
            lbFecha.setVisible(false);
            lbFechaPedido.setVisible(false);
        }
    }
    
    private void seleccionarCantidad(Producto producto){
        try {
            Ventana<CantidadProductoController> ventana = App.abrirVentanaEmergente( "cantidadProducto",
                    "Cantidad", 400, 200, true);
            
            ventana.getController().configurar();
            ventana.getStage().showAndWait();

            Number num = ventana.getController().getCantidad();
            if (num != null) {
                Integer cantidad = num.intValue(); // Lo recuperas como Integer
                if (cantidad > 0) {
                    agregarProducto(producto, cantidad);
                }
            }
        } catch (IOException ex) {
            System.getLogger(DatosPedidoController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
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
        pedido.setTotalAPagar(pedido.getTotalAPagar() + (producto.getPrecio() * cantidad));
        lbTotal.setText("" + pedido.getTotalAPagar());
    }
    
    private void aumentarPedido(ProductoPedido pPedido){
        for(ProductoPedido p: proPedidos){
            if(p.getProducto().getIdProducto() == pPedido.getProducto().getIdProducto()){
                p.setCantidad(p.getCantidad() + 1);
                int index = proPedidos.indexOf(p);
                proPedidos.set(index, p);
                this.pedido.setTotalAPagar(this.pedido.getTotalAPagar() 
                        + p.getProducto().getPrecio());
                lbTotal.setText("" + this.pedido.getTotalAPagar());
                break;
            }
        }
    }
    
    private void disminuirPedido(ProductoPedido pPedido){
        for(ProductoPedido p: proPedidos){
            if(p.getProducto().getIdProducto() == pPedido.getProducto().getIdProducto()){
                p.setCantidad(p.getCantidad() - 1);
                int index = proPedidos.indexOf(p);
                if(p.getCantidad() > 0){
                    proPedidos.set(index, p);
                } else {
                    proPedidos.remove(index);
                }
                this.pedido.setTotalAPagar(this.pedido.getTotalAPagar() 
                        - p.getProducto().getPrecio());
                lbTotal.setText("" + this.pedido.getTotalAPagar());
                break;
            }
        }
    }
    
    @FXML
    private void btnClicSeleccionUsuario(ActionEvent event) {
        try {
            Ventana<SeleccionUsuarioController> ventana = App.abrirVentanaEmergente("seleccionUsuario",
                    "Selección de usuario", 400, 200, true);
            if(pedido == null) pedido = new Pedido();
            ventana.getController().setPedido(pedido);
            ventana.getStage().showAndWait();
            
            if(pedido.getIdUsuario() != -1 && pedido.getIdUsuario() > 0){
                lbUsuario.setText(pedido.getNombreUsuario());
            }
        } catch (IOException ex) {
            System.getLogger(DatosPedidoController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana(){
        try {
            App.setRoot("consultaPedidos");
        } catch (IOException ex) {
            System.getLogger(DatosPedidoController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void clicConfirmar(ActionEvent event) {
        if(pedido != null && !proPedidos.isEmpty() && pedido.getIdUsuario() != -1 && pedido.getIdUsuario() > 0){
            if(modo.equals(ModoFormulario.REGISTRO)){
                nuevoPedido();
            } else {
                edicionPedido();
            }
                   
        } else{
            System.out.println("Faltan datos");
        }
    }
    
    private void nuevoPedido() {
        pedido.setFechaPedido(LocalDate.now());

        pedido.setProductos(new ArrayList<>(proPedidos));
        int resultado = PedidoDAO.realizarPedido(pedido);
        if (resultado != 0) {
            System.out.println("EXITOO-----");
            cerrarVentana();
        } else {
            System.out.println("Fallo");
        }
    }
    
    private void edicionPedido() {
        for (ProductoPedido pp : proPedidos) {
            Producto prod = pp.getProducto();
            if (prod.getEsPreparado() && (prod.getComponentes() == null || prod.getComponentes().isEmpty())) {
                pp.setProducto(ProductoDAO.obtenerProductosProducto(prod));
            }
        }

        pedido.setProductos(new ArrayList<>(proPedidos));

        int resultado = PedidoDAO.actualizarPedido(pedido);
        if (resultado != 0) {
            System.out.println("EDICIÓN EXITOSA-----");
            cerrarVentana();
        } else {
            System.out.println("Fallo al editar el pedido");
        }
    }
    
    private void actualizarTotal(){
        
    }
}
