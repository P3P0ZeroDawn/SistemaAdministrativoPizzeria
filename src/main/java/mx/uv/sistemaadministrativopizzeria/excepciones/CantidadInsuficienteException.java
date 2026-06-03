package mx.uv.sistemaadministrativopizzeria.excepciones;

/**
 * Excepción lanzada cuando no hay suficiente cantidad de un producto
 * para agregar a un pedido (no se permite stock negativo).
 */
public class CantidadInsuficienteException extends Exception {

    public CantidadInsuficienteException() {
        super("No hay suficiente cantidad disponible\npara agregar al pedido.");
    }

    public CantidadInsuficienteException(String message) {
        super(message);
    }
}
