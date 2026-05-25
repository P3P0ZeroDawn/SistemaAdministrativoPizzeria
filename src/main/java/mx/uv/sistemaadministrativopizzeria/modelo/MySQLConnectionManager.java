package mx.uv.sistemaadministrativopizzeria.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Clase diseñada para realizar una conexión al manejador de base de datos MySQL
 * bajo el esquema de conexión directa mediante JDBC.
 * Esta clase está construida bajo el patrón Singleton, cuyo objetivo es que 
 * se cree unicamente UNA instancia de la clase, en este caso si ya existe una 
 * conexión a la base de datos esta se reutilizará hasta que sea cerrada.
 * @author Frost
 */
public class MySQLConnectionManager {

    private static MySQLConnectionManager connSingleton;
    private Connection connection;
    private String driver;
    private String url;
    private String host = "localhost";
    private String port = "3306";
    private String db = "cajero";//Escribe la base de datos de conexión
    private String username = "admin_pizzeria";//Escribe el username del usuario de conexión
    private String password = "12345";//Escribe el password del usuario de conexión

    /**
     * El contructor de una clase creada con Singleton es privado, para que solo
     * se mande a llamar si el objeto connect es null, en el método buildConnection
     */
    private MySQLConnectionManager() throws SQLException {
        driver = "com.mysql.cj.jdbc.Driver";
        url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useTimezone=true&serverTimezone=UTC";
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connect();
    }
    
    /**
     * Establece la conexión con el SMBD MySQL solo cuando la conexión sea null
     * o esté cerrada
     */
    private void connect() throws SQLException{
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
        }
    }
    
    /**
     * Cierra la conexión con MySQL para optimizar recursos
    */
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Regresa un objeto de la clase PreparedStatement la cual es la que permite
     * enviar consultas mediante la conexión al manejador de base de datos.
     * @param query
     * @return
     * @throws SQLException 
     */
    public PreparedStatement prepareStatement(String query) throws SQLException{
        connect();
        return connection.prepareStatement(query);
    }    

    /**
     * Método estático cuyo objetivo es regresar la conexión creada 
     * bajo el patrón Singleton, en caso de que ya conexión no haya sido creada
     * este método la construye (invoca el constructor privado)
     * @return ConexionMySQL
     * @throws java.sql.SQLException
     */
    public static MySQLConnectionManager buildConnection() throws SQLException {
        if(connSingleton == null){
            connSingleton = new MySQLConnectionManager();
        }
        connSingleton.connect();
        return connSingleton;
    }

}
