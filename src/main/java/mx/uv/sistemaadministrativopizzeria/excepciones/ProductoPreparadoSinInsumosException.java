package mx.uv.sistemaadministrativopizzeria.excepciones;

/**
 * Excepción lanzada cuando se intenta guardar un producto preparado
 * sin haber asignado sus insumos (componentes) correspondientes.
 */
public class ProductoPreparadoSinInsumosException extends Exception {

    public ProductoPreparadoSinInsumosException() {
        super("Un producto preparado requiere al menos un insumo antes de ser guardado.");
    }

    public ProductoPreparadoSinInsumosException(String message) {
        super(message);
    }
}
