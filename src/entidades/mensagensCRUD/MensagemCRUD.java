/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.mensagensCRUD;

import entidades.TipoMensagem;

/**
 *
 * @author luanl
 */
public class MensagemCRUD {
    private int id;
    
    public MensagemCRUD(TipoMensagem tipoMsg) {
        this.id = tipoMsg.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
