/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import entidades.Agendamento;
import entidades.TipoMensagem;
import entidades.Usuario;

/**
 *
 * @author luanl
 */
public class Mensagem {
    private int id;
    private Boolean agendamento = null, admin = null;
    private String cpf = null, senha = null, mensagem = null;
    private Agendamento dadosAgendamento = null;
    private Usuario usuario = null;
    
    public Mensagem(TipoMensagem tipoMsg) {
        this.id = tipoMsg.getId();
    }
    
    // Pedido de login ou Pedido de dados para agendamento
    public Mensagem(TipoMensagem tipoMensagem, String cpf, String senha) {
        this.id = tipoMensagem.getId();
        this.cpf = cpf;
        this.senha = senha;
    }
    
    // Login aprovado
    public Mensagem(boolean agendamento, boolean admin) {
        this.id = TipoMensagem.LOGIN_APROVADO.getId();
        this.agendamento = agendamento;
        this.admin = admin;
    }
    
    // Tem agendamento
    public Mensagem(Agendamento dadosAgendamento) {
        this.id = TipoMensagem.TEM_AGENDAMENTO.getId();
        this.dadosAgendamento = dadosAgendamento;
    }
    
    // Pedido de cadastro
    public Mensagem(Usuario usuario) {
        this.id = TipoMensagem.PEDIDO_CADASTRO.getId();
        this.usuario = usuario;
    }
    
    // Erro ou sucesso generico
    public Mensagem(TipoMensagem tipoMsg, String mensagem) {
        this.id = tipoMsg.getId();
        this.mensagem = mensagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
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
    
    public Agendamento getDadosAgendamento() {
        return dadosAgendamento;
    }

    public void setDadosAgendamento(Agendamento dadosAgendamento) {
        this.dadosAgendamento = dadosAgendamento;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
