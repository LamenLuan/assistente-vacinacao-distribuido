/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import entidades.Agendamento;
import entidades.MensageiroCliente;
import entidades.TelaLoader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author luanl
 */
public class TelaChatController implements Initializable {

    private boolean admin;
    private Agendamento agendamento;
    
    @FXML
    private ListView<?> lvConversas;
    @FXML
    private VBox root;
    @FXML
    private TextField txConversa;

    public void inicializaDados(boolean admin, Agendamento agendamento) {
        this.admin = admin;
        this.agendamento = agendamento;
        
        ( (Stage) root.getScene().getWindow() ).setOnCloseRequest(
            eh -> {
                MensageiroCliente.enviaLogout();
            }
        );
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void onSairChat(ActionEvent event) {
        FXMLLoader loader = TelaLoader.Load(
                this, root, "/./telas/TelaPrincipal.fxml" ,
                "Assistente de Vacinação - Menu Principal"
        );
        
        TelaPrincipalController controller = loader.getController();
        controller.inicializaDados(admin, agendamento);
    }

    @FXML
    private void onEnviarConversa(ActionEvent event) {
    }
 
}
