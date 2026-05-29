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
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Producto;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.ProductoPedido;

/**
 *
 * @author hp
 */
public class ProductoPedidoDAO {
    
    public static Pedido obtenerProPedidos(Pedido pedido) throws SQLException{
        List<ProductoPedido> lista = null;
        try{
            lista = new ArrayList<>();
            MySQLConnectionManager conn = MySQLConnectionManager.buildConnection();
            String query = "SELECT pp.idProductoPedido, pp.idProducto, pp.idPedido, pp.cantidad"
                    + " FROM productoPedido AS pp JOIN producto AS p"
                    + " ON pp.idProducto = p.idProducto"
                    + " WHERE pp.idPedido = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, pedido.getIdPedido());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                lista.add(serializarProductoPedido(rs));
            }
            conn.close();
        } catch (SQLException ex){
            System.getLogger(ProductoPedidoDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        if(lista != null){
            pedido.setProductos(new ArrayList<>(lista));
            return pedido;
        }
        throw new SQLException("No se logró recuperar toda la información");
    }
    
    private static ProductoPedido serializarProductoPedido(ResultSet rs) throws SQLException, NullPointerException{
        ProductoPedido productoP = null;
        if(rs != null){
            productoP = new ProductoPedido();
            productoP.setIdProductoPedido(rs.getInt("idProductoPedido"));
            productoP.setCantidad(rs.getInt("cantidad"));
            Producto producto = new Producto();
            producto.setIdProducto(rs.getInt("idProducto"));
            productoP.setProducto(producto);
            return productoP;
        }
        throw new NullPointerException("No se logró recuperar toda la información");
    }
}
