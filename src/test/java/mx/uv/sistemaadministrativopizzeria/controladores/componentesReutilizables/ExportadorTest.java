package mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;

public class ExportadorTest {

    @Test
    public void testExportarInventarioCSVCreatesFile() throws IOException {
        File tmp = File.createTempFile("inventario", ".csv");
        tmp.deleteOnExit();

        Producto p = new Producto();
        p.setNombre("Prueba");
        p.setCodigo("C1");
        p.setDescripcion("Desc");
        p.setPrecio(10.0);
        p.setCantidad(2.0);
        p.setUnidadMedida("u");
        p.setEsInsumo(false);
        p.setEsPreparado(false);

        ArrayList<Producto> list = new ArrayList<>();
        list.add(p);

        Exportador.exportarInventarioCSV(tmp.getAbsolutePath(), list);

        assertTrue(tmp.exists());
        assertTrue(tmp.length() > 0);
    }

    @Test
    public void testExportarInventarioPDFCreatesFile() throws MalformedURLException, IOException {
        File tmp = File.createTempFile("inventario", ".pdf");
        tmp.deleteOnExit();

        Producto p = new Producto();
        p.setNombre("PruebaPDF");
        p.setEsInsumo(false);
        p.setEsPreparado(false);
        p.setPrecio(5.0);
        p.setCantidad(1.0);

        ArrayList<Producto> list = new ArrayList<>();
        list.add(p);

        Exportador.exportarInventarioPDF(tmp.getAbsolutePath(), list);

        assertTrue(tmp.exists());
        assertTrue(tmp.length() > 0);
    }
}
