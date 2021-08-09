/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import com.google.gson.Gson;
import entidades.mensagens.Mensagem;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
        DataOutputStream outbound, Mensagem mensagem
    ) throws IOException {
        Gson gson = new Gson();
        outbound.writeUTF( gson.toJson(mensagem) );
    }
    
    public static String recebeMensagem(
        DataInputStream inbound
    ) throws IOException {
        String string = inbound.readUTF();
        
        return string;
    }
    
    public static int getIdMensagem(String string) {
        Gson gson = new Gson();
        Mensagem mensagem = gson.fromJson(string, Mensagem.class);
        
        return mensagem.getId();
    }
    
    public static void fechaSocketEDutos(
        Socket client, DataOutputStream outbound, DataInputStream inbound
    ) throws IOException {
        outbound.close();
        inbound.close();
        client.close();
    }
}
