    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;
/**
 *
 * @author luanl
 */
public class Usuario {
    private String nome, cpf, dataNascimento, telefone, email, senha;
    private boolean genero, comorbidade, admin;
    private Agendamento agendamento;

    public Usuario(
        String nome, String cpf, String dataNascimento, String telefone,
        String email, String senha, boolean masculino, boolean comorbidade,
        boolean admin, Agendamento agendamento
    ) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
        this.genero = masculino;
        this.comorbidade = comorbidade;
        this.admin = admin;
        this.agendamento = agendamento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isGenero() {
        return genero;
    }

    public void setGenero(boolean genero) {
        this.genero = genero;
    }

    public boolean isComorbidade() {
        return comorbidade;
    }

    public void setComorbidade(boolean comorbidade) {
        this.comorbidade = comorbidade;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }
    
    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }
}
