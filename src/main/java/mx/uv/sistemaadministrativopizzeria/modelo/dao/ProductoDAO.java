/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.modelo.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mx.uv.sistemaadministrativopizzeria.modelo.MySQLConnectionManager;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;

/**
 *
 * @author pedro
 */
public class ProductoDAO {

    public static List<Producto> obtenerProductos() {

        List<Producto> lista = new ArrayList<>();

        try {

            MySQLConnectionManager conn =
                    MySQLConnectionManager.buildConnection();

            String query =
                    "SELECT * FROM producto WHERE activo = 1";

            PreparedStatement ps =
                    conn.prepareStatement(query);

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

                producto.setPrecio(
                        rs.getDouble("precio"));

                producto.setCantidad(
                        rs.getDouble("cantidad"));

                producto.setUnidadMedida(
                        rs.getString("unidadMedida"));

                producto.setActivo(
                        rs.getInt("activo") == 1);

                producto.setEsPreparado(
                        rs.getInt("esPreparado") == 1);

                producto.setEsInsumo(
                        rs.getInt("esInsumo") == 1);
            }

        } catch (SQLException ex) {

            System.getLogger(ProductoDAO.class.getName())
                    .log(System.Logger.Level.ERROR,
                            (String) null,
                            ex);
        }

        return producto;
    }
    
    public static boolean eliminarProducto(
        Producto producto) {

        if (producto != null) {

            try {

                MySQLConnectionManager conn =
                        MySQLConnectionManager.buildConnection();

                String query =
                        "UPDATE producto "
                        + "SET activo = 0 "
                        + "WHERE idProducto = ?";

                PreparedStatement ps =
                        conn.prepareStatement(query);

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
}
