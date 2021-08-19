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
public class CadastroSlot extends MensagemCRUD {
    private String nomePosto;
    private String data;
    private String slotCadastro;
    private String cpf;
    private String senha;
    private int qtdSlotVacinacao;

    public CadastroSlot(
            String nomePosto, String data, 
            String slotCadastro, int qtdSlotVacinacao, String cpf, String senha) {
        super(TipoMensagem.CADASTRO_SLOT);
        this.nomePosto = nomePosto;
        this.data = data;
        this.slotCadastro = slotCadastro;
        this.qtdSlotVacinacao = qtdSlotVacinacao;
        this.cpf = cpf;
        this.senha = senha;
    }

    public int getQtdSlotVacinacao() {
        return qtdSlotVacinacao;
    }

    public void setQtdSlotVacinacao(int qtdSlotVacinacao) {
        this.qtdSlotVacinacao = qtdSlotVacinacao;
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

    public String getSlotCadastro() {
        return slotCadastro;
    }

    public void setSlotCadastro(String slotCadastro) {
        this.slotCadastro = slotCadastro;
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
