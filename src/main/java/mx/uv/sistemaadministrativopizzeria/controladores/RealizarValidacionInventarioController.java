package mx.uv.sistemaadministrativopizzeria.controladores;

import java.net.URL;
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
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Badge;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.CeldaEstadoTabla;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.JavaFXUtils;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Validador;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoHistorial;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.HistorialInventarioDAO;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.ProductoDAO;

public class RealizarValidacionInventarioController implements Initializable {

    private ObservableList<ProductoHistorial> productos;

    @FXML
    private TableView<ProductoHistorial> tvProductos;
    @FXML
    private TableColumn<ProductoHistorial, ProductoHistorial> tcImagen;
    @FXML
    private TableColumn<ProductoHistorial, String> tcProducto;
    @FXML
    private TableColumn<ProductoHistorial, Double> tcSistema;
    @FXML
    private TableColumn<ProductoHistorial, String> tcUnidad;
    @FXML
    private TableColumn<ProductoHistorial, Double> tcReal;
    @FXML
    private TableColumn<ProductoHistorial, String> tcRazon;
    @FXML
    private TableColumn<ProductoHistorial, ProductoHistorial.EstatusExistencia> tcEstado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarProductos();
    }

    // ================= CONFIGURACIÓN TABLA =================
    private void configurarTabla() {

        

        // ---------------- IMAGEN ----------------
        tcImagen.setCellValueFactory(p
                -> new ReadOnlyObjectWrapper<>(p.getValue())
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
                        "-fx-background-color: white;"
                        + "-fx-background-radius: 10;"
                        + "-fx-padding: 5;"
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

                ProductoHistorial ph = item;

                if (ph.getProducto() != null) {
                    iv.setImage(ph.getProducto().getFoto());
                } else {
                    iv.setImage(null);
                }

                setGraphic(container);
            }
        });

        // ---------------- PRODUCTO ----------------
        tcProducto.setCellValueFactory(p
                -> new SimpleStringProperty(p.getValue().getNombreProducto())
        );

        // ---------------- SISTEMA ----------------
        tcSistema.setCellValueFactory(p
                -> new ReadOnlyObjectWrapper<>(p.getValue().getCantidadSistema())
        );

        // ---------------- UNIDAD ----------------
        tcUnidad.setCellValueFactory(p
                -> new SimpleStringProperty(p.getValue().getProducto().getUnidadMedida())
        );

        // ---------------- REAL (LIBRE EDICIÓN) ----------------
        
        tcReal.setCellValueFactory(p ->
                new ReadOnlyObjectWrapper<>(p.getValue().getCantidadReal())
        );
        
        tcReal.setCellFactory(col -> new TableCell<>() {

            private final TextField tf = new TextField();

            {
                Validador.permitirDecimal(tf, 10);

                tf.setAlignment(Pos.CENTER);

                tf.setMaxWidth(Double.MAX_VALUE);

                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                setGraphic(tf);

                tf.textProperty().addListener((obs, oldV, newV) -> {

                    ProductoHistorial item = getTableRow().getItem();

                    if (item == null) {
                        return;
                    }

                    if (newV == null || newV.isBlank()) {

                        item.setCantidadReal(0.0);
                        return;
                    }

                    try {

                        double valor = Double.parseDouble(newV);

                        if (valor < 0) {
                            tf.setText(oldV);
                            return;
                        }

                        item.setCantidadReal(valor);

                    } catch (NumberFormatException e) {

                        tf.setText(oldV);
                    }
                });
            }

            @Override
            protected void updateItem(Double value, boolean empty) {

                super.updateItem(value, empty);

                if (empty) {

                    setGraphic(null);

                } else {

                    if (!tf.isFocused()) {

                        tf.setText(
                                value == null || value == 0.0
                                        ? ""
                                        : String.valueOf(value)
                        );
                    }

                    setGraphic(tf);
                }
            }
        });

        // ---------------- RAZÓN ----------------
        tcRazon.setCellFactory(col -> new TableCell<>() {

            private final TextField tf = new TextField();

            {
                Validador.permitirTextoNumerico(tf, 150);

                tf.setMaxWidth(Double.MAX_VALUE);

                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                setGraphic(tf);

                tf.textProperty().addListener((obs, oldV, newV) -> {

                    ProductoHistorial item = getTableRow().getItem();

                    if (item != null) {

                        item.setRazon(newV);
                    }
                });
            }

            @Override
            protected void updateItem(String value, boolean empty) {

                super.updateItem(value, empty);

                if (empty) {

                    setGraphic(null);

                } else {

                    if (!tf.isFocused()) {

                        tf.setText(value == null ? "" : value);
                    }

                    setGraphic(tf);
                }
            }
        });

        // ---------------- ESTADO ----------------
        tcEstado.setCellValueFactory(p
                -> new ReadOnlyObjectWrapper<>(p.getValue().getEstatusExistencia())
        );

        tcEstado.setCellFactory(param -> new CeldaEstadoTabla<>(estatus -> {

            if (estatus == null) {
                return null;
            }

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

        tcSistema.setStyle("-fx-alignment:CENTER;");
        tcUnidad.setStyle("-fx-alignment:CENTER;");
        tcReal.setStyle("-fx-alignment:CENTER;");
        tcEstado.setStyle("-fx-alignment:CENTER;");
    }

    // ================= CARGA =================
    private void cargarProductos() {

        productos = FXCollections.observableArrayList();

        List<Producto> lista
                = ProductoDAO.obtenerProductosValidacionInventario();

        for (Producto p : lista) {

            ProductoHistorial ph = new ProductoHistorial();

            ph.setProducto(p);
            ph.setCantidadSistema(p.getCantidad());

            // inicio vacío
            ph.setCantidadReal(0.0);
            ph.setRazon("");
            ph.setEstatusExistencia(null);

            productos.add(ph);
        }

        tvProductos.setItems(productos);
    }

    // ================= VALIDACIÓN MANUAL =================
    @FXML
    private void clicBtnValidar(ActionEvent event) {
        validarExistencias();
    }

    private void validarExistencias() {
        for (ProductoHistorial item : productos) {
            calcularEstatus(item);
        }
        tvProductos.refresh();
    }

    // ================= LÓGICA =================
    private void calcularEstatus(ProductoHistorial item) {

        double sistema = item.getCantidadSistema();
        double real = item.getCantidadReal();

        if (real == sistema) {
            item.setEstatusExistencia(ProductoHistorial.EstatusExistencia.Correcta);
        } else if (real < sistema) {
            item.setEstatusExistencia(ProductoHistorial.EstatusExistencia.Faltante);
        } else {
            item.setEstatusExistencia(ProductoHistorial.EstatusExistencia.Sobrante);
        }
    }

    // ================= CANCELAR =================
    @FXML
    private void clicBtnCancelar(ActionEvent event) {

        if (JavaFXUtils.mostrarConfirmacion(
                "Cancelar",
                "¿Desea cancelar la validación?"
        )) {
            ((Stage) tvProductos.getScene().getWindow()).close();
        }
    }

    // ================= GUARDAR =================
    @FXML
    private void clicBtnGuardar(ActionEvent event) {
        validarExistencias();
        for (ProductoHistorial p : productos) {

            if (p.getEstatusExistencia() == null) {
                continue;
            }

            boolean diff
                    = p.getEstatusExistencia() != ProductoHistorial.EstatusExistencia.Correcta;

            boolean razonVacia
                    = p.getRazon() == null || p.getRazon().isBlank();

            if (diff && razonVacia) {

                JavaFXUtils.mostrarAdvertencia(
                        "Razón requerida",
                        "Debe ingresar una razón para " + p.getNombreProducto(),
                        false
                );
                return;
            }
        }

        boolean ok
                = HistorialInventarioDAO.guardarHistorialInventario(productos);

        if (ok) {
            JavaFXUtils.mostrarMensaje(
                    "Validación completada",
                    "Guardado correctamente",
                    false
            );

            ((Stage) tvProductos.getScene().getWindow()).close();
        } else {
            JavaFXUtils.mostrarError(
                    "Error",
                    "No se pudo guardar",
                    false
            );
        }
    }
}
