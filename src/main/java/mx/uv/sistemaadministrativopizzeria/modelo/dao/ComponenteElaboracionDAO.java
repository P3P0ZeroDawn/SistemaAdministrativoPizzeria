package mx.uv.sistemaadministrativopizzeria.modelo.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mx.uv.sistemaadministrativopizzeria.modelo.MySQLConnectionManager;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ComponenteElaboracion;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;

public class ComponenteElaboracionDAO {


    public static List<ComponenteElaboracion> obtenerPorProducto(
            int idProductoPreparado) {

        List<ComponenteElaboracion> lista =
                new ArrayList<>();

        try {

            MySQLConnectionManager conn =
                    MySQLConnectionManager.buildConnection();

            String query =
                    "SELECT ce.cantidad, p.* "
                    + "FROM componenteelaboracion ce "
                    + "JOIN producto p "
                    + "ON ce.idProducto = p.idProducto "
                    + "WHERE ce.idPreparado = ?";

            PreparedStatement ps =
                    conn.prepareStatement(query);

            ps.setInt(1, idProductoPreparado);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ComponenteElaboracion componente =
                        new ComponenteElaboracion();

                Producto producto =
                        ProductoDAO.serializarProducto(rs);

                componente.setProducto(producto);

                componente.setCantidad(
                        rs.getDouble("cantidad")
                );

                lista.add(componente);
            }

        } catch (SQLException ex) {

            System.getLogger(
                    ComponenteElaboracionDAO.class.getName()
            ).log(
                    System.Logger.Level.ERROR,
                    (String) null,
                    ex
            );
        }

        return lista;
    }


    public static boolean guardarComponentes(
            int idProductoPreparado,
            List<ComponenteElaboracion> componentes) {

        try {

            MySQLConnectionManager conn =
                    MySQLConnectionManager.buildConnection();

            String query =
                    "INSERT INTO componenteelaboracion "
                    + "(idPreparado, idProducto, cantidad) "
                    + "VALUES (?, ?, ?)";

            PreparedStatement ps =
                    conn.prepareStatement(query);

            for (ComponenteElaboracion c : componentes) {

                ps.setInt(1, idProductoPreparado);

                ps.setInt(2,
                        c.getProducto().getIdProducto());

                ps.setDouble(3,
                        c.getCantidad());

                ps.addBatch();
            }

            ps.executeBatch();

            conn.close();

            return true;

        } catch (SQLException ex) {

            System.getLogger(
                    ComponenteElaboracionDAO.class.getName()
            ).log(
                    System.Logger.Level.ERROR,
                    (String) null,
                    ex
            );
        }

        return false;
    }

    public static boolean eliminarPorProducto(
            int idProductoPreparado) {

        try {

            MySQLConnectionManager conn =
                    MySQLConnectionManager.buildConnection();

            String query =
                    "DELETE FROM componenteelaboracion "
                    + "WHERE idPreparado = ?";

            PreparedStatement ps =
                    conn.prepareStatement(query);

            ps.setInt(1, idProductoPreparado);

            ps.executeUpdate();

            conn.close();

            return true;

        } catch (SQLException ex) {

            System.getLogger(
                    ComponenteElaboracionDAO.class.getName()
            ).log(
                    System.Logger.Level.ERROR,
                    (String) null,
                    ex
            );
        }

        return false;
    }
}