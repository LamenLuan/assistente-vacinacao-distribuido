/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.mensagensCRUD;

import entidades.TipoMensagem;

/**
 *
 * @author a2057387
 */
public class PedidoRemovePosto extends MensagemCRUD {
    String cpf, senha, nomePostoAlvo;

    public PedidoRemovePosto(String cpf, String senha, String nomePostoAlvo) {
        super(TipoMensagem.PEDIDO_REMOVE_POSTO);
        this.cpf = cpf;
        this.senha = senha;
        this.nomePostoAlvo = nomePostoAlvo;
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

    public String getNomePostoAlvo() {
        return nomePostoAlvo;
    }

    public void setNomePostoAlvo(String nomePostoAlvo) {
        this.nomePostoAlvo = nomePostoAlvo;
    }
}
