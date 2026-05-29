/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.modelo.dao;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import mx.uv.sistemaadministrativopizzeria.modelo.MySQLConnectionManager;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.HistorialInventario;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoHistorial;

/**
 *
 * @author hp
 */
public class HistorialInventarioDAO {

    public static List<HistorialInventario> obtenerHistorialesInventarioFecha(LocalDate fecha) {
        List<HistorialInventario> lista = null;
        try {
            lista = new ArrayList<>();
            MySQLConnectionManager conn = MySQLConnectionManager.buildConnection();
            String query = "SELECT idHistorialInventario, fecha FROM historialinventario WHERE DATE(fecha) = ?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDate(1, Date.valueOf(fecha));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HistorialInventario historial = new HistorialInventario();
                historial.setIdHistorialInventario(rs.getInt("idHistorialInventario"));
                java.sql.Timestamp timestamp = rs.getTimestamp("fecha");
                historial.setFecha(timestamp.toLocalDateTime());
                lista.add(historial);
            }
            conn.close();
        } catch (SQLException ex) {
            System.getLogger(HistorialInventarioDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return lista;
    }

    public static List<ProductoHistorial> obtenerProductosDeHistorial(int idHistorialInventario) {
        List<ProductoHistorial> lista = null;
        try {
            lista = new ArrayList<>();
            MySQLConnectionManager conn = MySQLConnectionManager.buildConnection();
            String query = "SELECT idProductoHistorial, idProducto, cantidadSistema, cantidadReal, razon, "
                    + " estatusExistencia, nombre, codigo, foto, unidadMedida"
                    + " FROM v_historialproducto"
                    + " WHERE idHistorialInventario = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, idHistorialInventario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(serializarProductoHistorial(rs));
            }
            conn.close();
        } catch (SQLException | NullPointerException ex) {
            System.getLogger(HistorialInventarioDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return lista;
    }

    public static boolean guardarHistorialInventario(
            List<ProductoHistorial> productos) {

        try {

            MySQLConnectionManager conn
                    = MySQLConnectionManager
                            .buildConnection();

            conn.setAutoCommit(false);

            /*
         * INSERTAR HISTORIAL
             */
            String queryHistorial
                    = "INSERT INTO historialinventario "
                    + "(fecha) VALUES (LOCALTIME())";

            PreparedStatement psHistorial
                    = conn.prepareStatement(
                            queryHistorial,
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );

            psHistorial.executeUpdate();

            ResultSet rs
                    = psHistorial.getGeneratedKeys();

            int idHistorial = -1;

            if (rs.next()) {

                idHistorial = rs.getInt(1);
            }

            if (idHistorial <= 0) {

                conn.rollback();

                conn.close();

                return false;
            }

            /*
         * INSERTAR PRODUCTOS
             */
            String queryProducto
                    = "INSERT INTO productohistorial "
                    + "(idHistorialInventario, "
                    + "idProducto, "
                    + "cantidadSistema, "
                    + "cantidadReal, "
                    + "razon, "
                    + "estatusExistencia) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement psProducto
                    = conn.prepareStatement(
                            queryProducto
                    );

            for (ProductoHistorial p : productos) {

                psProducto.setInt(
                        1,
                        idHistorial
                );

                psProducto.setInt(
                        2,
                        p.getProducto()
                                .getIdProducto()
                );

                psProducto.setDouble(
                        3,
                        p.getCantidadSistema()
                );

                psProducto.setDouble(
                        4,
                        p.getCantidadReal()
                );

                psProducto.setString(
                        5,
                        p.getRazon()
                );

                psProducto.setString(
                        6,
                        p.getEstatusExistencia()
                                .name()
                );

                psProducto.addBatch();
                
                 double nuevoStock = calcularNuevoStock(
                        p.getCantidadSistema(),
                        p.getCantidadReal(),
                        p.getProducto().getCantidad()
                );

                actualizarStockProducto(
                        conn,
                        p.getProducto().getIdProducto(),
                        nuevoStock
                );
            }

            psProducto.executeBatch();

            conn.commit();

            conn.close();

            return true;

        } catch (SQLException ex) {

            System.getLogger(
                    HistorialInventarioDAO.class
                            .getName()
            ).log(
                    System.Logger.Level.ERROR,
                    (String) null,
                    ex
            );
        }

        return false;

    }
    
    private static double calcularNuevoStock(
            double sistema,
            double real,
            double stockActual
    ) {

        double diferencia = real - sistema;

        return stockActual + diferencia;
    }
    
    private static void actualizarStockProducto(
            MySQLConnectionManager conn,
            int idProducto,
            double nuevoStock
    ) throws SQLException {

        String query =
                "UPDATE producto SET cantidad = ? "
              + "WHERE idProducto = ?";

        PreparedStatement ps =
                conn.prepareStatement(query);

        ps.setDouble(1, nuevoStock);
        ps.setInt(2, idProducto);

        ps.executeUpdate();
    }

    private static ProductoHistorial serializarProductoHistorial(ResultSet rs) throws SQLException, NullPointerException {
        ProductoHistorial producto = null;
        if (rs != null) {
            producto = new ProductoHistorial();
            producto.setProducto(new Producto());
            producto.setIdProductoHistorial(rs.getInt("idProductoHistorial"));
            producto.setCantidadReal(rs.getDouble("cantidadReal"));
            producto.setRazon(rs.getString("razon"));
            producto.getProducto().setIdProducto(rs.getInt("idProducto"));
            producto.setCantidadSistema(rs.getDouble("cantidadSistema"));
            producto.getProducto().setNombre(rs.getString("nombre"));
            producto.getProducto().setCodigo(rs.getString("codigo"));
            producto.getProducto().setUnidadMedida(rs.getString("unidadMedida"));

            String estatusString = rs.getString("estatusExistencia");
            Blob blob = rs.getBlob("foto");
            if (estatusString == null) {
                throw new NullPointerException("No se logró recuperar toda la información. Estatus no válido");
            }
            if (blob != null) {
                byte[] bytes = blob.getBytes(1, (int) blob.length());
                Image foto = new Image(new ByteArrayInputStream(bytes));
                producto.getProducto().setFoto(foto);
            } else {
                producto.getProducto().setFoto(null);
            }

            try {
                ProductoHistorial.EstatusExistencia estatus = ProductoHistorial.EstatusExistencia.valueOf(estatusString);
                producto.setEstatusExistencia(estatus);
            } catch (IllegalArgumentException e) {
                throw new NullPointerException("No se logró convertir un dato. La información recuperada pudo corromperse");
            }
            return producto;
        }
        throw new NullPointerException("No se logró recuperar toda la información");
    }
}
