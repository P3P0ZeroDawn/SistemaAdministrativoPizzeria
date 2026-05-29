package mx.uv.sistemaadministrativopizzeria.modelo.beans;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ProductoValidacionInventarioTest {

    @Test
    public void testGettersSetters() {
        Producto p = new Producto();
        p.setNombre("Harina");
        ProductoValidacionInventario v = new ProductoValidacionInventario(p);
        assertEquals(p, v.getProducto());
        v.setCantidadReal(5);
        v.setRazon("Caducado");
        assertEquals(5, v.getCantidadReal());
        assertEquals("Caducado", v.getRazon());
    }
}
