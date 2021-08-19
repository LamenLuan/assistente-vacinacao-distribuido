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

    public UpdateDia(String nomePostoAlvo, String dataAntiga, String novaData) {
        super(TipoMensagem.UPDATE_DIA);
        this.nomePostoAlvo = nomePostoAlvo;
        this.dataAntiga = dataAntiga;
        this.novaData = novaData;
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
    
}
