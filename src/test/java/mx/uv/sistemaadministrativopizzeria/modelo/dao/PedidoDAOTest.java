package mx.uv.sistemaadministrativopizzeria.modelo.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido;
import org.junit.jupiter.api.Test;

public class PedidoDAOTest {

    @Test
    public void testSerializarPedidoConNombreCompleto() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("idPedido", 15);
        values.put("idUsuario", 3);
        values.put("fechaPedido", LocalDate.of(2026, 5, 29));
        values.put("total", 345.5);
        values.put("estatus", "EnPreparacion");
        values.put("nombre", "Maria");
        values.put("apellidoPaterno", "Sanchez");
        values.put("apellidoMaterno", "Ruiz");

        Pedido pedido = invocarSerializarPedido(ResultSetTestUtils.resultSet(values));

        assertEquals(15, pedido.getIdPedido());
        assertEquals(3, pedido.getIdUsuario());
        assertEquals(LocalDate.of(2026, 5, 29), pedido.getFechaPedido());
        assertEquals(345.5, pedido.getTotalAPagar());
        assertEquals(Pedido.EstatusPedido.EnPreparacion, pedido.getEstatus());
        assertEquals("Maria Sanchez Ruiz", pedido.getNombreUsuario());
    }

    @Test
    public void testSerializarPedidoNormalizaNombreConApellidosNulos() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("idPedido", 16);
        values.put("idUsuario", 4);
        values.put("fechaPedido", LocalDate.of(2026, 1, 1));
        values.put("total", 100.0);
        values.put("estatus", "Entregado");
        values.put("nombre", "Pedro");
        values.put("apellidoPaterno", null);
        values.put("apellidoMaterno", null);

        Pedido pedido = invocarSerializarPedido(ResultSetTestUtils.resultSet(values));

        assertEquals(Pedido.EstatusPedido.Entregado, pedido.getEstatus());
        assertEquals("Pedro", pedido.getNombreUsuario());
    }

    @Test
    public void testSerializarPedidoConEstatusInvalidoLanzaNullPointerException() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("idPedido", 17);
        values.put("idUsuario", 5);
        values.put("fechaPedido", LocalDate.now());
        values.put("total", 1.0);
        values.put("estatus", "NoExiste");

        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                () -> serializarPedidoMethod().invoke(null, ResultSetTestUtils.resultSet(values))
        );

        assertInstanceOf(NullPointerException.class, ex.getCause());
    }
    
    @Test
    public void testSerializarPedidoConEstatusNuloLanzaNullPointerException() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("idPedido", 18);
        values.put("idUsuario", 6);
        values.put("fechaPedido", LocalDate.now());
        values.put("total", 1.0);
        values.put("estatus", null);

        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                () -> serializarPedidoMethod().invoke(null, ResultSetTestUtils.resultSet(values))
        );

        assertInstanceOf(NullPointerException.class, ex.getCause());
    }
    
    @Test
    public void testSerializarPedidoConResultSetNuloLanzaNullPointerException() throws Exception {
        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                () -> serializarPedidoMethod().invoke(null, new Object[]{null})
        );

        assertInstanceOf(NullPointerException.class, ex.getCause());
    }

    private Pedido invocarSerializarPedido(ResultSet rs) throws Exception {
        return (Pedido) serializarPedidoMethod().invoke(null, rs);
    }

    private Method serializarPedidoMethod() throws Exception {
        Method method = PedidoDAO.class.getDeclaredMethod("serializarPedido", ResultSet.class);
        method.setAccessible(true);
        return method;
    }
}
