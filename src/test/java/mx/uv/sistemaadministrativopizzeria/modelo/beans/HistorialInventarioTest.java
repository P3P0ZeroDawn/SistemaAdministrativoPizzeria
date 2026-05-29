package mx.uv.sistemaadministrativopizzeria.modelo.beans;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class HistorialInventarioTest {

    @Test
    public void testToStringFormatsDateTime() {
        LocalDateTime dt = LocalDateTime.of(2022, 12, 3, 4, 5, 6);
        HistorialInventario h = new HistorialInventario();
        h.setFecha(dt);
        String s = h.toString();
        assertTrue(s.contains("2022-12-03"));
        assertFalse(s.contains("T"));
    }
}
