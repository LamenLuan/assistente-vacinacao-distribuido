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
    private Vacina vacinaPosto;

    public UpdateVacina(
            String nomePostoAlvo, String nomeVacinaAlvo, Vacina vacinasPosto
    ) {
        super(TipoMensagem.UPDATE_VACINA);
        this.nomePostoAlvo = nomePostoAlvo;
        this.nomeVacinaAlvo = nomeVacinaAlvo;
        this.vacinaPosto = vacinasPosto;
    }

    public Vacina getVacinaPosto() {
        return vacinaPosto;
    }

    public void setVacinaPosto(Vacina vacinaPosto) {
        this.vacinaPosto = vacinaPosto;
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
}
