package mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables;

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
        Alert alerta = crearAlerta(Alert.AlertType.INFORMATION, titulo, mensaje);
        mostrar(alerta, cierreautomatico);
    }

    public static void mostrarAdvertencia(String titulo,
            String mensaje,
            boolean cierreautomatico) {
        Alert alerta = crearAlerta(Alert.AlertType.WARNING, titulo, mensaje);
        mostrar(alerta, cierreautomatico);
    }

    public static void mostrarError(String titulo,
            String mensaje,
            boolean cierreautomatico) {
        Alert alerta = crearAlerta(Alert.AlertType.ERROR, titulo, mensaje);
        mostrar(alerta, cierreautomatico);
    }
    
    public static boolean mostrarConfirmacion(String titulo,
            String mensaje) {
        Alert alerta = crearAlerta(Alert.AlertType.CONFIRMATION, titulo, mensaje);
        
        return alerta.showAndWait()
            .filter(respuesta -> respuesta.getButtonData().isDefaultButton())
            .isPresent();
    }

    private static Alert crearAlerta(AlertType tipo,
            String titulo,
            String mensaje) {
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
        // aplicar paleta personalizada encima
        alerta.getDialogPane().getStylesheets().add(
            App.class.getResource("/css/custom-palette.css").toExternalForm()
        );
        //---Le asignamos a la alerta su clase de estilo---//
        alerta.getDialogPane().getStyleClass().add(
                "alert-personalizada"
        );
        return alerta;
    }
    
    private static void mostrar(Alert alerta,
        boolean cierreautomatico){

        if (cierreautomatico) {

            alerta.show();

            PauseTransition espera =
                    new PauseTransition(
                            Duration.seconds(
                                    SEGUNDOS_CIERRE_AUTOMATICO
                            )
                    );

            espera.setOnFinished(e -> alerta.close());

            espera.play();

        } else {

            alerta.showAndWait();
        }
    }
}
