package mx.uv.sistemaadministrativopizzeria.modelo.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoPedido;
import org.junit.jupiter.api.Test;

public class ProductoPedidoDAOTest {

    @Test
    public void testSerializarProductoPedido() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("idProductoPedido", 21);
        values.put("idProducto", 8);
        values.put("cantidad", 3);

        ProductoPedido productoPedido =
                (ProductoPedido) serializarProductoPedidoMethod()
                        .invoke(null, ResultSetTestUtils.resultSet(values));

        assertEquals(21, productoPedido.getIdProductoPedido());
        assertEquals(3, productoPedido.getCantidad());
        assertNotNull(productoPedido.getProducto());
        assertEquals(8, productoPedido.getProducto().getIdProducto());
    }

    @Test
    public void testSerializarProductoPedidoConResultSetNullLanzaNullPointerException() throws Exception {
        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                () -> serializarProductoPedidoMethod().invoke(null, new Object[]{null})
        );

        assertInstanceOf(NullPointerException.class, ex.getCause());
    }

    private Method serializarProductoPedidoMethod() throws Exception {
        Method method = ProductoPedidoDAO.class.getDeclaredMethod("serializarProductoPedido", ResultSet.class);
        method.setAccessible(true);
        return method;
    }
}
