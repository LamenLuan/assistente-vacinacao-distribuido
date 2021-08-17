/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author luanl
 */
public class MensageiroCliente {
    
    public static int porta = 1234;
    public static String ip = "192.168.1.3";
    
    public static void enviaMensagem(
        PrintWriter outbound, Mensagem mensagem
    ) throws IOException {
        Gson gson = new Gson();
        String msg = gson.toJson(mensagem);
        System.out.println("Cliente -> " + msg);
        outbound.println(msg);
    }
    
    public static String recebeMensagem(
        BufferedReader inbound
    ) throws IOException {
        String string = inbound.readLine();
        System.out.println("Cliente <- " + string);
        return string;
    }
    
    public static int getIdMensagem(String string) {
        Gson gson = new Gson();
        Mensagem mensagem = gson.fromJson(string, Mensagem.class);
        
        return mensagem.getId();
    }
    
    public static void fechaSocketEDutos(
        Socket client, PrintWriter outbound, BufferedReader inbound
    ) throws IOException {
        outbound.close();
        inbound.close();
        client.close();
    }
}
