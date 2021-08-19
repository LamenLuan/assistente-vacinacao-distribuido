/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.mensagensCRUD;

import entidades.Dia;
import entidades.TipoMensagem;

/**
 *
 * @author a2057387
 */
public class CadastroDiaVacinacao extends MensagemCRUD {
    private String cpf, senha, nomePosto;
    private Dia diaPosto;

    public CadastroDiaVacinacao(
        String cpf, String senha, String nomePosto,  Dia diaPosto
    ) {
        super(TipoMensagem.CADASTRO_DIA_VACINACAO);
        this.cpf = cpf;
        this.senha = senha;
        this.nomePosto = nomePosto;
        this.diaPosto = diaPosto;
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

    public Dia getDiaPosto() {
        return diaPosto;
    }

    public void setDiaPosto(Dia diaPosto) {
        this.diaPosto = diaPosto;
    }
    
}
