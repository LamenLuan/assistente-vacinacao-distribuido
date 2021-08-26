/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicativos;

import com.google.gson.Gson;
import entidades.AdministradorAtendente;
import entidades.Agendamento;
import entidades.Dia;
import entidades.DiasVacinacao;
import entidades.ListaPosto;
import entidades.ListaVacinas;
import entidades.Mensageiro;
import entidades.TipoMensagem;
import entidades.Usuario;
import entidades.Mensagem;
import entidades.PostoDeSaude;
import entidades.Slot;
import entidades.Vacina;
import entidades.mensagensCRUD.CadastroDiaVacinacao;
import entidades.mensagensCRUD.CadastroPostoPt1;
import entidades.mensagensCRUD.CadastroSlot;
import entidades.mensagensCRUD.CadastroVacina;
import entidades.mensagensCRUD.Erro;
import entidades.mensagensCRUD.ListagemDiasVacinacao;
import entidades.mensagensCRUD.ListagemPostos;
import entidades.mensagensCRUD.ListagemSlots;
import entidades.mensagensCRUD.ListagemVacinas;
import entidades.mensagensCRUD.MensagemCRUD;
import entidades.mensagensCRUD.PedidoListagemPostos;
import entidades.mensagensCRUD.PedidoRemovePosto;
import entidades.mensagensCRUD.PedidoUpdatePosto;
import entidades.mensagensCRUD.ReadDias;
import entidades.mensagensCRUD.RemoveDia;
import entidades.mensagensCRUD.RemoveSlot;
import entidades.mensagensCRUD.RemoveVacina;
import entidades.mensagensCRUD.SucessoCRUD;
import entidades.mensagensCRUD.UpdateDia;
import entidades.mensagensCRUD.UpdateSlot;
import entidades.mensagensCRUD.UpdateVacina;
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
    
    private static ArrayList<AdministradorAtendente> adminsAtendentes;

    private Servidor(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            Gson gson = new Gson();
            BufferedReader inbound = new BufferedReader(
                new InputStreamReader( client.getInputStream() )
            );
            PrintWriter outbound = new PrintWriter(
                client.getOutputStream(), true
            );
            
            String string = inbound.readLine();
            System.out.println("Servidor <- " + string);
            
            MensagemCRUD mensagem = gson.fromJson(string, MensagemCRUD.class);
            int id = mensagem.getId();
            
            if( id == TipoMensagem.PEDIDO_LOGIN.getId() ) {
                recebePedidoLogin(gson, string, outbound);
            } else if( id == TipoMensagem.PEDIDO_CADASTRO.getId() ) {
                recebePedidoCadastro(gson, string, outbound);
            } else if( id == TipoMensagem.PEDIDO_DADOS_AGENDAMENTO.getId() ) {
                recebePedidoDadosAgendamento(gson, string, outbound);
            } else if( id == TipoMensagem.PEDIDO_AGENDAMENTO.getId() ) {
                recebePedidoAgendamento(gson, string, outbound);
            } else if( id == TipoMensagem.PEDIDO_CANCELAMENTO.getId() ) {
                recebePedidoCancelamento(gson, string, outbound);
            } else if( id == TipoMensagem.PEDIDO_ABERTURA_CHAT.getId() ) {
                recebePedidoChat(gson, string, inbound, outbound);
            } else if( id == TipoMensagem.CADASTRO_NOME_ENDERECO_POSTO.getId() ) {
                recebePedidoCadastroPostoPt1(gson, string, outbound);
            } else if( id == TipoMensagem.LISTA_POSTOS_NOMES.getId() ) {
                recebeListagemNomesPostos(gson, string, outbound);
            } else if( id == TipoMensagem.PEDIDO_UPDATE_POSTO.getId() ) {
                updateNomePosto(gson, string, outbound);
            } else if( id == TipoMensagem.PEDIDO_REMOVE_POSTO.getId() ) {
                removePosto(gson, string, outbound);
            } else if( id == TipoMensagem.CADASTRO_VACINA.getId() ) {
                cadastraVacina(gson, string, outbound);
            } else if( id == TipoMensagem.LISTA_VACINAS.getId() ) {
                recebeListagemVacinas(gson, outbound);
            } else if( id == TipoMensagem.UPDATE_VACINA.getId() ) {
                updateVacina(gson, string, outbound);
            } else if( id == TipoMensagem.REMOVE_VACINA.getId() ) {
                removeVacina(gson, string, outbound);
            } else if( id == TipoMensagem.CADASTRO_DIA_VACINACAO.getId() ) {
                cadastraDia(gson, string, outbound);
            } else if( id == TipoMensagem.LISTA_DIAS.getId() ) {
                listagemDias(gson, string, outbound);
            } else if( id == TipoMensagem.UPDATE_DIA.getId() ) {
                updateDia(gson, string, outbound);
            } else if( id == TipoMensagem.REMOVE_DIA.getId() ) {
                removeDia(gson, string, outbound);
            } else if( id == TipoMensagem.CADASTRO_SLOT.getId() ) {
                cadastraSlot(gson, string, outbound);
            } else if( id == TipoMensagem.LISTA_SLOTS.getId() ) {
                listagemSlots(gson, string, outbound);
            } else if( id == TipoMensagem.UPDATE_SLOT.getId() ) {
                updateSlot(gson, string, outbound);
            } else if( id == TipoMensagem.REMOVE_SLOT.getId() ) {
                removeSlot(gson, string, outbound);
            } else {
                Mensagem msg = new Mensagem(
                    TipoMensagem.ERRO, "Identificador de mensagem inválido"
                );
                Mensageiro.enviaMensagem(outbound, msg, true);
            }
        } catch (IOException | NullPointerException ex) {}
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ServerSocket server = null;
     
        Scanner scanner = new Scanner(System.in);
        System.out.println("Porta:");
        porta = scanner.nextInt();
        
        usuarios = new ArrayList<>();
        addUsuarios();
        postos = new ArrayList<>();
        addPostos();
        adminsAtendentes = new ArrayList<>();
        
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
    
    private synchronized Usuario verificaAdmin(String cpf, String senha) {
        Usuario usuario = verificaCpf(cpf);
        if(usuario != null) {
            if( usuario.getSenha().equals(senha) && usuario.isAdmin() )
                return usuario;
        }
        return null;
    }
    
    private synchronized void saveUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }
    
    private synchronized void savePosto(PostoDeSaude posto) {
        postos.add(posto);
    }
    
    private void escreveMensagem(
        MensagemCRUD mensagem, Gson gson, PrintWriter outbound
    ) throws IOException {
        String str = gson.toJson(mensagem);
        outbound.println(str);
        System.out.println("Servidor -> " + str);
    }
    
    private void recebePedidoLogin(
        Gson gson, String string, PrintWriter outbound
    ) throws IOException {
        Agendamento agendamento = null;
        
        Mensagem mensagem = gson.fromJson(string, Mensagem.class);
        
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
        Gson gson, String string, PrintWriter outbound
    ) throws IOException {
        
        Mensagem mensagem = gson.fromJson(string, Mensagem.class);
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
                for(DiasVacinacao diaVacinacao : posto.getDiasVacinacao() ) {
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
        Gson gson, String string, PrintWriter outbound
    ) throws IOException {
        Mensagem mensagem = gson.fromJson(string, Mensagem.class);
        
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
                for (DiasVacinacao dia : posto.getDiasVacinacao() ) {
                    if( dia.getData().equals(data) ) {
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
                    mensagem.getSlot(), "", false
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
        Gson gson, String string, PrintWriter outbound
    ) throws IOException {
        
        Mensagem mensagem = gson.fromJson(string, Mensagem.class);
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
        Gson gson, String string, PrintWriter outbound
    ) throws IOException {
        
        Mensagem mensagem = gson.fromJson(string, Mensagem.class);
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
    
    private synchronized AdministradorAtendente saveAdmin(
        Usuario usuario, BufferedReader inbound, PrintWriter outbound
    ) {
        
        AdministradorAtendente admin = new AdministradorAtendente(
            usuario, client, inbound, outbound
        );
        adminsAtendentes.add(admin);
        
        return  admin;
    }
    
    private synchronized void removeAdmin(AdministradorAtendente admin) {
        admin.fechaSocketEDutos();
        adminsAtendentes.remove(admin);
    }
    
    private synchronized AdministradorAtendente buscaAdmin(Usuario usuario) {
        for (AdministradorAtendente admin : adminsAtendentes) {
            if(admin.getUsuario() == usuario) {
                return admin;
            }
        }
        return null;
    }
    
    private synchronized AdministradorAtendente buscaAdminDisponivel() {
        for (AdministradorAtendente admin : adminsAtendentes) {
            if( admin.isDisponivel() ) {
                return admin;
            }
        }
        return null;
    }
    
    private void loopAtendeAdmin(
        BufferedReader inbound, AdministradorAtendente admin
    ) throws IOException {
        
        boolean atendendo = true;
        while (atendendo) {  
            Mensagem mensagem = Mensageiro.recebeMensagem(inbound, true);
            int id = mensagem.getId();
            
            if( id == TipoMensagem.MENSAGEM_CLIENTE.getId() ) {
                mensagem = new Mensagem(
                    TipoMensagem.DIRECIONA_MSG_CLIENTE,
                    mensagem.getMensagem()
                );
                Mensageiro.enviaMensagem(
                    admin.getOutboundUser(), mensagem, true
                );
            }
            else if( id == TipoMensagem.ADMIN_DISPONIVEL.getId() ) {
                admin.setDisponivel(true);
            }
            else if( id == TipoMensagem.ADMIN_INDISPONIVEL.getId() ) {
                admin.setDisponivel(false);
            }
            else if( id == TipoMensagem.PEDIDO_LOGOUT_CHAT.getId() ) {
                // Se admin no meio de um atendimento
                if( !admin.isDisponivel() ) {
                    mensagem = new Mensagem(
                        TipoMensagem.ENCERRAMENTO_CHAT
                    );
                    Mensageiro.enviaMensagem(
                        admin.getOutboundUser(), mensagem, true
                    );
                }
                removeAdmin(admin);
                atendendo = false;
            }
        }
    }
    
    private void enviaDadosClienteParaAdmin(
        Usuario usuario, AdministradorAtendente admin
    ) throws IOException {
        
        Mensagem mensagem = new Mensagem(
            usuario.getNome(), TipoMensagem.DADOS_CHAT_CLIENTE
        ); 

        Mensageiro.enviaMensagem(
            admin.getOutboundAdm(), mensagem, true
        );
    }
    
    private void enviaDadosAdminParaCliente(
        AdministradorAtendente admin, PrintWriter outbound
    ) throws IOException {
        
        Mensagem mensagem = new Mensagem(
            admin.getUsuario().getNome(),
            TipoMensagem.DADOS_CHAT_ADMIN
        );
        
        Mensageiro.enviaMensagem(outbound, mensagem, true);
    }
    
    private synchronized void setClientUsuario(
        AdministradorAtendente admin, Socket client, BufferedReader inbound,
        PrintWriter outbound
    ) {
        admin.setClientUser(client);
        admin.setInboundUser(inbound);
        admin.setOutboundUser(outbound);
    }
    
    private void loopAtendeUsuario(
        BufferedReader inbound, PrintWriter outbound,
        AdministradorAtendente admin
    ) throws IOException {
        
        boolean atendendo = true;
        while (atendendo) {
            Mensagem mensagem = Mensageiro.recebeMensagem(inbound, true);
            int id = mensagem.getId();
            if( id == TipoMensagem.MENSAGEM_CLIENTE.getId() ) {
                mensagem = new Mensagem(
                    TipoMensagem.DIRECIONA_MSG_CLIENTE,
                    mensagem.getMensagem()
                );
                Mensageiro.enviaMensagem(
                    admin.getOutboundAdm(), mensagem, true
                );
            }
            else if( id == TipoMensagem.PEDIDO_LOGOUT_CHAT.getId() ) {
                // Aviso para o admin que o chat vai ser encerrado
                mensagem = new Mensagem(TipoMensagem.ENCERRAMENTO_CHAT);
                Mensageiro.enviaMensagem(
                    admin.getOutboundAdm(), mensagem, true
                );
                atendendo = false;
                // Retiro a conexao do usuario do admin
                setClientUsuario(admin, null, null, null);
                // Fecho a conexa do usuario
                Mensageiro.fechaSocketEDutos(client, outbound, inbound);
            }
        }
    }
    
    private void recebePedidoChat (
        Gson gson, String string, BufferedReader inbound, PrintWriter outbound
    ) throws IOException {
        Mensagem mensagem = gson.fromJson(string, Mensagem.class);
        Usuario usuario = verificaLogin(mensagem);
        
        if(usuario != null) {
            if( usuario.isAdmin() ) {
                AdministradorAtendente admin = buscaAdmin(usuario);
                if(admin == null) admin = saveAdmin(
                    usuario, inbound, outbound
                );
                loopAtendeAdmin(inbound, admin);
            }
            else {
                AdministradorAtendente admin = buscaAdminDisponivel();
                if(admin != null) {
                    admin.setDisponivel(false);
                    // Guardo a conexao do usuario no admin
                    setClientUsuario(admin, client, inbound, outbound);
                    // Avisa ao admin qual usuario ele vai atender
                    enviaDadosClienteParaAdmin(usuario, admin);
                    // Avisa o usuario o nome do admin que vai atende-lo
                    enviaDadosAdminParaCliente(admin, outbound);

                    loopAtendeUsuario(inbound, outbound, admin);
                }
                else {
                    mensagem = new Mensagem(
                        TipoMensagem.NENHUM_ADMIN_DISPONIVEL
                    );
                    try {
                        Mensageiro.enviaMensagem(outbound, mensagem, true);
                    } catch (IOException ex) {}
                }
            }
        }
        else {
            mensagem = new Mensagem(
                TipoMensagem.ERRO,
                "Dados de validação incorretos ou inexistentes."
            );
            try {
                Mensageiro.enviaMensagem(outbound, mensagem, true);
            } catch (IOException ex) {}
        }
    }
    
    private void recebePedidoCadastroPostoPt1(
        Gson gson, String string, PrintWriter outbound
    ) throws IOException {
        MensagemCRUD mensagem;
        CadastroPostoPt1 pedidoPostoPt1 = gson.fromJson(
            string, CadastroPostoPt1.class
        );
        String cpf = pedidoPostoPt1.getCpf(), senha = pedidoPostoPt1.getSenha();
        
        if(verificaAdmin(cpf, senha) != null) {
            
            PostoDeSaude posto = new PostoDeSaude(
                pedidoPostoPt1.getNomePosto(), pedidoPostoPt1.getEndPosto()
            );

            savePosto(posto);
            mensagem = new SucessoCRUD("Operação realizada com sucesso!");
        }
        else mensagem = new Erro(
            "Dados de validação incorretos ou inexistentes!"
        );    
        
        escreveMensagem(mensagem, gson, outbound);
    }
    
    private void recebeListagemNomesPostos(
            Gson gson, String string, PrintWriter outbound
    ) throws IOException {
        
        PedidoListagemPostos pedido = gson.fromJson(
            string, PedidoListagemPostos.class
        );
        MensagemCRUD mensagem;
        
        if(verificaAdmin( pedido.getCpf(), pedido.getSenha() ) != null) {
            ArrayList<ListaPosto> nomesPostos = new ArrayList<>();
            for(PostoDeSaude posto : postos) {
                nomesPostos.add( new ListaPosto( posto.getNomePosto() ) );
            }
            mensagem = new ListagemPostos(nomesPostos);
        }
        else mensagem = new Erro(
            "Dados de validação incorretos ou inexistentes!"
        );

        
        
        escreveMensagem(mensagem, gson, outbound);
    }
    
    private PostoDeSaude findPosto(String nomePosto){
        for (PostoDeSaude posto : postos) {
            if(posto.getNomePosto().equals(nomePosto)) return posto;
        }
        return null;
    }
    
    private Vacina findVacina(String nomePosto, String nomeVacina){
        for (int i = 0; i < postos.size(); i++) {
            PostoDeSaude p = postos.get(i);
            
            if(p.getNomePosto().equals(nomePosto)){
                for (int j = 0; j < p.getVacinasPosto().size(); j++) {
                    if(p.getVacinasPosto().get(j).getNomeVacina().equals(nomeVacina)){
                        return p.getVacinasPosto().get(j);
                    }
                }
            }
        }
        return null;
    }
    
    private void updateNomePosto(
            Gson gson, String string, PrintWriter outbound
    ) throws IOException {
        MensagemCRUD mensagem;
        
        PedidoUpdatePosto pedidoUpdatePosto = gson.fromJson(
            string, PedidoUpdatePosto.class
        );
        
        PostoDeSaude postoAlvo = findPosto(
            pedidoUpdatePosto.getNomePostoAlvo()
        );

        int indice = postos.indexOf(postoAlvo);
        postoAlvo.setNomePosto( pedidoUpdatePosto.getNomePosto() );
        postoAlvo.setEndPosto( pedidoUpdatePosto.getEndPosto() );
            
        postos.set(indice, postoAlvo);
        
        mensagem = new SucessoCRUD("Operação realizada com sucesso!");
        
        escreveMensagem(mensagem, gson, outbound);
    }
    
    private void removePosto(
            Gson gson, String string, PrintWriter outbound)
     throws IOException {
        MensagemCRUD mensagem;
        
        PedidoRemovePosto pedidoRemovePosto = gson.fromJson(
            string, PedidoRemovePosto.class
        );
        
        PostoDeSaude postoAlvo = findPosto(pedidoRemovePosto
                                           .getNomePostoAlvo());
        
        if(postos.remove(postoAlvo))
            mensagem = new SucessoCRUD("Operação realizada com sucesso!");
        else
            mensagem = new MensagemCRUD(TipoMensagem.ERRO);
       
        escreveMensagem(mensagem, gson, outbound);
    }
    
    private void saveVacina(PostoDeSaude posto, Vacina vacina) {
        int indice = postos.indexOf(posto);
        postos.get(indice).getVacinasPosto().add(vacina);
    }
    
    private void cadastraVacina(Gson gson, String string, PrintWriter outbound)
     throws IOException {
        MensagemCRUD mensagem;
        PostoDeSaude posto;
        CadastroVacina cadastraVacina = gson.fromJson(
            string, CadastroVacina.class
        );
        posto = findPosto(cadastraVacina.getNomePosto());
        
        saveVacina(posto, cadastraVacina.getVacinasPosto());
        
        mensagem = new SucessoCRUD("Operação realizada com sucesso!");
        
        escreveMensagem(mensagem, gson, outbound);
    }
    
    private void recebeListagemVacinas(
        Gson gson, PrintWriter outbound
    ) throws IOException {
        MensagemCRUD mensagem;
        ArrayList<ListaVacinas> listaVacinas = new ArrayList<>();

        for (int idx = 0; idx < postos.size(); idx++) {
            ListaVacinas itemListaVacina = new ListaVacinas();
            PostoDeSaude posto = postos.get(idx);
            
            itemListaVacina.setNomePosto(posto.getNomePosto());
            itemListaVacina.setVacinasPosto(posto.getVacinasPosto());
            listaVacinas.add(itemListaVacina);
        }
        
        mensagem = new ListagemVacinas(listaVacinas);
        
        escreveMensagem(mensagem, gson, outbound);
    }
    
    private void updateVacina(
            Gson gson, String string, PrintWriter outbound
    ) throws IOException {
        MensagemCRUD mensagem;
        
        UpdateVacina pedidoUpdateVacina = gson.fromJson(
            string, UpdateVacina.class
        );
        
        PostoDeSaude postoAlvo = findPosto(pedidoUpdateVacina
                                           .getNomePostoAlvo());
        
        Vacina vacinaAlvo = findVacina(
            pedidoUpdateVacina.getNomePostoAlvo(), 
            pedidoUpdateVacina.getNomeVacinaAlvo()
        );
        
        int indicePosto = postos.indexOf(postoAlvo);
        int indiceVacina =
            postos.get(indicePosto).getVacinasPosto().indexOf(vacinaAlvo);
            
        postos.get(indicePosto).getVacinasPosto().set(
            indiceVacina, pedidoUpdateVacina.getVacinasPosto()
        );
        
        mensagem = new SucessoCRUD("Operação realizada com sucesso!");
        
        escreveMensagem(mensagem, gson, outbound);
    }
    
    private void removeVacina(
        Gson gson, String string, PrintWriter outbound
    ) throws IOException {
        MensagemCRUD mensagem;
        
        RemoveVacina removeVacina = gson.fromJson(
            string, RemoveVacina.class
        );
        
        PostoDeSaude postoAlvo = findPosto(removeVacina.getNomePostoAlvo());
        
        Vacina vacinaAlvo = findVacina(
            removeVacina.getNomePostoAlvo(), 
            removeVacina.getNomeVacinaAlvo()
        );
        
        int indicePosto = postos.indexOf(postoAlvo);
        
        if(postos.get(indicePosto).getVacinasPosto().remove(vacinaAlvo)) {
            mensagem = new SucessoCRUD("Operação realizada com sucesso!");
        }
        
        else mensagem = new MensagemCRUD(TipoMensagem.ERRO);
       
        escreveMensagem(mensagem, gson, outbound);
    }
    
    private void cadastraDia(
            Gson gson, String string, PrintWriter outbound) 
    throws IOException {
        MensagemCRUD mensagem;
        PostoDeSaude posto;
        CadastroDiaVacinacao cadastroDia = gson.fromJson(
            string, CadastroDiaVacinacao.class
        );
        
        posto = findPosto(cadastroDia.getNomePosto());
        
        int indice = postos.indexOf(posto);
        postos.get(indice).getDiasVacinacao().add(
            new DiasVacinacao( cadastroDia.getDiaPosto().getData() )
        );
        
        mensagem = new SucessoCRUD("Operação realizada com sucesso!");
        
        escreveMensagem(mensagem, gson, outbound);    
    }
    
    private void listagemDias(
        Gson gson, String string, PrintWriter outbound
    ) throws IOException {
        MensagemCRUD mensagem;
        PostoDeSaude posto;
        ArrayList<Dia> dias = new ArrayList<>();
        
        ReadDias nomePosto = gson.fromJson(
            string, ReadDias.class
        );
        
        posto = findPosto(nomePosto.getNomePosto());
        
        for (DiasVacinacao diaVacinacao : posto.getDiasVacinacao()) {
            Dia novoDia = new Dia(diaVacinacao.getData());
            
            if(!dias.contains(novoDia)) dias.add(novoDia);
        }
        
        mensagem = new ListagemDiasVacinacao(posto.getNomePosto(), dias);
        
        escreveMensagem(mensagem, gson, outbound);
    }
    
    private DiasVacinacao findDia(String nomePosto, String data){
        for (int i = 0; i < postos.size(); i++) {
            PostoDeSaude p = postos.get(i);
            
            if(p.getNomePosto().equals(nomePosto)){
                for (int j = 0; j < p.getDiasVacinacao().size(); j++) {
                    if(p.getDiasVacinacao().get(j).getData().equals(data)){
                        return p.getDiasVacinacao().get(j);
                    }
                }
            }
        }
        return null;
    }
    
    private void updateDia(
        Gson gson, String string, PrintWriter outbound
    ) throws IOException {
        MensagemCRUD mensagem;
        PostoDeSaude postoAlvo;
        
        UpdateDia updateDia = gson.fromJson(
            string, UpdateDia.class
        );
        
        postoAlvo = findPosto(updateDia.getNomePostoAlvo());
        
        DiasVacinacao diaAlvo = findDia(
            postoAlvo.getNomePosto(), updateDia.getDataAntiga()
        );
        
        int indicePosto = postos.indexOf(postoAlvo);
        int indiceDia =
            postos.get(indicePosto).getDiasVacinacao().indexOf(diaAlvo);
        
        postos.get(indicePosto).getDiasVacinacao().get(indiceDia).setData(
            updateDia.getNovaData()
        );
        
        mensagem = new SucessoCRUD("Operação realizada com sucesso!");
        
        escreveMensagem(mensagem, gson, outbound);
    }
    
    private void removeDia(
        Gson gson, String string, PrintWriter outbound
    ) throws IOException {
        MensagemCRUD mensagem;
        PostoDeSaude postoAlvo;
        
        RemoveDia removeDia = gson.fromJson(string, RemoveDia.class);
        
        postoAlvo = findPosto(removeDia.getNomePostoAlvo());
        
        DiasVacinacao diaAlvo = findDia(postoAlvo.getNomePosto(), removeDia.getData());
        
        int indicePosto = postos.indexOf(postoAlvo);
        
        if(postos.get(indicePosto).getDiasVacinacao().remove(diaAlvo)) {
            mensagem = new SucessoCRUD("Operação realizada com sucesso!");
        }
        else mensagem = new MensagemCRUD(TipoMensagem.ERRO);
        
        escreveMensagem(mensagem, gson, outbound);
    }
    
     private void cadastraSlot(Gson gson, String string, PrintWriter outbound) 
        throws IOException {
        MensagemCRUD mensagem;
        PostoDeSaude posto;
        CadastroSlot cadastroSlot = gson.fromJson(
            string, CadastroSlot.class
        );
        
        posto = findPosto(cadastroSlot.getNomePosto());
        
        int indicePosto = postos.indexOf(posto);
        
        for (int i = 0; i < postos.get(indicePosto).getDiasVacinacao().size(); i++) {
            if(postos.get(indicePosto)
                    .getDiasVacinacao()
                    .get(i)
                    .getData()
                    .equals(cadastroSlot.getData())){
                Slot novoSlot = new Slot(cadastroSlot.getSlotCadastro(), 
                                        cadastroSlot.getQtdSlotVacinacao());
                postos.get(indicePosto)
                    .getDiasVacinacao()
                    .get(i).getSlots().add(novoSlot);
                break;
            }
            
        }
        
        mensagem = new SucessoCRUD("Operação realizada com sucesso!");
        
        escreveMensagem(mensagem, gson, outbound);    
    }
    
    private void listagemSlots(
            Gson gson, String string, PrintWriter outbound) 
    throws IOException {
        MensagemCRUD mensagem;
        PostoDeSaude posto;
        ArrayList<DiasVacinacao> dias = new ArrayList<>();
        
        ReadDias nomePosto = gson.fromJson(
            string, ReadDias.class
        );
        
        posto = findPosto(nomePosto.getNomePosto());
        
        dias = posto.getDiasVacinacao();
        
        mensagem = new ListagemSlots(posto.getNomePosto(), dias);
        
        escreveMensagem(mensagem, gson, outbound);
    }
    
    public Slot findSlot(DiasVacinacao dia, String periodo) {
        for (Slot slot : dia.getSlots()) {
            if(slot.getSlotVacinacao().equals(periodo)) return slot;
        }
        return null;
    }
    
    private void updateSlot(
            Gson gson, String string, PrintWriter outbound) 
    throws IOException {
        MensagemCRUD mensagem;
        PostoDeSaude posto;
        DiasVacinacao dia;
        Slot slot;
        
        UpdateSlot updateSlot = gson.fromJson(
            string, UpdateSlot.class
        );
        
        posto = findPosto(updateSlot.getNomePosto());
        dia = findDia(posto.getNomePosto(), updateSlot.getData());
        slot = findSlot(dia, updateSlot.getSlot().getSlotVacinacao());

        int indicePosto = postos.indexOf(posto);
        int indiceDia = postos.get(indicePosto).getDiasVacinacao().indexOf(dia);
        int indiceSlot = postos
                         .get(indicePosto)
                         .getDiasVacinacao()
                         .get(indiceDia)
                         .getSlots().indexOf(slot);
        
        postos.get(indicePosto).getDiasVacinacao().get(indiceDia)
                                                  .getSlots()
                                                  .get(indiceSlot)
                                                  .setQtdSlotVacinacao(
                                                          updateSlot.getSlot()
                                                          .getQtdSlotVacinacao());
        
        mensagem = new SucessoCRUD("Operação realizada com sucesso!");
        
        escreveMensagem(mensagem, gson, outbound);
    }
    
    private void removeSlot(
            Gson gson, String string, PrintWriter outbound) 
    throws IOException {
        MensagemCRUD mensagem;
        PostoDeSaude posto;
        DiasVacinacao dia;
        Slot slot;
        
        RemoveSlot removeSlot = gson.fromJson(
            string, RemoveSlot.class
        );
        
        posto = findPosto(removeSlot.getNomePosto());
        dia = findDia(posto.getNomePosto(), removeSlot.getData());
        slot = findSlot(dia, removeSlot.getSlotVacinacao());

        int indicePosto = postos.indexOf(posto);
        int indiceDia = postos.get(indicePosto).getDiasVacinacao().indexOf(dia);
        postos.get(indicePosto).getDiasVacinacao().get(indiceDia)
                                                  .getSlots().remove(slot);
        
        mensagem = new SucessoCRUD("Operação realizada com sucesso!");
        
        escreveMensagem(mensagem, gson, outbound);
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
        
        posto.getVacinasPosto().add( new Vacina("Pfizer", 200, true) );
        posto.getDiasVacinacao().add( new DiasVacinacao("20/08/2021") );
        posto.getDiasVacinacao().add( new DiasVacinacao("21/08/2021") );
        
        posto.getDiasVacinacao().get(0).getSlots().add( new Slot("Manhã", 15) );
        posto.getDiasVacinacao().get(0).getSlots().add( new Slot("Tarde", 15) );
        posto.getDiasVacinacao().get(1).getSlots().add( new Slot("Tarde", 15) );
        posto.getDiasVacinacao().get(1).getSlots().add( new Slot("Noite", 15) );
        postos.add(posto);
        
        posto = new PostoDeSaude(
            "Unidade de Saúde Soraka ADC", "Avenida Rito Gomes, 666"
        );
        posto.getVacinasPosto().add( new Vacina("Pfizer", 100, true) );
        posto.getDiasVacinacao().add( new DiasVacinacao("20/08/2021") );
        posto.getDiasVacinacao().get(0).getSlots().add( new Slot("Manhã", 15) );
        posto.getDiasVacinacao().get(0).getSlots().add( new Slot("Tarde", 15) );
        
        postos.add(posto);
    }
}
