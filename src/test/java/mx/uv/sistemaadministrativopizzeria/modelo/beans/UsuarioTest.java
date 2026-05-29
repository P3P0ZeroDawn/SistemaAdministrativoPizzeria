package mx.uv.sistemaadministrativopizzeria.modelo.beans;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UsuarioTest {

    @Test
    public void testToStringAndGetString() {
        Usuario u = new Usuario(1, "Juan", "Pérez", "López");
        assertEquals("Juan Pérez López", u.toString());
        assertEquals("Juan Pérez López", u.getString());
    }

    @Test
    public void testDireccionGettersSetters() {
        Usuario u = new Usuario();
        Usuario.Direccion d = u.new Direccion("Calle Falsa", "123", "01234", "Ciudad");
        assertEquals("Calle Falsa", d.getCalle());
        assertEquals("123", d.getNumero());
        assertEquals("01234", d.getCodigoPostal());
        assertEquals("Ciudad", d.getCiudad());

        d.setCalle("Otra Calle");
        d.setNumero("9");
        d.setCodigoPostal("99999");
        d.setCiudad("Otra Ciudad");

        assertEquals("Otra Calle", d.getCalle());
        assertEquals("9", d.getNumero());
        assertEquals("99999", d.getCodigoPostal());
        assertEquals("Otra Ciudad", d.getCiudad());
    }
}
