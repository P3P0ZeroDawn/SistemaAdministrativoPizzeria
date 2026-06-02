package mx.uv.sistemaadministrativopizzeria.utilidades;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.opencsv.CSVWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;

public class Exportador {

    /*
     * COLORES
     */
    private static final Color COLOR_HEADER =
            new DeviceRgb(30, 41, 59);

    private static final Color COLOR_FILAPAR =
            new DeviceRgb(241, 245, 249);

    /*
     * ============================
     * EXPORTAR CSV
     * ============================
     */
    public static void exportarInventarioCSV(
            String rutaCSV,
            List<Producto> productos)
            throws IOException {

        CSVWriter csv =
                new CSVWriter(
                        new FileWriter(rutaCSV)
                );

        String[] encabezados = {
            "NOMBRE",
            "CODIGO",
            "DESCRIPCION",
            "PRECIO",
            "CANTIDAD",
            "UNIDAD",
            "TIPO"
        };

        csv.writeNext(encabezados);

        for (Producto p : productos) {

            String tipo = obtenerTipo(p);

            String precio =
                    (p.getEsInsumo() == false)
                    ? valorSeguroDinero(p.getPrecio())
                    : "No aplica";

            String cantidad =
                    (p.getEsPreparado() == false)
                    ? valorSeguro(p.getCantidad())
                    : "No aplica";

            String unidad =
                    (p.getEsPreparado() == false)
                    ? textoSeguro(p.getUnidadMedida())
                    : "No aplica";

            String[] fila = {
                textoSeguro(p.getNombre()),
                textoSeguro(p.getCodigo()),
                textoSeguro(p.getDescripcion()),
                precio,
                cantidad,
                unidad,
                tipo
            };

            csv.writeNext(fila);
        }

        csv.close();
    }

    /*
     * ============================
     * EXPORTAR PDF
     * ============================
     */
    public static void exportarInventarioPDF(
            String rutaPDF,
            List<Producto> productos)
            throws FileNotFoundException,
            MalformedURLException {

        PdfDocument pdf =
                new PdfDocument(
                        new PdfWriter(rutaPDF)
                );

        Document documento =
                new Document(pdf);

        documento.setMargins(
                30,
                30,
                30,
                30
        );

        /*
         * LOGO
         */
        try {

            Image logo =
                    new Image(
                            ImageDataFactory.create(
                                    "src/main/resources/imagenes/logo.png"
                            )
                    );

            logo.setWidth(110);

            logo.setHorizontalAlignment(
                    HorizontalAlignment.CENTER
            );

            documento.add(logo);

        } catch (Exception ex) {

            System.out.println(
                    "No se pudo cargar el logo"
            );
        }

        /*
         * TITULO
         */
        Paragraph titulo =
                new Paragraph(
                        "REPORTE DE INVENTARIO"
                );

        titulo.setBold();

        titulo.setFontSize(22);

        titulo.setTextAlignment(
                TextAlignment.CENTER
        );

        titulo.setMarginBottom(5);

        documento.add(titulo);

        /*
         * FECHA
         */
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm:ss"
                );

        Paragraph fecha =
                new Paragraph(
                        "Fecha de generación: "
                        + LocalDateTime.now()
                                .format(formatter)
                );

        fecha.setTextAlignment(
                TextAlignment.CENTER
        );

        fecha.setFontColor(
                ColorConstants.GRAY
        );

        fecha.setMarginBottom(20);

        documento.add(fecha);

        /*
         * TABLA
         */
        float[] columnas = {
            3,
            2,
            4,
            2,
            2,
            2,
            2
        };

        Table tabla =
                new Table(columnas);

        tabla.useAllAvailableWidth();

        tabla.setHorizontalAlignment(
                HorizontalAlignment.CENTER
        );

        /*
         * HEADERS
         */
        tabla.addHeaderCell(
                crearHeaderTabla("NOMBRE")
        );

        tabla.addHeaderCell(
                crearHeaderTabla("CODIGO")
        );

        tabla.addHeaderCell(
                crearHeaderTabla("DESCRIPCION")
        );

