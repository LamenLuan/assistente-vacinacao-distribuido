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
public class LoginAprovado extends Mensagem {
    private boolean agendamento, admin;

    public LoginAprovado(boolean agendamento, boolean admin) {
        super( TipoMensagem.LOGIN_APROVADO.getId() );
        this.agendamento = agendamento;
        this.admin = admin;
    }

    public boolean isAgendamento() {
        return agendamento;
    }

    public void setAgendamento(boolean agendamento) {
        this.agendamento = agendamento;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
