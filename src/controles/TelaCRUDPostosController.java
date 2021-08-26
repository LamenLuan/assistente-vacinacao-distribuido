/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import com.google.gson.Gson;
import entidades.Agendamento;
import entidades.Alerta;
import entidades.ListaPosto;
import entidades.Mensageiro;
import entidades.MensageiroCliente;
import entidades.PostoDeSaude;
import entidades.TelaLoader;
import entidades.TipoMensagem;
import entidades.mensagensCRUD.CadastroPostoPt1;
import entidades.mensagensCRUD.Erro;
import entidades.mensagensCRUD.ListagemPostos;
import entidades.mensagensCRUD.PedidoListagemPostos;
import entidades.mensagensCRUD.PedidoRemovePosto;
import entidades.mensagensCRUD.PedidoUpdatePosto;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class TelaCRUDPostosController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private ListView<PostoDeSaude> lvPostos;
    @FXML
    private Button btVoltar;
    @FXML
    private TextField tfNomePosto;
    @FXML
    private TextField tfEnderecoPosto;
    @FXML
    private Button btAddPosto;
    @FXML
    private Button btUpdatePosto;
    @FXML
    private Button btRemovePosto;
    
    private ObservableList<PostoDeSaude> obsPostos;
    private ArrayList<PostoDeSaude> postosCadastrados = new ArrayList<>();

    private boolean admin;
    private String cpf, senha;
    private Agendamento agendamento;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
    }
    
    public void inicializaDados(
        boolean admin, Agendamento agendamento, String cpf, String senha
    ) {
        this.admin = admin;
        this.agendamento = agendamento;
        this.cpf = cpf;
        this.senha = senha;
        
        PedidoListagemPostos pedidoListagem = new PedidoListagemPostos(
            cpf, senha
        );
        
        try {
            Socket client = new Socket(
                InetAddress.getByName(Mensageiro.ip), Mensageiro.porta
            );
            PrintWriter outbound = new PrintWriter(
                client.getOutputStream(), true
            );
            BufferedReader inbound = new BufferedReader(
                new InputStreamReader( client.getInputStream() )
            );
            MensageiroCliente.enviaMensagem(outbound, pedidoListagem);
            
            Gson gson = new Gson();
            String string = MensageiroCliente.recebeMensagem(inbound);
            ListagemPostos postos = gson.fromJson(string, ListagemPostos.class);
            int id = postos.getId();
            
            if( id == TipoMensagem.LISTA_POSTOS_NOMES_RESPOSTA.getId() ) {
                for ( ListaPosto posto : postos.getPostosCadastrados() ) {
                    postosCadastrados.add(
                        new PostoDeSaude(posto.getNomePosto(), null)
                    );
                }
                obsPostos = FXCollections.observableArrayList(postosCadastrados);
                lvPostos.setItems(obsPostos);
            }
            else if( id == TipoMensagem.ERRO.getId()  ) {
                Erro erro = gson.fromJson(string, Erro.class);
                Alerta.mostraAlerta( "Erro com o servidor!", erro.getMensagem());
            }
            
        } catch (IOException ex) {
            System.err.println( "Erro:" + ex.getMessage() );
            Alerta.mostrarErroComunicacao();
        }
    }

    @FXML
    private void onVoltar(ActionEvent event) {
        FXMLLoader loader = TelaLoader.Load(
                this, root, "/./telas/TelaPrincipal.fxml" ,
                "Assistente de Vacinação - Menu Principal"
        );
        
        TelaPrincipalController controller = loader.getController();
        controller.inicializaDados(admin, agendamento, cpf, senha);
    }

    @FXML
    private void onAddPosto(ActionEvent event) {
        if(!tfNomePosto.getText().isEmpty() 
                && !tfEnderecoPosto.getText().isEmpty()){
            String nomePosto = tfNomePosto.getText();
            String enderecoPosto = tfEnderecoPosto.getText();

            CadastroPostoPt1 cadastroPosto = new CadastroPostoPt1(
                cpf, senha, nomePosto, enderecoPosto
            );

            try {
                Socket client = new Socket(
                    InetAddress.getByName(Mensageiro.ip), Mensageiro.porta
                );
                PrintWriter outbound = new PrintWriter(
                    client.getOutputStream(), true
                );
                BufferedReader inbound = new BufferedReader(
                    new InputStreamReader( client.getInputStream() )
                );
                MensageiroCliente.enviaMensagem(outbound, cadastroPosto);

                if( verificaSeOperacaoRealizada(inbound) ) {
                    PostoDeSaude posto = new PostoDeSaude(nomePosto, enderecoPosto);
                    postosCadastrados.add(posto);
                    obsPostos = FXCollections.observableArrayList(postosCadastrados);
                    lvPostos.setItems(obsPostos);
                    tfNomePosto.setText("");
                    tfEnderecoPosto.setText("");
                }

            } catch (IOException ex) {
                System.err.println( "Erro:" + ex.getMessage() );
                Alerta.mostrarErroComunicacao();
            }
        } else {
            Alerta.mostraAlerta("Dados não inseridos", "Preencha todos os "
                        + "campos antes de adicionar um posto!");
        }
        
    }
    
    private boolean verificaSeOperacaoRealizada(BufferedReader inbound)
        throws IOException  {
       
        Gson gson = new Gson();
        String string = MensageiroCliente.recebeMensagem(inbound);
        int id = MensageiroCliente.getIdMensagem(string);
        
        if( id == TipoMensagem.ERRO.getId() ) {
            Erro erro = gson.fromJson(string, Erro.class);
            Alerta.mostraAlerta( "Erro com o servidor!", erro.getMensagem() );
        }
        else if( id == TipoMensagem.OPERACAO_CRUD_SUCESSO.getId() ) {
            Alerta.mostraConfirmacao("Operação efetuada com sucesso!");
            return true;
        }
        
        return false;
   }

    @FXML
    private void onUpdatePosto(ActionEvent event) {
        if(lvPostos.getSelectionModel().getSelectedItem() != null){
            if(!tfNomePosto.getText().isBlank() 
                    && !tfEnderecoPosto.getText().isBlank()){
                String nomePostoAlvo = lvPostos
                .getSelectionModel()
                .getSelectedItem()
                .getNomePosto();
                String nomePosto = tfNomePosto.getText();
                String enderecoPosto = tfEnderecoPosto.getText();

                PedidoUpdatePosto updatePosto = new PedidoUpdatePosto(
                    cpf, senha, nomePostoAlvo, nomePosto, enderecoPosto
                );

                try {
                    Socket client = new Socket(
                        InetAddress.getByName(Mensageiro.ip), Mensageiro.porta
                    );
                    PrintWriter outbound = new PrintWriter(
                        client.getOutputStream(), true
                    );
                    BufferedReader inbound = new BufferedReader(
                        new InputStreamReader( client.getInputStream() )
                    );
                    MensageiroCliente.enviaMensagem(outbound, updatePosto);

                    verificaSeOperacaoRealizada(inbound);

                } catch (IOException ex) {
                    System.err.println( "Erro:" + ex.getMessage() );
                    Alerta.mostrarErroComunicacao();
                }

                PostoDeSaude postoAlvo = findPosto(nomePostoAlvo);

                if(postoAlvo != null){
                    int indice = postosCadastrados.indexOf(postoAlvo);
                    postoAlvo.setNomePosto(nomePosto);
                    postoAlvo.setEndPosto(enderecoPosto);

                    postosCadastrados.set(indice, postoAlvo);
                }

                obsPostos = FXCollections.observableArrayList(postosCadastrados);

                lvPostos.setItems(obsPostos);

                tfNomePosto.setText("");
                tfEnderecoPosto.setText("");
            } else {
                Alerta.mostraAlerta("Dados não inseridos", "Todos os campos são"
                        + " obrigatórios para alterar o posto!");
            }
        } else {
            Alerta.mostraAlerta("Posto não selecionado", "Selecione um posto "
                        + "para alterar!");
        }
        
    }

    @FXML
    private void onRemovePosto(ActionEvent event) {
        if(lvPostos.getSelectionModel().getSelectedItem() != null){
            String nomePostoAlvo = lvPostos
                .getSelectionModel()
                .getSelectedItem()
                .getNomePosto();
        
            PedidoRemovePosto removePosto = new PedidoRemovePosto(
                cpf, senha, nomePostoAlvo
            );

            try {
                Socket client = new Socket(
                    InetAddress.getByName(Mensageiro.ip), Mensageiro.porta
                );
                PrintWriter outbound = new PrintWriter(
                    client.getOutputStream(), true
                );
                BufferedReader inbound = new BufferedReader(
                    new InputStreamReader( client.getInputStream() )
                );
                MensageiroCliente.enviaMensagem(outbound, removePosto);

                verificaSeOperacaoRealizada(inbound);

            } catch (IOException ex) {
                System.err.println( "Erro:" + ex.getMessage() );
                Alerta.mostrarErroComunicacao();
            }

            PostoDeSaude postoAlvo = findPosto(nomePostoAlvo);

            if(postoAlvo != null){
                postosCadastrados.remove(postoAlvo);
            }

            obsPostos = FXCollections.observableArrayList(postosCadastrados);

            lvPostos.setItems(obsPostos);

            tfNomePosto.setText("");
            tfEnderecoPosto.setText("");
        } else {
            Alerta.mostraAlerta("Posto não selecionado", "Selecione um posto "
                    + "antes de remover!");
        }
        
    }

    @FXML
    private void onSelecionaPosto(MouseEvent event) {
        PostoDeSaude posto = lvPostos.getSelectionModel().getSelectedItem();
        tfNomePosto.setText( posto.getNomePosto() );
    }

    private PostoDeSaude findPosto(String nomePosto){
        for (PostoDeSaude posto : postosCadastrados) {
            if(posto.getNomePosto().equals(nomePosto)) return posto;
        }
        return null;
    } 

    
}
