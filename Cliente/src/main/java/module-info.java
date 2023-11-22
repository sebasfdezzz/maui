module com.mycompany.cliente {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.cliente to javafx.fxml;
    exports com.mycompany.cliente;
}
