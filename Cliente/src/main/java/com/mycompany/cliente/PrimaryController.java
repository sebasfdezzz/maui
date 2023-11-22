package com.mycompany.cliente;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class PrimaryController {
    private int tipo_de_mensaje = 1;
    private static PrimaryController interfaz;
    private static int priv_key=-1;

    @FXML
    private TextField msgArea;

    @FXML
    private TextArea chatView;

    @FXML
    private Button enviarBtn;

    @FXML
    private TextField input_llave;

    @FXML
    private TextField hash_input;

    @FXML
    private Label hash_output;

    @FXML
    private RadioButton btn1;

    @FXML
    private RadioButton btn2;

    @FXML
    private RadioButton btn3;

    @FXML
    private RadioButton btn4;

    @FXML
    private RadioButton btn5;

    @FXML
    private void enviarMensaje(ActionEvent event) {
        String mensaje = msgArea.getText();
        if (!mensaje.isEmpty()) {
            try {
                String mensajeEnviado = ("Yo: " + mensaje.split("#")[0]);
                printMessage(mensajeEnviado);
                String msj = mensaje.split("#")[0];
                String usuario = mensaje.split("#")[1];
                int llave = 0;
                int resumen;
                String firma;
                switch(tipo_de_mensaje){
                    case 1:
                        sendMsg.writeUTF(tipo_de_mensaje + "@" +msj + "#" +usuario);
                        break;
                    case 2:
                        llave = Integer.parseInt( input_llave.getText());
                        msj = Encriptar.encriptar_desencriptar_asimetrico(msj, llave);
                        sendMsg.writeUTF(tipo_de_mensaje + "@" +msj + "#" +usuario);
                        break;
                    case 3:
                        llave = Integer.parseInt(input_llave.getText());
                        msj = Encriptar.encriptar_desencriptar_asimetrico(msj, llave);
                        sendMsg.writeUTF(tipo_de_mensaje + "@" +msj + "#" +usuario);
                        break;
                    case 4:
                        int llave_rnd = Encriptar.generar_llave();
                        resumen = msj.hashCode();
                        msj = Encriptar.encriptar_desencriptar_asimetrico(msj, llave_rnd);
                        firma = Encriptar.encriptar_desencriptar_asimetrico(String.valueOf(resumen), llave_rnd);
                        int llave_pub = Integer.parseInt( input_llave.getText());
                        String llave_rnd_encriptada = Encriptar.encriptar_desencriptar_asimetrico(String.valueOf(llave_rnd), llave_pub);
                        sendMsg.writeUTF(tipo_de_mensaje + "@" +msj + "@" + firma + "@" + llave_rnd_encriptada + "#" +usuario);
                        break;
                    case 5:
                        resumen = msj.hashCode();
                        llave = Integer.parseInt( input_llave.getText());
                        firma = Encriptar.encriptar_desencriptar_asimetrico(String.valueOf(resumen), llave);
                        sendMsg.writeUTF(tipo_de_mensaje + "@" +msj + "@" + firma + "#" +usuario);
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static DataOutputStream sendMsg;

    public static void SetDataOutputStream(DataOutputStream msg) {
        sendMsg = msg;
    }

    public static void GetMessage(String msg) {
        if (interfaz != null) {
            javafx.application.Platform.runLater(() -> {
                //recibe todo antes del #, separado todo por @
                int tipo_de_mensaje = Integer.parseInt(msg.split("@")[1]);
                String msj = msg.split("@")[2];
                String msj_listo ="";
                String usuario = msg.split("@")[0];
                int llave = 0;
                String firma_lista;
                switch(tipo_de_mensaje){
                    case 1:
                        msj_listo = msj;
                        break;
                    case 2:
                        llave = Integer.parseInt(interfaz.input_llave.getText());
                        msj_listo = Encriptar.desencriptar_simetrico(msj, llave);
                        break;
                    case 3:
                        llave = Integer.parseInt(interfaz.input_llave.getText());
                        msj_listo = Encriptar.encriptar_desencriptar_asimetrico(msj, llave);
                        break;
                    case 4:
                        llave = Integer.parseInt(interfaz.input_llave.getText());
                        int llave_rnd = Integer.parseInt(Encriptar.encriptar_desencriptar_asimetrico(msg.split("@")[4], llave));
                        msj_listo = Encriptar.desencriptar_simetrico(msj, llave_rnd);
                        firma_lista = Encriptar.desencriptar_simetrico(msg.split("@")[3], llave_rnd);
                        msj_listo = msj_listo + "|"+firma_lista;
                        break;
                    case 5:
                        llave = Integer.parseInt(interfaz.input_llave.getText());
                        msj_listo = msj;
                        firma_lista = Encriptar.encriptar_desencriptar_asimetrico(msg.split("@")[3], llave);
                        msj_listo = msj_listo + "|"+firma_lista;
                        break;
                    default:
                        System.out.println("Tipo de mensaje no reconocido: "+tipo_de_mensaje);
                        break;
                }
                interfaz.printMessage(usuario +": "+msj_listo);
            });
        }
    }

    private void printMessage(String msg) {
        String mensaje = chatView.getText();

        chatView.setText(mensaje + "\n" + msg);
    }

    @FXML
    private void initialize() {
        interfaz = this;
    }

    @FXML
    private void texto_plano(){
        tipo_de_mensaje = 1;
        btn2.setSelected(false);
        btn3.setSelected(false);
        btn4.setSelected(false);
        btn5.setSelected(false);
    }
    @FXML
    private void simetrico(){
        tipo_de_mensaje = 2;
        btn1.setSelected(false);
        btn3.setSelected(false);
        btn4.setSelected(false);
        btn5.setSelected(false);
    }
    @FXML
    private void asimetrico(){
        tipo_de_mensaje = 3;
        btn1.setSelected(false);
        btn2.setSelected(false);
        btn4.setSelected(false);
        btn5.setSelected(false);
    }
    @FXML
    private void sobre_digital(){
        tipo_de_mensaje = 4;
        btn1.setSelected(false);
        btn2.setSelected(false);
        btn3.setSelected(false);
        btn5.setSelected(false);
    }
    @FXML
    private void firmado(){
        tipo_de_mensaje = 5;
        btn1.setSelected(false);
        btn2.setSelected(false);
        btn3.setSelected(false);
        btn4.setSelected(false);
    }

    @FXML
    private void calcular_hash(){
        String texto = hash_input.getText();
        hash_output.setText(String.valueOf(texto.hashCode()));
    }

    public void setPrivKey(int key){
        priv_key = key;
    }

}
