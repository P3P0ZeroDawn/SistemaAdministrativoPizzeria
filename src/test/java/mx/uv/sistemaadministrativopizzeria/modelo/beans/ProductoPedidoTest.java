package mx.uv.sistemaadministrativopizzeria.modelo.beans;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ProductoPedidoTest {

    @Test
    public void testGetStringReturnsProductName() {
        Producto p = new Producto();
        p.setNombre("Queso");
        ProductoPedido pp = new ProductoPedido(1, p, 3);
        assertEquals("Queso", pp.getString());
    }
}
