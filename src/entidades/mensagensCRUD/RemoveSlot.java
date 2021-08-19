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
public class RemoveSlot extends MensagemCRUD {
    private String cpf;
    private String senha;
    private String nomePosto;
    private String data;
    private String slotVacinacao;

    public RemoveSlot(String nomePosto, String data, String slotVacinacao,
                        String cpf, String senha) {
        super(TipoMensagem.REMOVE_SLOT);
        this.nomePosto = nomePosto;
        this.data = data;
        this.slotVacinacao = slotVacinacao;
        this.cpf = cpf;
        this.senha = senha;
    }

    public String getSlotVacinacao() {
        return slotVacinacao;
    }

    public void setSlotVacinacao(String slotVacinacao) {
        this.slotVacinacao = slotVacinacao;
    }

    public String getNomePosto() {
        return nomePosto;
    }

    public void setNomePosto(String nomePosto) {
        this.nomePosto = nomePosto;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
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
}
