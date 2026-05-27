/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.modelo.beans;

/**
 *
 * @author pedro
 */
public class Usuario implements mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.ItemObservableList{
    private int idUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String telefono;
    private String email;
    private Boolean activo;
    private tipoUsuario tipoUsuario;
    private rolEmpleado rolEmpleado;
    private String usuario;
    private String password;
    private Direccion direccion;

    @Override
    public String getString() {
        return this.nombre + " " + this.apellidoPaterno + " " + this.apellidoMaterno;
    }
    
    public enum tipoUsuario {Cliente, Empleado};
    public enum rolEmpleado {Administrador, Cajero};
    
    public class Direccion {
        private String calle;
        private String numero;
        private String codigoPostal;
        private String ciudad;

        public Direccion() {
        }
        
        public Direccion(String calle, String numero,
                String codigoPostal, String ciudad) {
            this.calle = calle;
            this.numero = numero;
            this.codigoPostal = codigoPostal;
            this.ciudad = ciudad;
        }

        public String getCalle() {
            return calle;
        }

        public void setCalle(String calle) {
            this.calle = calle;
        }

        public String getNumero() {
            return numero;
        }

        public void setNumero(String numero) {
            this.numero = numero;
        }

        public String getCodigoPostal() {
            return codigoPostal;
        }

        public void setCodigoPostal(String codigoPostal) {
            this.codigoPostal = codigoPostal;
        }

        public String getCiudad() {
            return ciudad;
        }

        public void setCiudad(String ciudad) {
            this.ciudad = ciudad;
        }
    }

    public Usuario() {
        this.direccion = new Direccion();
    }

    public Usuario(int idUsuario, String nombre, String apellidoPaterno, String apellidoMaterno) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
    }

    public Usuario(int idUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, String telefono, String email, Boolean activo, tipoUsuario tipoUsuario, rolEmpleado rolEmpleado, String usuario, String password, Direccion direccion) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
        this.email = email;
        this.activo = activo;
        this.tipoUsuario = tipoUsuario;
        this.rolEmpleado = rolEmpleado;
        this.usuario = usuario;
        this.password = password;
        this.direccion = direccion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public tipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(tipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public rolEmpleado getRolEmpleado() {
        return rolEmpleado;
    }

    public void setRolEmpleado(rolEmpleado rolEmpleado) {
        this.rolEmpleado = rolEmpleado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
    
    @Override
    public String toString() {
        return this.nombre + " " + this.apellidoPaterno + " " + this.apellidoMaterno;
    }
}
