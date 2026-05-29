package mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoHistorial;

public class ItemValidacionInventario
        extends ListCell<ProductoHistorial> {

    private final HBox contenedor;

    /*
     * IMAGEN
     */
    private final ImageView imageView;

    private final StackPane imageContainer;

    /*
     * COLUMNAS
     */
    private final Label lblNombre;

    private final Label lblSistema;

    private final Label lblUnidad;

    private final Label lblEstatus;

    private final TextField tfCantidadReal;

    private final TextField tfRazon;

    private ProductoHistorial itemActual;

    public ItemValidacionInventario() {

        /*
         * IMAGEN
         */
        imageView = new ImageView();

        imageContainer = new StackPane();

        imageView.setFitWidth(52);

        imageView.setFitHeight(52);

        imageView.setPreserveRatio(true);

        Rectangle clip = new Rectangle(52, 52);

        clip.setArcWidth(12);

        clip.setArcHeight(12);

        imageView.setClip(clip);

        imageContainer.setMinSize(52, 52);

        imageContainer.setPrefSize(52, 52);

        imageContainer.setMaxSize(52, 52);

        imageContainer.getChildren().add(imageView);

        imageContainer.setStyle(
                "-fx-background-color: #F4F4F4;"
                + "-fx-background-radius: 12;"
                + "-fx-border-color: #E5E5E5;"
                + "-fx-border-radius: 12;"
        );

        /*
         * NOMBRE
         */
        lblNombre = new Label();

        lblNombre.setStyle(
                "-fx-font-size: 14px;"
                + "-fx-font-weight: bold;"
        );

        lblNombre.setPrefWidth(180);

        lblNombre.setMinWidth(180);

        /*
         * SISTEMA
         */
        lblSistema = new Label();

        lblSistema.setPrefWidth(90);

        lblSistema.setMinWidth(90);

        /*
         * UNIDAD
         */
        lblUnidad = new Label();

        lblUnidad.setPrefWidth(80);

        lblUnidad.setMinWidth(80);

        /*
         * CANTIDAD REAL
         */
        tfCantidadReal = new TextField();

        tfCantidadReal.setPromptText("Real");

        tfCantidadReal.setPrefWidth(90);

        tfCantidadReal.setMinWidth(90);

        tfCantidadReal.setMaxWidth(90);

        tfCantidadReal.setPrefHeight(32);

        /*
         * RAZON
         */
        tfRazon = new TextField();

        tfRazon.setPromptText("Razón");

        tfRazon.setPrefHeight(32);

        HBox.setHgrow(
                tfRazon,
                Priority.ALWAYS
        );

        /*
         * ESTATUS
         */
        lblEstatus = new Label();

        lblEstatus.setPrefWidth(100);

        lblEstatus.setMinWidth(100);

        lblEstatus.setAlignment(Pos.CENTER);

        /*
         * CONTENEDOR
         */
        contenedor =
                new HBox(
                        12,
                        imageContainer,
                        lblNombre,
                        lblSistema,
                        lblUnidad,
                        tfCantidadReal,
                        tfRazon,
                        lblEstatus
                );

        contenedor.setAlignment(Pos.CENTER_LEFT);

        contenedor.setPadding(
                new Insets(8)
        );

        contenedor.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #DDDDDD;"
                + "-fx-border-radius: 10;"
                + "-fx-background-radius: 10;"
        );

        /*
         * LISTENER CANTIDAD
         */
        tfCantidadReal.textProperty()
                .addListener((obs, oldV, newV) -> {

            if (itemActual == null) {
                return;
            }

            try {

                double cantidadReal =
                        Double.parseDouble(newV);

                itemActual.setCantidadReal(
                        cantidadReal
                );

                calcularEstatus(itemActual);

                actualizarEstado(itemActual);

            } catch (NumberFormatException ex) {

            }
        });

        /*
         * LISTENER RAZON
         */
        tfRazon.textProperty()
                .addListener((obs, oldV, newV) -> {

            if (itemActual != null) {

                itemActual.setRazon(newV);
            }
        });
    }

    @Override
    protected void updateItem(
            ProductoHistorial item,
            boolean empty) {

        super.updateItem(item, empty);

        if (empty || item == null) {

            itemActual = null;

            setGraphic(null);

            return;
        }

        itemActual = item;

        /*
         * IMAGEN
         * SI NO HAY FOTO, EL CONTENEDOR
         * SIGUE OCUPANDO EL MISMO ESPACIO
         */
        if (item.getProducto() != null
                && item.getProducto().getFoto() != null) {

            imageView.setImage(
                    item.getProducto().getFoto()
            );

        } else {

            imageView.setImage(null);
        }

        /*
         * DATOS
         */
        lblNombre.setText(
                item.getNombreProducto()
        );

        lblSistema.setText(
                String.valueOf(
                        item.getCantidadSistema()
                )
        );

        lblUnidad.setText(
                item.getProducto()
                        .getUnidadMedida()
        );

        tfCantidadReal.setText(
                String.valueOf(
                        item.getCantidadReal()
                )
        );

        tfRazon.setText(
                item.getRazon() != null
                        ? item.getRazon()
                        : ""
        );

        actualizarEstado(item);

        setGraphic(contenedor);
    }

    private void calcularEstatus(
            ProductoHistorial item) {

        double sistema =
                item.getCantidadSistema();

        double real =
                item.getCantidadReal();

        if (real == sistema) {

            item.setEstatusExistencia(
                    ProductoHistorial
                            .EstatusExistencia
                            .Correcta
            );

        } else if (real < sistema) {

            item.setEstatusExistencia(
                    ProductoHistorial
                            .EstatusExistencia
                            .Faltante
            );

        } else {

            item.setEstatusExistencia(
                    ProductoHistorial
                            .EstatusExistencia
                            .Sobrante
            );
        }
    }

    private void actualizarEstado(
            ProductoHistorial item) {

        lblEstatus.setText(
                item.getEstatusExistencia()
                        .name()
        );

        switch (item.getEstatusExistencia()) {

            case Correcta:

                lblEstatus.setStyle(
                        "-fx-background-color: #DCFCE7;"
                        + "-fx-text-fill: #166534;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 6 10 6 10;"
                        + "-fx-background-radius: 8;"
                );

                break;

            case Faltante:

                lblEstatus.setStyle(
                        "-fx-background-color: #FEE2E2;"
                        + "-fx-text-fill: #991B1B;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 6 10 6 10;"
                        + "-fx-background-radius: 8;"
                );

                break;

            case Sobrante:

                lblEstatus.setStyle(
                        "-fx-background-color: #FFEDD5;"
                        + "-fx-text-fill: #9A3412;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 6 10 6 10;"
                        + "-fx-background-radius: 8;"
                );

                break;
        }
    }
    
    
}