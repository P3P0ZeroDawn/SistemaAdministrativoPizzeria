/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.modelo.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import mx.uv.sistemaadministrativopizzeria.modelo.MySQLConnectionManager;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ComponenteElaboracion;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoPedido;

/**
 *
 * @author hp
 */
public class PedidoDAO {

    public static List<Pedido> obtenerPedidos() {
        List<Pedido> lista = null;
        try {
            lista = new ArrayList<>();
            MySQLConnectionManager conn = MySQLConnectionManager.buildConnection();
            String query = "SELECT p.idPedido, p.idUsuario, p.fechaPedido, p.total, p.estatus,"
                    + " u.nombre, u.apellidoPaterno, u.apellidoMaterno"
                    + " FROM pedido AS p JOIN Usuario AS u ON p.idUsuario = u.idUsuario;";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(serializarPedido(rs));
            }
            conn.close();
        } catch (SQLException ex) {
            System.getLogger(PedidoDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return lista;
    }

    public static int cambiarEstatus(int idPedido, Pedido.EstatusPedido estatus) {
        int resultado = 0;
        try {
            MySQLConnectionManager conn = MySQLConnectionManager.buildConnection();
            String query = "UPDATE pedido SET estatus = ? WHERE idPedido = ?;";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, estatus.name());
            ps.setInt(2, idPedido);

            resultado = ps.executeUpdate();

            conn.close();
        } catch (SQLException ex) {
            System.getLogger(PedidoDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return resultado;
    }

    public static int realizarPedido(Pedido pedido) {
        int resultado = 0;
        MySQLConnectionManager conn = null;

        try {
            conn = MySQLConnectionManager.buildConnection();

            conn.setAutoCommit(false);

            //Descontar inventario 
            for (ProductoPedido pp : pedido.getProductos()) {
                Producto prod = pp.getProducto();
                int cantidadPedida = pp.getCantidad();

                if (prod.getEsPreparado()) {
                    if (prod.getComponentes() == null || prod.getComponentes().isEmpty()) {
                        throw new SQLException("El producto preparado '" + prod.getNombre() + "' no tiene insumos asignados en memoria.");
                    }

                    for (ComponenteElaboracion comp : prod.getComponentes()) {
                        double cantidadRequerida = comp.getCantidad() * cantidadPedida;

                        String queryInsumo = "UPDATE producto SET cantidad = cantidad - ? WHERE idProducto = ? AND cantidad >= ?";
                        PreparedStatement psInsumo = conn.prepareStatement(queryInsumo);
                        psInsumo.setDouble(1, cantidadRequerida);
                        psInsumo.setInt(2, comp.getProducto().getIdProducto());
                        psInsumo.setDouble(3, cantidadRequerida);

                        int rowsInsumo = psInsumo.executeUpdate();
                        if (rowsInsumo == 0) {
                            throw new SQLException("Stock insuficiente para el insumo: " + comp.getProducto().getNombre());
                        }
                    }
                } else {
                    String queryProd = "UPDATE producto SET cantidad = cantidad - ? WHERE idProducto = ? AND cantidad >= ?";
                    PreparedStatement psProd = conn.prepareStatement(queryProd);
                    psProd.setDouble(1, (double) cantidadPedida);
                    psProd.setInt(2, prod.getIdProducto());
                    psProd.setDouble(3, (double) cantidadPedida);

                    int rowsProd = psProd.executeUpdate();
                    if (rowsProd == 0) {
                        throw new SQLException("Stock insuficiente para el producto: " + prod.getNombre());
                    }
                }
            }

            // Registrar el Pedido
            String queryPedido = "INSERT INTO pedido (idUsuario, fechaPedido, total, estatus) VALUES (?, ?, ?, ?)";
            PreparedStatement psPedido = conn.prepareStatement(queryPedido);
            psPedido.setInt(1, pedido.getIdUsuario());
            psPedido.setDate(2, Date.valueOf(pedido.getFechaPedido())); // Convertimos LocalDate a java.sql.Date
            psPedido.setDouble(3, pedido.getTotalAPagar());
            psPedido.setString(4, Pedido.EstatusPedido.EnPreparacion.name());
            psPedido.executeUpdate();

            // 3. Obtener el ID autoincrementable que MySQL le asignó al Pedido
            int idPedidoGenerado = 0;
            PreparedStatement psId = conn.prepareStatement("SELECT LAST_INSERT_ID()");
            ResultSet rsId = psId.executeQuery();
            if (rsId.next()) {
                idPedidoGenerado = rsId.getInt(1);
                pedido.setIdPedido(idPedidoGenerado);
            } else {
                throw new SQLException("No se pudo obtener el ID generado para el pedido.");
            }

            //Registrar productoPedidos
            String queryPP = "INSERT INTO productoPedido (idProducto, idPedido, cantidad) VALUES (?, ?, ?)";
            PreparedStatement psPP = conn.prepareStatement(queryPP);
            for (ProductoPedido pp : pedido.getProductos()) {
                psPP.setInt(1, pp.getProducto().getIdProducto());
                psPP.setInt(2, idPedidoGenerado);
                psPP.setInt(3, pp.getCantidad());
                psPP.executeUpdate();
            }

            conn.commit();
            resultado = 1;

        } catch (SQLException ex) {
            System.getLogger(PedidoDAO.class.getName()).log(System.Logger.Level.ERROR, ex.getMessage(), ex);
            try {
                if (conn != null) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
            } catch (SQLException rollbackEx) {
                System.getLogger(PedidoDAO.class.getName()).log(System.Logger.Level.ERROR, "Error al ejecutar rollback", rollbackEx);
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeEx) {
                System.getLogger(PedidoDAO.class.getName()).log(System.Logger.Level.ERROR, "Error al cerrar conexión", closeEx);
            }
        }

        return resultado;
    }

    private static Pedido serializarPedido(ResultSet rs) throws SQLException, NullPointerException {
        Pedido pedido = null;
        if (rs != null) {
            pedido = new Pedido();
            pedido.setIdPedido(rs.getInt("idPedido"));
            pedido.setIdUsuario(rs.getInt("idUsuario"));
            pedido.setFechaPedido(rs.getObject("fechaPedido", LocalDate.class));
            pedido.setTotalAPagar(rs.getDouble("total"));

            String estatusString = rs.getString("estatus");
            if (estatusString == null) {
                throw new NullPointerException("No se logro recuperar toda la información. Estatus no valido");
            }
            try {
                Pedido.EstatusPedido estatus = Pedido.EstatusPedido.valueOf(estatusString);
                pedido.setEstatus(estatus);
            } catch (IllegalArgumentException e) {
                throw new NullPointerException("No se logro convertir un dato.La información recuperada pudo corromperse");
            }
            String nombreUsuario = "" + rs.getString("nombre")
                    + rs.getString("apellidoPaterno") + rs.getString("apellidoMaterno");
            pedido.setNombreUsuario(nombreUsuario);
            return pedido;
        }
        throw new NullPointerException("No se logro recuperar toda la información");
    }

    public static int actualizarPedido(Pedido pedido) {
        int resultado = 0;
        MySQLConnectionManager conn = null;

        try {
            conn = MySQLConnectionManager.buildConnection();
            conn.setAutoCommit(false);

            int idPedido = pedido.getIdPedido();

            // PASO 1: Recuperar el pedido viejo para DEVOLVER los productos al inventario
            String queryViejo = "SELECT pp.idProducto, pp.cantidad, p.esPreparado FROM productoPedido pp "
                    + "JOIN producto p ON pp.idProducto = p.idProducto WHERE pp.idPedido = ?";
            PreparedStatement psViejo = conn.prepareStatement(queryViejo);
            psViejo.setInt(1, idPedido);
            ResultSet rsViejo = psViejo.executeQuery();

            while (rsViejo.next()) {
                int idProdViejo = rsViejo.getInt("idProducto");
                int cantVieja = rsViejo.getInt("cantidad");
                boolean esPreparadoViejo = rsViejo.getInt("esPreparado") == 1;

                if (esPreparadoViejo) {
                    // Recuperar insumos asociados al producto preparado
                    String queryInsumosViejos = "SELECT idProducto, cantidadCP FROM v_productoComponente WHERE idPreparado = ?";
                    PreparedStatement psInsV = conn.prepareStatement(queryInsumosViejos);
                    psInsV.setInt(1, idProdViejo);
                    ResultSet rsInsV = psInsV.executeQuery();

                    while (rsInsV.next()) {
                        // idProducto representa el insumo/componente
                        int idInsumo = rsInsV.getInt("idProducto");
                        double cantComponente = rsInsV.getDouble("cantidadCP");

                        // Devolver las cantidades al stock original
                        String updateInsumo = "UPDATE producto SET cantidad = cantidad + ? WHERE idProducto = ?";
                        PreparedStatement psUpIns = conn.prepareStatement(updateInsumo);
                        psUpIns.setDouble(1, cantComponente * cantVieja);
                        psUpIns.setInt(2, idInsumo);
                        psUpIns.executeUpdate();
                    }
                } else {
                    // Si era producto normal, solo lo sumamos de vuelta
                    String queryRegresarProd = "UPDATE producto SET cantidad = cantidad + ? WHERE idProducto = ?";
                    PreparedStatement psRegresarProd = conn.prepareStatement(queryRegresarProd);
                    psRegresarProd.setDouble(1, (double) cantVieja);
                    psRegresarProd.setInt(2, idProdViejo);
                    psRegresarProd.executeUpdate();
                }
            }

            // PASO 2: Eliminar las relaciones antiguas de productoPedido
            String queryEliminarPP = "DELETE FROM productoPedido WHERE idPedido = ?";
            PreparedStatement psEliminarPP = conn.prepareStatement(queryEliminarPP);
            psEliminarPP.setInt(1, idPedido);
            psEliminarPP.executeUpdate();

            // PASO 3: Descontar el NUEVO inventario e Insertar los nuevos productoPedido
            for (ProductoPedido pp : pedido.getProductos()) {
                Producto prod = pp.getProducto();
                int cantidadPedida = pp.getCantidad();

                if (prod.getEsPreparado()) {
                    if (prod.getComponentes() == null || prod.getComponentes().isEmpty()) {
                        throw new SQLException("El producto preparado '" + prod.getNombre() + "' no tiene insumos asignados.");
                    }

                    for (ComponenteElaboracion comp : prod.getComponentes()) {
                        double cantidadRequerida = comp.getCantidad() * cantidadPedida;
                        String queryInsumo = "UPDATE producto SET cantidad = cantidad - ? WHERE idProducto = ? AND cantidad >= ?";
                        PreparedStatement psInsumo = conn.prepareStatement(queryInsumo);
                        psInsumo.setDouble(1, cantidadRequerida);
                        psInsumo.setInt(2, comp.getProducto().getIdProducto());
                        psInsumo.setDouble(3, cantidadRequerida);

                        if (psInsumo.executeUpdate() == 0) {
                            throw new SQLException("Stock insuficiente para el insumo: " + comp.getProducto().getNombre());
                        }
                    }
                } else {
                    String queryProd = "UPDATE producto SET cantidad = cantidad - ? WHERE idProducto = ? AND cantidad >= ?";
                    PreparedStatement psProd = conn.prepareStatement(queryProd);
                    psProd.setDouble(1, (double) cantidadPedida);
                    psProd.setInt(2, prod.getIdProducto());
                    psProd.setDouble(3, (double) cantidadPedida);

                    if (psProd.executeUpdate() == 0) {
                        throw new SQLException("Stock insuficiente para el producto: " + prod.getNombre());
                    }
                }

                // Insertar la nueva relación actualizada
                String queryInsertarPP = "INSERT INTO productoPedido (idProducto, idPedido, cantidad) VALUES (?, ?, ?)";
                PreparedStatement psInsertarPP = conn.prepareStatement(queryInsertarPP);
                psInsertarPP.setInt(1, prod.getIdProducto());
                psInsertarPP.setInt(2, idPedido);
                psInsertarPP.setInt(3, cantidadPedida);
                psInsertarPP.executeUpdate();
            }

            // PASO 4: Actualizar los datos generales del Pedido (Total)
            String queryUpdatePedido = "UPDATE pedido SET total = ? WHERE idPedido = ?";
            PreparedStatement psUpdatePedido = conn.prepareStatement(queryUpdatePedido);
            psUpdatePedido.setDouble(1, pedido.getTotalAPagar());
            psUpdatePedido.setInt(2, idPedido);
            psUpdatePedido.executeUpdate();

            conn.commit();
            resultado = 1;

        } catch (SQLException ex) {
            System.getLogger(PedidoDAO.class.getName()).log(System.Logger.Level.ERROR, ex.getMessage(), ex);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.getLogger(PedidoDAO.class.getName()).log(System.Logger.Level.ERROR, "Error en rollback", rollbackEx);
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeEx) {
                System.getLogger(PedidoDAO.class.getName()).log(System.Logger.Level.ERROR, "Error al cerrar conexión", closeEx);
            }
        }

        return resultado;
    }

    public static int cancelarPedido(Pedido pedido) {
        int resultado = 0;
        MySQLConnectionManager conn = null;

        try {
            conn = MySQLConnectionManager.buildConnection();
            conn.setAutoCommit(false);

            //Devolver las cantidades al inventario
            for (ProductoPedido pp : pedido.getProductos()) {
                Producto prod = pp.getProducto();
                int cantidadPedida = pp.getCantidad();

                if (prod.getEsPreparado()) {
                    if (prod.getComponentes() == null || prod.getComponentes().isEmpty()) {
                        throw new SQLException("El producto preparado '" + prod.getNombre() + "' no tiene insumos asignados en memoria.");
                    }

                    // Devolver el stock de cada insumo multiplicando por la cantidad de preparados del pedido
                    for (ComponenteElaboracion comp : prod.getComponentes()) {
                        double cantidadADevolver = comp.getCantidad() * cantidadPedida;

                        String queryInsumo = "UPDATE producto SET cantidad = cantidad + ? WHERE idProducto = ?";
                        PreparedStatement psInsumo = conn.prepareStatement(queryInsumo);
                        psInsumo.setDouble(1, cantidadADevolver);
                        psInsumo.setInt(2, comp.getProducto().getIdProducto());
                        psInsumo.executeUpdate();
                    }
                } else {
                    // Si es un producto normal, sumamos de vuelta la cantidad pedida directamente
                    String queryProd = "UPDATE producto SET cantidad = cantidad + ? WHERE idProducto = ?";
                    PreparedStatement psProd = conn.prepareStatement(queryProd);
                    psProd.setDouble(1, (double) cantidadPedida);
                    psProd.setInt(2, prod.getIdProducto());
                    psProd.executeUpdate();
                }
            }

            // PASO 2: Modificar a 0 la cantidad en la tabla
            String queryActualizarPP = "UPDATE productoPedido SET cantidad = 0 WHERE idPedido = ?;";
            PreparedStatement psActualizarPP = conn.prepareStatement(queryActualizarPP);
            psActualizarPP.setInt(1, pedido.getIdPedido());
            psActualizarPP.executeUpdate();

            // PASO 3: Opcional pero recomendado - Cambiar el estatus del Pedido general a "CANCELADO"
            String queryUpdatePedido = "UPDATE pedido SET estatus = 'Cancelado', total = 0.0 WHERE idPedido = ?";
            PreparedStatement psUpdatePedido = conn.prepareStatement(queryUpdatePedido);
            psUpdatePedido.setInt(1, pedido.getIdPedido());
            psUpdatePedido.executeUpdate();

            conn.commit(); // Consolidamos los cambios de manera segura
            resultado = 1;

        } catch (SQLException ex) {
            System.getLogger(PedidoDAO.class.getName()).log(System.Logger.Level.ERROR, ex.getMessage(), ex);
            try {
                if (conn != null) {
                    conn.rollback(); // Deshacemos todo ante cualquier error crítico
                }
            } catch (SQLException rollbackEx) {
                System.getLogger(PedidoDAO.class.getName()).log(System.Logger.Level.ERROR, "Error al ejecutar rollback", rollbackEx);
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeEx) {
                System.getLogger(PedidoDAO.class.getName()).log(System.Logger.Level.ERROR, "Error al cerrar conexión", closeEx);
            }
        }

        return resultado;
    }
}
