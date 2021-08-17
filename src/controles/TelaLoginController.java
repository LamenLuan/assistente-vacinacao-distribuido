/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controles;

import com.google.gson.Gson;
import entidades.Agendamento;
import entidades.Alerta;
import entidades.Mensageiro;
import entidades.TelaLoader;
import entidades.TipoMensagem;
import entidades.Mensagem;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
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
public class TelaLoginController implements Initializable {

    String cpf, senha;
    
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
    
    private boolean verificaSeCamposValidos() {
        Pattern padrao = Pattern.compile("^[0-9]{11}$");
        Matcher matcher = padrao.matcher(cpf);
        
        if( cpf.isBlank() || senhaField.getText().isBlank() ) {
            Alerta.mostrarCamposVazios();
        }
        else if( !matcher.find() ) {
            Alerta.mostrarCampoInvalido("O CPF deve ter 11 dígitos!");
        }
        else return true;
        
        return false;
    }
    
    private void abreTelaPrincipal(boolean admin, Agendamento agendamento) {
        FXMLLoader loader = TelaLoader.Load(
                this, root, "/./telas/TelaPrincipal.fxml" ,
                "Assistente de Vacinação - Menu Principal"
        );
        
        TelaPrincipalController controller = loader.getController();
        controller.inicializaDados(admin, cpf, senha, agendamento);
    }
    
    private void verificaSeLoginAprovado(BufferedReader inbound) throws IOException {
        Mensagem mensagem = Mensageiro.recebeMensagem(inbound, false);
        int id = mensagem.getId();
        
        if( id == TipoMensagem.LOGIN_INVALIDO.getId() ) {
            Alerta.mostraAlerta(
                "Login inválido",
                "CPF e/ou senha incorretos (ou inexistentes)"
            );
        }
        else if( id == TipoMensagem.LOGIN_APROVADO.getId() ) {
            Mensagem temAgendamento = null;
            
            if( mensagem.isAgendamento() ) 
                temAgendamento = Mensageiro.recebeMensagem(inbound, false);
            
            abreTelaPrincipal(
                mensagem.isAdmin(),
                temAgendamento != null
                        ? temAgendamento.getDadosAgendamento() : null
            );
        }
    }

    @FXML
    private void onEntrar(ActionEvent event) { 
        cpf = cpfField.getText();
        senha = senhaField.getText();
        
        if( verificaSeCamposValidos() ) {
            Mensagem pedidoLogin = new Mensagem(
                TipoMensagem.PEDIDO_LOGIN, cpf, senha
            );
            try {
                Socket client = new Socket(
                    InetAddress.getByName(Mensageiro.ip),
                    Mensageiro.porta
                );
                PrintWriter outbound = new PrintWriter(
                    client.getOutputStream(), true
                );
                BufferedReader inbound = new BufferedReader(
                    new InputStreamReader( client.getInputStream() )
                );
                
                Mensageiro.enviaMensagem(outbound, pedidoLogin, false);
                verificaSeLoginAprovado(inbound);
                
                Mensageiro.fechaSocketEDutos(client, outbound, inbound);
            } catch (IOException ex) {
                System.err.println( "Erro:" + ex.getMessage() );
                Alerta.mostrarErroComunicacao();
            }
        }
    }

    @FXML
    private void onCadastrar(ActionEvent event) {
        TelaLoader.Load(
            this, root, "/./telas/TelaCadastroUsuario1.fxml" ,
            "Assistente de Vacinação - Ficha de cadastro"
        );
    }

    @FXML
    private void onInstaLogin(ActionEvent event) {
        cpfField.setText("11111111111");
        senhaField.setText("senha");
        onEntrar(null);
    }

    @FXML
    private void onInstaLoginAdmin(ActionEvent event) {
        cpfField.setText("99999999999");
        senhaField.setText("senha");
        onEntrar(null);
    }
}
