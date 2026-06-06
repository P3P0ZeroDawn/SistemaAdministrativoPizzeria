package mx.uv.sistemaadministrativopizzeria.controladores;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Test;

public class CantidadProductoControllerTest {

    static {
        new JFXPanel();
    }

    @Test
    public void testConfigurarConCantidadInicialPrecargaYSeleccionaTexto() throws Exception {
        CantidadProductoController controller = new CantidadProductoController();
        TextField tfCantidad = new TextField();
        inyectarCampo(controller, "tfCantidad", tfCantidad);

        controller.configurar(false, 7);

        assertEquals("7", tfCantidad.getText());
        assertNotNull(tfCantidad.getTextFormatter());
        assertEquals("7", tfCantidad.getSelectedText());
    }

    @Test
    public void testConfigurarSinCantidadInicialLimpiaCampo() throws Exception {
        CantidadProductoController controller = new CantidadProductoController();
        TextField tfCantidad = new TextField("123");
        inyectarCampo(controller, "tfCantidad", tfCantidad);

        controller.configurar(false);

        assertEquals("", tfCantidad.getText());
        assertNotNull(tfCantidad.getTextFormatter());
    }

    private void inyectarCampo(Object target, String nombreCampo, Object valor) throws Exception {
        Field field = target.getClass().getDeclaredField(nombreCampo);
        field.setAccessible(true);
        field.set(target, valor);
    }
}
