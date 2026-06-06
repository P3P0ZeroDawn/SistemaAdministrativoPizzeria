package mx.uv.sistemaadministrativopizzeria.modelo.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import javafx.embed.swing.JFXPanel;

import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;

public class ProductoDAOTest {

    static {
        new JFXPanel(); // Inicializar JavaFX para Image
    }

    private ResultSet createResultSetProxy(Map<String, Object> values) {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();

                if ("getInt".equals(name) && args != null && args.length == 1 && args[0] instanceof String) {
                    Object v = values.get((String) args[0]);
                    if (v == null) return 0;
                    if (v instanceof Number) return ((Number) v).intValue();
                }

                if ("getString".equals(name) && args != null && args.length == 1 && args[0] instanceof String) {
                    return values.get((String) args[0]);
                }

                if ("getObject".equals(name) && args != null && args.length >= 1 && args[0] instanceof String) {
                    return values.get((String) args[0]);
                }

                if ("getBytes".equals(name) && args != null && args.length == 1 && args[0] instanceof String) {
                    return values.get((String) args[0]);
                }

                // default for primitives
                Class<?> ret = method.getReturnType();
                if (ret.isPrimitive()) {
                    if (ret == boolean.class) return false;
                    if (ret == byte.class) return (byte) 0;
                    if (ret == short.class) return (short) 0;
                    if (ret == int.class) return 0;
                    if (ret == long.class) return 0L;
                    if (ret == float.class) return 0f;
                    if (ret == double.class) return 0d;
                    if (ret == char.class) return '\0';
                }

                return null;
            }
        };

        return (ResultSet) Proxy.newProxyInstance(
                ResultSet.class.getClassLoader(),
                new Class<?>[] { ResultSet.class },
                handler
        );
    }

    @Test
    public void testSerializarProductoWithNullFoto() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("idProducto", 7);
        values.put("nombre", "Queso");
        values.put("codigo", "C7");
        values.put("descripcion", "Delicioso");
        values.put("precio", 12.5);
        values.put("cantidad", 2.0);
        values.put("unidadMedida", "kg");
        values.put("activo", 1);
        values.put("esPreparado", 0);
        values.put("esInsumo", 1);
        values.put("foto", null);

        ResultSet rs = createResultSetProxy(values);

        Producto p = ProductoDAO.serializarProducto(rs);

        assertNotNull(p);
        assertEquals(7, p.getIdProducto());
        assertEquals("Queso", p.getNombre());
        assertEquals("C7", p.getCodigo());
        assertEquals(12.5, p.getPrecio());
        assertEquals(2.0, p.getCantidad());
        assertEquals("kg", p.getUnidadMedida());
        assertTrue(p.getActivo());
        assertFalse(p.getEsPreparado());
        assertTrue(p.getEsInsumo());
        assertNull(p.getFoto());
    }

    @Test
    public void testSerializarProductoWithFotoBytes() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("idProducto", 8);
        values.put("nombre", "Imagen");
        values.put("codigo", "IMG");
        values.put("descripcion", "Con foto");
        values.put("precio", 1.0);
        values.put("cantidad", 0.5);
        values.put("unidadMedida", "u");
        values.put("activo", 1);
        values.put("esPreparado", 1);
        values.put("esInsumo", 0);
        values.put("foto", new byte[] { (byte)0x89, 0x50, 0x4E, 0x47 });

        ResultSet rs = createResultSetProxy(values);

        Producto p = ProductoDAO.serializarProducto(rs);

        assertNotNull(p);
        assertEquals(8, p.getIdProducto());
        assertEquals("Imagen", p.getNombre());
        assertNotNull(p.getFoto());
    }
    
    @Test
    public void testSerializarProductoConResultSetNuloRetornaNull() {
        assertNull(ProductoDAO.serializarProducto(null));
    }
    
    @Test
    public void testSerializarProductoConCamposOpcionalesNulos() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("idProducto", 9);
        values.put("nombre", "Producto parcial");
        values.put("codigo", "PAR");
        values.put("descripcion", null);
        values.put("precio", null);
        values.put("cantidad", null);
        values.put("unidadMedida", null);
        values.put("activo", 0);
        values.put("esPreparado", 0);
        values.put("esInsumo", 0);
        values.put("foto", null);

        Producto p = ProductoDAO.serializarProducto(createResultSetProxy(values));

        assertNotNull(p);
        assertEquals(9, p.getIdProducto());
        assertEquals("Producto parcial", p.getNombre());
        assertNull(p.getDescripcion());
        assertNull(p.getPrecio());
        assertNull(p.getCantidad());
        assertNull(p.getUnidadMedida());
        assertFalse(p.getActivo());
        assertFalse(p.getEsPreparado());
        assertFalse(p.getEsInsumo());
    }
}
