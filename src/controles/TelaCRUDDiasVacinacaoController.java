/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import com.google.gson.Gson;
import entidades.Agendamento;
import entidades.Alerta;
import entidades.Dia;
import entidades.DiasVacinacao;
import entidades.ListaPosto;
import entidades.Mensageiro;
import entidades.MensageiroCliente;
import entidades.PostoDeSaude;
import entidades.TelaLoader;
import entidades.TipoMensagem;
import entidades.mensagensCRUD.CadastroDiaVacinacao;
import entidades.mensagensCRUD.Erro;
import entidades.mensagensCRUD.ListagemDiasVacinacao;
import entidades.mensagensCRUD.ListagemPostos;
import entidades.mensagensCRUD.PedidoListagemPostos;
import entidades.mensagensCRUD.ReadDias;
import entidades.mensagensCRUD.RemoveDia;
import entidades.mensagensCRUD.UpdateDia;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class TelaCRUDDiasVacinacaoController implements Initializable {

    @FXML
    private Button btVoltar;
    @FXML
    private ListView<PostoDeSaude> lvPostosDeSaude;
    @FXML
    private ListView<DiasVacinacao> lvDiasVacinacao;
    @FXML
    private Button btAddData;
    @FXML
    private Button btUpdateData;
    @FXML
    private Button btRemoveData;
    
    private ObservableList<PostoDeSaude> obsPostos;
    private ArrayList<PostoDeSaude> postosCadastrados = new ArrayList<>();
    private PostoDeSaude posto;
    
    private ObservableList<DiasVacinacao> obsDias;
    private ArrayList<Dia> listaDias = new ArrayList<>();
    private DiasVacinacao dia;
    
    private boolean admin;
    private String cpf, senha;
    private Agendamento agendamento;
    
    @FXML
    private DatePicker dpData;
    @FXML
    private AnchorPane root;

    /**
     * Initializes the controller class.
     */
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
        
        PedidoListagemPostos pedidoListagemPostos = new PedidoListagemPostos(
            cpf, senha
        );
        dpData.setPromptText("Selecione uma data");
        
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
            MensageiroCliente.enviaMensagem(outbound, pedidoListagemPostos);
            
            Gson gson = new Gson();
            String stringPostos = MensageiroCliente.recebeMensagem(inbound);
            ListagemPostos postos = gson.fromJson(stringPostos, 
                                                  ListagemPostos.class);

            if(postos.getId()  == TipoMensagem.ERRO.getId() ) {
                Erro erro = gson.fromJson(stringPostos, Erro.class);
                Alerta.mostraAlerta( "Erro com o servidor!", erro.getMensagem());
            } else {
                for (ListaPosto posto : postos.getPostosCadastrados()) {
                    postosCadastrados.add(new PostoDeSaude(posto.getNomePosto(), 
                                                           "ignora"));
                }
            }
            
            MensageiroCliente.fechaSocketEDutos(client, outbound, inbound);
            
        } catch (IOException ex) {
            System.err.println( "Erro:" + ex.getMessage() );
            Alerta.mostrarErroComunicacao();
        }
        
        obsPostos = FXCollections.observableArrayList(postosCadastrados);
        lvPostosDeSaude.setItems(obsPostos);
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
    private void onAddData(ActionEvent event) {
        if(posto != null){
            if(!dpData.getEditor().getText().isEmpty()){
                String nomePosto = posto.getNomePosto();
                String data = dpData.getEditor().getText();

                Dia dia = new Dia(data);
                DiasVacinacao novoDia = new DiasVacinacao(data);

                CadastroDiaVacinacao cadastroDia = new CadastroDiaVacinacao(
                    cpf, senha, nomePosto, dia
                );

                posto = findPosto(nomePosto);

                saveDia(posto, novoDia);

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
                    MensageiroCliente.enviaMensagem(outbound, cadastroDia);

                    verificaSeOperacaoRealizada(inbound);

                    MensageiroCliente.fechaSocketEDutos(client, outbound, inbound);

                } catch (IOException ex) {
                    System.err.println( "Erro:" + ex.getMessage() );
                    Alerta.mostrarErroComunicacao();
                }
                
                dpData.getEditor().setText("");
                
                obsDias = FXCollections.observableArrayList();
                lvDiasVacinacao.setItems(obsDias);
                
                posto = null;
            } else {
                Alerta.mostraAlerta("Data não selecionada", "Selecione uma "
                    + "data para ser inserida!");
            }
            
        } else {
            Alerta.mostraAlerta("Posto não selecionado", "Selecione um posto"
                    + " para adicionar uma data!");
        }
        
    }

    @FXML
    private void onUpdateData(ActionEvent event) {
        if(dia != null){
            if(!dpData.getEditor().getText().isEmpty()){
                String nomePostoAlvo = posto.getNomePosto();
                String dataAntiga = dia.getData();
                String novaData = dpData.getEditor().getText();

                UpdateDia updateDia = new UpdateDia(nomePostoAlvo, dataAntiga, novaData,
                                                        cpf, senha);

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
                    MensageiroCliente.enviaMensagem(outbound, updateDia);

                    verificaSeOperacaoRealizada(inbound);

                } catch (IOException ex) {
                    System.err.println( "Erro:" + ex.getMessage() );
                    Alerta.mostrarErroComunicacao();
                }

                int indicePosto = postosCadastrados.indexOf(posto);

                DiasVacinacao diaSearch = findDia(nomePostoAlvo, dataAntiga);

                int indiceDia = postosCadastrados.get(indicePosto)
                                                    .getDiasVacinacao()
                                                    .indexOf(diaSearch);

                postosCadastrados.get(indicePosto)
                                 .getDiasVacinacao()
                                 .get(indiceDia).setData(novaData);

                obsDias = FXCollections.observableArrayList(postosCadastrados
                                                                .get(indicePosto)
                                                                .getDiasVacinacao());

                lvDiasVacinacao.setItems(obsDias);
                
                dia = null;
                posto = null;
                
                dpData.getEditor().setText("");
            } else {
                Alerta.mostraAlerta("Nova data não selecionada", "Selecione "
                        + "uma nova data para alterar a antiga!");
            }
            
        } else {
            Alerta.mostraAlerta("Data não selecionada", "Selecione uma "
                    + "data para ser alterada!");
        }
    }

    @FXML
    private void onRemoveData(ActionEvent event) {
        if(dia != null){
            String nomePostoAlvo = posto.getNomePosto();
            String data = dia.getData();

            RemoveDia removeDia = new RemoveDia(cpf, senha, nomePostoAlvo, data);

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
                MensageiroCliente.enviaMensagem(outbound, removeDia);

                verificaSeOperacaoRealizada(inbound);

                MensageiroCliente.fechaSocketEDutos(client, outbound, inbound);

            } catch (IOException ex) {
                System.err.println( "Erro:" + ex.getMessage() );
                Alerta.mostrarErroComunicacao();
            }

            PostoDeSaude postoAlvo = findPosto(nomePostoAlvo);

            DiasVacinacao diaAlvo = findDia(nomePostoAlvo, data);

            int indicePosto = postosCadastrados.indexOf(postoAlvo);

            postosCadastrados.get(indicePosto).getDiasVacinacao().remove(diaAlvo);

            obsDias = FXCollections.observableArrayList(postosCadastrados
                                                        .get(indicePosto)
                                                        .getDiasVacinacao());

            lvDiasVacinacao.setItems(obsDias);
            
            dia = null;
            
            dpData.getEditor().setText("");
        } else {
            Alerta.mostraAlerta("Data não selecionada", "Selecione uma data"
                    + " a ser removida!");
        }
        
    }

    @FXML
    private void onSelecionarPosto(MouseEvent event) {
        posto = lvPostosDeSaude.getSelectionModel().getSelectedItem();
        posto.getDiasVacinacao().removeAll(posto.getDiasVacinacao());
        
        ReadDias readDias = new ReadDias( cpf, senha, posto.getNomePosto() );
        
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
            
            MensageiroCliente.enviaMensagem(outbound, readDias);
        
            Gson gson = new Gson();
            String stringDiasVac = MensageiroCliente.recebeMensagem(inbound);
            ListagemDiasVacinacao dias = gson.fromJson(stringDiasVac, 
                                                   ListagemDiasVacinacao.class);

            if(dias.getId()  == TipoMensagem.ERRO.getId() ) {
                Erro erro = gson.fromJson(stringDiasVac, Erro.class);
                Alerta.mostraAlerta( "Erro com o servidor!", erro.getMensagem());
            } else {
                listaDias = dias.getDiaPosto();
            }
            
            listaDias.forEach(d -> { 
                posto.getDiasVacinacao().add(new DiasVacinacao(d.getData()));
            });
            
            
            MensageiroCliente.fechaSocketEDutos(client, outbound, inbound);
            
        } catch (IOException ex) {
            System.err.println( "Erro:" + ex.getMessage() );
            Alerta.mostrarErroComunicacao();
        }
        
        obsDias = FXCollections.observableArrayList(posto.getDiasVacinacao());
        
        lvDiasVacinacao.setItems(obsDias);
        dpData.setPromptText("Selecione uma data");
    }

    @FXML
    private void onSelecionaDia(MouseEvent event) {
        dia = lvDiasVacinacao.getSelectionModel().getSelectedItem();
    }


    private void saveDia(PostoDeSaude posto, DiasVacinacao novoDia) {
        int indice = postosCadastrados.indexOf(posto);
        
        postosCadastrados.get(indice).getDiasVacinacao().add(novoDia);
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
    
    private PostoDeSaude findPosto(String nomePosto){
        for (PostoDeSaude p : postosCadastrados) {
            if(p.getNomePosto().equals(nomePosto)) return p;
        }
        return null;
    }
    
    private DiasVacinacao findDia(String nomePosto, String data){
        for (int i = 0; i < postosCadastrados.size(); i++) {
            PostoDeSaude p = postosCadastrados.get(i);
            
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
}