        tabla.addHeaderCell(
                crearHeaderTabla("PRECIO")
        );

        tabla.addHeaderCell(
                crearHeaderTabla("CANTIDAD")
        );

        tabla.addHeaderCell(
                crearHeaderTabla("UNIDAD")
        );

        tabla.addHeaderCell(
                crearHeaderTabla("TIPO")
        );

        /*
         * DATOS
         */
        int fila = 0;

        for (Producto p : productos) {

            String tipo = obtenerTipo(p);

            String precio =
                    (p.getEsInsumo() == false)
                    ? valorSeguroDinero(p.getPrecio())
                    : "No aplica";

            String cantidad =
                    (p.getEsPreparado() == false)
                    ? valorSeguro(p.getCantidad())
                    : "No aplica";

            String unidad =
                    (p.getEsPreparado() == false)
                    ? textoSeguro(p.getUnidadMedida())
                    : "No aplica";

            Color fondo =
                    (fila % 2 == 0)
                    ? COLOR_FILAPAR
                    : ColorConstants.WHITE;

            tabla.addCell(
                    crearCeldaTabla(
                            textoSeguro(
                                    p.getNombre()
                            ),
                            fondo
                    )
            );

            tabla.addCell(
                    crearCeldaTabla(
                            textoSeguro(
                                    p.getCodigo()
                            ),
                            fondo
                    )
            );

            tabla.addCell(
                    crearCeldaTabla(
                            textoSeguro(
                                    p.getDescripcion()
                            ),
                            fondo
                    )
            );

            tabla.addCell(
                    crearCeldaTabla(
                            precio,
                            fondo
                    )
            );

            tabla.addCell(
                    crearCeldaTabla(
                            cantidad,
                            fondo
                    )
            );

            tabla.addCell(
                    crearCeldaTabla(
                            unidad,
                            fondo
                    )
            );

            tabla.addCell(
                    crearCeldaTabla(
                            tipo,
                            fondo
                    )
            );

            fila++;
        }

        documento.add(tabla);

