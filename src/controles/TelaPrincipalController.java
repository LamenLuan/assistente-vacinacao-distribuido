/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import entidades.Agendamento;
import entidades.MensageiroCliente;
import entidades.TelaLoader;
import entidades.TipoMensagem;
import entidades.mensagens.Mensagem;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author luanl
 */
public class TelaPrincipalController implements Initializable {

    private boolean admin;
    private Agendamento agendamento;
    
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

    public void inicializaDados(boolean admin, Agendamento agendamento) {
        this.admin = admin;
        this.agendamento = agendamento;
        
        if(agendamento != null) {
            lbAgendamento.setText("Agendamento marcado:");
            lbDose.setText(
                "Aplicação da " + (agendamento.isSegundaDose() ? "2" : "1")
                + "ª dose"
            );
            lbLocal.setText( agendamento.getLocal() );
            lbData.setText(
                agendamento.getData() + " - " + agendamento.getHora()
            );
            btCancelarAgendamento.setDisable(false);
        }
        
        ( (Stage) root.getScene().getWindow() ).setOnCloseRequest(
            eh -> MensageiroCliente.enviaLogout()
        );
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void onCancelarAgendamento(ActionEvent event) {
    }

    @FXML
    private void onAgendar(ActionEvent event) {
        TelaLoader.Load(
            this, root, "/./telas/TelaCadastroAgendamento.fxml" ,
            "Assistente de Vacinação - Ficha de Agendamento"
        );
    }

    @FXML
    private void onAcessarChat(ActionEvent event) {
        TelaLoader.Load(
            this, root, "/./telas/TelaChat.fxml" ,
            "Assistente de Vacinação - Chat de Dúvidas"
        );
    }

    private void cancelaEnventoDeSaida() {
        ( (Stage) root.getScene().getWindow() ).setOnCloseRequest(null);
    }
    
    @FXML
    private void onLogout(ActionEvent event) {
        MensageiroCliente.enviaLogout();
        cancelaEnventoDeSaida();
        TelaLoader.Load(
            this, root, "/./telas/TelaLogin.fxml" ,
            "Assistente de Vacinação - Acesso ao sistema"
        );
    }
    
}
