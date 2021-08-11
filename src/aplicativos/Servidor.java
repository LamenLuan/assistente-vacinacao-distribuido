/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicativos;

import com.google.gson.Gson;
import entidades.Agendamento;
import entidades.TipoMensagem;
import entidades.Usuario;
import entidades.mensagens.Erro;
import entidades.mensagens.LoginAprovado;
import entidades.mensagens.Mensagem;
import entidades.mensagens.PedidoCadastro;
import entidades.mensagens.PedidoLogin;
import entidades.mensagens.TemAgendamento;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
/**
 *
 * @author luanl
 */
public class Servidor extends Thread {
    
    private static int porta = 1234;
    protected Socket client;
    private static ArrayList<Usuario> usuarios;

    private Servidor(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
       BufferedReader inbound;
       PrintWriter outbound;
       Gson gson = new Gson();
       
       String string;
       Mensagem mensagem;
       
        try {
            inbound = new BufferedReader(
                new InputStreamReader( client.getInputStream() )
            );
            outbound = new PrintWriter( client.getOutputStream(), true );
            
            string = inbound.readLine();
            System.out.println("Servidor <- " + string);
            
            mensagem = gson.fromJson(string, Mensagem.class);
            int idMensagem = mensagem.getId();
            
            if( idMensagem == TipoMensagem.PEDIDO_LOGIN.getId() ) {
                recebePedidoLogin(gson, string, outbound);
            }
            else if( idMensagem == TipoMensagem.PEDIDO_CADASTRO.getId() ) {
                recebePedidoCadastro(gson, string, outbound);
            }
            else escreveMensagem(
                new Erro("Identificador de mensagem inválido"), gson, outbound
            );
        } catch (IOException ex) {
            System.err.println("Erro: " + ex.getMessage() );
        }
       
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ServerSocket server = null;
     
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Porta:");
//        porta = scanner.nextInt();
        
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
            server = new ServerSocket(porta);
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
    
    private synchronized Usuario verificaCpf(String cpf) {
        for(Usuario usuario : usuarios) {
            if( usuario.getCpf().equals(cpf) ) return usuario;
        } 
        return null;
    }
    
    private synchronized Usuario verificaLogin(PedidoLogin pedido) {
        Usuario usuario = verificaCpf( pedido.getCpf() );
        if(usuario != null)
            if( usuario.getSenha().equals( pedido.getSenha() ) ) return usuario;
        return null;
    }
    
    private synchronized void saveUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }
    
    private void escreveMensagem(
        Mensagem mensagem, Gson gson, PrintWriter outbound
    ) throws IOException {
        String str = gson.toJson(mensagem);
        outbound.println(str);
        System.out.println("Servidor -> " + str);
    }
    
    private void recebePedidoLogin(
        Gson gson, String string, PrintWriter outbound
    ) throws IOException {
        Mensagem mensagem;
        Agendamento agendamento = null;
        PedidoLogin pedido = gson.fromJson(string, PedidoLogin.class);
        
        if(pedido.getCpf() != null && pedido.getSenha() != null) {
            Usuario usuario = verificaLogin(pedido);

            if(usuario != null) {
               mensagem = new LoginAprovado(usuario);
               agendamento = usuario.getAgendamento();
            }
            else mensagem = new Mensagem(TipoMensagem.LOGIN_INVALIDO);

            escreveMensagem(mensagem, gson, outbound);

            if(agendamento == null) return;

            mensagem = new TemAgendamento(agendamento);
        }
        else mensagem = new Erro("Dados para login incompletos");
        
        escreveMensagem(mensagem, gson, outbound);
    }
    
    private boolean isPedidoCadastroValido(Usuario usuario) {
        if(usuario == null) return false;
        return (
            usuario.getNome() != null &&
            usuario.getCpf() != null &&
            usuario.getDataNascimento() != null &&
            usuario.getTelefone() != null &&
            usuario.getEmail() != null &&
            usuario.getSenha() != null
        );
    }
    
    private void recebePedidoCadastro(
        Gson gson, String string, PrintWriter outbound
    ) throws IOException {
        Mensagem mensagem;
        Usuario usuario;
        PedidoCadastro pedidoCadastro = gson.fromJson(
            string, PedidoCadastro.class
        );
        usuario = pedidoCadastro.getUsuario();
        
        if ( isPedidoCadastroValido(usuario) ) {  
            if (verificaCpf(usuario.getCpf()) == null) {
                saveUsuario(usuario);
                mensagem = new Mensagem(TipoMensagem.CADASTRO_EFETUADO);
            } else {
                mensagem = new Mensagem(TipoMensagem.CPF_JA_CADASTRADO);
            }
        }
        else mensagem = new Erro("Dados para cadastro incompletos");
        
        escreveMensagem(mensagem, gson, outbound);
    }
}
