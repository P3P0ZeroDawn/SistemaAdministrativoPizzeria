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
import mx.uv.sistemaadministrativopizzeria.utilidades.EncriptadorPassword;
import mx.uv.sistemaadministrativopizzeria.modelo.MySQLConnectionManager;
import mx.uv.sistemaadministrativopizzeria.modelo.beans.Usuario;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.JavaFXUtils;
import mx.uv.sistemaadministrativopizzeria.excepciones.UsuarioConPedidosException;

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
            ps.setBytes(2, EncriptadorPassword.sha256Bytes(password));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usuario = serializarUsuario(rs);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            System.getLogger(UsuarioDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (UnsupportedEncodingException ex) {
            System.getLogger(UsuarioDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return usuario;
    }

    public static List<Usuario> obtenerUsuarios() {
        List<Usuario> lista = null;
        try {
            lista = new ArrayList<>();
            MySQLConnectionManager conn = MySQLConnectionManager.buildConnection();
            String query = "SELECT * FROM usuario WHERE activo = 1;";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(serializarUsuario(rs));
            }
            conn.close();
        } catch (SQLException ex) {
            System.getLogger(UsuarioDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return lista;
    }

    public static Usuario buscarUsuario(int idUsuario) {
        Usuario usuario = null;
        try {
            MySQLConnectionManager conn = MySQLConnectionManager.buildConnection();
            String query = "SELECT * FROM usuario WHERE idUsuario = ?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                usuario = serializarUsuario(rs);
            }
            conn.close();
        } catch (SQLException ex) {
            System.getLogger(UsuarioDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return usuario;
    }

    public static boolean registrarUsuario(Usuario usuario) throws mx.uv.sistemaadministrativopizzeria.excepciones.UsuarioDuplicadoException {
        if (usuario != null) {
            try {
                // Verificar usuario duplicado solo para empleados
                if (usuario.getTipoUsuario().equals(Usuario.tipoUsuario.Empleado)) {
                    if (verificarUsuarioExistente(usuario.getUsuario())) {
                        throw new mx.uv.sistemaadministrativopizzeria.excepciones.UsuarioDuplicadoException();
                    }
                }

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
                if (usuario.getTipoUsuario().equals(Usuario.tipoUsuario.Empleado)) {
                    ps.setString(8, usuario.getUsuario());
                    ps.setBytes(9, EncriptadorPassword.sha256Bytes(usuario.getPassword()));
                    ps.setString(10, usuario.getRolEmpleado().toString());
                } else {
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
            } catch (SQLException e) {
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
            boolean modifContrasenia) throws mx.uv.sistemaadministrativopizzeria.excepciones.UsuarioDuplicadoException {

        if (usuario != null) {

            try {

                // Verificar usuario duplicado solo para empleados
                if (usuario.getTipoUsuario().equals(Usuario.tipoUsuario.Empleado)) {
                    if (verificarUsuarioExistenteExcluyendo(usuario.getUsuario(), usuario.getIdUsuario())) {
                        throw new mx.uv.sistemaadministrativopizzeria.excepciones.UsuarioDuplicadoException();
                    }
                }

                MySQLConnectionManager conn
                        = MySQLConnectionManager.buildConnection();

                String campoContrasenia
                        = modifContrasenia
                                ? ", password = ?"
                                : "";

                String query
                        = "UPDATE usuario SET "
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
                ps.setInt(indice++, 1);
                ps.setString(indice++, usuario.getTipoUsuario().toString());

                if (usuario.getTipoUsuario()
                        == Usuario.tipoUsuario.Empleado) {

                    ps.setString(indice++, usuario.getUsuario());

                    ps.setString(indice++,
                            usuario.getRolEmpleado().toString());

                } else {

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

                if (modifContrasenia) {

                    ps.setBytes(indice++,
                            EncriptadorPassword.sha256Bytes(
                                    usuario.getPassword()
                            ));
                }

                ps.setInt(indice++, usuario.getIdUsuario());

                ps.executeUpdate();

                conn.close();

                return true;

            } catch (SQLException e) {

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
    
    public static List<Usuario> buscarUsuarios(
        String busqueda,
        boolean porNombre,
        boolean porTelefono,
        boolean porDireccion) {

        List<Usuario> lista = new ArrayList<>();

        try {

            MySQLConnectionManager conn =
                    MySQLConnectionManager.buildConnection();

            StringBuilder query = new StringBuilder(
                    "SELECT * FROM usuario WHERE activo = 1"
            );

            List<String> filtros = new ArrayList<>();

            if (porNombre) {
                filtros.add(
                    "(nombre LIKE ? OR apellidoPaterno LIKE ? OR apellidoMaterno LIKE ?)"
                );
            }

            if (porTelefono) {
                filtros.add("telefono LIKE ?");
            }

            if (porDireccion) {
                filtros.add(
                    "(calle LIKE ? OR ciudad LIKE ? OR codigoPostal LIKE ?)"
                );
            }

            if (!filtros.isEmpty()) {

                query.append(" AND (");
                query.append(String.join(" OR ", filtros));
                query.append(")");
            }

            PreparedStatement ps =
                    conn.prepareStatement(query.toString());

            int indice = 1;

            if (porNombre) {

                ps.setString(indice++, "%" + busqueda + "%");
                ps.setString(indice++, "%" + busqueda + "%");
                ps.setString(indice++, "%" + busqueda + "%");
            }

            if (porTelefono) {

                ps.setString(indice++, "%" + busqueda + "%");
            }

            if (porDireccion) {

                ps.setString(indice++, "%" + busqueda + "%");
                ps.setString(indice++, "%" + busqueda + "%");
                ps.setString(indice++, "%" + busqueda + "%");
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(serializarUsuario(rs));
            }

            conn.close();

        } catch (SQLException ex) {

            System.getLogger(UsuarioDAO.class.getName())
                    .log(System.Logger.Level.ERROR,
                            (String) null,
                            ex);
        }

        return lista;
    }
    
    public static boolean eliminarUsuario(Usuario usuario) throws UsuarioConPedidosException {

        if (usuario != null) {

            try {

                // Si es cliente, verificar si tiene pedidos registrados
                if (usuario.getTipoUsuario() == Usuario.tipoUsuario.Cliente) {
                    MySQLConnectionManager connCheck = MySQLConnectionManager.buildConnection();
                    String queryCheck = "SELECT COUNT(*) AS total FROM pedido WHERE idUsuario = ?";
                    java.sql.PreparedStatement psCheck = connCheck.prepareStatement(queryCheck);
                    psCheck.setInt(1, usuario.getIdUsuario());
                    java.sql.ResultSet rsCheck = psCheck.executeQuery();
                    if (rsCheck.next()) {
                        int total = rsCheck.getInt("total");
                        connCheck.close();
                        if (total > 0) {
                            throw new UsuarioConPedidosException("El usuario es cliente y\ntiene pedidos registrados.");
                        }
                    } else {
                        connCheck.close();
                    }
                }


                MySQLConnectionManager conn
                        = MySQLConnectionManager.buildConnection();

                String query
                        = "UPDATE usuario SET "
                        + "activo = 0 "
                        + " WHERE idUsuario = ?";

                PreparedStatement ps = conn.prepareStatement(query);

                ps.setInt(1, usuario.getIdUsuario());

                ps.executeUpdate();

                conn.close();

                return true;

            } catch (SQLException e) {

                System.out.println(e.getMessage());

            }
        }
        return false;
    }

    public static boolean verificarUsuarioExistente(String usuario) {
        int count = 0;

        try {
            MySQLConnectionManager conn = MySQLConnectionManager.buildConnection();
            String query = "SELECT COUNT(*) FROM usuario WHERE usuario = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            conn.close();
        } catch (SQLException ex) {
            System.getLogger(
                    UsuarioDAO.class.getName()
            ).log(
                    System.Logger.Level.ERROR,
                    (String) null,
                    ex
            );
        }
        return (count > 0);
    }

    public static boolean verificarUsuarioExistenteExcluyendo(String usuario, int idUsuario) {
        int count = 0;

        try {
            MySQLConnectionManager conn = MySQLConnectionManager.buildConnection();
            String query = "SELECT COUNT(*) FROM usuario WHERE usuario = ? AND idUsuario != ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, usuario);
            ps.setInt(2, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            conn.close();
        } catch (SQLException ex) {
            System.getLogger(
                    UsuarioDAO.class.getName()
            ).log(
                    System.Logger.Level.ERROR,
                    (String) null,
                    ex
            );
        }
        return (count > 0);
    }

    public static Usuario serializarUsuario(ResultSet rs) {
        Usuario usuario = null;
        try {
            if (rs != null) {
                usuario = new Usuario();
                Usuario.Direccion direccion = usuario.new Direccion();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellidoPaterno(rs.getString("apellidoPaterno"));
                usuario.setApellidoMaterno((rs.getString("apellidoMaterno") != null) ? rs.getString("apellidoMaterno") : "");
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));
                usuario.setActivo(rs.getInt("activo") == 1);
                usuario.setTipoUsuario(Usuario.tipoUsuario.valueOf(rs.getString("tipoUsuario")));
                usuario.setUsuario((rs.getString("usuario") != null) ? rs.getString("usuario") : null);
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
