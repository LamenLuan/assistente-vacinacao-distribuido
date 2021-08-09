/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.mensagens;

import entidades.TipoMensagem;
import entidades.Usuario;

/**
 *
 * @author luanl
 */
public class LoginAprovado extends Mensagem {
    private boolean agendamento, admin;

    public LoginAprovado(Usuario usuario) {
        super(TipoMensagem.LOGIN_APROVADO);
        this.agendamento = (usuario.getAgendamento() != null);
        this.admin = usuario.isAdmin();
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
