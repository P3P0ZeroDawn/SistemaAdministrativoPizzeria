package mx.uv.sistemaadministrativopizzeria.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.JavaFXUtils;

public class CantidadProductoController implements Initializable {

    private Double cantidad;

    @FXML
    private TextField tfCantidad;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void configurar() {

    }

    public Double getCantidad() {
        return cantidad;
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {

        ((Stage) tfCantidad.getScene()
                .getWindow())
                .close();
    }

    @FXML
    private void clicBtnConfirmar(ActionEvent event) {

        try {

            Double valor =
                    Double.valueOf(
                            tfCantidad.getText().trim()
                    );

            if (valor <= 0) {

                JavaFXUtils.mostrarAdvertencia(
                        "Cantidad inválida",
                        "Ingrese una cantidad mayor a 0",
                        false
                );

                return;
            }

            cantidad = valor;

            ((Stage) tfCantidad.getScene()
                    .getWindow())
                    .close();

        } catch (NumberFormatException ex) {

            JavaFXUtils.mostrarError(
                    "Cantidad inválida",
                    "Ingrese un número válido",
                    false
            );
        }
    }
}