        documento.close();
    }

        public static void exportarPedidosCSV(
                        String rutaCSV,
                        java.util.List<mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido> pedidos)
                        throws java.io.IOException {

                CSVWriter csv = new CSVWriter(new FileWriter(rutaCSV));

                String[] encabezados = {
                        "CLIENTE",
                        "FECHA",
                        "ESTATUS",
                        "TOTAL",
                        "PRODUCTOS"
                };

                csv.writeNext(encabezados);

                for (mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido pedido : pedidos) {
                        String cliente = textoSeguro(pedido.getNombreUsuario());
                        String fechaStr = pedido.getFechaPedido() != null ? pedido.getFechaPedido().toString() : "-";
                        String estatus = pedido.getEstatus() != null ? pedido.getEstatus().toString() : "-";
                        String total = valorSeguroDinero(pedido.getTotalAPagar());

                        StringBuilder productosTxt = new StringBuilder();
                        if (pedido.getProductos() != null && !pedido.getProductos().isEmpty()) {
                                for (mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoPedido pp : pedido.getProductos()) {
                                        String nombre = pp.getProducto() != null ? pp.getProducto().getNombre() : "-";
                                        productosTxt.append(nombre).append(" x").append(pp.getCantidad()).append("; ");
                                }
                        } else {
                                productosTxt.append("No se cargaron productos");
                        }

                        String[] fila = {
                                cliente,
                                fechaStr,
                                estatus,
                                total,
                                productosTxt.toString()
                        };

                        csv.writeNext(fila);
                }

                csv.close();
        }

    /*
     * ============================
     * CELDAS
     * ============================
     */
    private static Cell crearCeldaTabla(
            String texto,
            Color fondo) {

        Paragraph parrafo =
                new Paragraph(texto);

        parrafo.setFontSize(10);

        Cell celda =
                new Cell();

        celda.add(parrafo);

        celda.setPadding(8);

        celda.setBackgroundColor(fondo);

        celda.setTextAlignment(
                TextAlignment.CENTER
        );

        return celda;
    }

    private static Cell crearHeaderTabla(
            String texto) {

        Paragraph parrafo =
                new Paragraph(texto);

        parrafo.setBold();

        parrafo.setFontColor(
                ColorConstants.WHITE
        );

        parrafo.setFontSize(11);

        Cell celda =
                new Cell();

        celda.add(parrafo);

        celda.setBackgroundColor(
                COLOR_HEADER
        );

        celda.setTextAlignment(
                TextAlignment.CENTER
        );

        celda.setPadding(10);

        return celda;
    }

    /*
     * ============================
     * UTILIDADES
     * ============================
     */
    private static String obtenerTipo(
            Producto p) {

        if (p.getEsPreparado()) {
            return "Preparado";
        }

        if (p.getEsInsumo()) {
            return "Insumo";
        }

        return "No aplica";
    }

    private static String textoSeguro(
            String texto) {

        return texto != null
                && !texto.isBlank()
                ? texto
                : "No aplica";
    }

    private static String valorSeguro(
            Double valor) {

        return valor != null
                ? String.valueOf(valor)
                : "No aplica";
    }

    private static String valorSeguroDinero(
            Double valor) {

        return valor != null
                ? String.format(
                        "$ %,.2f",
                        valor
                )
                : "No aplica";
    }

        /*
         * ============================
         * EXPORTAR PEDIDO A PDF
         * ============================
         */
        public static void exportarPedidoPDF(
                        String rutaPDF,
                        mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido pedido)
                        throws java.io.FileNotFoundException,
                        java.net.MalformedURLException {

                PdfDocument pdf = new PdfDocument(new PdfWriter(rutaPDF));

                Document documento = new Document(pdf);

                documento.setMargins(30, 30, 30, 30);

                // --- Header / logo (best-effort) ---
                try {
                        Image logo = new Image(ImageDataFactory.create("src/main/resources/imagenes/logo.png"));
                        logo.setWidth(110);
                        logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
                        documento.add(logo);
                } catch (Exception ex) {
                        System.out.println("No se pudo cargar el logo");
                }

                Paragraph titulo = new Paragraph("PEDIDO")
                                .setBold()
                                .setFontSize(18)
                                .setTextAlignment(TextAlignment.CENTER)
                                .setMarginBottom(5);

                documento.add(titulo);

                Paragraph meta = new Paragraph(
                                "Cliente: " + (pedido.getNombreUsuario() != null ? pedido.getNombreUsuario() : "-")
                                + "    Fecha: " + (pedido.getFechaPedido() != null ? pedido.getFechaPedido().toString() : "-")
                                + "    Estatus: " + (pedido.getEstatus() != null ? pedido.getEstatus().toString() : "-")
                );

                meta.setTextAlignment(TextAlignment.CENTER);
                meta.setFontColor(ColorConstants.GRAY);
                meta.setMarginBottom(10);
                documento.add(meta);

                // Table: Producto | Cantidad | Precio unitario | Subtotal
                float[] columnas = {4, 1, 2, 2};
                Table tabla = new Table(columnas);
                tabla.useAllAvailableWidth();

                tabla.addHeaderCell(crearHeaderTabla("PRODUCTO"));
                tabla.addHeaderCell(crearHeaderTabla("CANT."));
                tabla.addHeaderCell(crearHeaderTabla("PRECIO"));
                tabla.addHeaderCell(crearHeaderTabla("SUBTOTAL"));

                double total = 0.0;

                if (pedido.getProductos() != null) {
                        for (mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoPedido pp : pedido.getProductos()) {
                                String nombre = pp.getProducto() != null ? pp.getProducto().getNombre() : "-";
                                Double precio = pp.getProducto() != null ? pp.getProducto().getPrecio() : null;
                                int cantidad = pp.getCantidad();
                                double subtotal = (precio != null ? precio : 0.0) * cantidad;
                                total += subtotal;

                                tabla.addCell(crearCeldaTabla(textoSeguro(nombre), ColorConstants.WHITE));
                                tabla.addCell(crearCeldaTabla(String.valueOf(cantidad), ColorConstants.WHITE));
                                tabla.addCell(crearCeldaTabla(valorSeguroDinero(precio), ColorConstants.WHITE));
                                tabla.addCell(crearCeldaTabla(valorSeguroDinero(subtotal), ColorConstants.WHITE));
                        }
                }

                documento.add(tabla);

                Paragraph resumen = new Paragraph("Total: " + valorSeguroDinero(total));
                resumen.setTextAlignment(TextAlignment.RIGHT);
                resumen.setBold();
                resumen.setMarginTop(10);
                documento.add(resumen);

                documento.close();
        }

        /*
         * ============================
         * EXPORTAR LISTA DE PEDIDOS (RESULTADOS DE BÚSQUEDA)
         * ============================
         */
        public static void exportarPedidosBusquedaPDF(
                        String rutaPDF,
                        java.util.List<mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido> pedidos)
                        throws java.io.FileNotFoundException,
                        java.net.MalformedURLException {

                PdfDocument pdf = new PdfDocument(new PdfWriter(rutaPDF));
                Document documento = new Document(pdf);
                documento.setMargins(30, 30, 30, 30);

                try {
                        Image logo = new Image(ImageDataFactory.create("src/main/resources/imagenes/logo.png"));
                        logo.setWidth(110);
                        logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
                        documento.add(logo);
                } catch (Exception ex) {
                        System.out.println("No se pudo cargar el logo");
                }

                Paragraph titulo = new Paragraph("REPORTE DE PEDIDOS")
                                .setBold()
                                .setFontSize(18)
                                .setTextAlignment(TextAlignment.CENTER)
                                .setMarginBottom(5);

                documento.add(titulo);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                Paragraph fecha = new Paragraph("Fecha de generación: " + LocalDateTime.now().format(formatter));
                fecha.setTextAlignment(TextAlignment.CENTER);
                fecha.setFontColor(ColorConstants.GRAY);
                fecha.setMarginBottom(10);
                documento.add(fecha);

                // Cabecera de la tabla de pedidos (sin ID)
                float[] columnas = {3, 2, 2, 2, 5};
                Table tabla = new Table(columnas);
                tabla.useAllAvailableWidth();

                tabla.addHeaderCell(crearHeaderTabla("CLIENTE"));
                tabla.addHeaderCell(crearHeaderTabla("FECHA"));
                tabla.addHeaderCell(crearHeaderTabla("ESTATUS"));
                tabla.addHeaderCell(crearHeaderTabla("TOTAL"));
                tabla.addHeaderCell(crearHeaderTabla("PRODUCTOS"));

                for (mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido pedido : pedidos) {
                            String cliente = textoSeguro(pedido.getNombreUsuario());
                        String fechaStr = pedido.getFechaPedido() != null ? pedido.getFechaPedido().toString() : "-";
                        String estatus = pedido.getEstatus() != null ? pedido.getEstatus().toString() : "-";
                        String total = valorSeguroDinero(pedido.getTotalAPagar());

                        // Concatenar productos
                        StringBuilder productosTxt = new StringBuilder();
                        if (pedido.getProductos() != null && !pedido.getProductos().isEmpty()) {
                                for (mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoPedido pp : pedido.getProductos()) {
                                        String nombre = pp.getProducto() != null ? pp.getProducto().getNombre() : "-";
                                        productosTxt.append(nombre).append(" x").append(pp.getCantidad()).append("; ");
                                }
                        } else {
                                productosTxt.append("No se cargaron productos");
                        }

                        tabla.addCell(crearCeldaTabla(cliente, ColorConstants.WHITE));
                        tabla.addCell(crearCeldaTabla(fechaStr, ColorConstants.WHITE));
                        tabla.addCell(crearCeldaTabla(estatus, ColorConstants.WHITE));
                        tabla.addCell(crearCeldaTabla(total, ColorConstants.WHITE));
                        tabla.addCell(crearCeldaTabla(productosTxt.toString(), ColorConstants.WHITE));
                }

                documento.add(tabla);
                documento.close();
        }
}