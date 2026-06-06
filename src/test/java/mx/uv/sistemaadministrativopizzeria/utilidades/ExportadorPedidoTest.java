package mx.uv.sistemaadministrativopizzeria.utilidades;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoPedido;

public class ExportadorPedidoTest {

    @Test
    public void testExportarPedidoPDFCreatesFile() throws Exception {
        File tmp = File.createTempFile("pedido", ".pdf");
        tmp.deleteOnExit();

        Pedido pedido = new Pedido();
        pedido.setNombreUsuario("ClientePrueba");
        pedido.setFechaPedido(java.time.LocalDate.of(2024, 5, 1));

        Producto p = new Producto();
        p.setNombre("Pizza");
        p.setPrecio(120.0);

        ProductoPedido pp = new ProductoPedido();
        pp.setProducto(p);
        pp.setCantidad(2);

        ArrayList<ProductoPedido> lista = new ArrayList<>();
        lista.add(pp);

        pedido.setProductos(lista);

        Exportador.exportarPedidoPDF(tmp.getAbsolutePath(), pedido);

        assertTrue(tmp.exists());
        assertTrue(tmp.length() > 0);
    }
    
    @Test
    public void testExportarPedidosCSVIncluyeProductosYCubreDatosFaltantes() throws IOException {
        File tmp = File.createTempFile("pedidos", ".csv");
        tmp.deleteOnExit();

        Pedido pedidoConProductos = new Pedido();
        pedidoConProductos.setNombreUsuario("Cliente Uno");
        pedidoConProductos.setFechaPedido(java.time.LocalDate.of(2026, 6, 1));
        pedidoConProductos.setEstatus(Pedido.EstatusPedido.EnPreparacion);
        pedidoConProductos.setTotalAPagar(240.0);

        Producto producto = new Producto();
        producto.setNombre("Pizza");

        ProductoPedido productoPedido = new ProductoPedido();
        productoPedido.setProducto(producto);
        productoPedido.setCantidad(2);

        ArrayList<ProductoPedido> productos = new ArrayList<>();
        productos.add(productoPedido);
        pedidoConProductos.setProductos(productos);

        Pedido pedidoSinProductos = new Pedido();
        pedidoSinProductos.setNombreUsuario(null);
        pedidoSinProductos.setFechaPedido(null);
        pedidoSinProductos.setEstatus(null);
        pedidoSinProductos.setTotalAPagar(null);
        pedidoSinProductos.setProductos(null);

        ArrayList<Pedido> pedidos = new ArrayList<>();
        pedidos.add(pedidoConProductos);
        pedidos.add(pedidoSinProductos);

        Exportador.exportarPedidosCSV(tmp.getAbsolutePath(), pedidos);

        List<String> lineas = Files.readAllLines(tmp.toPath(), StandardCharsets.UTF_8);
        assertEquals(3, lineas.size());
        assertTrue(lineas.get(1).contains("\"Cliente Uno\""));
        assertTrue(lineas.get(1).contains("\"Pizza x2; \""));
        assertTrue(lineas.get(1).contains("\"$ 240.00\""));
        assertTrue(lineas.get(2).contains("\"No aplica\""));
        assertTrue(lineas.get(2).contains("\"-\""));
        assertTrue(lineas.get(2).contains("\"No se cargaron productos\""));
    }
}
