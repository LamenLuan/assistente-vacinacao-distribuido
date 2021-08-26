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
import entidades.ListaSlotsListView;
import entidades.Mensageiro;
import entidades.MensageiroCliente;
import entidades.PostoDeSaude;
import entidades.Slot;
import entidades.TelaLoader;
import entidades.TipoMensagem;
import entidades.mensagensCRUD.CadastroSlot;
import entidades.mensagensCRUD.Erro;
import entidades.mensagensCRUD.ListagemDiasVacinacao;
import entidades.mensagensCRUD.ListagemPostos;
import entidades.mensagensCRUD.ListagemSlots;
import entidades.mensagensCRUD.PedidoListagemPostos;
import entidades.mensagensCRUD.ReadDias;
import entidades.mensagensCRUD.ReadSlots;
import entidades.mensagensCRUD.RemoveSlot;
import entidades.mensagensCRUD.UpdateSlot;
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
public class TelaCRUDSlotsController implements Initializable {

    @FXML
    private Button btVoltar;
    @FXML
    private ListView<PostoDeSaude> lvPostosDeSaude;
    @FXML
    private ListView<DiasVacinacao> lvDiasVacinacao;
    @FXML
    private TextField tfQuantidade;
    @FXML
    private Button btAddSlot;
    @FXML
    private Button btUpdateSlot;
    @FXML
    private Button btRemoveSlot;
    @FXML
    private AnchorPane root;
    @FXML
    private TextField tfSlot;
    
    private ObservableList<PostoDeSaude> obsPostos;
    private ArrayList<PostoDeSaude> postosCadastrados = new ArrayList<>();
    private PostoDeSaude posto;
    
    private ObservableList<DiasVacinacao> obsDias;
    private ArrayList<Dia> listaDias = new ArrayList<>();
    private DiasVacinacao dia;
    
    private ObservableList<ListaSlotsListView> obsSlots;
    private ArrayList<ListaSlotsListView> listaSlots = new ArrayList<>();
    private ArrayList<ListaSlotsListView> listaAuxSlots = new ArrayList<>();
    private Slot slot;
    
    @FXML
    private ListView<ListaSlotsListView> lvSlots;
    
