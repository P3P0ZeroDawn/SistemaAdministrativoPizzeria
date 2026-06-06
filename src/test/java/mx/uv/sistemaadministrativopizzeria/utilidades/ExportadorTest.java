package mx.uv.sistemaadministrativopizzeria.utilidades;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
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
    public void testExportarInventarioCSVRespetaTiposYValoresNoAplican() throws IOException {
        File tmp = File.createTempFile("inventario-tipos", ".csv");
        tmp.deleteOnExit();

        Producto preparado = new Producto();
        preparado.setNombre("Pizza");
        preparado.setCodigo("PZ");
        preparado.setDescripcion("");
        preparado.setPrecio(150.0);
        preparado.setCantidad(99.0);
        preparado.setUnidadMedida("pieza");
        preparado.setEsPreparado(true);
        preparado.setEsInsumo(false);

        Producto insumo = new Producto();
        insumo.setNombre(null);
        insumo.setCodigo("HAR");
        insumo.setDescripcion("Harina");
        insumo.setPrecio(20.0);
        insumo.setCantidad(3.5);
        insumo.setUnidadMedida("kg");
        insumo.setEsPreparado(false);
        insumo.setEsInsumo(true);

        ArrayList<Producto> productos = new ArrayList<>();
        productos.add(preparado);
        productos.add(insumo);

        Exportador.exportarInventarioCSV(tmp.getAbsolutePath(), productos);

        List<String> lineas = Files.readAllLines(tmp.toPath(), StandardCharsets.UTF_8);
        assertEquals(3, lineas.size());
        assertTrue(lineas.get(1).contains("\"Preparado\""));
        assertTrue(lineas.get(1).contains("\"$ 150.00\""));
        assertTrue(lineas.get(1).contains("\"No aplica\""));
        assertTrue(lineas.get(2).contains("\"Insumo\""));
        assertTrue(lineas.get(2).contains("\"3.5\""));
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
