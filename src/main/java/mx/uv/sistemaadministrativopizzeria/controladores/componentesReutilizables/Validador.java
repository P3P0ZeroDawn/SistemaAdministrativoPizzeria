/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables;

import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import mx.uv.sistemaadministrativopizzeria.excepciones.DatosFaltantesException;

/**
 *
 * @author pedro
 */
public class Validador {
    public static void validarCampo(TextField campo, String mensaje)
        throws DatosFaltantesException {

        if (campo.getText() == null || campo.getText().trim().isEmpty()) {
            campo.requestFocus();
            throw new DatosFaltantesException(mensaje);
        }
    }
    
    public static void validarCampoPassword(PasswordField campo, String mensaje)
        throws DatosFaltantesException {

        if (campo.getText() == null || campo.getText().trim().isEmpty()) {
            campo.requestFocus();
            throw new DatosFaltantesException(mensaje);
        }
    }
    
    public static void validarCampoComboBox(ComboBox campo, String mensaje)
        throws DatosFaltantesException {
        if (campo.getValue() == null) {
            campo.requestFocus();
            throw new DatosFaltantesException(mensaje);
        }
    }
    
    public static void validarComprobacionContrasenia(PasswordField contrasenia1,
            PasswordField contrasenia2, String mensaje)
        throws DatosFaltantesException {
        if (!contrasenia1.getText().equals(contrasenia2.getText())) {
            throw new DatosFaltantesException("Las contraseñas no coinciden");
        }
    }
}
