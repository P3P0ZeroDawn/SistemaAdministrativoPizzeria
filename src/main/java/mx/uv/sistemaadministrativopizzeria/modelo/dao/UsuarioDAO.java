/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.modelo.dao;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mx.uv.sistemaadministrativopizzeria.modelo.MySQLConnectionManager;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Usuario;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.JavaFXUtils;

/**
 *
 * @author pedro
 */
public class UsuarioDAO {
    public static Usuario validarLogin(String usuarioIngresado, String password) {
        Usuario usuario = null;
        try {
            MySQLConnectionManager conn = MySQLConnectionManager.buildConnection();
            String query = "SELECT * FROM usuario WHERE usuario = ? AND password = ?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, usuarioIngresado);
            ps.setBytes(2, JavaFXUtils.sha256Bytes(password));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                usuario = serializarUsuario(rs);
            }
            conn.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            System.getLogger(UsuarioDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (UnsupportedEncodingException ex) {
            System.getLogger(UsuarioDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return usuario;
    }
    
    public static List<Usuario> obtenerUsuarios(){
        List<Usuario> lista = null;
        try {
            lista = new ArrayList<>();
            MySQLConnectionManager conn = MySQLConnectionManager.buildConnection();
            String query = "SELECT * FROM usuario WHERE activo = 1;";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                lista.add(serializarUsuario(rs));
            }
            conn.close();
        } catch (SQLException ex) {
            System.getLogger(UsuarioDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return lista;
    }
            
    public static Usuario serializarUsuario(ResultSet rs){
        Usuario usuario = null;
        try {
            if(rs != null){
                usuario = new Usuario();
                Usuario.Direccion direccion = usuario.new Direccion();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellidoPaterno(rs.getString("apellidoPaterno"));
                usuario.setApellidoMaterno(rs.getString("apellidoMaterno"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));
                usuario.setActivo(rs.getInt("activo")==1);
                usuario.setTipoUsuario(Usuario.tipoUsuario.valueOf(rs.getString("tipoUsuario")));
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setRolEmpleado((rs.getString("rolEmpleado") != null) ? Usuario.rolEmpleado.valueOf(rs.getString("rolEmpleado")) : null);
                direccion.setCalle(rs.getString("calle"));
                direccion.setNumero(rs.getString("numero"));
                direccion.setCodigoPostal(rs.getString("codigoPostal"));
                direccion.setCiudad(rs.getString("ciudad"));
            }
        } catch (SQLException ex) {
            System.getLogger(UsuarioDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return usuario;
    }
}
