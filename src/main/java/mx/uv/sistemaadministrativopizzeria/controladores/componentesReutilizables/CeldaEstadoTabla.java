/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables;

import java.util.function.Function;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;

/**
 *
 * @author hp
 */
public class CeldaEstadoTabla<S, T> extends TableCell<S, T> {

    private final Function<T, Badge> badgeProvider;

    public CeldaEstadoTabla(Function<T, Badge> badgeProvider) {
        this.badgeProvider = badgeProvider;
        setAlignment(Pos.CENTER);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setStyle("");
        } else {
            Badge badge = badgeProvider.apply(item);

            if (badge != null) {
                setText(badge.getTexto());

                setStyle(
                        "-fx-background-color: " + badge.getColor() + ";"
                        + "-fx-text-fill: black;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                );
            } else {
                setText(item.toString());
                setStyle("");
            }
        }
    }

}
