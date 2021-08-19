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
public class CadastroPostoPt1 extends MensagemCRUD {
    String cpf, senha, nomePosto, endPosto;

    public CadastroPostoPt1(
        String cpf, String senha, String nomePosto, String endPosto
    ) {
        super(TipoMensagem.CADASTRO_NOME_ENDERECO_POSTO);
        this.cpf = cpf;
        this.senha = senha;
        this.nomePosto = nomePosto;
        this.endPosto = endPosto;
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

    public String getNomePosto() {
        return nomePosto;
    }

    public void setNomePosto(String nomePosto) {
        this.nomePosto = nomePosto;
    }

    public String getEndPosto() {
        return endPosto;
    }

    public void setEndPosto(String endPosto) {
        this.endPosto = endPosto;
    }
}
