package mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.embed.swing.JFXPanel;
import mx.uv.sistemaadministrativopizzeria.excepciones.DatosFaltantesException;

public class ValidadorTest {

    static {
        // Inicializar la plataforma JavaFX para poder usar controles en pruebas
        new JFXPanel();
    }

    @Test
    public void testRequeridoThrowsOnEmpty() {
        TextField tf = new TextField();
        tf.setText("");
        assertThrows(DatosFaltantesException.class, () -> Validador.requerido(tf, "falta"));
    }
    
    @Test
    public void testValidacionesAceptanValoresCorrectos() {
        TextField requerido = new TextField("  dato  ");
        TextField email = new TextField("cliente@example.com");
        TextField numero = new TextField("12345");
        TextField password = new TextField("Segura1!");
        ComboBox<String> combo = new ComboBox<>();
        combo.setValue("Seleccion");

        assertAll(
                () -> assertDoesNotThrow(() -> Validador.requerido(requerido, "falta")),
                () -> assertDoesNotThrow(() -> Validador.email(email, "email")),
                () -> assertDoesNotThrow(() -> Validador.numerico(numero, "num")),
                () -> assertDoesNotThrow(() -> Validador.passwordSegura(password, "pw")),
                () -> assertDoesNotThrow(() -> Validador.comboRequerido(combo, "combo"))
        );
    }

    @Test
    public void testLongitudMaximaThrows() {
        TextField tf = new TextField();
        tf.setText("abcdef");
        assertThrows(DatosFaltantesException.class, () -> Validador.longitudMaxima(tf, 3, "max"));
    }

    @Test
    public void testLongitudMinimaThrows() {
        TextField tf = new TextField();
        tf.setText("a");
        assertThrows(DatosFaltantesException.class, () -> Validador.longitudMinima(tf, 3, "min"));
    }

    @Test
    public void testEmailValidation() {
        TextField tf = new TextField();
        tf.setText("no-mail");
        assertThrows(DatosFaltantesException.class, () -> Validador.email(tf, "email"));
    }

    @Test
    public void testNumericoValidation() {
        TextField tf = new TextField();
        tf.setText("12a");
        assertThrows(DatosFaltantesException.class, () -> Validador.numerico(tf, "num"));
    }

    @Test
    public void testComboRequerido() {
        ComboBox<String> cb = new ComboBox<>();
        cb.setValue(null);
        assertThrows(DatosFaltantesException.class, () -> Validador.comboRequerido(cb, "c"));
    }

    @Test
    public void testPasswordsIguales() {
        TextField p1 = new TextField();
        TextField p2 = new TextField();
        p1.setText("abc");
        p2.setText("def");
        assertThrows(DatosFaltantesException.class, () -> Validador.passwordsIguales(p1, p2, "pw"));
    }

    @Test
    public void testSoloTextoValidation() {
        TextField tf = new TextField();
        tf.setText("Texto válido");
        try {
            Validador.soloTexto(tf, "txt");
        } catch (DatosFaltantesException ex) {
            fail("No debería lanzar excepción para texto válido");
        }
        tf.setText("Inv@lido123");
        assertThrows(DatosFaltantesException.class, () -> Validador.soloTexto(tf, "txt"));
    }

    @Test
    public void testPasswordSegura() {
        TextField tf = new TextField();
        tf.setText("Weak1");
        assertThrows(DatosFaltantesException.class, () -> Validador.passwordSegura(tf, "pw"));
    }

    @Test
    public void testFiltrosSetTextFormatter() {
        TextField tf = new TextField();
        Validador.permitirSoloNumeros(tf, 5);
        assertNotNull(tf.getTextFormatter());
        TextField tf2 = new TextField();
        Validador.permitirCorreo(tf2, 10);
        assertNotNull(tf2.getTextFormatter());
    }
    
    @Test
    public void testFiltroSoloNumerosRechazaLetrasYLongitudMayor() {
        TextField tf = new TextField();
        Validador.permitirSoloNumeros(tf, 3);

        tf.setText("12");
        assertEquals("12", tf.getText());

        tf.setText("12a");
        assertEquals("12", tf.getText());

        tf.setText("1234");
        assertEquals("12", tf.getText());
    }
    
    @Test
    public void testFiltroDecimalConLimitesRechazaFormatoInvalido() {
        TextField tf = new TextField();
        Validador.permitirDecimal(tf, 3, 2);

        tf.setText("123.45");
        assertEquals("123.45", tf.getText());

        tf.setText("123.456");
        assertEquals("123.45", tf.getText());

        tf.setText("12.3.4");
        assertEquals("123.45", tf.getText());

        tf.setText("1234");
        assertEquals("123.45", tf.getText());
    }
}
