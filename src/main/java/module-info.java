module mx.uv.sistemaadministrativopizzeria {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;
    requires java.desktop;
    requires com.opencsv;
    requires kernel;
    requires layout;
    requires io;
    requires javafx.swing;
    
    opens mx.uv.sistemaadministrativopizzeria to javafx.fxml;
    opens mx.uv.sistemaadministrativopizzeria.controladores to javafx.fxml;
    opens mx.uv.sistemaadministrativopizzeria.modelo.beans to javafx.base;
    exports mx.uv.sistemaadministrativopizzeria;
}
