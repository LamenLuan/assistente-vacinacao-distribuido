/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicativos;

import entidades.Agendamento;
import entidades.DiaVacinacao;
import entidades.Mensageiro;
import entidades.TipoMensagem;
import entidades.Usuario;
import entidades.Mensagem;
import entidades.PostoDeSaude;
import entidades.Slot;
import entidades.Vacina;
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
    private static ArrayList<PostoDeSaude> postos;

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
            int id = mensagem.getId();
            
            if( id == TipoMensagem.PEDIDO_LOGIN.getId() ) {
                recebePedidoLogin(mensagem, outbound);
            }
            else if( id == TipoMensagem.PEDIDO_CADASTRO.getId() ) {
                recebePedidoCadastro(mensagem, outbound);
            }
            else if( id == TipoMensagem.PEDIDO_DADOS_AGENDAMENTO.getId() ) {
                recebePedidoDadosAgendamento(mensagem, outbound);
            }
            else if( id == TipoMensagem.PEDIDO_AGENDAMENTO.getId() ) {
                recebePedidoAgendamento(mensagem, outbound);
            }
            else if( id == TipoMensagem.PEDIDO_CANCELAMENTO.getId() ) {
                recebePedidoCancelamento(mensagem, outbound);
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
        addUsuarios();
        postos = new ArrayList<>();
        addPostos();
        
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

            mensagem = new Mensagem(
                agendamento.getNomePosto(),
                agendamento.getEndPosto(),
                agendamento.getData(),
                agendamento.getSlot(),
                agendamento.getVacina(),
                agendamento.isSegundaDose()
            );
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
    
    private synchronized ArrayList<PostoDeSaude> temVacinas() {
        ArrayList<PostoDeSaude> postosComVacinas = new ArrayList<>();
        
        for(PostoDeSaude posto : postos) {
            if( !posto.getDiasVacinacao().isEmpty() ) {
                for(DiaVacinacao diaVacinacao : posto.getDiasVacinacao() ) {
                    for( Slot slot : diaVacinacao.getSlots() ) {
                        if(slot.getQtdSlotVacinacao() > 0) {
                            postosComVacinas.add(posto);
                            break;
                        }
                    }
                    if(postosComVacinas.contains(posto)) break;
                }
            }
        }
        
        return postosComVacinas;
    }

    private void recebePedidoDadosAgendamento(
        Mensagem mensagem, PrintWriter outbound
    ) throws IOException {
        if(verificaLogin(mensagem) != null) {
            ArrayList<PostoDeSaude> postosComVacinas = temVacinas();
            
            if( postosComVacinas.isEmpty() ) 
                mensagem = new Mensagem(TipoMensagem.NAO_TEM_VACINAS);
            else {
                mensagem = new Mensagem(postosComVacinas);
            }
        }
        else {
            mensagem = new Mensagem(
                TipoMensagem.ERRO,
                "Dados de validação incorretos ou inexistentes!"
            );
        }
        
        Mensageiro.enviaMensagem(outbound, mensagem, true);
    }
    
    private synchronized Slot encontraSlot(
            String nome, String data, String slotPosto
    ) {
        // Verifico se os dados do agendamento condizem com os do servidor
        for (PostoDeSaude posto : postos) {
            if( posto.getNomePosto().equals(nome) ) {
                for (DiaVacinacao dia : posto.getDiasVacinacao() ) {
                    if( dia.getDia().equals(data) ) {
                        for ( Slot slot : dia.getSlots() ) {
                            if( slot.getSlotVacinacao().equals(slotPosto) )
                                return slot;
                        }   
                    }
                }
            }
        }
        
        return null;
    }
    
    private synchronized void devolveSlot(Agendamento agendamento) {
        Slot slot = encontraSlot(
            agendamento.getNomePosto(),
            agendamento.getData(),
            agendamento.getSlot()
        );

        if(slot != null) {
            slot.setQtdSlotVacinacao(slot.getQtdSlotVacinacao() + 1);
        }
    }
    
    private synchronized void saveAgendamento(
        Mensagem mensagem, Slot slotAgendamento, Usuario usuario,
        PrintWriter outbound
    ) throws IOException {
        
        Agendamento agendamento = usuario.getAgendamento();
        
        if(agendamento != null ) devolveSlot(agendamento);
        
        slotAgendamento.setQtdSlotVacinacao(
            slotAgendamento.getQtdSlotVacinacao() - 1
        );

        String endereco = null;
        for (PostoDeSaude posto : postos) {
            if( posto.getNomePosto().equals( mensagem.getNomePosto() ) ) {
                endereco = posto.getEndPosto();
            }
        }

        if(endereco != null) {
            usuario.setAgendamento(
                new Agendamento(
                    mensagem.getNomePosto(), endereco, mensagem.getData(),
                    mensagem.getSlot(), null, false
                )
            );
            
            mensagem = new Mensagem(TipoMensagem.AGENDAMENTO_CONCLUIDO);
        }
        else mensagem = new Mensagem(
            TipoMensagem.ERRO, "Erro, agendamento não pôde ser salvo."
        );
        
        Mensageiro.enviaMensagem(outbound, mensagem, true);
    }

    private synchronized void recebePedidoAgendamento(
        Mensagem mensagem, PrintWriter outbound
    ) throws IOException {
        String nome = mensagem.getNomePosto(), data = mensagem.getData(),
            slotPosto = mensagem.getSlot();
        Usuario usuario = verificaLogin(mensagem);
        
        if(usuario != null) {
            Slot slot = encontraSlot(nome, data, slotPosto);
            if(slot != null && slot.getQtdSlotVacinacao() > 0)
                saveAgendamento(mensagem, slot, usuario, outbound);
        }
        else {
            mensagem = new Mensagem(
                TipoMensagem.ERRO,
                "Dados de validação incorretos ou inexistentes!"
            );
            Mensageiro.enviaMensagem(outbound, mensagem, true);
        }
    }
    
    private void recebePedidoCancelamento(
        Mensagem mensagem, PrintWriter outbound
    ) throws IOException {
        
        Usuario usuario = verificaLogin(mensagem);
        
        if(usuario != null) {
            Agendamento agendamento = usuario.getAgendamento();
            if(agendamento != null) {
                devolveSlot(agendamento);
                usuario.setAgendamento(null);
                mensagem = new Mensagem(TipoMensagem.CONFIRMACAO_CANCELAMENTO);
            }
            else mensagem = new Mensagem(
                TipoMensagem.ERRO,
                "Não existe agendamento registrado no servidor."
            );
        }
        else {
            mensagem = new Mensagem(
                TipoMensagem.ERRO,
                "Dados de validação incorretos ou inexistentes."
            );
        }
        
        Mensageiro.enviaMensagem(outbound, mensagem, true);
    }
    
    private static void addUsuarios() {
        usuarios.add(
            new Usuario(
                "Fulano", "11111111111", "01/01/2001", "1111111111",
                "", "senha", true, true, false, null
            )
        );
        usuarios.add(
            new Usuario(
                "Ciclano", "99999999999", "01/01/2001", "1111111111",
                "", "senha", true, true, true, null
            )
        );
    }
    
    private static void addPostos() {
        PostoDeSaude posto = new PostoDeSaude(
            "Unidade de Saúde Zé Gotinha Rei Delas", "Avenida Alan Turing, 0101"
        );
        
        posto.getVacinasPosto().add(
            new Vacina("Pfizer", 200, true)
        );
        posto.getDiasVacinacao().add(
            new DiaVacinacao( "20/08/2021", new ArrayList<>() )
        );
        posto.getDiasVacinacao().add(
            new DiaVacinacao( "21/08/2021", new ArrayList<>() )
        );
        
        posto.getDiasVacinacao().get(0).getSlots().add(
            new Slot("Manhã", 15)
        );
        posto.getDiasVacinacao().get(0).getSlots().add(
            new Slot("Tarde", 15)
        );
        posto.getDiasVacinacao().get(1).getSlots().add(
            new Slot("Tarde", 15)
        );
        posto.getDiasVacinacao().get(1).getSlots().add(
            new Slot("Noite", 15)
        );
        
        postos.add(posto);
    }
}
