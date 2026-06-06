package mx.uv.sistemaadministrativopizzeria;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AppTest {

    @Test
    public void testMetadatosGuardaYReemplazaValores() {
        App.setMetadato("usuarioActual", "pedro");
        assertEquals("pedro", App.getMetadato("usuarioActual"));

        App.setMetadato("usuarioActual", "admin");
        assertEquals("admin", App.getMetadato("usuarioActual"));
    }

    @Test
    public void testGetMetadatoInexistenteRetornaNull() {
        assertNull(App.getMetadato("clave-inexistente"));
    }
}
