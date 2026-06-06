package mx.uv.sistemaadministrativopizzeria.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.JavaFXUtils;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Validador;

public class CantidadProductoController implements Initializable {

    private Number cantidad;
    private boolean esDecimal;

    @FXML
    private TextField tfCantidad;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización básica por defecto si es necesaria
    }

    /**
     * Configura el comportamiento de la ventana.
     *
     * @param esDecimal true si se permiten decimales, false si solo enteros.
     */
    public void configurar(boolean esDecimal) {
        this.esDecimal = esDecimal;
        this.cantidad = null;
        tfCantidad.clear();

        if (esDecimal) {
            Validador.permitirDecimal(tfCantidad, 10);
        } else {
            Validador.permitirSoloNumeros(tfCantidad, 10);
        }
    }
    
    public void configurar(boolean esDecimal, Number cantidadInicial) {
        configurar(esDecimal);
        if(cantidadInicial != null){
            tfCantidad.setText(String.valueOf(cantidadInicial));
            tfCantidad.selectAll();
        }
    }

    public void configurar() {
        configurar(true); 
    }

    /**
     * Devuelve el valor recuperado. Puedes usar .doubleValue() o .intValue()
     * según ocupes.
     */
    public Number getCantidad() {
        return cantidad;
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        this.cantidad = null;
        cerrarVentana();
    }

    @FXML
    private void clicBtnConfirmar(ActionEvent event) {
        String texto = tfCantidad.getText().trim();

        if (texto.isEmpty()) {
            JavaFXUtils.mostrarAdvertencia("Campo requerido", "Por favor, ingrese una cantidad.", false);
            return;
        }

        try {
            if (esDecimal) {
                Double valorDouble = Double.valueOf(texto);
                if (valorDouble <= 0) {
                    mostrarErrorMenorCero();
                    return;
                }
                cantidad = valorDouble;
            } else {
                Integer valorInt = Integer.valueOf(texto);
                if (valorInt <= 0) {
                    mostrarErrorMenorCero();
                    return;
                }
                cantidad = valorInt;
            }

            cerrarVentana();

        } catch (NumberFormatException ex) {
            JavaFXUtils.mostrarError("Cantidad inválida", "Ingrese un número válido.", false);
        }
    }

    private void mostrarErrorMenorCero() {
        JavaFXUtils.mostrarAdvertencia("Cantidad inválida", "Ingrese una cantidad mayor a 0.", false);
    }

    private void cerrarVentana() {
        ((Stage) tfCantidad.getScene().getWindow()).close();
    }
}
