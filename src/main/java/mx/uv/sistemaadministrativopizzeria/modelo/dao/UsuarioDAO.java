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
import java.sql.Types;
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
    
    public static Usuario buscarUsuario(int idUsuario){
        Usuario usuario = null;
        try {
            MySQLConnectionManager conn = MySQLConnectionManager.buildConnection();
            String query = "SELECT * FROM usuario WHERE idUsuario = ?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                usuario = serializarUsuario(rs);
            }
            conn.close();
        } catch (SQLException ex) {
            System.getLogger(UsuarioDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return usuario;
    }
    
    public static boolean registrarUsuario(Usuario usuario){
        if(usuario != null){
            try{
                MySQLConnectionManager conn = MySQLConnectionManager.buildConnection();
                String query = "INSERT INTO usuario (nombre, apellidoPaterno, apellidoMaterno, "
                        + "telefono, email, activo, tipoUsuario, usuario, password, rolEmpleado, "
                        + "calle, numero, codigoPostal, ciudad) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, usuario.getNombre());
                ps.setString(2, usuario.getApellidoPaterno());
                ps.setString(3, usuario.getApellidoMaterno());
                ps.setString(4, usuario.getTelefono());
                ps.setString(5, usuario.getEmail());
                ps.setInt(6, 1);
                ps.setString(7, usuario.getTipoUsuario().toString());
                if(usuario.getTipoUsuario().equals(Usuario.tipoUsuario.Empleado)){
                    ps.setString(8, usuario.getUsuario());
                    ps.setBytes(9, JavaFXUtils.sha256Bytes(usuario.getPassword()));
                    ps.setString(10, usuario.getRolEmpleado().toString());
                }else{
                    ps.setNull(8, Types.VARCHAR);
                    ps.setNull(9, Types.VARBINARY);
                    ps.setNull(10, Types.VARCHAR);
                }
                ps.setString(11, usuario.getDireccion().getCalle());
                ps.setString(12, usuario.getDireccion().getNumero());
                ps.setString(13, usuario.getDireccion().getCodigoPostal());
                ps.setString(14, usuario.getDireccion().getCiudad());
                ps.executeUpdate();
                return true;
            }catch (SQLException e){
                System.out.println(e.getMessage());
            } catch (NoSuchAlgorithmException ex) {
                System.getLogger(UsuarioDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (UnsupportedEncodingException ex) {
                System.getLogger(UsuarioDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
        return false;
    }
    
    public static boolean actualizarUsuario(Usuario usuario,
        boolean modifContrasenia){

            if(usuario != null){

                try{

                    MySQLConnectionManager conn =
                            MySQLConnectionManager.buildConnection();

                    String campoContrasenia =
                            modifContrasenia
                            ? ", password = ?"
                            : "";

                    String query =
                            "UPDATE usuario SET "
                            + "nombre = ?, "
                            + "apellidoPaterno = ?, "
                            + "apellidoMaterno = ?, "
                            + "telefono = ?, "
                            + "email = ?, "
                            + "activo = ?, "
                            + "tipoUsuario = ?, "
                            + "usuario = ?, "
                            + "rolEmpleado = ?, "
                            + "calle = ?, "
                            + "numero = ?, "
                            + "codigoPostal = ?, "
                            + "ciudad = ?"
                            + campoContrasenia
                            + " WHERE idUsuario = ?";

                    PreparedStatement ps = conn.prepareStatement(query);

                    int indice = 1;

                    ps.setString(indice++, usuario.getNombre());
                    ps.setString(indice++, usuario.getApellidoPaterno());
                    ps.setString(indice++, usuario.getApellidoMaterno());
                    ps.setString(indice++, usuario.getTelefono());
                    ps.setString(indice++, usuario.getEmail());
                    ps.setInt(indice++, usuario.getActivo()? 1 : 0);
                    ps.setString(indice++, usuario.getTipoUsuario().toString());

                    if(usuario.getTipoUsuario()
                            == Usuario.tipoUsuario.Empleado){

                        ps.setString(indice++, usuario.getUsuario());

                        ps.setString(indice++,
                                usuario.getRolEmpleado().toString());

                    }else{

                        ps.setNull(indice++, Types.VARCHAR);
                        ps.setNull(indice++, Types.VARCHAR);
                    }

                    ps.setString(indice++,
                            usuario.getDireccion().getCalle());

                    ps.setString(indice++,
                            usuario.getDireccion().getNumero());

                    ps.setString(indice++,
                            usuario.getDireccion().getCodigoPostal());

                    ps.setString(indice++,
                            usuario.getDireccion().getCiudad());

                    if(modifContrasenia){

                        ps.setBytes(indice++,
                                JavaFXUtils.sha256Bytes(
                                        usuario.getPassword()
                                ));
                    }

                    ps.setInt(indice++, usuario.getIdUsuario());

                    ps.executeUpdate();

                    conn.close();

                    return true;

                }catch (SQLException e){

                    System.out.println(e.getMessage());

                } catch (NoSuchAlgorithmException ex) {

                    System.getLogger(UsuarioDAO.class.getName())
                            .log(System.Logger.Level.ERROR,
                                    (String) null,
                                    ex);

                } catch (UnsupportedEncodingException ex) {

                    System.getLogger(UsuarioDAO.class.getName())
                            .log(System.Logger.Level.ERROR,
                                    (String) null,
                                    ex);
                }
            }

            return false;
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
                usuario.setApellidoMaterno((rs.getString("apellidoMaterno") != null) ? rs.getString("apellidoMaterno") : "");
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));
                usuario.setActivo(rs.getInt("activo")==1);
                usuario.setTipoUsuario(Usuario.tipoUsuario.valueOf(rs.getString("tipoUsuario")));
                usuario.setUsuario((rs.getString("usuario") != null) ?  rs.getString("usuario") : null);
                usuario.setRolEmpleado((rs.getString("rolEmpleado") != null) ? Usuario.rolEmpleado.valueOf(rs.getString("rolEmpleado")) : null);
                direccion.setCalle(rs.getString("calle"));
                direccion.setNumero(rs.getString("numero"));
                direccion.setCodigoPostal(rs.getString("codigoPostal"));
                direccion.setCiudad(rs.getString("ciudad"));
                usuario.setDireccion(direccion);
            }
        } catch (SQLException ex) {
            System.getLogger(UsuarioDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return usuario;
    }
}
