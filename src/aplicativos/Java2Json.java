/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicativos;

import com.google.gson.Gson;
import entidades.Agendamento;
import entidades.Usuario;
import entidades.mensagens.Mensagem;
import entidades.mensagens.PedidoLogin;
import entidades.mensagens.TemAgendamento;

/**
 *
 * @author luanl
 */
public class Java2Json {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String mensagem = "{\"id\":1,\"cpf\":\"jooj\",\"senha\":\"123\"}";
        Mensagem sadsa = new TemAgendamento( new Agendamento("aaaaaa", "11/11/1111", "11:11", "Pfizer", true) );
        
        Gson gson = new Gson();
       
        System.out.println( gson.toJson(sadsa) );
    }
    
}
