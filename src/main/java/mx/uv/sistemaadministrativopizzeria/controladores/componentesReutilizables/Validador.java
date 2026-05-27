package mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables;

import java.util.regex.Pattern;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputControl;
import mx.uv.sistemaadministrativopizzeria.excepciones.DatosFaltantesException;

public class Validador {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public static void requerido(TextInputControl campo, String mensaje)
            throws DatosFaltantesException {

        if (campo.getText() == null || campo.getText().trim().isEmpty()) {
            campo.requestFocus();
            throw new DatosFaltantesException(mensaje);
        }
    }

    public static void longitudMaxima(TextInputControl campo,
                                      int maximo,
                                      String mensaje)
            throws DatosFaltantesException {

        if (campo.getText() != null &&
                campo.getText().length() > maximo) {

            campo.requestFocus();
            throw new DatosFaltantesException(mensaje);
        }
    }

    public static void longitudMinima(TextInputControl campo,
                                      int minimo,
                                      String mensaje)
            throws DatosFaltantesException {

        if (campo.getText() == null ||
                campo.getText().length() < minimo) {

            campo.requestFocus();
            throw new DatosFaltantesException(mensaje);
        }
    }

    public static void email(TextInputControl campo,
                             String mensaje)
            throws DatosFaltantesException {

        if (!EMAIL_PATTERN.matcher(campo.getText()).matches()) {
            campo.requestFocus();
            throw new DatosFaltantesException(mensaje);
        }
    }

    public static void numerico(TextInputControl campo,
                                String mensaje)
            throws DatosFaltantesException {

        try {
            Integer.valueOf(campo.getText());
        } catch (NumberFormatException e) {
            campo.requestFocus();
            throw new DatosFaltantesException(mensaje);
        }
    }

    public static void comboRequerido(ComboBox<?> combo,
                                      String mensaje)
            throws DatosFaltantesException {

        if (combo.getValue() == null) {
            combo.requestFocus();
            throw new DatosFaltantesException(mensaje);
        }
    }

    public static void passwordsIguales(TextInputControl p1,
                                        TextInputControl p2,
                                        String mensaje)
            throws DatosFaltantesException {

        if (!p1.getText().equals(p2.getText())) {
            p2.requestFocus();
            throw new DatosFaltantesException(mensaje);
        }
    }
}