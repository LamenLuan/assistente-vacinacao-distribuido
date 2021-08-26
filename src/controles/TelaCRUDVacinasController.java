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
import entidades.ListaVacinaListView;
import entidades.ListaVacinas;
import entidades.Mensageiro;
import entidades.MensageiroCliente;
import entidades.PostoDeSaude;
import entidades.TelaLoader;
import entidades.TipoMensagem;
import entidades.Vacina;
import entidades.mensagensCRUD.CadastroVacina;
import entidades.mensagensCRUD.Erro;
import entidades.mensagensCRUD.ListagemPostos;
import entidades.mensagensCRUD.ListagemVacinas;
import entidades.mensagensCRUD.PedidoListagemPostos;
import entidades.mensagensCRUD.PedidoListagemVacinas;
import entidades.mensagensCRUD.RemoveVacina;
import entidades.mensagensCRUD.UpdateVacina;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class TelaCRUDVacinasController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Button btVoltar;
    @FXML
    private TextField tfNomeVacina;
    @FXML
    private TextField tfQuantidade;
    @FXML
    private Button btAddVacina;
    @FXML
    private Button btUpdateVacina;
    @FXML
    private Button btRemoveVacina;
    @FXML
    private RadioButton rbSim;
    @FXML
    private RadioButton rbNao;

    @FXML
    private ListView<PostoDeSaude> lvPostosDeSaude;
    
    private ObservableList<PostoDeSaude> obsPostos;
    private ArrayList<PostoDeSaude> postosCadastrados = new ArrayList<>();
    private PostoDeSaude posto;
    
    private ObservableList<ListaVacinaListView> obsVacinas;
    private ArrayList<ListaVacinaListView> listaVacinas = new ArrayList<>();
    
    private ArrayList<ListaVacinas> vacinasCadastradas = new ArrayList<>();
    private ListaVacinaListView itemListaVacinas;
    
    @FXML
    private ToggleGroup tgSegundaDose;
    @FXML
    private ListView<ListaVacinaListView> lvVacinas;
    private boolean admin;
    private Agendamento agendamento;
    private String cpf;
    private String senha;

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
        rbSim.setSelected(false);
        rbNao.setSelected(false);
        
        rbSim.setUserData(true);
        rbNao.setUserData(false);
        
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
                                                           "tanto faz"));
                }
            }
            
            MensageiroCliente.fechaSocketEDutos(client, outbound, inbound);
            
        } catch (IOException ex) {
            System.err.println( "Erro:" + ex.getMessage() );
            Alerta.mostrarErroComunicacao();
        }
        
        carregaVacinas();
        
        obsPostos = FXCollections.observableArrayList(postosCadastrados);
        obsVacinas = FXCollections.observableArrayList(listaVacinas);
        
        lvPostosDeSaude.setItems(obsPostos);
        lvVacinas.setItems(obsVacinas);
        
        tfNomeVacina.setText("");
        tfQuantidade.setText("");
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
    private void onAddVac(ActionEvent event) {
        if(posto != null){
            if(!tfNomeVacina.getText().isEmpty()
                    && !tfQuantidade.getText().isEmpty()){
                String nomeVacina = tfNomeVacina.getText();
                int qtdd = Integer.parseInt(tfQuantidade.getText());
                boolean segundaDose = getToggleData(tgSegundaDose);

                Vacina vacina = new Vacina(nomeVacina, qtdd, segundaDose);

                saveVacina(posto, vacina);

                CadastroVacina cadastraVacina = new CadastroVacina(
                    cpf, senha, posto.getNomePosto(), vacina
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
                    MensageiroCliente.enviaMensagem(outbound, cadastraVacina);

                    verificaSeOperacaoRealizada(inbound);

                    MensageiroCliente.fechaSocketEDutos(client, outbound, inbound);

                } catch (IOException ex) {
                    System.err.println( "Erro:" + ex.getMessage() );
                    Alerta.mostrarErroComunicacao();
                }

                obsPostos = FXCollections.observableArrayList(postosCadastrados);
                obsVacinas = FXCollections.observableArrayList(listaVacinas);

                lvPostosDeSaude.setItems(obsPostos);
                lvVacinas.setItems(obsVacinas);

                tfNomeVacina.setText("");
                tfQuantidade.setText("");

                rbSim.setSelected(false);
                rbNao.setSelected(false);
            }
            else {
                Alerta.mostraAlerta("Dados não inseridos", "Preencha todos os "
                        + "campos antes de adicionar uma vacina!");
            }
            
        } else {
            Alerta.mostraAlerta("Posto não selecionado", "Selecione um posto "
                    + "antes de inserir uma vacina!");
        }
    }

    @FXML
    private void onUpdateVac(ActionEvent event) {
        if(itemListaVacinas != null){
            if(!tfNomeVacina.getText().isEmpty()
                    && !tfQuantidade.getText().isEmpty()){
                String nomePostoAlvo = itemListaVacinas.getNomePosto();
                String nomeVacinaAlvo = itemListaVacinas.getVacina().getNomeVacina();
                Vacina novaVacina = new Vacina(
                    tfNomeVacina.getText(), Integer.parseInt(tfQuantidade.getText()),
                    getToggleData(tgSegundaDose)                          
                );

                UpdateVacina updateVacina = new UpdateVacina(nomePostoAlvo, 
                                                             nomeVacinaAlvo, 
                                                             novaVacina,
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
                    MensageiroCliente.enviaMensagem(outbound, updateVacina);

                    verificaSeOperacaoRealizada(inbound);

                } catch (IOException ex) {
                    System.err.println( "Erro:" + ex.getMessage() );
                    Alerta.mostrarErroComunicacao();
                }

                PostoDeSaude postoAlvo = findPosto(nomePostoAlvo);

                Vacina vacinaAlvo = findVacina(nomePostoAlvo, nomeVacinaAlvo);

                int indicePosto = postosCadastrados.indexOf(postoAlvo);
                int indiceVacina = postosCadastrados
                                    .get(indicePosto)
                                    .getVacinasPosto().indexOf(vacinaAlvo);

                postosCadastrados.get(indicePosto).getVacinasPosto().set(indiceVacina, 
                                                         novaVacina);

                for (int i = 0; i < listaVacinas.size(); i++) {
                    ListaVacinaListView item = listaVacinas.get(i);

                    if(item.getNomePosto().equals(nomePostoAlvo)
                       && item.getVacina().getNomeVacina().equals(nomeVacinaAlvo)){

                        item.setVacina(novaVacina);
                        listaVacinas.set(i, item);
                        break;
                    }
                }

                obsPostos = FXCollections.observableArrayList(postosCadastrados);
                obsVacinas = FXCollections.observableArrayList(listaVacinas);

                lvPostosDeSaude.setItems(obsPostos);
                lvVacinas.setItems(obsVacinas);

                tfNomeVacina.setText("");
                tfQuantidade.setText("");

                rbSim.setSelected(false);
                rbNao.setSelected(false);
            } else {
                Alerta.mostraAlerta("Dados não inseridos", "Preencha todos os "
                        + "campos antes de alterar uma vacina!");
            }
            
        } else {
            Alerta.mostraAlerta("Vacina não selecionada", "Selecione a "
                    + "vacina a ser alterada!");
        }
        
    }

    
    private boolean getToggleData(ToggleGroup tg) {
        return (boolean) tg.getSelectedToggle().getUserData();
    }

    @FXML
    private void onSelecionaPosto(MouseEvent event) {
        posto = lvPostosDeSaude.getSelectionModel().getSelectedItem();
    }
    
    private void saveVacina(PostoDeSaude posto, Vacina vacina) {
        int indice = postosCadastrados.indexOf(posto);
        int FLAG = 0;
        if(postosCadastrados.get(indice).getVacinasPosto()== null)
            postosCadastrados.get(indice).setVacinasPosto(new ArrayList<>());
        
        postosCadastrados.get(indice).getVacinasPosto().add(vacina);
        
        PostoDeSaude postoSearch = postosCadastrados.get(indice);
        
        if(vacinasCadastradas.isEmpty()){
            vacinasCadastradas.add(new ListaVacinas(postoSearch.getNomePosto(),
                                                    new ArrayList<>()));
                
            vacinasCadastradas.get(0).getVacinasPosto().add(vacina);
            FLAG = 1;
        }
        
        for (int i = 0; i < vacinasCadastradas.size() && FLAG == 0; i++) {
            if(postoSearch.getNomePosto().equals(vacinasCadastradas.get(i)
                                                          .getNomePosto())) {
                vacinasCadastradas.get(i).getVacinasPosto().add(vacina);
                listaVacinas.add(
                    new ListaVacinaListView(postoSearch.getNomePosto(), vacina)
                );
                break;
            }   
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
    private void onSelecionaVacina(MouseEvent event) {
        itemListaVacinas = lvVacinas.getSelectionModel().getSelectedItem();
        
        tfNomeVacina.setText(itemListaVacinas.getVacina().getNomeVacina());
        
        tfQuantidade.setText(Integer.toString(itemListaVacinas.getVacina()
                                                              .getQtdVacina()));
        
        if(itemListaVacinas.getVacina().isHasSegundaDose())
            rbSim.setSelected(true);
        else 
            rbNao.setSelected(true);
        
    }

    private void carregaVacinas() {
        PedidoListagemVacinas pedidoListagemVacinas = new PedidoListagemVacinas(
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
            
            MensageiroCliente.enviaMensagem(outbound, pedidoListagemVacinas);
        
            Gson gsonVacinas = new Gson();
            String stringVacinas = MensageiroCliente.recebeMensagem(inbound);
            ListagemVacinas vacinas = gsonVacinas.fromJson(stringVacinas, 
                                                        ListagemVacinas.class);

            if(vacinas.getId()  == TipoMensagem.ERRO.getId() ) {
                Erro erro = gsonVacinas.fromJson(stringVacinas, Erro.class);
                Alerta.mostraAlerta( "Erro com o servidor!", erro.getMensagem());
            } else {
                vacinasCadastradas = vacinas.getVacinasCadastradas();
            }
            
            int indicePosto;
            
            for (ListaVacinas v : vacinasCadastradas) {
                indicePosto = postosCadastrados.indexOf(findPosto(v.getNomePosto()));
                for (Vacina vacina : v.getVacinasPosto()) {
                    listaVacinas.add(new ListaVacinaListView(v.getNomePosto(), 
                                                             vacina));
                    postosCadastrados.get(indicePosto).getVacinasPosto()
                                                                   .add(vacina);
                }
            }
            
            MensageiroCliente.fechaSocketEDutos(client, outbound, inbound);
            
        } catch (IOException ex) {
            System.err.println( "Erro:" + ex.getMessage() );
            Alerta.mostrarErroComunicacao();
        }
    }

    @FXML
    private void onRemoveVac(ActionEvent event) {
        if(itemListaVacinas != null){
            String nomePostoAlvo = itemListaVacinas.getNomePosto();
            String nomeVacinaAlvo = tfNomeVacina.getText();

            RemoveVacina removeVacina = new RemoveVacina(nomePostoAlvo, 
                                                         nomeVacinaAlvo,
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
                MensageiroCliente.enviaMensagem(outbound, removeVacina);

                verificaSeOperacaoRealizada(inbound);

            } catch (IOException ex) {
                System.err.println( "Erro:" + ex.getMessage() );
                Alerta.mostrarErroComunicacao();
            }

            PostoDeSaude postoAlvo = findPosto(nomePostoAlvo);

            Vacina vacinaAlvo = findVacina(nomePostoAlvo, nomeVacinaAlvo);

            int indicePosto = postosCadastrados.indexOf(postoAlvo);

            postosCadastrados.get(indicePosto).getVacinasPosto().remove(vacinaAlvo);

            for (int i = 0; i < listaVacinas.size(); i++) {
                ListaVacinaListView item = listaVacinas.get(i);

                if(item.getNomePosto().equals(nomePostoAlvo)
                   && item.getVacina().getNomeVacina().equals(nomeVacinaAlvo)){

                    listaVacinas.remove(item);
                    break;
                }
            }

            obsPostos = FXCollections.observableArrayList(postosCadastrados);
            obsVacinas = FXCollections.observableArrayList(listaVacinas);

            lvPostosDeSaude.setItems(obsPostos);
            lvVacinas.setItems(obsVacinas);

            tfNomeVacina.setText("");
            tfQuantidade.setText("");

            rbSim.setSelected(false);
            rbNao.setSelected(false);
            
            itemListaVacinas = null;
        } else {
            Alerta.mostraAlerta("Vacina não selecionada", "Selecione uma "
                    + "vacina para ser removida!");
        }
        
    }
    
    private Vacina findVacina(String nomePosto, String nomeVacina){
        for (int i = 0; i < postosCadastrados.size(); i++) {
            PostoDeSaude p = postosCadastrados.get(i);
            
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
    
    private PostoDeSaude findPosto(String nomePosto){
        for (PostoDeSaude posto : postosCadastrados) {
            if(posto.getNomePosto().equals(nomePosto)) return posto;
        }
        return null;
    }
}
