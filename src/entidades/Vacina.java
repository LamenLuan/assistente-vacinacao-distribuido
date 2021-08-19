/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 *
 * @author Gabriel
 */
public class Vacina {
    private String nomeVacina;
    private int qtdVacina;
    private boolean hasSegundaDose;
    
    public Vacina(String nomeVacina,int qtdVacina, boolean hasSegundaDose){
        this.nomeVacina = nomeVacina;
        this.qtdVacina = qtdVacina;
        this.hasSegundaDose = hasSegundaDose;
    }

    public String getNomeVacina() {
        return nomeVacina;
    }

    public void setNomeVacina(String nomeVacina) {
        this.nomeVacina = nomeVacina;
    }

    public int getQtdVacina() {
        return qtdVacina;
    }

    public void setQtdVacina(int qtdVacina) {
        this.qtdVacina = qtdVacina;
    }
    
    public boolean isHasSegundaDose() {
        return hasSegundaDose;
    }

    public void setHasSegundaDose(boolean hasSegundaDose) {
        this.hasSegundaDose = hasSegundaDose;
    }
}
