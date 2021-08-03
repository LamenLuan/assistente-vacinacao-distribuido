/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.mensagens;

import entidades.TipoMensagem;

/**
 *
 * @author luanl
 */
public class Mensagem {

    protected int id;

    public Mensagem(TipoMensagem mensagem) {
        this.id = mensagem.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
