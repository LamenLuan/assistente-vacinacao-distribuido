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
public class RemoveVacina extends MensagemCRUD {
    private String nomePostoAlvo;
    private String nomeVacinaAlvo;

    public RemoveVacina(String nomePostoAlvo, String nomeVacinaAlvo) {
        super(TipoMensagem.REMOVE_VACINA);
        this.nomePostoAlvo = nomePostoAlvo;
        this.nomeVacinaAlvo = nomeVacinaAlvo;
    }

    public String getNomeVacinaAlvo() {
        return nomeVacinaAlvo;
    }

    public void setNomeVacinaAlvo(String nomeVacinaAlvo) {
        this.nomeVacinaAlvo = nomeVacinaAlvo;
    }

    public String getNomePostoAlvo() {
        return nomePostoAlvo;
    }

    public void setNomePostoAlvo(String nomePostoAlvo) {
        this.nomePostoAlvo = nomePostoAlvo;
    }
    
}
