package mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables;

import java.util.regex.Pattern;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputControl;
import mx.uv.sistemaadministrativopizzeria.excepciones.DatosFaltantesException;
import javafx.scene.control.TextFormatter;

public class Validador {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    private static final Pattern SOLO_TEXTO_PATTERN =
        Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");

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
    
    public static void soloTexto(TextInputControl campo,
                              String mensaje)
        throws DatosFaltantesException {

        if (!SOLO_TEXTO_PATTERN
                .matcher(campo.getText())
                .matches()) {

            campo.requestFocus();

            throw new DatosFaltantesException(mensaje);
        }
    }
    
    // =========================
    // FILTROS DE ENTRADA
    // =========================

    private static void aplicarFiltro(TextInputControl campo,
                                      String regexPermitido,
                                      int longitudMaxima) {

        campo.setTextFormatter(new TextFormatter<>(change -> {

            String nuevoTexto = change.getControlNewText();

            boolean textoValido =
                    nuevoTexto.matches(regexPermitido);

            boolean longitudValida =
                    nuevoTexto.length() <= longitudMaxima;

            if (textoValido && longitudValida) {
                return change;
            }

            return null;
        }));
    }

    // =========================
    // MÉTODOS REUTILIZABLES
    // =========================

    public static void permitirSoloTexto(TextInputControl campo,
                                         int longitudMaxima) {

        aplicarFiltro(
                campo,
                "[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*",
                longitudMaxima
        );
    }

    public static void permitirSoloNumeros(TextInputControl campo,
                                           int longitudMaxima) {

        aplicarFiltro(
                campo,
                "[0-9]*",
                longitudMaxima
        );
    }

    public static void permitirTextoNumerico(TextInputControl campo,
                                             int longitudMaxima) {

        aplicarFiltro(
                campo,
                "[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\-\\s]*",
                longitudMaxima
        );
    }

    public static void permitirCorreo(TextInputControl campo,
                                      int longitudMaxima) {

        aplicarFiltro(
                campo,
                "[a-zA-Z0-9@._\\-]*",
                longitudMaxima
        );
    }

    public static void permitirTelefono(TextInputControl campo,
                                        int longitudMaxima) {

        aplicarFiltro(
                campo,
                "[0-9+\\-\\s]*",
                longitudMaxima
        );
    }

    public static void permitirDireccion(TextInputControl campo,
                                         int longitudMaxima) {

        aplicarFiltro(
                campo,
                "[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9#.,\\-\\s]*",
                longitudMaxima
        );
    }
    
    public static void permitirPassword(TextInputControl campo,
                                    int longitudMaxima) {

        aplicarFiltro(
                campo,
                "[a-zA-Z0-9@#$%^&*!?_.,\\-]*",
                longitudMaxima
        );
    }
    
    public static void permitirDecimal(TextInputControl campo,
                                   int longitudMaxima) {

        aplicarFiltro(
                campo,
                "[0-9.]*",
                longitudMaxima
        );
    }
    
    public static void permitirDecimal(TextInputControl campo,
                                   int digitosEnteros,
                                   int digitosDecimales) {

        int longitudMaxima = digitosEnteros + digitosDecimales + 1; // +1 por el punto decimal
        
        campo.setTextFormatter(new TextFormatter<>(change -> {
            String nuevoTexto = change.getControlNewText();
            
            if (nuevoTexto.isEmpty()) {
                return change;
            }
            
            // Permite solo dígitos y un punto decimal
            if (!nuevoTexto.matches("[0-9.]*")) {
                return null;
            }
            
            // Permite máximo un punto decimal
            if (nuevoTexto.split("\\.", -1).length > 2) {
                return null;
            }
            
            // Validar longitud total
            if (nuevoTexto.length() > longitudMaxima) {
                return null;
            }
            
            // Si contiene punto, validar cantidad de dígitos antes y después
            if (nuevoTexto.contains(".")) {
                String[] partes = nuevoTexto.split("\\.", -1);
                if (partes.length >= 2) {
                    String parteEntera = partes[0];
                    String parteDecimal = partes[1];
                    
                    if (parteEntera.length() > digitosEnteros || parteDecimal.length() > digitosDecimales) {
                        return null;
                    }
                }
            } else {
                // Sin punto decimal, validar solo la parte entera
                if (nuevoTexto.length() > digitosEnteros) {
                    return null;
                }
            }
            
            return change;
        }));
    }
    
    public static void passwordSegura(TextInputControl campo,
                                  String mensaje)
        throws DatosFaltantesException {

        String password = campo.getText();

        boolean valida =
                password.matches(
                        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&*!?_.,\\-]).{8,}$"
                );

        if (!valida) {

            campo.requestFocus();

            throw new DatosFaltantesException(mensaje);
        }
    }
}