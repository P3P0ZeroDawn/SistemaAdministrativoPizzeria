package mx.uv.sistemaadministrativopizzeria.utilidades;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
}
