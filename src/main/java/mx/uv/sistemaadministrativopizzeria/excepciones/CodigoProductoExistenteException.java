package mx.uv.sistemaadministrativopizzeria.excepciones;

/**
 * Excepción que indica que un código de producto ya existe en el sistema.
 */
public class CodigoProductoExistenteException extends Exception {

    public CodigoProductoExistenteException() {
        super("El código de producto ya se encuentra registrado.");
    }

    public CodigoProductoExistenteException(String message) {
        super(message);
    }
}
