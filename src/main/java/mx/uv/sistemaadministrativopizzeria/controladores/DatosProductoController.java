/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.JavaFXUtils;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ModoFormulario;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Validador;
import mx.uv.sistemaadministrativopizzeria.excepciones.DatosFaltantesException;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.ProductoDAO;
import java.nio.file.Files;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class DatosProductoController implements Initializable {

    private File archivofoto;
    
    private ModoFormulario modo;
    private Producto productoEdicion;
    
    @FXML
    private ImageView ivFoto;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfCodigo;
    @FXML
    private TextField tfDescripcion;
    @FXML
    private TextField tfPrecio;
    @FXML
    private TextField tfCantidad;
    @FXML
    private TextField tfUnidadMedida;
    @FXML
    private Button btnModificarComponentes;
    @FXML
    private CheckBox chbInsumo;
    @FXML
    private CheckBox chbPreparado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chbPreparado.setOnAction(e -> configurarTipoProducto());

        chbInsumo.setOnAction(e -> configurarTipoProducto());
    }    

    @FXML
    private void clicBtnSeleccionarImagen(ActionEvent event) {
        abrirSeleccionadorFoto();
    }

    
    public void configurar(ModoFormulario modo, Producto producto) {

        this.modo = modo;
        this.productoEdicion = producto;

        if (modo == ModoFormulario.EDICION && producto != null) {
            cargarDatosProducto(producto);
            
            tfCodigo.setDisable(true);
        }
    }
    
    private Producto recuperarProducto()
        throws DatosFaltantesException {

        Validador.requerido(
                tfNombre,
                "Ingrese el nombre del producto"
        );

        Validador.requerido(
                tfCodigo,
                "Ingrese el código del producto"
        );

        /*
         * Insumos no manejan precio
         */
        
        if(!chbInsumo.isSelected()){
            Validador.requerido(
                    tfPrecio,
                    "Ingrese el precio"
            );
        }
        
        /*
         * SOLO preparados NO manejan cantidad
         */
        if (!chbPreparado.isSelected()) {

            Validador.requerido(
                    tfCantidad,
                    "Ingrese la cantidad"
            );

            Validador.requerido(
                    tfUnidadMedida,
                    "Ingrese la unidad de medida"
            );
        }

        Producto producto = new Producto();

        producto.setNombre(
                tfNombre.getText().trim()
        );

        producto.setCodigo(
                tfCodigo.getText().trim()
        );

        producto.setDescripcion(
                tfDescripcion.getText().trim()
        );

        if(!chbInsumo.isSelected()){
            producto.setPrecio(
                    Double.valueOf(
                            tfPrecio.getText()
                    )
            );
        }
     
        /*
         * PREPARADOS NO MANEJAN STOCK
         */
        if (chbPreparado.isSelected()) {

            producto.setCantidad(null);

            producto.setUnidadMedida(null);

        } else {

            producto.setCantidad(
                    Double.valueOf(
                            tfCantidad.getText()
                    )
            );

            producto.setUnidadMedida(
                    tfUnidadMedida.getText().trim()
            );
        }

        producto.setEsInsumo(
                chbInsumo.isSelected()
        );

        producto.setEsPreparado(
                chbPreparado.isSelected()
        );

        producto.setActivo(true);

        producto.setFoto(
                ivFoto.getImage()
        );

        return producto;
    }
    
    private void abrirSeleccionadorFoto(){
        FileChooser dialogoSeleccion = new FileChooser();
        dialogoSeleccion.setTitle("Selecciona la foto del alumno(a)");
        String descripcionFormato = "Archivos de imagen (*.png, *.jpg, *.jpeg)";
        List<String> formatos = new ArrayList();
        formatos.add("*.png");
        formatos.add("*.jpg");
        formatos.add("*.jpeg");
        FileChooser.ExtensionFilter filtroSeleccion =
                new FileChooser.ExtensionFilter(descripcionFormato, formatos);
        dialogoSeleccion.getExtensionFilters().add(filtroSeleccion);
        archivofoto = dialogoSeleccion.showOpenDialog(tfNombre.getScene().getWindow());
        if(archivofoto!=null){
            mostrarImagen(archivofoto);
        }
    }
    
    private void mostrarImagen(File foto){
        try{
            java.awt.image.BufferedImage buffer = javax.imageio.ImageIO.read(foto);
            Image image = SwingFXUtils.toFXImage(buffer, null);
            ivFoto.setImage(image);
        }catch (IOException e){
            JavaFXUtils.mostrarError("Error al cargar",
                    "Lo sentimos, no se puede visualizar la foto del producto seleccionado",
                    false);
        }catch(NullPointerException n){
            JavaFXUtils.mostrarError("Error al cargar",
                    "Debes seleccionar una imagen...",
                    false);
        }
    }
    
    private void configurarTipoProducto() {

        if (chbPreparado.isSelected()) {

            chbInsumo.setSelected(false);
        }

        if (chbInsumo.isSelected()) {

            chbPreparado.setSelected(false);
        }

        boolean esPreparado =
                chbPreparado.isSelected();

        tfCantidad.setDisable(esPreparado);

        tfUnidadMedida.setDisable(esPreparado);

        /*
         * INSUMOS NO MANEJAN PRECIO
         */
        tfPrecio.setDisable(
                chbInsumo.isSelected()
        );

        btnModificarComponentes.setVisible(esPreparado);
    }
    
    private void cargarDatosProducto(Producto producto) {

        tfNombre.setText(
                producto.getNombre()
        );

        tfCodigo.setText(
                producto.getCodigo()
        );

        tfDescripcion.setText(
                producto.getDescripcion()
        );

        if (producto.getPrecio() != null) {

            tfPrecio.setText(
                    String.valueOf(
                            producto.getPrecio()
                    )
            );
        }

        if (producto.getCantidad() != null) {

            tfCantidad.setText(
                    String.valueOf(
                            producto.getCantidad()
                    )
            );
        }

        if (producto.getUnidadMedida() != null) {

            tfUnidadMedida.setText(
                    producto.getUnidadMedida()
            );
        }

        chbInsumo.setSelected(
                producto.getEsInsumo()
        );

        chbPreparado.setSelected(
                producto.getEsPreparado()
        );

        configurarTipoProducto();

        if (producto.getFoto() != null) {

            ivFoto.setImage(
                    producto.getFoto()
            );
        }
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        ((Stage) tfNombre.getScene().getWindow()).close();
    }

    @FXML
    private void clicBtnGuardar(ActionEvent event) {

        try {

            Producto productoRecuperado = recuperarProducto();
            boolean resultado = false;
            if (modo == ModoFormulario.REGISTRO) {

                resultado =
                        ProductoDAO.registrarProducto(
                                productoRecuperado,
                                recuperarFotoBytes()
                        );

            } else {

                productoRecuperado.setIdProducto(
                        productoEdicion.getIdProducto()
                );

                resultado =
                        ProductoDAO.actualizarProducto(
                                productoRecuperado,
                                recuperarFotoBytes(),
                                archivofoto != null
                        );
            }

            if (resultado) {

                JavaFXUtils.mostrarMensaje(
                        "Producto guardado",
                        "El producto se guardó correctamente",
                        false
                );

                ((Stage) tfNombre.getScene().getWindow()).close();

            } else {

                JavaFXUtils.mostrarError(
                        "Error",
                        "No se pudo guardar el producto",
                        false
                );
            }

        } catch (DatosFaltantesException ex) {

            JavaFXUtils.mostrarAdvertencia(
                    "Datos faltantes",
                    ex.getMessage(),
                    false
            );

        } catch (NumberFormatException ex) {

            JavaFXUtils.mostrarError(
                    "Formato inválido",
                    "Precio o cantidad inválidos",
                    false
            );
        }
    }

    @FXML
    private void clicBtnModificarComponentes(ActionEvent event) {
    }
    
    private byte[] recuperarFotoBytes() {

        try {

            if (archivofoto != null) {

                return Files.readAllBytes(
                        archivofoto.toPath()
                );
            }

        } catch (IOException ex) {

            System.getLogger(DatosProductoController.class.getName())
                    .log(System.Logger.Level.ERROR,
                            (String) null,
                            ex);
        }

        return null;
    }
}
