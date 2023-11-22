package com.mycompany.cliente;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SecondaryController {

    @FXML
    private TextField name;

    @FXML
    private TextField number;

    @FXML
    private Button enviarBtn;

    @FXML
    private void login() throws IOException {
        String nombre = name.getText();
        String numero = number.getText();

        sent_to_server(nombre, numero);

        App.setRoot("chat");
    }

    private void sent_to_server(String name, String number) throws IOException {

        Client.startConnection(name + "|" + number);

    }

}