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
public class Mensageiro {
    
    public static int porta = 1234;
    public static String ip = "192.168.1.3";
    
    public static void enviaMensagem(
        PrintWriter outbound, Mensagem mensagem, boolean servidor
    ) throws IOException {
        Gson gson = new Gson();
        String msg = gson.toJson(mensagem);
        outbound.println(msg);
        System.out.println(
            (servidor ? "Servidor" : "Cliente") + " -> " + msg
        );
    }
    
    public static Mensagem recebeMensagem(
        BufferedReader inbound, boolean servidor
    ) throws IOException {
        Gson gson = new Gson();
        String string = inbound.readLine();
        System.out.println(
            (servidor ? "Servidor" : "Cliente") + " <- " + string
        );
        
        return gson.fromJson(string, Mensagem.class);
    }
    
    public static void fechaSocketEDutos(
        Socket client, PrintWriter outbound, BufferedReader inbound
    ) throws IOException {
        outbound.close();
        inbound.close();
        client.close();
    }
}
