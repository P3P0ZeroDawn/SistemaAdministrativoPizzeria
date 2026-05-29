package mx.uv.sistemaadministrativopizzeria.modelo.beans;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ComponenteElaboracionTest {

    @Test
    public void testGetStringWithProduct() {
        Producto p = new Producto();
        p.setNombre("Tomate");
        ComponenteElaboracion c = new ComponenteElaboracion(p, 2.0);
        assertEquals("Tomate", c.getString());
    }

    @Test
    public void testGetStringWhenProductNull() {
        ComponenteElaboracion c = new ComponenteElaboracion();
        c.setProducto(null);
        assertEquals("", c.getString());
    }
}
