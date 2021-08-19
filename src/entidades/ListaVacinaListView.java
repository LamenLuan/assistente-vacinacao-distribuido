/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 *
 * @author a2057387
 */
public class ListaVacinaListView {
    private String nomePosto;
    private Vacina vacina;

    public ListaVacinaListView(String nomePosto, Vacina vacina) {
        this.nomePosto = nomePosto;
        this.vacina = vacina;
    }

    public Vacina getVacina() {
        return vacina;
    }

    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
    }

    public String getNomePosto() {
        return nomePosto;
    }

    public void setNomePosto(String nomePosto) {
        this.nomePosto = nomePosto;
    }
    
    @Override
    public String toString(){
        return "Nome do posto: " + this.nomePosto + " || Vacina cadastrada: "
                + this.vacina.getNomeVacina() + " || Necessita segunda dose: "
                + String.valueOf(this.vacina.isHasSegundaDose())
                + " || Quantidade disponível: "
                + Integer.toString(this.vacina.getQtdVacina());
    }
}
