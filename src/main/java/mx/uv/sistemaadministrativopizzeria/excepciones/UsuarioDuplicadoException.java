package mx.uv.sistemaadministrativopizzeria.excepciones;

/**
 * Excepción que indica que un nombre de usuario ya existe en el sistema.
 */
public class UsuarioDuplicadoException extends Exception {

    public UsuarioDuplicadoException() {
        super("El nombre de usuario ya se encuentra registrado.");
    }

    public UsuarioDuplicadoException(String message) {
        super(message);
    }
}
