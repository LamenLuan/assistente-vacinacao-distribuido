/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import entidades.Alerta;
import entidades.TelaLoader;
import entidades.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author luanl
 */
public class TelaCadastroUsuario1Controller implements Initializable {

    boolean admin;
    
    @FXML
    private TextField nomeField;
    @FXML
    private TextField cpfField;
    @FXML
    private DatePicker dataField;
    @FXML
    private VBox root;
    @FXML
    private ToggleGroup tgGenero;
    @FXML
    private ToggleGroup tgComorbidade;
    @FXML
    private RadioButton rbMasculino;
    @FXML
    private RadioButton rbComorbidade;
    @FXML
    private RadioButton rbNaoComorbidade;
    @FXML
    private RadioButton rbFeminino;
    
    public void inicializaDados(Usuario usuario) {
        nomeField.setText( usuario.getNome() );
        cpfField.setText( usuario.getCpf() );
        dataField.getEditor().setText( usuario.getDataNascimento() );
        
        if( usuario.isMasculino() ) rbMasculino.setSelected(true);
        else rbFeminino.setSelected(true);
        
        // Nao precisa de else aqui pois a comorbidade eh default false
        if( usuario.isComorbidade() ) rbComorbidade.setSelected(true);
    }
    
    public void modoAdmin() {
        this.admin = true;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        admin = false;
        rbMasculino.setUserData(true);
        rbFeminino.setUserData(false);
        rbComorbidade.setUserData(true);
        rbNaoComorbidade.setUserData(false);
    }
    
    private boolean verificaSeCamposValidos(
        String nome, String cpf, String dataNascimento
    ) {
        Pattern padrao = Pattern.compile("^[0-9]{11}$");
        Matcher matcher = padrao.matcher(cpf);
        
        if(
            nome.isBlank() || cpf.isBlank() || dataNascimento.isBlank() || 
            tgGenero.getSelectedToggle() == null
        ) Alerta.mostrarCamposVazios();
        else if( !matcher.find() ) {
            Alerta.mostrarCampoInvalido("O CPF deve ter 11 dígitos!");
        }
        else return true;
 
        return false;
    }
    
    private boolean getToggleData(ToggleGroup tg) {
        return (boolean) tg.getSelectedToggle().getUserData();
    }

    @FXML
    private void onProsseguirCadastro(ActionEvent event) {
       
        boolean masculino, comorbidade;
        String nome, cpf, dataNascimento;
        
        nome = nomeField.getText();
        cpf = cpfField.getText();
        dataNascimento = dataField.getEditor().getText();
        
        if( verificaSeCamposValidos(nome, cpf, dataNascimento) ) {
            masculino = getToggleData(tgGenero);
            comorbidade = getToggleData(tgComorbidade);
            
            Usuario usuario = new Usuario(
                nome, cpf, dataNascimento, null, null, null,
                masculino, comorbidade, this.admin, null
            );
            
            FXMLLoader loader = TelaLoader.Load(
                this, root, "/./telas/TelaCadastroUsuario2.fxml" ,
                "Assistente de Vacinação - Ficha de cadastro"
            );

            TelaCadastroUsuario2Controller controller = loader.getController();
            controller.inicializaDados(usuario);
        }
    }

    @FXML
    private void onVoltar(ActionEvent event) {
        TelaLoader.Load(
            this, root, "/./telas/TelaLogin.fxml" ,
            "Assistente de Vacinação - Acesso ao sistema"
        );
    }

    @FXML
    private void instaPreencher(ActionEvent event) {
        nomeField.setText("Beltrana");
        cpfField.setText("22222222222");
        dataField.getEditor().setText("01/01/2001");
        rbFeminino.setSelected(true);
        rbComorbidade.setSelected(true);
        
        onProsseguirCadastro(null);
    }
    
}
