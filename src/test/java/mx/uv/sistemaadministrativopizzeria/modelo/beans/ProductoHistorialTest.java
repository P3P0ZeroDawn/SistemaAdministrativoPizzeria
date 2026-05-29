package mx.uv.sistemaadministrativopizzeria.modelo.beans;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ProductoHistorialTest {

    @Test
    public void testGetNombreProducto() {
        Producto p = new Producto();
        p.setNombre("Anchoa");
        ProductoHistorial ph = new ProductoHistorial();
        ph.setProducto(p);
        assertEquals("Anchoa", ph.getNombreProducto());
    }
}
