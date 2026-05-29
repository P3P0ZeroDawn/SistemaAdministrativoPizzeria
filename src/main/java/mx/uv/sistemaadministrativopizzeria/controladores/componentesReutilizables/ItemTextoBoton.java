package mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables;

import java.util.List;
import java.util.function.Function;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;

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

        configurarItemTextoBoton();

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

            if (config.getTexto() != null && config.getIcono()== null) {
                btn.setText(config.getTexto());
            }

            if (config.getIcono()!= null) {
                agregarIcono(config, btn);
            }

            /*
             * TAMAÑO BOTON
             */
            if (config.getTexto() != null
                    && !config.getTexto().isBlank()) {

                btn.setPrefHeight(42);
                btn.setMinHeight(42);
                btn.setMaxHeight(42);

            } else {

                btn.setPrefSize(42, 42);
                btn.setMinSize(42, 42);
                btn.setMaxSize(42, 42);
            }

            btn.setAlignment(Pos.CENTER);

            btn.setOnAction(e -> {

                if (getItem() != null) {

                    config.getAccion().accept(getItem());
                }
            });

            accionesBox.getChildren().add(btn);
        }
    }

    @SafeVarargs
    public ItemTextoBoton(
            int posicion,
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

        configurarItemTextoBoton();

        content.getChildren().add(imageContainer);

        content.getChildren().add(label);

        content.getChildren().add(accionesBox);

        /*
         * BADGES
         */
        if (posicion == 1) {
            accionesBox.getChildren().add(badgesBox);
        }

        /*
         * BOTONES
         */
        int i;

        for (i = 0; i < botones.length; i++) {

            BotonAccion<T> config = botones[i];

            Button btn = new Button();

            if (config.getTexto() != null
                    && config.getIcono()== null) {

                btn.setText(config.getTexto());
            }

            if (config.getIcono()!= null) {
                agregarIcono(config, btn);
            }

            if (config.getTexto() != null
                    && !config.getTexto().isBlank()) {

                btn.setPrefHeight(42);
                btn.setMinHeight(42);
                btn.setMaxHeight(42);

            } else {

                btn.setPrefSize(42, 42);
                btn.setMinSize(42, 42);
                btn.setMaxSize(42, 42);
            }

            btn.setAlignment(Pos.CENTER);

            btn.setOnAction(e -> {

                if (getItem() != null) {

                    config.getAccion().accept(getItem());
                }
            });

            accionesBox.getChildren().add(btn);

            /*
             * INSERTAR BADGES EN POSICIÓN
             */
            if (posicion == (i + 2)) {

                accionesBox.getChildren().add(badgesBox);
            }
        }
    }

    private void configurarItemTextoBoton() {

        /*
         * TAMAÑO CELDA
         */
        setMinHeight(80);

        setPrefHeight(80);

        setMaxHeight(80);

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        /*
         * CONTENEDOR PRINCIPAL
         */
        content.setAlignment(Pos.CENTER_LEFT);

        content.setFillHeight(false);

        content.setStyle(
                "-fx-padding: 3 10 3 10;"
        );

        /*
         * BADGES
         */
        badgesBox.setAlignment(Pos.CENTER_LEFT);

        badgesBox.setMinHeight(30);

        badgesBox.setPrefHeight(30);

        badgesBox.setMaxHeight(30);

        /*
         * ACCIONES
         */
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
    }

    private void agregarIcono(BotonAccion<T> config, Button btn) {

        FontIcon icon = new FontIcon(config.getIcono());

        icon.setIconSize(20);

        btn.setGraphic(icon);
    }

    @Override
    protected void updateItem(T item, boolean empty) {

        super.updateItem(item, empty);

        if (empty || item == null) {

            setText(null);

            setGraphic(null);

            return;
        }

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

                    /*
                     * TAMAÑOS FIJOS
                     */
                    lblBadge.setMinHeight(30);

                    lblBadge.setPrefHeight(30);

                    lblBadge.setMaxHeight(30);

                    lblBadge.setMinWidth(110);

                    lblBadge.setPrefWidth(110);

                    lblBadge.setWrapText(false);

                    /*
                     * ESTILO
                     */
                    lblBadge.setStyle(
                            "-fx-background-color: "
                            + badge.getColor()
                            + ";"
                            + "-fx-text-fill: black;"
                            + "-fx-background-radius: 999;"
                            + "-fx-font-size: 14px;"
                            + "-fx-font-weight: bold;"
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