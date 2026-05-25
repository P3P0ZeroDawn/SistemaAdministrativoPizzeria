module mx.uv.sistemaadministrativopizzeria {
    requires javafx.controls;
    requires javafx.fxml;

    opens mx.uv.sistemaadministrativopizzeria to javafx.fxml;
    exports mx.uv.sistemaadministrativopizzeria;
}
