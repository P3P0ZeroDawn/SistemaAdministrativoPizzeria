package mx.uv.sistemaadministrativopizzeria.excepciones;

/**
 * Excepción que indica que no se puede eliminar un producto porque
 * ha sido usado en al menos un pedido.
 */
public class ProductoUsadoEnPedidoException extends Exception {

    public ProductoUsadoEnPedidoException() {
        super("No se puede eliminar el producto porque fue usado en al menos un pedido.");
    }

    public ProductoUsadoEnPedidoException(String message) {
        super(message);
    }
}
