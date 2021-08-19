/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.util.ArrayList;

/**
 *
 * @author a2057387
 */
public class ListaVacinas {
    private String nomePosto;
    private ArrayList<Vacina> vacinasPosto;

    public ListaVacinas(String nomePosto, ArrayList<Vacina> vacinasPosto) {
        this.nomePosto = nomePosto;
        this.vacinasPosto = vacinasPosto;
    }

    public ListaVacinas() {
    }

    public ArrayList<Vacina> getVacinasPosto() {
        return vacinasPosto;
    }

    public void setVacinasPosto(ArrayList<Vacina> vacinasPosto) {
        this.vacinasPosto = vacinasPosto;
    }

    public String getNomePosto() {
        return nomePosto;
    }

    public void setNomePosto(String nomePosto) {
        this.nomePosto = nomePosto;
    }
}
