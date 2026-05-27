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
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

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
    
    private final ImageView imageView;

    private final StackPane imageContainer;

    @SafeVarargs
    public ItemTextoBoton(
            Function<T, List<Badge>> badgesProvider,
            BotonAccion<T>... botones
    ) {

        this.badgesProvider = badgesProvider;

        this.label = new Label();
        
        this.imageView = new ImageView();

        this.imageContainer = new StackPane();

        this.badgesBox = new HBox(10);

        this.accionesBox = new HBox(12);

        this.content = new HBox(20);

        /*
         * TAMAÑO CELDA
         */
        setPrefHeight(USE_COMPUTED_SIZE);

        /*
         * CONFIGURACION CONTENEDORES
         */
        content.setAlignment(Pos.CENTER_LEFT);

        content.setStyle(
                "-fx-padding: 3 10 3 10;"
        );

        badgesBox.setAlignment(Pos.CENTER);

        accionesBox.setAlignment(Pos.CENTER);
        
        /*
         * IMAGEN
         */
        imageView.setFitWidth(54);

        imageView.setFitHeight(54);

        imageView.setPreserveRatio(true);

        Rectangle clip = new Rectangle(54, 54);

        clip.setArcWidth(12);

        clip.setArcHeight(12);

        imageView.setClip(clip);

        imageContainer.setMinSize(54, 54);

        imageContainer.setPrefSize(54, 54);

        imageContainer.setMaxSize(54, 54);

        imageContainer.getChildren().add(imageView);

        imageContainer.setStyle(
                "-fx-background-color: #F4F4F4;"
                + "-fx-background-radius: 12;"
        );

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

        content.getChildren().add(imageContainer);
        
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
            iv.setFitWidth(20);

            iv.setFitHeight(20);

            btn.setGraphic(iv);

            /*
             * TAMAÑO BOTON
             */
            btn.setPrefSize(42, 42);

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
            * IMAGEN
            */
           if (item instanceof ItemConImagen) {

                ItemConImagen itemConImagen =
                        (ItemConImagen) item;

                if (itemConImagen.getImagen() != null) {

                    imageView.setImage(
                            itemConImagen.getImagen()
                    );

                    imageContainer.setVisible(true);

                    imageContainer.setManaged(true);

                } else {

                    imageContainer.setVisible(false);

                    imageContainer.setManaged(false);
                }

            } else {

                imageContainer.setVisible(false);

                imageContainer.setManaged(false);
            }
            
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