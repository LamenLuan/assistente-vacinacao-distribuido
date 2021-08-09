/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import com.google.gson.Gson;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author luanl
 */
public class TelaCadastroUsuario2Controller implements Initializable {

    Usuario usuario;
    
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
    
    public void inicializaDados(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    private boolean verificaTelefoneEEmail(String telefone, String email) {
        Pattern padrao = Pattern.compile("^[0-9]{11}$");
        Matcher matcher = padrao.matcher(telefone);
        
        if( !matcher.find() ) {
            Alerta.mostrarCampoInvalido(
                "O número de telefone deve ter 11 dígitos! (DDD + número)"
            );
            return false;
        }
        
        if( !email.isBlank() ) {
            padrao = Pattern.compile(
                "^[a-zA-Z0-9.a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+"
                + "(\\.[a-zA-Z]+)+$"
            );
            matcher = padrao.matcher(email);

            if( !matcher.find() ) {
                Alerta.mostrarCampoInvalido(
                    "Digite um email valido (Exemplo: vacinado@email.com)"
                );
                return false;
            }
        }
        
        return true;
    }
    
    private boolean verificaSenhasIguais(String senha, String confirmaSenha) {
        if( !senha.equals(confirmaSenha) ) {
            Alerta.mostraAlerta(
                "Senhas não coincidem",
                    "Verifique os campos de senha, eles precisam ser iguais"
            );
            return false;
        }
        return true;
    }
    
    private boolean verificaSeCamposValidos(
        String telefone, String email, String senha, String confirmaSenha
    ) {
        if( telefone.isBlank() || senha.isBlank() || confirmaSenha.isBlank() ) {
            Alerta.mostraAlerta(
                "Campos vazios",
                "Preencha todos os campos para concluir o cadastro"
            );
        }
        else if(
            verificaTelefoneEEmail(telefone, email) && 
            verificaSenhasIguais(senha, confirmaSenha)
        ) return true;
 
        return false;
    }

    @FXML
    private void onConcluirCadastro(ActionEvent event) {
        String telefone = telefoneField.getText();
        String email = emailField.getText();
        String senha = senhaField.getText();
        String confirmaSenha = confirmaSenhaField.getText();
        
        if( verificaSeCamposValidos(telefone, email, senha, confirmaSenha) ) {
            usuario.setTelefone(telefone);
            usuario.setEmail(email);
            usuario.setSenha(senha);
            
            TelaLoader.Load(
                this, root, "/./telas/TelaLogin.fxml" ,
                "Assistente de Vacinação - Acesso ao sistema"
            );
        }
    }

    @FXML
    private void onVoltar(ActionEvent event) {
        FXMLLoader loader = TelaLoader.Load(
            this, root, "/./telas/TelaCadastroUsuario1.fxml" ,
            "Assistente de Vacinação - Ficha de cadastro"
        );
        TelaCadastroUsuario1Controller controller = loader.getController();
        controller.inicializaDados(usuario);
    }

    @FXML
    private void instaCompletaCadastro(ActionEvent event) {
        telefoneField.setText("11111111111");
        senhaField.setText("senha");
        confirmaSenhaField.setText("senha");
        onConcluirCadastro(null);
    }

}