    private boolean admin;
    private Agendamento agendamento;
    private String cpf;
    private String senha;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
    private void onAddSlot(ActionEvent event) {
        if(dia != null){
            if(!tfSlot.getText().isEmpty()
                    && !tfQuantidade.getText().isEmpty()){
                String nomePosto = posto.getNomePosto();
                String data = dia.getData();
                String slotCadastro = tfSlot.getText();
                int qtdSlotVacinacao = Integer.parseInt(tfQuantidade.getText());

                CadastroSlot cadastroSlot = new CadastroSlot(nomePosto, data, 
                                            slotCadastro, qtdSlotVacinacao, cpf, senha);

                Slot slot = new Slot(slotCadastro, qtdSlotVacinacao);

                saveSlot(data, slot);

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
                    MensageiroCliente.enviaMensagem(outbound, cadastroSlot);

                    verificaSeOperacaoRealizada(inbound);

                    MensageiroCliente.fechaSocketEDutos(client, outbound, inbound);
                    
                    tfSlot.setText("");
                    tfQuantidade.setText("");
                    
                    dia = null;

                } catch (IOException ex) {
                    System.err.println( "Erro:" + ex.getMessage() );
                    Alerta.mostrarErroComunicacao();
                }
            } else {
                Alerta.mostraAlerta("Dados não inseridos", "Preencha todos os "
                        + "campos antes de adicionar um slot!");
            }
        } else {
            Alerta.mostraAlerta("Data não selecionada", "Selecione uma "
                    + "data para inserir um slot!");
        }
        
    }

    @FXML
    private void onUpdateSlot(ActionEvent event) {
        ListaSlotsListView itemSlot = lvSlots.getSelectionModel().getSelectedItem();
        
        if(itemSlot != null){
            if(!tfSlot.getText().isEmpty()
                    && !tfQuantidade.getText().isEmpty()){
                String nomePosto = itemSlot.getNomePosto();
                String data = itemSlot.getData();

                itemSlot.getSlot().setQtdSlotVacinacao(Integer.parseInt(tfQuantidade.getText()));

                Slot slot = itemSlot.getSlot();

                UpdateSlot updateSlot = new UpdateSlot(nomePosto, data, itemSlot.getSlot(), cpf, senha);

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
                    MensageiroCliente.enviaMensagem(outbound, updateSlot);

                    verificaSeOperacaoRealizada(inbound);

                    for (int i = 0; i < postosCadastrados.size(); i++) {
                        PostoDeSaude p = postosCadastrados.get(i);
                        if(p.getNomePosto().equals(nomePosto)){
                            for (int j = 0; j < p.getDiasVacinacao().size(); j++) {
                                DiasVacinacao d = p.getDiasVacinacao().get(j);
                                if(d.getData().equals(data)){
                                    for (int k = 0; k < d.getSlots().size(); k++) {
                                        Slot s = d.getSlots().get(k);
                                        if(s.getSlotVacinacao().equals(
                                                                   slot
                                                                   .getSlotVacinacao()))
                                            postosCadastrados.get(i)
                                                             .getDiasVacinacao()
                                                             .get(j)
                                                             .getSlots()
                                                             .get(k)
                                                             .setQtdSlotVacinacao(
                                                               s.getQtdSlotVacinacao());
                                    }
                                    break;
                                }
                            }
                        }
                    }

                } catch (IOException ex) {
                    System.err.println( "Erro:" + ex.getMessage() );
                    Alerta.mostrarErroComunicacao();
                }

                for (int i = 0; i < listaSlots.size(); i++) {
                    ListaSlotsListView ls = listaSlots.get(i);
                    if(ls.getSlot().getSlotVacinacao().equals(slot.getSlotVacinacao())
                            && ls.getData().equals(data)){
                        listaSlots.get(i).setSlot(slot);
                        break;
                    }
                }

                obsSlots = FXCollections.observableArrayList(listaSlots);
                lvSlots.setItems(obsSlots);
            } else {
                Alerta.mostraAlerta("Dados não inseridos", "Preencha todos os "
                        + "campos antes de alterar um slot!");
            }
            
        } else {
            Alerta.mostraAlerta("Slot não selecionado", "Selecione um "
                    + "slot para ser alterado!");
        }
        
    }

    @FXML
    private void onRemoveSlot(ActionEvent event) {
        ListaSlotsListView slot = lvSlots.getSelectionModel().getSelectedItem();
        
        if(slot != null){
            String nomePosto = posto.getNomePosto();
            String data = slot.getData();
            String slotVacinacao = slot.getSlot().getSlotVacinacao();

            RemoveSlot removeSlot = new RemoveSlot(nomePosto, data, slotVacinacao, cpf, senha);

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
                MensageiroCliente.enviaMensagem(outbound, removeSlot);

                verificaSeOperacaoRealizada(inbound);

                for (int i = 0; i < postosCadastrados.size(); i++) {
                    PostoDeSaude p = postosCadastrados.get(i);
                    if(p.getNomePosto().equals(nomePosto)){
                        for (int j = 0; j < p.getDiasVacinacao().size(); j++) {
                            DiasVacinacao d = p.getDiasVacinacao().get(j);
                            if(d.getData().equals(data))
                                for (int k = 0; k < d.getSlots().size(); k++) {
                                    Slot s = d.getSlots().get(k);
                                    if(s.getSlotVacinacao().equals(slot.getSlot().getSlotVacinacao()))
                                        postosCadastrados.get(i)
                                                         .getDiasVacinacao()
                                                         .get(j)
                                                         .getSlots()
                                                         .remove(s);
                                }
                        }
                    }
                }

            } catch (IOException ex) {
                System.err.println( "Erro:" + ex.getMessage() );
                Alerta.mostrarErroComunicacao();
            }

            for (int i = 0; i < listaSlots.size(); i++) {
                ListaSlotsListView ls = listaSlots.get(i);
                if(ls.getSlot().getSlotVacinacao().equals(slot.getSlot().getSlotVacinacao()))
                    listaSlots.remove(i);   
            }

            obsSlots = FXCollections.observableArrayList(listaSlots);
            lvSlots.setItems(obsSlots);
        } else {
            Alerta.mostraAlerta("Slot não selecionado", "Selecione um "
                    + "slot para ser removido!");
        }
    }

    @FXML
    private void onSelecionarPosto(MouseEvent event) {
        posto = lvPostosDeSaude.getSelectionModel().getSelectedItem();
        posto.getDiasVacinacao().removeAll(posto.getDiasVacinacao());
        listaSlots.removeAll(listaSlots);
        
        ReadDias readDias = new ReadDias(cpf, senha, posto.getNomePosto());
        
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
            
            MensageiroCliente.enviaMensagem(outbound, readDias);
        
            Gson gson = new Gson();
            String stringVacinas = MensageiroCliente.recebeMensagem(inbound);
            ListagemDiasVacinacao dias = gson.fromJson(stringVacinas, 
                                                   ListagemDiasVacinacao.class);

            if(dias.getId()  == TipoMensagem.ERRO.getId() ) {
                Erro erro = gson.fromJson(stringVacinas, Erro.class);
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
        
        ReadSlots readSlots = new ReadSlots(posto.getNomePosto(), cpf, senha);
        
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
            
            MensageiroCliente.enviaMensagem(outbound, readSlots);
        
            Gson gson = new Gson();
            String stringSlots = MensageiroCliente.recebeMensagem(inbound);
            ListagemSlots slots = gson.fromJson(stringSlots, 
                                                   ListagemSlots.class);

            if(slots.getId()  == TipoMensagem.ERRO.getId() ) {
                Erro erro = gson.fromJson(stringSlots, Erro.class);
                Alerta.mostraAlerta( "Erro com o servidor!", erro.getMensagem());
            } else {

                slots.getDiaPosto().forEach(d -> {
                    for (int i = 0; i < d.getSlots().size(); i++) {
                        ListaSlotsListView item = new ListaSlotsListView(
                                slots.getNomePosto(),
                                d.getData(),
                                d.getSlots().get(i));
                        
                        listaSlots.add(item);
                    }
                });
                }
            
            MensageiroCliente.fechaSocketEDutos(client, outbound, inbound);
            
        } catch (IOException ex) {
            System.err.println( "Erro:" + ex.getMessage() );
            Alerta.mostrarErroComunicacao();
        }
        
        obsDias = FXCollections.observableArrayList(posto.getDiasVacinacao());
        lvDiasVacinacao.setItems(obsDias);
        
        obsSlots = FXCollections.observableArrayList(listaSlots);
        lvSlots.setItems(obsSlots);
    }

    @FXML
    private void onSelecionarDia(MouseEvent event) {
        dia = lvDiasVacinacao.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void onSelecionarSlot(MouseEvent event) {
        ListaSlotsListView slot = lvSlots.getSelectionModel().getSelectedItem();
        tfSlot.setText(slot.getSlot().getSlotVacinacao());
        tfQuantidade.setText(Integer.toString(slot.getSlot().getQtdSlotVacinacao()));
    }

    private void saveSlot(String data, Slot slot) {
        for (int i = 0; i < posto.getDiasVacinacao().size(); i++) {
            if(posto.getDiasVacinacao().get(i).getData().equals(data)){
                posto.getDiasVacinacao().get(i).getSlots().add(slot);
                break;
            }
        }
        
        ListaSlotsListView novoSlot = new ListaSlotsListView(posto.getNomePosto(), 
                                                             data, slot);
        listaSlots.add(novoSlot);
        
        obsSlots = FXCollections.observableArrayList(listaSlots);
        lvSlots.setItems(obsSlots);
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
        for (PostoDeSaude posto : postosCadastrados) {
            if(posto.getNomePosto().equals(nomePosto)) return posto;
        }
        return null;
    }
}
