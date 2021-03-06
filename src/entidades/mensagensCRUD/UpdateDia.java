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
public class UpdateDia extends MensagemCRUD {
    private String nomePostoAlvo;
    private String dataAntiga;
    private String novaData;
    private String cpf;
    private String senha;

    public UpdateDia(String nomePostoAlvo, String dataAntiga, String novaData, 
                        String cpf, String senha) {
        super(TipoMensagem.UPDATE_DIA);
        this.nomePostoAlvo = nomePostoAlvo;
        this.dataAntiga = dataAntiga;
        this.novaData = novaData;
        this.cpf = cpf;
        this.senha = senha;
    }

    public String getNovaData() {
        return novaData;
    }

    public void setNovaData(String novaData) {
        this.novaData = novaData;
    }

    public String getNomePostoAlvo() {
        return nomePostoAlvo;
    }

    public void setNomePostoAlvo(String nomePostoAlvo) {
        this.nomePostoAlvo = nomePostoAlvo;
    }

    public String getDataAntiga() {
        return dataAntiga;
    }

    public void setDataAntiga(String dataAntiga) {
        this.dataAntiga = dataAntiga;
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
