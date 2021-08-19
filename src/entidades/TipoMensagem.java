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
    PEDIDO_CADASTRO(5),
    CPF_JA_CADASTRADO(6),
    CADASTRO_EFETUADO(7),
    PEDIDO_DADOS_AGENDAMENTO(8),
    NAO_TEM_VACINAS(9),
    TEM_VACINAS(10),
    PEDIDO_AGENDAMENTO(11),
    AGENDAMENTO_CONCLUIDO(12),
    PEDIDO_CANCELAMENTO(13),
    CONFIRMACAO_CANCELAMENTO(14),
    ERRO(100);
    
    private final int id;

    private TipoMensagem(int id) {
        this.id = id;
    }
 
   public int getId() {
        return id;
    }
}
