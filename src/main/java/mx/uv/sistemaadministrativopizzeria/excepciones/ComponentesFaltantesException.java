package mx.uv.sistemaadministrativopizzeria.excepciones;

/**
 * Excepción que indica que no se han agregado componentes
 * a un producto preparado cuando se requiere al menos uno.
 */
public class ComponentesFaltantesException extends Exception {

    public ComponentesFaltantesException() {
        super("Debes agregar al menos un componente para este producto preparado.");
    }

    public ComponentesFaltantesException(String message) {
        super(message);
    }
}
