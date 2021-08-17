/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicativos;

import com.google.gson.Gson;
import entidades.Agendamento;
import entidades.Mensageiro;
import entidades.TipoMensagem;
import entidades.Usuario;
import entidades.Mensagem;
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
        try {
            BufferedReader inbound = new BufferedReader(
                new InputStreamReader( client.getInputStream() )
            );
            PrintWriter outbound = new PrintWriter(
                client.getOutputStream(), true
            );
            
            Mensagem mensagem = Mensageiro.recebeMensagem(inbound, true);
            int idMensagem = mensagem.getId();
            
            if( idMensagem == TipoMensagem.PEDIDO_LOGIN.getId() ) {
                recebePedidoLogin(mensagem, outbound);
            }
            else if( idMensagem == TipoMensagem.PEDIDO_CADASTRO.getId() ) {
                recebePedidoCadastro(mensagem, outbound);
            }
            else {
                mensagem = new Mensagem(
                    TipoMensagem.ERRO, "Identificador de mensagem inválido"
                );
                Mensageiro.enviaMensagem(outbound, mensagem, true);
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
    
    private synchronized Usuario verificaLogin(Mensagem pedido) {
        Usuario usuario = verificaCpf( pedido.getCpf() );
        if(usuario != null)
            if( usuario.getSenha().equals( pedido.getSenha() ) ) return usuario;
        return null;
    }
    
    private synchronized void saveUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }
    
    private void recebePedidoLogin(
        Mensagem mensagem, PrintWriter outbound
    ) throws IOException {
        Agendamento agendamento = null;
        
        if(mensagem.getCpf() != null && mensagem.getSenha() != null) {
            Usuario usuario = verificaLogin(mensagem);

            if(usuario != null) {
               agendamento = usuario.getAgendamento();
               mensagem = new Mensagem(
                    agendamento != null, usuario.isAdmin()
               );
            }
            else mensagem = new Mensagem(TipoMensagem.LOGIN_INVALIDO);

            Mensageiro.enviaMensagem(outbound, mensagem, true);

            if(agendamento == null) return;

            mensagem = new Mensagem(agendamento);
        }
        else mensagem = new Mensagem(
            TipoMensagem.ERRO, "Dados para login incompletos"
        );
        
        Mensageiro.enviaMensagem(outbound, mensagem, true);
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
        Mensagem mensagem, PrintWriter outbound
    ) throws IOException {
        
        Usuario usuario = mensagem.getUsuario();
        
        if ( isPedidoCadastroValido(usuario) ) {  
            if (verificaCpf(usuario.getCpf()) == null) {
                saveUsuario(usuario);
                mensagem = new Mensagem(TipoMensagem.CADASTRO_EFETUADO);
            } else {
                mensagem = new Mensagem(TipoMensagem.CPF_JA_CADASTRADO);
            }
        }
        else mensagem = new Mensagem(
            TipoMensagem.ERRO, "Dados para cadastro incompletos"
        );
        
        Mensageiro.enviaMensagem(outbound, mensagem, true);
    }
}
