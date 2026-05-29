package mx.uv.sistemaadministrativopizzeria.modelo.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javafx.embed.swing.JFXPanel;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoHistorial;
import org.junit.jupiter.api.Test;

public class HistorialInventarioDAOTest {

    static {
        new JFXPanel();
    }

    @Test
    public void testCalcularNuevoStockSumaDiferencia() throws Exception {
        double resultado = (double) calcularNuevoStockMethod().invoke(null, 10.0, 12.5, 4.0);

        assertEquals(6.5, resultado);
    }

    @Test
    public void testSerializarProductoHistorialSinFoto() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("idProductoHistorial", 5);
        values.put("idProducto", 12);
        values.put("cantidadSistema", 20.0);
        values.put("cantidadReal", 18.0);
        values.put("razon", "Merma");
        values.put("estatusExistencia", "Faltante");
        values.put("nombre", "Queso");
        values.put("codigo", "Q-1");
        values.put("unidadMedida", "kg");
        values.put("foto", null);

        ProductoHistorial producto =
                (ProductoHistorial) serializarProductoHistorialMethod()
                        .invoke(null, ResultSetTestUtils.resultSet(values));

        assertEquals(5, producto.getIdProductoHistorial());
        assertEquals(20.0, producto.getCantidadSistema());
        assertEquals(18.0, producto.getCantidadReal());
        assertEquals("Merma", producto.getRazon());
        assertEquals(ProductoHistorial.EstatusExistencia.Faltante, producto.getEstatusExistencia());
        assertNotNull(producto.getProducto());
        assertEquals(12, producto.getProducto().getIdProducto());
        assertEquals("Queso", producto.getProducto().getNombre());
        assertEquals("Q-1", producto.getProducto().getCodigo());
        assertEquals("kg", producto.getProducto().getUnidadMedida());
        assertNull(producto.getProducto().getFoto());
    }

    @Test
    public void testSerializarProductoHistorialConEstatusNuloLanzaNullPointerException() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("estatusExistencia", null);

        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                () -> serializarProductoHistorialMethod().invoke(null, ResultSetTestUtils.resultSet(values))
        );

        assertInstanceOf(NullPointerException.class, ex.getCause());
    }

    private Method calcularNuevoStockMethod() throws Exception {
        Method method = HistorialInventarioDAO.class.getDeclaredMethod(
                "calcularNuevoStock",
                double.class,
                double.class,
                double.class
        );
        method.setAccessible(true);
        return method;
    }

    private Method serializarProductoHistorialMethod() throws Exception {
        Method method = HistorialInventarioDAO.class.getDeclaredMethod("serializarProductoHistorial", ResultSet.class);
        method.setAccessible(true);
        return method;
    }
}
