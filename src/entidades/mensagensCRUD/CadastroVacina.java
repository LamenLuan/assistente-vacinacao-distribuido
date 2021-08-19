/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.mensagensCRUD;

import entidades.TipoMensagem;
import entidades.Vacina;

/**
 *
 * @author a2057387
 */
public class CadastroVacina extends MensagemCRUD {
    private String cpf, senha, nomePosto;
    private Vacina vacinasPosto;

    public CadastroVacina(
        String cpf, String senha, String nomePosto,
        Vacina vacinasPosto
    ) {
        super(TipoMensagem.CADASTRO_VACINA);
        this.cpf = cpf;
        this.senha = senha;
        this.nomePosto = nomePosto;
        this.vacinasPosto = vacinasPosto;
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

    public Vacina getVacinasPosto() {
        return vacinasPosto;
    }

    public void setVacinasPosto(Vacina vacinasPosto) {
        this.vacinasPosto = vacinasPosto;
    }

    public String getNomePosto() {
        return nomePosto;
    }

    public void setNomePosto(String nomePosto) {
        this.nomePosto = nomePosto;
    }
}
