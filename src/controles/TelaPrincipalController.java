/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import entidades.Agendamento;
import entidades.Alerta;
import entidades.Mensageiro;
import entidades.Mensagem;
import entidades.PostoDeSaude;
import entidades.TelaLoader;
import entidades.TipoMensagem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author luanl
 */
public class TelaPrincipalController implements Initializable {

    private boolean admin;
    private String cpf, senha;
    private Agendamento agendamento;
    private ArrayList<PostoDeSaude> postosSaude;
    
    @FXML
    private Label lbAgendamento;
    @FXML
    private BorderPane root;
    @FXML
    private Label lbDose;
    @FXML
    private Label lbLocal;
    @FXML
    private Label lbData;
    @FXML
    private Button btCancelarAgendamento;
    @FXML
    private Label lbBemVindo;
    @FXML
    private Menu mCadastrar;
    
    private void inicializaInfoAgendamento() {
        if(agendamento != null) {
            lbAgendamento.setText("Agendamento marcado:");
            lbDose.setText(
                "Aplicação da " + (agendamento.isSegundaDose() ? "2" : "1")
                + "ª dose"
            );
            lbLocal.setText(
                agendamento.getNomePosto() + " - " + agendamento.getEndPosto()
            );
            lbData.setText(
                agendamento.getData() + " - " + agendamento.getSlot()
            );
            btCancelarAgendamento.setDisable(false);
        }
        else {
            lbAgendamento.setText("Nenhum agendamento marcado");
            lbDose.setText("");
            lbLocal.setText("");
            lbData.setText("");
            btCancelarAgendamento.setDisable(true);
        }
    }

    public void inicializaDados(
        boolean admin, Agendamento agendamento, String cpf, String senha
    ) {
        this.admin = admin;
        this.agendamento = agendamento;
        this.cpf = cpf;
        this.senha = senha;
        
        lbBemVindo.setText(
            "Bem vindo " + (admin ? "Administrador" : "Usuário") + '!'
        );
        
        inicializaInfoAgendamento();
        
        if(admin) mCadastrar.setVisible(true);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        postosSaude = new ArrayList<>();
    }    

    @FXML
    private void onCancelarAgendamento(ActionEvent event) {
        Mensagem mensagem = new Mensagem(
            TipoMensagem.PEDIDO_CANCELAMENTO, cpf, senha
        );
        
        try {
            Socket client = new Socket(
                InetAddress.getByName(Mensageiro.ip),
                Mensageiro.porta
            );
            PrintWriter outbound = new PrintWriter(
                client.getOutputStream(), true
            );
            BufferedReader inbound = new BufferedReader(
                new InputStreamReader( client.getInputStream() )
            );
            Mensageiro.enviaMensagem(outbound, mensagem, false);
            mensagem = Mensageiro.recebeMensagem(inbound, false);
            int id = mensagem.getId();
            
            if( id == TipoMensagem.CONFIRMACAO_CANCELAMENTO.getId() ) {
                agendamento = null;
                inicializaInfoAgendamento();
            }
            else if( id == TipoMensagem.ERRO.getId() ) {
                onLogout(null);
                Alerta.mostraAlerta(
                    "Erro com o servidor!", mensagem.getMensagem()
                );
            }
            
            Mensageiro.fechaSocketEDutos(client, outbound, inbound);
            
        } catch (IOException ex) {
            System.err.println( "Erro:" + ex.getMessage() );
            Alerta.mostrarErroComunicacao();
        }
    }
    
    private boolean verificaVacinasDisponiveis(BufferedReader inbound)
        throws IOException {
        
        Mensagem mensagem = Mensageiro.recebeMensagem(inbound, false);
        int id = mensagem.getId();
        
        if( id == TipoMensagem.NAO_TEM_VACINAS.getId() ) {
            Alerta.mostraAlerta(
                "Sem dias disponíveis ainda",
                "Não existem dias para agendamento nesse momento, " +
                "tente novamente mais tarde"
            );
        }
        else if( id == TipoMensagem.TEM_VACINAS.getId() ) {
            postosSaude = mensagem.getPostosSaude();
            return true;
        }
        else if( id == TipoMensagem.ERRO.getId() ) {
            Alerta.mostraAlerta(
                "Erro com o servidor!", mensagem.getMensagem()
            );
        }
        else {
            Alerta.mostrarErroComunicacao();
        }
        
        return false;
    }

    @FXML
    private void onAgendar(ActionEvent event) {
        
        Mensagem mensagem = new Mensagem(
            TipoMensagem.PEDIDO_DADOS_AGENDAMENTO, cpf, senha
        );
        
        try {
            Socket client = new Socket(
                InetAddress.getByName(Mensageiro.ip),
                Mensageiro.porta
            );
            PrintWriter outbound = new PrintWriter(
                client.getOutputStream(), true
            );
            BufferedReader inbound = new BufferedReader(
                new InputStreamReader( client.getInputStream() )
            );
            Mensageiro.enviaMensagem(outbound, mensagem, false);

            if( verificaVacinasDisponiveis(inbound) ) {
                FXMLLoader loader = TelaLoader.Load(
                    this, root, "/./telas/TelaCadastroAgendamento.fxml" ,
                    "Assistente de Vacinação - Ficha de Agendamento"
                );

                TelaCadastroAgendamentoController controller = 
                    loader.getController();
                controller.inicializaDados(
                    admin, cpf, senha, agendamento, postosSaude
                );
            }
            
            Mensageiro.fechaSocketEDutos(client, outbound, inbound);
            
        } catch (IOException ex) {
            System.err.println( "Erro:" + ex.getMessage() );
            Alerta.mostrarErroComunicacao();
        }
    }

    @FXML
    private void onAcessarChat(ActionEvent event) {
        FXMLLoader loader = TelaLoader.Load(
            this, root, "/./telas/TelaChat.fxml" ,
            "Assistente de Vacinação - Chat de Dúvidas"
        );
        
        TelaChatController controller = loader.getController();
        controller.inicializaDados(admin, cpf, senha, agendamento);
    }
    
    @FXML
    private void onLogout(ActionEvent event) {
        TelaLoader.Load(
            this, root, "/./telas/TelaLogin.fxml" ,
            "Assistente de Vacinação - Acesso ao sistema"
        );
    }

    @FXML
    private void onCadastrarAdmin(ActionEvent event) {
        FXMLLoader loader = TelaLoader.Load(
            this, root, "/./telas/TelaCadastroUsuario1.fxml" ,
            "Assistente de Vacinação - Ficha de cadastro (admin)"
        );
        TelaCadastroUsuario1Controller controller = loader.getController();
        controller.modoAdmin();
    }
    
}
