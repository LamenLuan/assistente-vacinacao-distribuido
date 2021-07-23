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
public class TelaLoginController implements Initializable {

    @FXML
    private TextField cpfField;
    @FXML
    private PasswordField senhaField;
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
    private void onEntrar(ActionEvent event) {
        TelaLoader.Load(
            this, root, "/./telas/TelaPrincipal.fxml" ,
            "Assistente de Vacinação - Menu Principal"
        );
    }

    @FXML
    private void onCadastrar(ActionEvent event) {
        TelaLoader.Load(
            this, root, "/./telas/TelaCadastroCidadao1.fxml" ,
            "Assistente de Vacinação - Ficha de cadastro"
        );
    }
}
