/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.mensagens;

import entidades.TipoMensagem;

/**
 *
 * @author luanl
 */
public class PedidoLogin extends Mensagem {
    private String cpf, senha;

    public PedidoLogin(String cpf, String senha) {
        super( TipoMensagem.PEDIDO_LOGIN.getId() );
        this.cpf = cpf;
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
