/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import entidades.TelaLoader;
import entidades.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author luanl
 */
public class TelaPrincipalController implements Initializable {

    private Usuario usuario;
    
    @FXML
    private Label lbUsuario;
    @FXML
    private Label lbAgendamento;
    @FXML
    private Label lbDadosAgendamento;
    @FXML
    private BorderPane root;

    public void inicializaDados(Usuario usuario) {
        this.usuario = usuario;
        
        lbUsuario.setText("Bem vindo " + usuario.getNome() + "!");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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

    @FXML
    private void onVoltar(ActionEvent event) {
        TelaLoader.Load(
            this, root, "/./telas/TelaLogin.fxml" ,
            "Assistente de Vacinação - Acesso ao sistema"
        );
    }
    
}
