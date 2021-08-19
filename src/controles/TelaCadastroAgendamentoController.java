/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import entidades.Agendamento;
import entidades.Alerta;
import entidades.DiaVacinacao;
import entidades.Mensageiro;
import entidades.Mensagem;
import entidades.PostoDeSaude;
import entidades.Slot;
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
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author luanl
 */
public class TelaCadastroAgendamentoController implements Initializable {
    
    private boolean admin;
    private String cpf, senha;
    private Agendamento agendamento;
    private ArrayList<PostoDeSaude> postosSaude;
    
    @FXML
    private ComboBox<PostoDeSaude> cbPostos;
    @FXML
    private ComboBox<DiaVacinacao> cbDatas;
    @FXML
    private ComboBox<Slot> cbHorarios;
    @FXML
    private VBox root;
    @FXML
    private Button btConcluir;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void inicializaDados(
        boolean admin, String cpf, String senha, Agendamento agendamento,
        ArrayList<PostoDeSaude> postosSaude
    ) {
        this.admin = admin;
        this.cpf = cpf;
        this.senha = senha;
        this.agendamento = agendamento;
        this.postosSaude = postosSaude;
        
        cbPostos.getItems().addAll(postosSaude);
    }

    @FXML
    private void onConcluirCadastro(ActionEvent event) {
            
        Mensagem mensagem = new Mensagem(
            cpf, senha, cbPostos.getValue().getNomePosto(),
            cbDatas.getValue().getDia(),
            cbHorarios.getValue().getSlotVacinacao()
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
            if( id == TipoMensagem.AGENDAMENTO_CONCLUIDO.getId() ) {
                PostoDeSaude posto = cbPostos.getValue();
                agendamento = new Agendamento(
                    posto.getNomePosto(),
                    posto.getEndPosto(),
                    cbDatas.getValue().getDia(),
                    cbHorarios.getValue().getSlotVacinacao(),
                    null, false
                );
                onVoltar(null);
            }
            else if( id == TipoMensagem.ERRO.getId() ) {
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
    private void onSelecionaPosto(ActionEvent event) {
        cbDatas.getItems().clear();
        cbHorarios.getItems().clear();
        cbHorarios.setDisable(true);
        btConcluir.setDisable(true);
        
        PostoDeSaude posto = cbPostos.getSelectionModel().getSelectedItem();
        cbDatas.getItems().addAll(
            posto.getDiasVacinacao()
        );
        cbDatas.setDisable(false);
    }

    @FXML
    private void onSelecionaData(ActionEvent event) {
        cbHorarios.getItems().clear();
        btConcluir.setDisable(true);
        
        DiaVacinacao dia = cbDatas.getSelectionModel().getSelectedItem();
        cbHorarios.getItems().addAll(
            dia.getSlots()
        );
        cbHorarios.setDisable(false);
    }

    @FXML
    private void onSelecionaSlot(ActionEvent event) {
        btConcluir.setDisable(false);
    }
    
}
