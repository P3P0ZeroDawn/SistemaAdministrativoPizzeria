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
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Pedido;

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
