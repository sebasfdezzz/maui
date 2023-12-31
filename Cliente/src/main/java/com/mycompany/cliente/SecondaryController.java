package com.mycompany.cliente;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SecondaryController {

    private static String ProjectFolder = System.getProperty("user.dir") + "/..";
    System.out.println("Parent Folder: " + ProjectFolder);

    @FXML
    private TextField name;

    @FXML
    private TextField number;

    @FXML
    private TextField pass;

    @FXML
    private Button enviarBtn;

    @FXML
    private void login() throws IOException {
        String nombre = name.getText();
        String numero = number.getText();
        String pass = pass.getText();
        if(local_login(nombre,pass)){
            sent_to_server(nombre, numero);
        }else{
            System.out.println("Error validando credenciales");
        }
        App.setRoot("chat");
    }

    private void sent_to_server(String name, String number) throws IOException {

        Client.startConnection(name + "|" + number);

    }

    private boolean local_login(String nombre, String pass) {
        Path pubKeyPath = Paths.get(ProjectFolder, "Pub", nombre + ".pub");
        Path privKeyPath = Paths.get(ProjectFolder, "Priv", nombre + ".key");

        boolean pubKeyExists = Files.exists(pubKeyPath);
        boolean privKeyExists = Files.exists(privKeyPath);

        if (pubKeyExists && privKeyExists) {
            try {
                String content = new String(Files.readAllBytes(privKeyPath));
                int priv_key = Integer.parseInt(Encriptar.desencriptar_simetrico(content, custom_hash(pass)));
                if(priv_key < 0 || priv_key >36){
                    return false;
                }
                PrimaryController.setPrivKey(priv_key);
                return true;
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            PrimaryController.setPrivKey(create_credentials(nombre, pass););
            return true;
        }
    }

    private int create_credentials(String nombre, String pass) {
        try {
            int pubKey;
            String pubKeyContent;
            do {
                pubKey = (int) (Math.random() * 32) + 1;
                pubKeyContent = String.valueOf(pubKey);
            } while (pubKeyExists(nombre, pubKey));

            Files.write(Paths.get(ProjectFolder, "Pub", nombre + ".pub"), pubKeyContent.getBytes());

            int privKey = 32 - pubKey;
            String encryptedPrivKey = Encriptar.encriptar_desencriptar_asimetrico(String.valueOf(privKey), custom_hash(pass));
            Files.write(Paths.get(ProjectFolder, "Priv", nombre + ".key"), encryptedPrivKey.getBytes());

            String certificateContent = "nommbre: " + nombre + "\n" +
                                        "Llave publica: " + pubKey + "\n" +
                                        "AR: AR1";

            Files.write(Paths.get(ProjectFolder, "Certificates", nombre + ".certificate"), certificateContent.getBytes());

            return privKey;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private boolean pubKeyExists(int pubKey) {
        File pubFolder = new File(ProjectFolder, "Pub");
        File[] pubFiles = pubFolder.listFiles();

        if (pubFiles != null) {
            for (File pubFile : pubFiles) {
                try {
                    String existingPubKeyContent = new String(Files.readAllBytes(pubFile.toPath()));
                    int existingPubKey = Integer.parseInt(existingPubKeyContent.trim());

                    if (existingPubKey == pubKey) {
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }




    private int custom_hash(String text) {
        return Math.abs(text.hashCode()) % 36;
    }

}