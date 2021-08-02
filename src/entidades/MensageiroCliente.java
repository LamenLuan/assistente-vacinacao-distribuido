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
    
    public static void enviaMensagem(
        Socket client, Mensagem mensagem
    ) throws IOException {
        
        DataOutputStream outbound = new DataOutputStream(
            client.getOutputStream()
        );
        Gson gson = new Gson();

        outbound.writeUTF( gson.toJson(mensagem) );
    }
    
    public static String recebeMensagem(Socket client) throws IOException {
        DataInputStream inbound = new DataInputStream(
            client.getInputStream()
        );
        String string = inbound.readUTF();
        inbound.close();
        
        return string;
    }
    
    public static int getIdMensagem(String string) {
        Gson gson = new Gson();
        Mensagem mensagem = gson.fromJson(string, Mensagem.class);
        
        return mensagem.getId();
    }
}
