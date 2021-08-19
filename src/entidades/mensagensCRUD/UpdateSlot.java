/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.mensagensCRUD;

import entidades.Slot;
import entidades.TipoMensagem;

/**
 *
 * @author a2057387
 */
public class UpdateSlot extends MensagemCRUD {
    private String nomePosto;
    private String data;
    private Slot slot;
    private String cpf;
    private String senha;

    public UpdateSlot(String nomePosto, String data, Slot slot,String cpf,
                        String senha) {
        super(TipoMensagem.UPDATE_SLOT);
        this.nomePosto = nomePosto;
        this.data = data;
        this.slot = slot;
        this.cpf = cpf;
        this.senha = senha;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
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
