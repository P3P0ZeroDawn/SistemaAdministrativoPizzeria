package mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables;

import java.util.List;
import java.util.function.Function;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Permite mostrar:
 * - Texto principal
 * - Badges dinámicos
 * - Botones con iconos
 *
 * @author hp
 */
public class ItemTextoBoton<T extends ItemObservableList> extends ListCell<T> {

    private final HBox content;

    private final HBox badgesBox;

    private final HBox accionesBox;

    private final Label label;

    private final Function<T, List<Badge>> badgesProvider;

    @SafeVarargs
    public ItemTextoBoton(
            Function<T, List<Badge>> badgesProvider,
            BotonAccion<T>... botones
    ) {

        this.badgesProvider = badgesProvider;

        this.label = new Label();

        this.badgesBox = new HBox(10);

        this.accionesBox = new HBox(12);

        this.content = new HBox(20);

        /*
         * TAMAÑO CELDA
         */
        setMinHeight(72);

        /*
         * CONFIGURACION CONTENEDORES
         */
        content.setAlignment(Pos.CENTER_LEFT);

        content.setStyle(
                "-fx-padding: 10 15 10 15;"
        );

        badgesBox.setAlignment(Pos.CENTER);

        accionesBox.setAlignment(Pos.CENTER);

        /*
         * LABEL PRINCIPAL
         */
        HBox.setHgrow(label, Priority.ALWAYS);

        label.setMaxWidth(Double.MAX_VALUE);

        label.setAlignment(Pos.CENTER_LEFT);

        label.setStyle(
                "-fx-font-size: 18px;"
                + "-fx-font-weight: regular;"
        );

        /*
         * ESTRUCTURA
         */
        accionesBox.getChildren().add(badgesBox);

        content.getChildren().add(label);

        content.getChildren().add(accionesBox);

        /*
         * BOTONES
         */
        for (BotonAccion<T> config : botones) {

            Button btn = new Button();

            Image img = new Image(
                    getClass().getResourceAsStream(
                            config.getRutaIcono()
                    )
            );

            ImageView iv = new ImageView(img);

            /*
             * TAMAÑO ICONOS
             */
            iv.setFitWidth(24);

            iv.setFitHeight(24);

            btn.setGraphic(iv);

            /*
             * TAMAÑO BOTON
             */
            btn.setPrefSize(64, 64);

            btn.setAlignment(Pos.CENTER);

            btn.setOnAction(e -> {

                if (getItem() != null) {

                    config.getAccion().accept(getItem());
                }
            });

            accionesBox.getChildren().add(btn);
        }
    }

    @Override
    protected void updateItem(T item, boolean empty) {

        super.updateItem(item, empty);

        if (empty || item == null) {

            setText(null);

            setGraphic(null);

        } else {

            /*
             * TEXTO PRINCIPAL
             */
            label.setText(item.getString());

            /*
             * LIMPIAR BADGES
             */
            badgesBox.getChildren().clear();

            /*
             * GENERAR BADGES
             */
            if (badgesProvider != null) {

                List<Badge> badges =
                        badgesProvider.apply(item);

                if (badges != null) {

                    for (Badge badge : badges) {

                        Label lblBadge =
                                new Label(badge.getTexto());

                        lblBadge.setAlignment(Pos.CENTER);

                        lblBadge.setStyle(
                                "-fx-background-color: "
                                + badge.getColor()
                                + ";"
                                + "-fx-text-fill: black;"
                                + "-fx-padding: 6 14 6 14;"
                                + "-fx-background-radius: 10;"
                                + "-fx-font-size: 18px;"
                                + "-fx-font-weight: regular;"
                        );

                        badgesBox.getChildren()
                                .add(lblBadge);
                    }
                }
            }

            setText(null);

            setGraphic(content);
        }
    }
}