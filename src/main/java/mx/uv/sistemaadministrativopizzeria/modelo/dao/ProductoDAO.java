package mx.uv.sistemaadministrativopizzeria.modelo.dao;

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import mx.uv.sistemaadministrativopizzeria.modelo.MySQLConnectionManager;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ComponenteElaboracion;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;

/**
 *
 * @author pedro
 */
public class ProductoDAO {

    public static List<Producto> obtenerProductos() {

        List<Producto> lista = new ArrayList<>();

        try {

            MySQLConnectionManager conn
                    = MySQLConnectionManager.buildConnection();

            String query
                    = "SELECT * FROM producto WHERE activo = 1";

            PreparedStatement ps
                    = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                lista.add(
                        serializarProducto(rs)
                );
            }

            conn.close();

        } catch (SQLException ex) {

            System.getLogger(ProductoDAO.class.getName())
                    .log(System.Logger.Level.ERROR,
                            (String) null,
                            ex);
        }

        return lista;
    }

    public static List<Producto> obtenerProdParaPedido() {

        List<Producto> lista = new ArrayList<>();

        try {

            MySQLConnectionManager conn
                    = MySQLConnectionManager.buildConnection();

            String query
                    = "SELECT * FROM producto WHERE activo = 1 AND esInsumo = 0";

            PreparedStatement ps
                    = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                lista.add(
                        serializarProducto(rs)
                );
            }

            conn.close();

        } catch (SQLException ex) {

            System.getLogger(ProductoDAO.class.getName())
                    .log(System.Logger.Level.ERROR,
                            (String) null,
                            ex);
        }

        return lista;
    }

    public static Producto obtenerProducto(int idProducto) {

        Producto producto = null;

        try {

            MySQLConnectionManager conn
                    = MySQLConnectionManager.buildConnection();

            String query
                    = "SELECT * FROM producto WHERE idProducto = ?";

            PreparedStatement ps
                    = conn.prepareStatement(query);
            ps.setInt(1, idProducto);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                producto = serializarProducto(rs);
            }
            conn.close();

        } catch (SQLException ex) {

            System.getLogger(ProductoDAO.class.getName())
                    .log(System.Logger.Level.ERROR,
                            (String) null,
                            ex);
        }
        return producto;
    }

    public static Producto obtenerProductosProducto(
            Producto producto) {

        List<ComponenteElaboracion> lista
                = new ArrayList<>();

        try {

            MySQLConnectionManager conn
                    = MySQLConnectionManager.buildConnection();

            String query
                    = "SELECT * FROM v_productoComponente"
                    + " WHERE idPreparado = ?;";

            PreparedStatement ps
                    = conn.prepareStatement(query);

            ps.setInt(1,
                    producto.getIdProducto());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Producto p
                        = serializarProducto(rs);

                ComponenteElaboracion componente
                        = new ComponenteElaboracion();

                componente.setProducto(p);

                componente.setCantidad(
                        rs.getDouble("cantidadCP")
                );

                lista.add(componente);
            }

            conn.close();

        } catch (SQLException ex) {

            System.getLogger(
                    ProductoDAO.class.getName()
            ).log(
                    System.Logger.Level.ERROR,
                    (String) null,
                    ex
            );
        }

        producto.setComponentes(
                new ArrayList<>(lista)
        );

        return producto;
    }

    public static boolean verificarCodigoExistente(String codigo) {
        int count = 0;

        try {
            MySQLConnectionManager conn = MySQLConnectionManager.buildConnection();
            String query = "SELECT COUNT(*) FROM producto WHERE codigo = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            conn.close();
        } catch (SQLException ex) {
            System.getLogger(
                    ProductoDAO.class.getName()
            ).log(
                    System.Logger.Level.ERROR,
                    (String) null,
                    ex
            );
        }
        return (count > 0);
    }

    public static List<Producto>
            obtenerProductosValidacionInventario() {

        List<Producto> lista
                = new ArrayList<>();

        try {

            MySQLConnectionManager conn
                    = MySQLConnectionManager
                            .buildConnection();

            String query
                    = "SELECT * FROM producto "
                    + "WHERE activo = 1 "
                    + "AND esPreparado = 0";

            PreparedStatement ps
                    = conn.prepareStatement(query);

            ResultSet rs
                    = ps.executeQuery();

            while (rs.next()) {

                lista.add(
                        serializarProducto(rs)
                );
            }

            conn.close();

        } catch (SQLException ex) {

            System.getLogger(
                    ProductoDAO.class.getName()
            ).log(
                    System.Logger.Level.ERROR,
                    (String) null,
                    ex
            );
        }

        return lista;

    }

    public static Producto buscarProducto(int idProducto) {

        Producto producto = null;

        try {

            MySQLConnectionManager conn
                    = MySQLConnectionManager.buildConnection();

            String query
                    = "SELECT * FROM producto "
                    + "WHERE idProducto = ?";

            PreparedStatement ps
                    = conn.prepareStatement(query);

            ps.setInt(1, idProducto);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                producto = serializarProducto(rs);
            }

            conn.close();

        } catch (SQLException ex) {

            System.getLogger(ProductoDAO.class.getName())
                    .log(System.Logger.Level.ERROR,
                            (String) null,
                            ex);
        }

        return producto;
    }

    public static int registrarProducto(
            Producto producto,
            byte[] fotoBytes) throws mx.uv.sistemaadministrativopizzeria.excepciones.CodigoProductoExistenteException {

        if (producto != null) {

            try {

                MySQLConnectionManager conn
                        = MySQLConnectionManager.buildConnection();

                String query
                        = "INSERT INTO producto "
                        + "(nombre, codigo, descripcion, "
                        + "precio, foto, cantidad, unidadMedida, "
                        + "activo, esPreparado, esInsumo) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                // Verificar código duplicado antes de insertar
                if (verificarCodigoExistente(producto.getCodigo())) {
                    conn.close();
                    throw new mx.uv.sistemaadministrativopizzeria.excepciones.CodigoProductoExistenteException();
                }

                PreparedStatement ps
                        = conn.prepareStatement(
                                query,
                                java.sql.Statement.RETURN_GENERATED_KEYS
                        );

                ps.setString(1,
                        producto.getNombre());

                ps.setString(2,
                        producto.getCodigo());

                ps.setString(3,
                        producto.getDescripcion());

                ps.setObject(4,
                        producto.getPrecio());

                ps.setBytes(5,
                        fotoBytes);

                ps.setObject(6,
                        producto.getCantidad());

                ps.setString(7,
                        producto.getUnidadMedida());

                ps.setInt(8,
                        producto.getActivo() ? 1 : 0);

                ps.setInt(9,
                        producto.getEsPreparado() ? 1 : 0);

                ps.setInt(10,
                        producto.getEsInsumo() ? 1 : 0);

                int filas = ps.executeUpdate();

                if (filas > 0) {

                    ResultSet rs
                            = ps.getGeneratedKeys();

                    if (rs.next()) {

                        int idGenerado
                                = rs.getInt(1);

                        conn.close();

                        return idGenerado;
                    }
                }

                conn.close();

            } catch (SQLException ex) {

                System.getLogger(ProductoDAO.class.getName())
                        .log(
                                System.Logger.Level.ERROR,
                                (String) null,
                                ex
                        );
            }
        }

        return -1;
    }

    public static boolean actualizarProducto(
            Producto producto,
            byte[] fotoBytes,
            boolean modificarFoto) {

        if (producto != null) {

            try {

                MySQLConnectionManager conn
                        = MySQLConnectionManager.buildConnection();

                String campoFoto
                        = modificarFoto
                                ? ", foto = ? "
                                : "";

                String query
                        = "UPDATE producto SET "
                        + "nombre = ?, "
                        + "descripcion = ?, "
                        + "precio = ?, "
                        + "cantidad = ?, "
                        + "unidadMedida = ?, "
                        + "esPreparado = ?, "
                        + "esInsumo = ? "
                        + campoFoto
                        + "WHERE idProducto = ?";

                PreparedStatement ps
                        = conn.prepareStatement(query);

                int indice = 1;

                ps.setString(indice++,
                        producto.getNombre());

                ps.setString(indice++,
                        producto.getDescripcion());

                /*
                 * PUEDE SER NULL
                 */
                ps.setObject(indice++,
                        producto.getPrecio());

                /*
                 * PUEDE SER NULL
                 */
                ps.setObject(indice++,
                        producto.getCantidad());

                /*
                 * PUEDE SER NULL
                 */
                ps.setString(indice++,
                        producto.getUnidadMedida());

                ps.setInt(indice++,
                        producto.getEsPreparado() ? 1 : 0);

                ps.setInt(indice++,
                        producto.getEsInsumo() ? 1 : 0);

                if (modificarFoto) {

                    ps.setBytes(indice++,
                            fotoBytes);
                }

                ps.setInt(indice++,
                        producto.getIdProducto());

                ps.executeUpdate();

                conn.close();

                return true;

            } catch (SQLException ex) {

                System.getLogger(ProductoDAO.class.getName())
                        .log(System.Logger.Level.ERROR,
                                (String) null,
                                ex);
            }
        }

        return false;
    }

    public static List<Producto> buscarProductos(
            String busqueda,
            boolean porNombre,
            boolean porCodigo) {

        List<Producto> lista = new ArrayList<>();

        try {

            MySQLConnectionManager conn
                    = MySQLConnectionManager.buildConnection();

            StringBuilder query
                    = new StringBuilder(
                            "SELECT * FROM producto "
                            + "WHERE activo = 1"
                    );

            List<String> filtros
                    = new ArrayList<>();

            if (porNombre) {

                filtros.add("nombre LIKE ?");
            }

            if (porCodigo) {

                filtros.add("codigo LIKE ?");
            }

            if (!filtros.isEmpty()) {

                query.append(" AND (");

                query.append(
                        String.join(" OR ", filtros)
                );

                query.append(")");
            }

            PreparedStatement ps
                    = conn.prepareStatement(
                            query.toString()
                    );

            int indice = 1;

            if (porNombre) {

                ps.setString(indice++,
                        "%" + busqueda + "%");
            }

            if (porCodigo) {

                ps.setString(indice++,
                        "%" + busqueda + "%");
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                lista.add(
                        serializarProducto(rs)
                );
            }

            conn.close();

        } catch (SQLException ex) {

            System.getLogger(ProductoDAO.class.getName())
                    .log(System.Logger.Level.ERROR,
                            (String) null,
                            ex);
        }

        return lista;
    }

    public static boolean eliminarProducto(
            Producto producto) throws mx.uv.sistemaadministrativopizzeria.excepciones.ProductoUsadoEnPedidoException {

        if (producto != null) {

            try {

                MySQLConnectionManager conn
                        = MySQLConnectionManager.buildConnection();

                // Revisar si el producto fue usado en algún pedido
                String queryCheck = "SELECT 1 FROM productoPedido WHERE idProducto = ? LIMIT 1";
                java.sql.PreparedStatement psCheck = conn.prepareStatement(queryCheck);
                psCheck.setInt(1, producto.getIdProducto());
                java.sql.ResultSet rsCheck = psCheck.executeQuery();
                if (rsCheck.next()) {
                    conn.close();
                    throw new mx.uv.sistemaadministrativopizzeria.excepciones.ProductoUsadoEnPedidoException();
                }

                String query
                        = "UPDATE producto "
                        + "SET activo = 0 "
                        + "WHERE idProducto = ?";

                PreparedStatement ps
                        = conn.prepareStatement(query);

                ps.setInt(1,
                        producto.getIdProducto());

                ps.executeUpdate();

                conn.close();

                return true;

            } catch (SQLException ex) {

                System.getLogger(ProductoDAO.class.getName())
                        .log(System.Logger.Level.ERROR,
                                (String) null,
                                ex);
            }
        }

        return false;
    }

    public static Producto serializarProducto(ResultSet rs) {

        Producto producto = null;

        try {

            if (rs != null) {

                producto = new Producto();

                producto.setIdProducto(
                        rs.getInt("idProducto"));

                producto.setNombre(
                        rs.getString("nombre"));

                producto.setCodigo(
                        rs.getString("codigo"));

                producto.setDescripcion(
                        rs.getString("descripcion"));

                Double precio
                        = rs.getObject(
                                "precio",
                                Double.class
                        );

                producto.setPrecio(precio);

                Double cantidad
                        = rs.getObject(
                                "cantidad",
                                Double.class
                        );

                producto.setCantidad(cantidad);

                producto.setUnidadMedida(
                        rs.getString("unidadMedida"));

                producto.setActivo(
                        rs.getInt("activo") == 1);

                producto.setEsPreparado(
                        rs.getInt("esPreparado") == 1);

                producto.setEsInsumo(
                        rs.getInt("esInsumo") == 1);

                byte[] fotoBytes
                        = rs.getBytes("foto");

                if (fotoBytes != null) {

                    producto.setFoto(
                            new Image(
                                    new ByteArrayInputStream(
                                            fotoBytes
                                    )
                            )
                    );
                }
            }

        } catch (SQLException ex) {

            System.getLogger(ProductoDAO.class.getName())
                    .log(System.Logger.Level.ERROR,
                            (String) null,
                            ex);
        }

        return producto;
    }
}
