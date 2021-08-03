/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 *
 * @author luanl
 */
public enum TipoMensagem {
    PEDIDO_LOGIN(1),
    LOGIN_INVALIDO(2),
    LOGIN_APROVADO(3),
    TEM_AGENDAMENTO(4),
    LOGOUT(24);
    
    private final int id;

    private TipoMensagem(int id) {
        this.id = id;
    }
 
   public int getId() {
        return id;
    }
}
