/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import entidades.TelaLoader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author luanl
 */
public class TelaCadastroCidadao2Controller implements Initializable {

    @FXML
    private TextField telefoneField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField senhaField;
    @FXML
    private PasswordField confirmaSenhaField;
    @FXML
    private VBox root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onConcluirCadastro(ActionEvent event) {
        TelaLoader.Load(
            this, root, "/./telas/TelaLogin.fxml" ,
            "Assistente de Vacinação - Acesso ao sistema"
        );
    }

    @FXML
    private void onVoltar(ActionEvent event) {
        TelaLoader.Load(
            this, root, "/./telas/TelaCadastroCidadao1.fxml" ,
            "Assistente de Vacinação - Ficha de cadastro"
        );
    }
    
}
