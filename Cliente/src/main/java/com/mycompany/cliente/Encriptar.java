package com.mycompany.cliente;

import java.util.Random;

public class Encriptar {
    private static String abecedario = "abcdefghijklmnopqrstuvwxyz1234567890";

    public static String encriptar_desencriptar_asimetrico(String texto, int llave) {
        StringBuilder textoEncriptado = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char caracterActual = texto.charAt(i);
            int indiceCaracter = abecedario.indexOf(caracterActual);
            if (indiceCaracter != -1) {
                int indiceEncriptado = (indiceCaracter + llave) % abecedario.length();
                char caracterEncriptado = abecedario.charAt(indiceEncriptado);
                textoEncriptado.append(caracterEncriptado);
            } else {
                textoEncriptado.append(caracterActual);
            }
        }
        return textoEncriptado.toString();
    }

    public static String desencriptar_simetrico(String texto, int llave) {
        StringBuilder textoEncriptado = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char caracterActual = texto.charAt(i);
            int indiceCaracter = abecedario.indexOf(caracterActual);
            if (indiceCaracter != -1) {
                int indiceEncriptado = 0;
                if(indiceCaracter - llave < 0){
                    indiceEncriptado = (abecedario.length() + (indiceCaracter - llave)) % abecedario.length();
                } else{
                    indiceEncriptado = (indiceCaracter - llave) % abecedario.length();
                }
                char caracterEncriptado = abecedario.charAt(indiceEncriptado);
                textoEncriptado.append(caracterEncriptado);
            } else {
                textoEncriptado.append(caracterActual);
            }
        }
        return textoEncriptado.toString();
    }

    public static int generar_llave(){
        Random random = new Random();
        return random.nextInt(abecedario.length()); 
    }
    
}
