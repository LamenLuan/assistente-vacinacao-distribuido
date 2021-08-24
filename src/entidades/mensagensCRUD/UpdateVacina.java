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
public class UpdateVacina extends MensagemCRUD {
    private String nomePostoAlvo;
    private String nomeVacinaAlvo;
    private Vacina vacinasPosto;
    private String cpf;
    private String senha;

    public UpdateVacina(String nomePostoAlvo, String nomeVacinaAlvo, 
                        Vacina vacinaPosto, String cpf, String senha) {
        super(TipoMensagem.UPDATE_VACINA);
        this.nomePostoAlvo = nomePostoAlvo;
        this.nomeVacinaAlvo = nomeVacinaAlvo;
        this.vacinasPosto = vacinaPosto;
        this.cpf = cpf;
        this.senha = senha;
    }    

    public Vacina getVacinasPosto() {
        return vacinasPosto;
    }

    public void setVacinasPosto(Vacina vacinasPosto) {
        this.vacinasPosto = vacinasPosto;
    }

    public String getNomePostoAlvo() {
        return nomePostoAlvo;
    }

    public void setNomePostoAlvo(String nomePostoAlvo) {
        this.nomePostoAlvo = nomePostoAlvo;
    }

    public String getNomeVacinaAlvo() {
        return nomeVacinaAlvo;
    }

    public void setNomeVacinaAlvo(String nomeVacinaAlvo) {
        this.nomeVacinaAlvo = nomeVacinaAlvo;
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
