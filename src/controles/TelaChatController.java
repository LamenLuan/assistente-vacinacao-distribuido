/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import entidades.Agendamento;
import entidades.Alerta;
import entidades.ChatListener;
import entidades.Mensageiro;
import entidades.Mensagem;
import entidades.TelaLoader;
import entidades.TipoMensagem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author luanl
 */
public class TelaChatController implements Initializable {

    private boolean admin, disponivel;
    private String cpf, senha, nomeDestinatario;
    private Agendamento agendamento;
    
    private Socket clientListener;
    private BufferedReader inboundListener;
    private PrintWriter outboundListener;
    private ChatListener chatListener;
    private ObservableList<String> conversas;
    
    @FXML
    private ListView<String> lvConversas;
    @FXML
    private VBox root;
    @FXML
    private TextField txConversa;
    @FXML
    private Button btDisponivel;
    @FXML
    private Button btEnviar;

    public void inicializaDados(
        boolean admin, String cpf, String senha, Agendamento agendamento
    ) {
        this.admin = admin;
        this.cpf = cpf;
        this.senha = senha;
        this.agendamento = agendamento;
        this.nomeDestinatario = "Destinatário:";
        
        conversas = FXCollections.observableArrayList();
        lvConversas.setItems(conversas);
                
        if(admin) btDisponivel.setVisible(true);
        else conectaUsuario();
        
        Window window = root.getScene().getWindow();
        if(window != null) window.setOnCloseRequest(ex -> {
            fazerLogout();
        });
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        disponivel = false;
    }
    
    private void enviaPedidoAberturaChat() throws IOException {
        clientListener = new Socket(
            InetAddress.getByName(Mensageiro.ip),
            Mensageiro.porta
        );
        inboundListener = new BufferedReader(
            new InputStreamReader( clientListener.getInputStream() )
        );
        outboundListener = new PrintWriter(
            clientListener.getOutputStream(), true
        );
        Mensagem mensagem = new Mensagem(
            cpf, senha,
            clientListener.getInetAddress().getHostAddress(),
            clientListener.getLocalPort()
        );
        Mensageiro.enviaMensagem(outboundListener, mensagem, false);
    }
    
    public void fechaChat() throws IOException {
        Mensageiro.fechaSocketEDutos(
            clientListener, outboundListener, inboundListener
        );
        root.getScene().getWindow().setOnCloseRequest(ex -> {});
        abreMenuPrincipal();
    }
    
    private void conectaUsuario() {
        try {
            enviaPedidoAberturaChat();
            Mensagem mensagem = Mensageiro.recebeMensagem(
                inboundListener, false
            );

            int id = mensagem.getId();
            if( id == TipoMensagem.DADOS_CHAT_ADMIN.getId() ) {
                chatListener =  new ChatListener(inboundListener, this);
                chatListener.start();
                setChatDisable(false);
                nomeDestinatario = mensagem.getNome();
                conversas.add(
                    "Você está conversando com " + nomeDestinatario + " agora!"
                );
            }
            else if ( id == TipoMensagem.NENHUM_ADMIN_DISPONIVEL.getId() ) {
                Alerta.mostraAlerta(
                    "Chat indisponível",
                    "Nenhum agente de saúde está disponível para o chat"
                );
                fechaChat();
            }
            else if ( id == TipoMensagem.ERRO.getId() ) {
                Alerta.mostraAlerta(
                    "Erro com o servidor!", mensagem.getMensagem()
                );
                fechaChat();
            }
        } catch (IOException ex) {
            Alerta.mostrarErroComunicacao();
            abreMenuPrincipal();
        }
    }
    
    private void abreMenuPrincipal() {
        FXMLLoader loader = TelaLoader.Load(
            this, root, "/./telas/TelaPrincipal.fxml" ,
            "Assistente de Vacinação - Menu Principal"
        );

        TelaPrincipalController controller = loader.getController();
        controller.inicializaDados(admin, agendamento, cpf, senha);
    }
    
    private void fazerLogout() {
        try {
            if(clientListener != null) {
                Mensagem mensagem = new Mensagem(
                    TipoMensagem.PEDIDO_LOGOUT_CHAT
                );
                Mensageiro.enviaMensagem(outboundListener, mensagem, false);

                Mensageiro.fechaSocketEDutos(
                    clientListener, outboundListener, inboundListener
                );
            }
            abreMenuPrincipal();
        } catch (IOException ex) {
            Alerta.mostrarErroComunicacao();
        }
    }

    @FXML
    private void onSairChat(ActionEvent event) {
        root.getScene().getWindow().setOnCloseRequest(ex -> {});
        fazerLogout();
    }

    @FXML
    private void onEnviarConversa(ActionEvent event) {
        String stringMensagem = txConversa.getText();
        if( stringMensagem.isEmpty() ) {
            Alerta.mostrarCamposVazios();
        }
        else {
            Mensagem mensagem = new Mensagem(
                TipoMensagem.MENSAGEM_CLIENTE, stringMensagem
            );
            try {
                Mensageiro.enviaMensagem(outboundListener, mensagem, false);
                conversas.add("Você: " + stringMensagem);
                txConversa.clear();
            } catch (IOException ex) {
                Alerta.mostrarErroComunicacao();
            }
        }
    }
    
    public void setAdminIndisponivel() {
        disponivel = false;
        btDisponivel.setText("Disponível");
    }

    @FXML
    private void onMudaDisponibilidade(ActionEvent event) {
        try {
            Mensagem mensagem;
            
            if(clientListener == null) enviaPedidoAberturaChat();
            
            disponivel = !disponivel;
            
            if(disponivel) {
                btDisponivel.setText("Indisponível");
                mensagem = new Mensagem(
                    TipoMensagem.ADMIN_DISPONIVEL
                );
            }
            else {
                btDisponivel.setText("Disponível");
                mensagem = new Mensagem(
                    TipoMensagem.ADMIN_INDISPONIVEL
                );
            }

            Mensageiro.enviaMensagem(outboundListener, mensagem, false);
            
            if(disponivel && chatListener == null) {
                chatListener = new ChatListener(
                    inboundListener, this
                );
                chatListener.start();
            }
            
        } catch (IOException ex) {
            Alerta.mostrarErroComunicacao();
        }
    }
    
    public void setChatDisable(boolean value) {
        lvConversas.setDisable(value);
        txConversa.setDisable(value);
        btEnviar.setDisable(value);
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getNomeDestinatario() {
        return nomeDestinatario;
    }
    
    public void setNomeDestinatario(String nomeDestinatario) {
        this.nomeDestinatario = nomeDestinatario;
    }
    
    public ObservableList<String> getConversas() {
        return conversas;
    }
    
    public void setBtDisponivelDisable(boolean value) {
        btDisponivel.setDisable(value);
    }
}
