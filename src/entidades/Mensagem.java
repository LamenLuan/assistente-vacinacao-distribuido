/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.util.ArrayList;

/**
 *
 * @author luanl
 */
public class Mensagem {
    private int id;
    private Boolean agendamento = null, admin = null, segundaDose = null;
    private String cpf = null, senha = null, mensagem = null, data = null,
        nomePosto = null, endPosto = null, slot = null, vacina = null,
        ipUsuario = null;
    private Integer portaUsuario = null;
    private Usuario usuario = null;
    private ArrayList<PostoDeSaude> postosSaude = null;
    
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
    public Mensagem(
        String nomePosto, String endPosto, String data, String slot,
        String vacina, boolean segundaDose
    ) {
        this.id = TipoMensagem.TEM_AGENDAMENTO.getId();
        this.nomePosto = nomePosto;
        this.endPosto = endPosto;
        this.data = data;
        this.slot = slot;
        this.vacina = vacina;
        this.segundaDose = segundaDose;
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
    
    // Pedido agendamento
    public Mensagem(
        String cpf, String senha, String nomePosto, String data, String slot
    ) {
        this.id = TipoMensagem.PEDIDO_AGENDAMENTO.getId();
        this.cpf = cpf;
        this.senha = senha;
        this.nomePosto = nomePosto;
        this.data = data;
        this.slot = slot;
    }
    
    // Tem vacinas
    public Mensagem(ArrayList<PostoDeSaude> dadosAgendamento) {
        this.id = TipoMensagem.TEM_VACINAS.getId();
        this.postosSaude = dadosAgendamento;
    }
    
    // Pedido de abertura de chat
    public Mensagem(
        String cpf, String senha, String ipUsuario, int portaUsuario
    ) {
        this.id = TipoMensagem.PEDIDO_ABERTURA_CHAT.getId();
        this.cpf = cpf;
        this.senha = senha;
        this.ipUsuario = ipUsuario;
        this.portaUsuario = portaUsuario;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
    
    public boolean isSegundaDose() {
        return segundaDose;
    }
    
    public void setSegundaDose(boolean segundaDose) {
        this.segundaDose = segundaDose;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNomePosto() {
        return nomePosto;
    }

    public void setNomePosto(String nomePosto) {
        this.nomePosto = nomePosto;
    }

    public String getEndPosto() {
        return endPosto;
    }

    public void setEndPosto(String endPosto) {
        this.endPosto = endPosto;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getVacina() {
        return vacina;
    }

    public void setVacina(String vacina) {
        this.vacina = vacina;
    }

    public ArrayList<PostoDeSaude> getPostosSaude() {
        return postosSaude;
    }

    public void setPostosSaude(ArrayList<PostoDeSaude> postosSaude) {
        this.postosSaude = postosSaude;
    }
}
