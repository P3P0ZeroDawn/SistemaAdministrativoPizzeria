package mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables;

import java.util.function.Function;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;

public class CeldaEstadoTabla<S, T> extends TableCell<S, T> {

    private final Function<T, Badge> badgeProvider;

    private final Label lblEstado = new Label();

    public CeldaEstadoTabla(Function<T, Badge> badgeProvider) {

        this.badgeProvider = badgeProvider;

        setAlignment(Pos.CENTER);

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        lblEstado.setAlignment(Pos.CENTER);

        lblEstado.setMinWidth(100);
        lblEstado.setPrefWidth(100);
        lblEstado.setMaxWidth(100);

        lblEstado.setMinHeight(28);
        lblEstado.setPrefHeight(28);
        lblEstado.setMaxHeight(28);
    }

    @Override
    protected void updateItem(T item, boolean empty) {

        super.updateItem(item, empty);

        if (empty || item == null) {

            setText(null);
            setGraphic(null);

            return;
        }

        Badge badge = badgeProvider.apply(item);

        if (badge == null) {

            setText(item.toString());
            setGraphic(null);

            return;
        }

        lblEstado.setText(badge.getTexto());

        lblEstado.setStyle(
                "-fx-background-color: " + badge.getColor() + ";" +
                "-fx-text-fill: black;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 999;" +
                "-fx-padding: 4 12 4 12;"
        );

        setText(null);

        setGraphic(lblEstado);
    }
}