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
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author luanl
 */
public class TelaCadastroAgendamentoController implements Initializable {

    @FXML
    private ComboBox<?> cbLocais;
    @FXML
    private ComboBox<?> cbDatas;
    @FXML
    private ComboBox<?> cbHorarios;
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
            this, root, "/./telas/TelaPrincipal.fxml" ,
            "Assistente de Vacinação - Menu Principal"
        );
    }

    @FXML
    private void onVoltar(ActionEvent event) {
        TelaLoader.Load(
            this, root, "/./telas/TelaPrincipal.fxml" ,
            "Assistente de Vacinação - Menu Principal"
        );
    }
    
}
