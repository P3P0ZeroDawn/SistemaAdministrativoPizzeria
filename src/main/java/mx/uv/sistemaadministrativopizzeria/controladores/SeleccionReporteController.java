package mx.uv.sistemaadministrativopizzeria.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class SeleccionReporteController
        implements Initializable {

    private String formatoSeleccionado;

    @Override
    public void initialize(
            URL url,
            ResourceBundle rb) {
    }

    @FXML
    private void clicPDF(
            ActionEvent event) {

        formatoSeleccionado = "PDF";
        cerrar();
    }

    @FXML
    private void clicCSV(
            ActionEvent event) {

        formatoSeleccionado = "CSV";
        cerrar();
    }

    private void cerrar() {

        ((Stage) btnDummy
                .getScene()
                .getWindow())
                .close();
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    /*
     * SOLO PARA OBTENER LA VENTANA
     */
    @FXML
    private javafx.scene.control.Button btnDummy;
}