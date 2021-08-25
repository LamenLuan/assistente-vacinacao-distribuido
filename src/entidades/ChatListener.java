/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import controles.TelaChatController;
import java.io.BufferedReader;
import java.io.IOException;
import javafx.application.Platform;

/**
 *
 * @author luanl
 */
public class ChatListener extends Thread {
    
    private TelaChatController telaChatController;
    private final BufferedReader inbound;

    public ChatListener(
        BufferedReader inbound, TelaChatController telaChatController
    ) {
        this.inbound = inbound;
        this.telaChatController = telaChatController;
    }
    
    @Override
    public void run() {       
        try {
            while (true) {
                if( telaChatController.isAdmin() ) {
                    while (true) {
                        Mensagem mensagem = Mensageiro.recebeMensagem(
                            inbound, false
                        );
                        if(mensagem != null) {
                            int id = mensagem.getId();

                            if( id == TipoMensagem.DADOS_CHAT_CLIENTE.getId() ) {

                                // GUARDAR O NOME DO CLIENTE NO CONTROLLER

                                Platform.runLater(() -> {
                                    telaChatController.setChatDisable(false);
                                    telaChatController.getConversas().add(
                                        "Você está conversando com " +
                                        mensagem.getNome() + " agora!"
                                    );
                                    telaChatController.setAdminIndisponivel();
                                });
                                break;
                            }
                        }

                    }
                }
                Mensagem mensagem = Mensageiro.recebeMensagem(inbound, false);
                if(mensagem != null) {
                    int id = mensagem.getId();
                    if( id == TipoMensagem.AVISO_ENCERRAMENTO_CHAT.getId() ) {
                        if( telaChatController.isAdmin() ) {
                            Platform.runLater(() -> {
                                Alerta.mostraConfirmacao(
                                    "Chat encerrado pelo usuario"
                                );
                                telaChatController.getConversas().clear();
                                telaChatController.setChatDisable(true);
                            });
                        }
                        else {
                            Platform.runLater(() -> {
                                Alerta.mostraConfirmacao(
                                    "Chat encerrado pelo agente de saúde"
                                );
                                try {
                                    telaChatController.fechaChat();
                                } catch (IOException ex) {}
                            });
                        }
                    }
                }
            }
        } catch (IOException ex) {}
    }
    
}
