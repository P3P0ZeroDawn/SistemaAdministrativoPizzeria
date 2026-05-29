package mx.uv.sistemaadministrativopizzeria.modelo.beans;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class PedidoTest {

    @Test
    public void testDefaultsAndToString() {
        Pedido pedido = new Pedido();
        assertEquals(0.0, pedido.getTotalAPagar());
        pedido.setNombreUsuario("Carlos");
        pedido.setFechaPedido(LocalDate.of(2023,1,2));
        String s = pedido.getString();
        assertTrue(s.contains("Carlos"));
        assertTrue(s.contains("2023-01-02"));
        assertEquals("Carlos", pedido.toString());
    }

    @Test
    public void testProductosListSetGet() {
        Pedido p = new Pedido();
        ArrayList<ProductoPedido> list = new ArrayList<>();
        ProductoPedido pp = new ProductoPedido();
        list.add(pp);
        p.setProductos(list);
        assertNotNull(p.getProductos());
        assertEquals(1, p.getProductos().size());
    }
}
