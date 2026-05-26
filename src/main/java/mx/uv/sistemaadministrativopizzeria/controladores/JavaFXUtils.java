package mx.uv.sistemaadministrativopizzeria.controladores;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;
import mx.uv.sistemaadministrativopizzeria.App;

public class JavaFXUtils {

    public static Double SEGUNDOS_CIERRE_AUTOMATICO = 1.5;

    public static void mostrarMensaje(String titulo,
            String mensaje, boolean cierreautomatico) {
        crearAlerta(Alert.AlertType.INFORMATION, titulo, mensaje, cierreautomatico);
    }

    public static void mostrarAdvertencia(String titulo,
            String mensaje,
            boolean cierreautomatico) {
        crearAlerta(Alert.AlertType.WARNING, titulo, mensaje, cierreautomatico);
    }

    public static void mostrarError(String titulo,
            String mensaje,
            boolean cierreautomatico) {
        crearAlerta(Alert.AlertType.ERROR, titulo, mensaje, cierreautomatico);
    }

    private static void crearAlerta(AlertType tipo,
            String titulo,
            String mensaje,
            boolean cierreautomatico) {
        //---Creamos el dialogo, en JavaFX se usa la clase Alert---//
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.getDialogPane().autosize();
        //---Le aplicamos la hoja de estilo CSS---//
        alerta.getDialogPane().getStylesheets().add(
                App.class.getResource("/css/primer-light.css").toExternalForm()
        );
        //---Le asignamos a la alerta su clase de estilo---//
        alerta.getDialogPane().getStyleClass().add(
                "alert-personalizada"
        );
        if (cierreautomatico) {
            alerta.show();
            PauseTransition espera = new PauseTransition(
                    Duration.seconds(SEGUNDOS_CIERRE_AUTOMATICO));
            espera.setOnFinished(e -> alerta.close());
            espera.play();
        } else {
            alerta.showAndWait();
        }
    }
    
    public static byte[] sha256Bytes(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes("UTF-8"));
    }
}
