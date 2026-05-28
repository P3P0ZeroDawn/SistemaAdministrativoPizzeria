package mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoHistorial;

public class ItemValidacionInventario
        extends ListCell<ProductoHistorial> {

    private final HBox contenedor;

    private final Label lblNombre;
    private final Label lblSistema;
    private final Label lblEstatus;

    private final TextField tfCantidadReal;
    private final TextField tfRazon;

    private ProductoHistorial itemActual;

    public ItemValidacionInventario() {

        /*
         * NOMBRE
         */
        lblNombre = new Label();

        lblNombre.setStyle(
                "-fx-font-size: 14px;"
                + "-fx-font-weight: bold;"
        );

        lblNombre.setPrefWidth(180);

        /*
         * EXISTENCIA SISTEMA
         */
        lblSistema = new Label();

        lblSistema.setPrefWidth(110);

        /*
         * CANTIDAD REAL
         */
        tfCantidadReal = new TextField();

        tfCantidadReal.setPromptText(
                "Real"
        );

        tfCantidadReal.setMaxWidth(90);

        tfCantidadReal.setPrefHeight(30);

        /*
         * RAZON
         */
        tfRazon = new TextField();

        tfRazon.setPromptText(
                "Razón"
        );

        tfRazon.setPrefHeight(30);

        HBox.setHgrow(
                tfRazon,
                Priority.ALWAYS
        );

        /*
         * ESTATUS
         */
        lblEstatus = new Label();

        lblEstatus.setMinWidth(90);

        lblEstatus.setAlignment(
                Pos.CENTER
        );

        /*
         * CONTENEDOR
         */
        contenedor =
                new HBox(
                        12,
                        lblNombre,
                        lblSistema,
                        tfCantidadReal,
                        tfRazon,
                        lblEstatus
                );

        contenedor.setAlignment(
                Pos.CENTER_LEFT
        );

        contenedor.setStyle(
                "-fx-padding: 8;"
                + "-fx-background-color: white;"
                + "-fx-border-color: #dcdcdc;"
                + "-fx-border-radius: 8;"
                + "-fx-background-radius: 8;"
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

        lblNombre.setText(
                item.getNombreProducto()
        );

        lblSistema.setText(
                "Sistema: "
                + item.getCantidadSistema()
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
                        "-fx-text-fill: #16a34a;"
                        + "-fx-font-weight: bold;"
                );

                break;

            case Faltante:

                lblEstatus.setStyle(
                        "-fx-text-fill: #dc2626;"
                        + "-fx-font-weight: bold;"
                );

                break;

            case Sobrante:

                lblEstatus.setStyle(
                        "-fx-text-fill: #ea580c;"
                        + "-fx-font-weight: bold;"
                );

                break;
        }
    }
}