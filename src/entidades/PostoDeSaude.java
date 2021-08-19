/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.util.ArrayList;

/**
 *
 * @author Gabriel
 */
public class PostoDeSaude {
    private String nomePosto;
    private String endPosto;
    private ArrayList<Vacina> vacinasPosto;
    private ArrayList<DiaVacinacao> diasVacinacao;

    public PostoDeSaude(String nomePosto, String endPosto) {
        this.nomePosto = nomePosto;
        this.endPosto = endPosto;
        this.vacinasPosto = new ArrayList<>();
        this.diasVacinacao = new ArrayList<>();
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

    public ArrayList<DiaVacinacao> getDiasVacinacao() {
        return diasVacinacao;
    }

    public void setDiasVacinacao(ArrayList<DiaVacinacao> diasVacinacao) {
        this.diasVacinacao = diasVacinacao;
    }

    public ArrayList<Vacina> getVacinasPosto() {
        return vacinasPosto;
    }

    public void setVacinasPosto(ArrayList<Vacina> vacinasPosto) {
        this.vacinasPosto = vacinasPosto;
    }
    
    @Override
    public String toString(){
        return this.nomePosto;
    }
}
