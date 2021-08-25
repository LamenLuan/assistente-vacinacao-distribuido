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
                if ( telaChatController.isAdmin() ) {
                    while (true) {
                        Mensagem mensagem = Mensageiro.recebeMensagem(
                                inbound, false
                        );
                        if (mensagem != null) {
                            int id = mensagem.getId();
                            
                            if (id == TipoMensagem.DADOS_CHAT_CLIENTE.getId()) {
                                String nomeDestinatario = mensagem.getNome();
                                Platform.runLater(() -> {
                                    telaChatController.setChatDisable(false);
                                    telaChatController.setNomeDestinatario(
                                            nomeDestinatario
                                    );
                                    telaChatController.getConversas().add(
                                            "Você está conversando com "
                                            + nomeDestinatario + " agora!"
                                    );
                                    telaChatController.setAdminIndisponivel();
                                });
                                break;
                            }
                        }
                        
                    }
                }
                while (true) {
                    Mensagem mensagem = Mensageiro.recebeMensagem(inbound, false);
                    if (mensagem != null) {
                        int id = mensagem.getId();
                        
                        if (id == TipoMensagem.DIRECIONA_MSG_CLIENTE.getId()) {
                            Platform.runLater(() -> {
                                String nomeDestinatario
                                        = telaChatController.getNomeDestinatario();
                                
                                telaChatController.getConversas().add(
                                        nomeDestinatario + ": "
                                        + mensagem.getMensagem()
                                );
                            });
                        } else if (id == TipoMensagem.ENCERRAMENTO_CHAT.getId()) {
                            if ( telaChatController.isAdmin() ) {
                                Platform.runLater(() -> {
                                    Alerta.mostraConfirmacao(
                                            "Chat encerrado pelo usuario"
                                    );
                                    telaChatController.getConversas().clear();
                                    telaChatController.setChatDisable(true);
                                });
                                break;
                            } else {
                                Platform.runLater(() -> {
                                    Alerta.mostraConfirmacao(
                                            "Chat encerrado pelo agente de saúde"
                                    );
                                    try {
                                        telaChatController.fechaChat();
                                    } catch (IOException ex) {
                                    }
                                });
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
        }
    }
    
}
