/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import com.google.gson.Gson;
import entidades.Alerta;
import entidades.MensageiroCliente;
import entidades.TelaLoader;
import entidades.Usuario;
import entidades.mensagens.Mensagem;
import entidades.mensagens.PedidoLogin;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    }
    
    private boolean verificaSeCamposValidos(String cpf, String senha) {
        Pattern padrao;
        Matcher matcher;
        
        padrao = Pattern.compile("^[0-9]{11}$");
        matcher = padrao.matcher(cpf);
        
        if( cpf.isBlank() || senhaField.getText().isBlank() ) {
            Alerta.mostraAlerta(
                "Campos vazios",
                "Preencha os campos de CPF e senha para acessar"
            );
        }
        else if( !matcher.find() ) {
            Alerta.mostrarCampoInvalido("O CPF deve ter 11 dígitos!");
        }
        else return true;
        
        return false;
    }
    
    private void verificaSeLoginAprovado(Socket client) throws IOException {
        Gson gson = new Gson();
        String string = MensageiroCliente.recebeMensagem(client);
        int id = MensageiroCliente.getIdMensagem(string);
        
        System.out.println(id);
    }

    @FXML
    private void onEntrar(ActionEvent event) { 
        String cpf = cpfField.getText(), senha = senhaField.getText();
        
        if( verificaSeCamposValidos(cpf, senha) ) {
            PedidoLogin pedidoLogin = new PedidoLogin(cpf, senha);
            try {
                Socket client = new Socket(
                    InetAddress.getByName("192.168.1.3"), 1234
                );
                MensageiroCliente.enviaMensagem(client, pedidoLogin);
                verificaSeLoginAprovado(client);
                client.close();
            } catch (IOException ex) {
                Alerta.mostraAlerta(
                    "Erro de comunicação",
                    "Aplicação foi incapaz de se comunicar com servidor, "
                    + "tente novamente mais tarde."
                );
            }
            
//            FXMLLoader loader = TelaLoader.Load(
//                this, root, "/./telas/TelaPrincipal.fxml" ,
//                "Assistente de Vacinação - Menu Principal"
//            );
//            TelaPrincipalController controller = loader.getController();
//            controller.inicializaDados(usuario);
        }
    }

    @FXML
    private void onCadastrar(ActionEvent event) {
        TelaLoader.Load(
            this, root, "/./telas/TelaCadastroCidadao1.fxml" ,
            "Assistente de Vacinação - Ficha de cadastro"
        );
    }
}
