package mx.uv.sistemaadministrativopizzeria.controladores;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import mx.uv.sistemaadministrativopizzeria.App;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Badge;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.BotonAccion;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ItemTextoBoton;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.JavaFXUtils;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Ventana;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ComponenteElaboracion;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.ComponenteElaboracionDAO;
import mx.uv.sistemaadministrativopizzeria.modelo.dao.ProductoDAO;

public class ComponentesProductoController
        implements Initializable {

    private final ObservableList<Producto>
            productosDisponibles
            = FXCollections.observableArrayList();

    private final ObservableList<ComponenteElaboracion>
            componentes
            = FXCollections.observableArrayList();

    private Producto productoPreparado;

    @FXML
    private ListView<Producto>
            lvProductosDisponibles;

    @FXML
    private ListView<ComponenteElaboracion>
            lvComponentesAgregados;

    @Override
    public void initialize(
            URL url,
            ResourceBundle rb) {

        configurarListaProductosDisponibles();
        configurarListaComponentes();

        lvProductosDisponibles.setItems(
                productosDisponibles
        );

        lvComponentesAgregados.setItems(
                componentes
        );
    }

    public void configurar(
            Producto producto) {

        this.productoPreparado = producto;

        cargarProductosDisponibles();
        cargarComponentes();
    }

    private void configurarListaProductosDisponibles() {

        lvProductosDisponibles.setCellFactory(param
                -> new ItemTextoBoton<>(
                        producto -> {

                            List<Badge> badges =
                                    new ArrayList<>();

                            if (producto.getCantidad()
                                    != null) {

                                badges.add(
                                        new Badge(
                                                producto.getCantidad()
                                                + " "
                                                + producto.getUnidadMedida(),
                                                "#dbeafe"
                                        )
                                );
                            }

                            if (producto.getEsPreparado()) {

                                badges.add(
                                        new Badge(
                                                "Preparado",
                                                "#dcfce7"
                                        )
                                );
                            }

                            return badges;
                        },
                        new BotonAccion<>(
                                "Agregar",
                                "/imagenes/agregar.png",
                                producto -> {
                                    solicitarCantidad(
                                            producto
                                    );
                                }
                        )
                )
        );
    }

    private void configurarListaComponentes() {

        lvComponentesAgregados.setCellFactory(param
                -> new ItemTextoBoton<>(
                        componente -> {

                            List<Badge> badges =
                                    new ArrayList<>();

                            badges.add(
                                    new Badge(
                                            componente.getCantidad()
                                            + " "
                                            + componente
                                                    .getProducto()
                                                    .getUnidadMedida(),
                                            "#fde68a"
                                    )
                            );

                            return badges;
                        },
                        new BotonAccion<>(
                                "Quitar",
                                "/imagenes/eliminar.png",
                                componente -> {
                                    disminuirComponente(
                                            componente
                                    );
                                }
                        )
                )
        );
    }

    private void cargarProductosDisponibles() {

        List<Producto> lista =
                ProductoDAO.obtenerProductos();

        productosDisponibles.clear();

        for (Producto p : lista) {

            /*
             * SOLO INSUMOS
             * EVITAR QUE EL PRODUCTO
             * SE AGREGUE A SI MISMO
             */
            if (p.getEsInsumo()
                    && (productoPreparado == null
                    || p.getIdProducto()
                    != productoPreparado.getIdProducto())) {

                productosDisponibles.add(p);
            }
        }
    }

    private void cargarComponentes() {

        componentes.clear();

        /*
         * PRODUCTO NUEVO
         */
        if (productoPreparado == null
                || productoPreparado.getIdProducto()
                <= 0) {

            return;
        }

        List<ComponenteElaboracion> lista =
                ComponenteElaboracionDAO
                        .obtenerPorProducto(
                                productoPreparado
                                        .getIdProducto()
                        );

        componentes.addAll(lista);
    }

    private void solicitarCantidad(
            Producto producto) {

        try {

            Ventana<CantidadProductoController>
                    ventana =
                    App.abrirVentanaEmergente(
                            "cantidadProducto",
                            "Cantidad",
                            400,
                            200,
                            true
                    );

            ventana.getController()
                    .configurar();

            ventana.getStage()
                    .showAndWait();

            Double cantidad =
                    ventana.getController()
                            .getCantidad();

            if (cantidad != null
                    && cantidad > 0) {

                agregarComponente(
                        producto,
                        cantidad
                );
            }

        } catch (Exception ex) {

            System.getLogger(
                    ComponentesProductoController
                            .class
                            .getName()
            ).log(
                    System.Logger.Level.ERROR,
                    (String) null,
                    ex
            );

            JavaFXUtils.mostrarError(
                    "Error",
                    "No se pudo obtener la cantidad",
                    false
            );
        }
    }

    private void agregarComponente(
            Producto producto,
            Double cantidad) {

        for (ComponenteElaboracion c
                : componentes) {

            if (c.getProducto()
                    .getIdProducto()
                    == producto.getIdProducto()) {

                c.setCantidad(
                        c.getCantidad()
                        + cantidad
                );

                lvComponentesAgregados
                        .refresh();

                return;
            }
        }

        componentes.add(
                new ComponenteElaboracion(
                        producto,
                        cantidad
                )
        );
    }

    private void disminuirComponente(
            ComponenteElaboracion componente) {

        componentes.remove(componente);

        lvComponentesAgregados.refresh();
    }

    @FXML
    private void clicBtnCancelar(
            ActionEvent event) {

        cerrarVentana();
    }

    @FXML
    private void clicBtnGuardar(
            ActionEvent event) {

        /*
         * VALIDAR PRODUCTO
         */
        if (productoPreparado == null
                || productoPreparado.getIdProducto()
                <= 0) {

            JavaFXUtils.mostrarError(
                    "Producto no guardado",
                    "Primero debes guardar el producto",
                    false
            );

            return;
        }

        /*
         * VALIDAR COMPONENTES
         */
        if (componentes.isEmpty()) {

            JavaFXUtils.mostrarError(
                    "Sin componentes",
                    "Debes agregar al menos un componente",
                    false
            );

            return;
        }

        /*
         * ELIMINAR ANTERIORES
         */
        boolean eliminado =
                ComponenteElaboracionDAO
                        .eliminarPorProducto(
                                productoPreparado
                                        .getIdProducto()
                        );

        if (!eliminado) {

            JavaFXUtils.mostrarError(
                    "Error",
                    "No se pudieron actualizar los componentes",
                    false
            );

            return;
        }

        /*
         * GUARDAR NUEVOS
         */
        boolean guardado =
                ComponenteElaboracionDAO
                        .guardarComponentes(
                                productoPreparado
                                        .getIdProducto(),
                                new ArrayList<>(
                                        componentes
                                )
                        );

        if (guardado) {

            JavaFXUtils.mostrarMensaje(
                    "Componentes guardados",
                    "Los componentes se guardaron correctamente",
                    false
            );

            cerrarVentana();

        } else {

            JavaFXUtils.mostrarError(
                    "Error",
                    "No se pudieron guardar los componentes",
                    false
            );
        }
    }

    private void cerrarVentana() {

        ((Stage) lvProductosDisponibles
                .getScene()
                .getWindow())
                .close();
    }
}