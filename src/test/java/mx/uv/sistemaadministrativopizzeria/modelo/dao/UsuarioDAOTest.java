package mx.uv.sistemaadministrativopizzeria.modelo.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Usuario;
import org.junit.jupiter.api.Test;

public class UsuarioDAOTest {

    @Test
    public void testSerializarUsuarioEmpleadoAdministrador() {
        Map<String, Object> values = new HashMap<>();
        values.put("idUsuario", 4);
        values.put("nombre", "Ana");
        values.put("apellidoPaterno", "Lopez");
        values.put("apellidoMaterno", "Garcia");
        values.put("telefono", "2281234567");
        values.put("email", "ana@test.com");
        values.put("activo", 1);
        values.put("tipoUsuario", "Empleado");
        values.put("usuario", "alopez");
        values.put("rolEmpleado", "Administrador");
        values.put("calle", "Xalapa");
        values.put("numero", "12");
        values.put("codigoPostal", "91000");
        values.put("ciudad", "Xalapa");

        ResultSet rs = ResultSetTestUtils.resultSet(values);

        Usuario usuario = UsuarioDAO.serializarUsuario(rs);

        assertNotNull(usuario);
        assertEquals(4, usuario.getIdUsuario());
        assertEquals("Ana", usuario.getNombre());
        assertEquals("Lopez", usuario.getApellidoPaterno());
        assertEquals("Garcia", usuario.getApellidoMaterno());
        assertTrue(usuario.getActivo());
        assertEquals(Usuario.tipoUsuario.Empleado, usuario.getTipoUsuario());
        assertEquals("alopez", usuario.getUsuario());
        assertEquals(Usuario.rolEmpleado.Administrador, usuario.getRolEmpleado());
        assertEquals("Xalapa", usuario.getDireccion().getCalle());
        assertEquals("12", usuario.getDireccion().getNumero());
        assertEquals("91000", usuario.getDireccion().getCodigoPostal());
        assertEquals("Xalapa", usuario.getDireccion().getCiudad());
    }

    @Test
    public void testSerializarUsuarioClienteSinDatosEmpleado() {
        Map<String, Object> values = new HashMap<>();
        values.put("idUsuario", 9);
        values.put("nombre", "Luis");
        values.put("apellidoPaterno", "Perez");
        values.put("apellidoMaterno", null);
        values.put("telefono", "2280000000");
        values.put("email", "luis@test.com");
        values.put("activo", 1);
        values.put("tipoUsuario", "Cliente");
        values.put("usuario", null);
        values.put("rolEmpleado", null);
        values.put("calle", "Centro");
        values.put("numero", "2");
        values.put("codigoPostal", "91100");
        values.put("ciudad", "Xalapa");

        Usuario usuario = UsuarioDAO.serializarUsuario(ResultSetTestUtils.resultSet(values));

        assertNotNull(usuario);
        assertEquals("", usuario.getApellidoMaterno());
        assertEquals(Usuario.tipoUsuario.Cliente, usuario.getTipoUsuario());
        assertNull(usuario.getUsuario());
        assertNull(usuario.getRolEmpleado());
    }

    @Test
    public void testSerializarUsuarioConResultSetNullRegresaNull() {
        assertNull(UsuarioDAO.serializarUsuario(null));
    }
}
