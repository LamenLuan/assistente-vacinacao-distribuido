/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicativos;

import com.google.gson.Gson;
import entidades.Agendamento;
import entidades.MensageiroCliente;
import entidades.TipoMensagem;
import entidades.Usuario;
import entidades.mensagens.LoginAprovado;
import entidades.mensagens.Mensagem;
import entidades.mensagens.PedidoLogin;
import entidades.mensagens.TemAgendamento;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author luanl
 */
public class Servidor extends Thread {
    protected Socket client;
    private static ArrayList<Usuario> usuarios;

    private Servidor(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
       DataInputStream inbound;
       DataOutputStream outbound;
       Gson gson = new Gson();
       
       String string;
       Mensagem mensagem;
       
        try {
            inbound = new DataInputStream( client.getInputStream() );
            outbound = new DataOutputStream( client.getOutputStream() );
            
            string = inbound.readUTF();
            System.out.println("Servidor <- " + string);
            
            mensagem = gson.fromJson(string, Mensagem.class);
            int idMensagem = mensagem.getId();
            
            if( idMensagem == TipoMensagem.PEDIDO_LOGIN.getId() ) {
                recebePedidoLogin(gson, string, outbound);
            }
        } catch (IOException ex) {
            System.err.println("Erro: " + ex.getMessage() );
        }
       
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ServerSocket server = null;
        
        usuarios = new ArrayList<>();
        usuarios.add(
            new Usuario(
                "Fulano", "11111111111", "01/01/2001", "1111111111",
                "", "senha", true, true, false, new Agendamento(
                    "Local", "Data", "Hora", "Astrazeneca", true
                )
            )
        );
        usuarios.add(
            new Usuario(
                "Ciclano", "99999999999", "01/01/2001", "1111111111",
                "", "senha", true, true, true, null
            )
        );
        
        try {
            server = new ServerSocket(MensageiroCliente.porta);
            try {
                while (true) {
                    new Servidor( server.accept() ).start(); 
                }
            } catch (IOException ex) {
                System.err.println("Falha no accept."); 
                System.exit(1); 
            }
        } catch (IOException e) {
            System.err.println("Erro na conexao com a porta.");
            System.exit(1);
        } finally {
            try {
                if(server != null) server.close();
            } catch (IOException e) {
                System.err.println("Erro no fechamento da porta."); 
                System.exit(1); 
            }
        }
    }
    
    private synchronized Usuario verificaLogin(PedidoLogin pedido) {
        for(Usuario usuario : usuarios) {
            if( usuario.getCpf().equals( pedido.getCpf() ) ) {
                if( usuario.getSenha().equals( pedido.getSenha() ) )
                    return usuario;
            }
        }  
        return null;
    }
    
    private void escreveMensagem(
        Mensagem mensagem, Gson gson, DataOutputStream outbound
    ) throws IOException {
        String str = gson.toJson(mensagem);
        outbound.writeUTF(str);
        System.out.println("Servidor -> " + str);
    }
    
    private void recebePedidoLogin(
        Gson gson, String string, DataOutputStream outbound
    ) throws IOException {
        Mensagem mensagem;
        Agendamento agendamento = null;
        PedidoLogin pedido = gson.fromJson(string, PedidoLogin.class);
        Usuario usuario = verificaLogin(pedido);

        if(usuario != null) {
           mensagem = new LoginAprovado(usuario);
           agendamento = usuario.getAgendamento();
        }
        else mensagem = new Mensagem(TipoMensagem.LOGIN_INVALIDO);
        
        escreveMensagem(mensagem, gson, outbound);
        
        if(agendamento == null) return;
        
        mensagem = new TemAgendamento(agendamento);
        escreveMensagem(mensagem, gson, outbound);
    }
}
