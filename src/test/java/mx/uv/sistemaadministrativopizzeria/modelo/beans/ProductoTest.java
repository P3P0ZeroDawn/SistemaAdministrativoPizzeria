package mx.uv.sistemaadministrativopizzeria.modelo.beans;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class ProductoTest {

    @Test
    public void testGettersSettersAndToString() {
        Producto p = new Producto();
        p.setNombre("Margarita");
        p.setPrecio(99.5);
        p.setCantidad(1.0);
        p.setUnidadMedida("pieza");

        assertEquals("Margarita", p.getNombre());
        assertEquals(99.5, p.getPrecio());
        assertEquals(1.0, p.getCantidad());
        assertEquals("pieza", p.getUnidadMedida());
        assertEquals("Margarita", p.getString());
        assertEquals("Margarita", p.toString());
    }

    @Test
    public void testComponentesList() {
        Producto p = new Producto();
        ArrayList<ComponenteElaboracion> comp = new ArrayList<>();
        ComponenteElaboracion c = new ComponenteElaboracion();
        comp.add(c);
        p.setComponentes(comp);
        assertNotNull(p.getComponentes());
        assertEquals(1, p.getComponentes().size());
    }
}
