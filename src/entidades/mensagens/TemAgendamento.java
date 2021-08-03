/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.mensagens;

import entidades.Agendamento;
import entidades.TipoMensagem;

/**
 *
 * @author luanl
 */
public class TemAgendamento extends Mensagem {
    Agendamento agendamento;

    public TemAgendamento(Agendamento agendamento) {
        super(TipoMensagem.TEM_AGENDAMENTO);
        this.agendamento = agendamento;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }
}
