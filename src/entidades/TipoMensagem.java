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
    CADASTRO_NOME_ENDERECO_POSTO(30),
    LISTA_POSTOS_NOMES(32),
    LISTA_POSTOS_NOMES_RESPOSTA(33),
    PEDIDO_UPDATE_POSTO(34),
    PEDIDO_REMOVE_POSTO(36),
    CADASTRO_VACINA(40),
    LISTA_VACINAS(42),
    LISTA_VACINAS_RESPOSTA(43),
    UPDATE_VACINA(44),
    REMOVE_VACINA(46),
    CADASTRO_DIA_VACINACAO(50),
    LISTA_DIAS(52),
    LISTA_DIAS_RESPOSTA(53),
    UPDATE_DIA(54),
    REMOVE_DIA(56),
    CADASTRO_SLOT(60),
    LISTA_SLOTS(62),
    LISTA_SLOTS_RESPOSTA(63),
    UPDATE_SLOT(64),
    REMOVE_SLOT(66),
    ERRO(100),
    OPERACAO_CRUD_SUCESSO(200);
    
    private final int id;

    private TipoMensagem(int id) {
        this.id = id;
    }
 
   public int getId() {
        return id;
    }
}
