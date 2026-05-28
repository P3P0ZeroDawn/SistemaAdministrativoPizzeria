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
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ComponenteProducto;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoPedido;

/**
 *
 * @author hp
 */
public class PedidoDAO {
    
    public static List<Pedido> obtenerPedidos(){
        List<Pedido> lista = null;
        try{
            lista = new ArrayList<>();
            MySQLConnectionManager conn = MySQLConnectionManager.buildConnection();
            String query = "SELECT p.idPedido, p.idUsuario, p.fechaPedido, p.total, p.estatus,"
                    + " u.nombre, u.apellidoPaterno, u.apellidoMaterno"
                    + " FROM pedido AS p JOIN Usuario AS u ON p.idUsuario = u.idUsuario;";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                lista.add(serializarPedido(rs));
            }
            conn.close();
        } catch (SQLException ex){
            System.getLogger(PedidoDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return lista;
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
                    
                    for (ComponenteProducto comp : prod.getComponentes()) {
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
            psPedido.setString(4, pedido.getEstatus().name());
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
                }
            } catch (SQLException rollbackEx) {
                System.getLogger(PedidoDAO.class.getName()).log(System.Logger.Level.ERROR, "Error al ejecutar rollback", rollbackEx);
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException closeEx) {
                System.getLogger(PedidoDAO.class.getName()).log(System.Logger.Level.ERROR, "Error al cerrar conexión", closeEx);
            }
        }
        
        return resultado;
    }
    
    private static Pedido serializarPedido(ResultSet rs) throws SQLException, NullPointerException{
        Pedido pedido = null;
        if(rs != null){
            pedido = new Pedido();
            pedido.setIdPedido(rs.getInt("idPedido"));
            pedido.setIdUsuario(rs.getInt("idUsuario"));
            pedido.setFechaPedido(rs.getObject("fechaPedido", LocalDate.class));
            pedido.setTotalAPagar(rs.getDouble("total"));
            
            String estatusString = rs.getString("estatus");
            if(estatusString == null){
                throw new NullPointerException("No se logro recuperar toda la información. Estatus no valido");
            }
            try{
                Pedido.EstatusPedido estatus = Pedido.EstatusPedido.valueOf(estatusString);
                pedido.setEstatus(estatus);
            } catch (IllegalArgumentException e){
                throw new NullPointerException("No se logro convertir un dato.La información recuperada pudo corromperse");
            }
            String nombreUsuario = "" + rs.getString("nombre") 
                    + rs.getString("apellidoPaterno") + rs.getString("apellidoMaterno");
            pedido.setNombreUsuario(nombreUsuario);
            return pedido;
        }
        throw new NullPointerException("No se logro recuperar toda la información");
    }
}
