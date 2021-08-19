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
public class RemoveDia extends MensagemCRUD {
    private String cpf;
    private String senha;
    private String nomePostoAlvo;
    private String data;

    public RemoveDia(
        String cpf, String senha, String nomePostoAlvo, String data
    ) {
        super(TipoMensagem.REMOVE_DIA);
        this.cpf = cpf;
        this.senha = senha;
        this.nomePostoAlvo = nomePostoAlvo;
        this.data = data;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNomePostoAlvo() {
        return nomePostoAlvo;
    }

    public void setNomePostoAlvo(String nomePostoAlvo) {
        this.nomePostoAlvo = nomePostoAlvo;
    }
    
}
