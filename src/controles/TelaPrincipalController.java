/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import entidades.Agendamento;
import entidades.TelaLoader;
import java.net.URL;
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

    public void inicializaDados(
        boolean admin, String cpf, String senha, Agendamento agendamento
    ) {
        this.admin = admin;
        this.cpf = cpf;
        this.senha = senha;
        this.agendamento = agendamento;
        
        lbBemVindo.setText(
            "Bem vindo " + (admin ? "Administrador" : "Usuário") + '!'
        );
        
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
        
        if(admin) mCadastrar.setVisible(true);
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
            this, root, "/./telas/TelaCadastroCidadao1.fxml" ,
            "Assistente de Vacinação - Ficha de cadastro (admin)"
        );
        TelaCadastroCidadao1Controller controller = loader.getController();
        controller.modoAdmin();
    }
    
}
