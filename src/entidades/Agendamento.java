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
public class Agendamento {
    private String nomePosto, endPosto, data, slot, vacina;
    private boolean segundaDose;

    public Agendamento(
        String nomePosto, String endPosto, String data, String slot, String vacina,
        boolean segundaDose
    ) {
        this.nomePosto = nomePosto;
        this.endPosto = endPosto;
        this.data = data;
        this.slot = slot;
        this.vacina = vacina;
        this.segundaDose = segundaDose;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public boolean isSegundaDose() {
        return segundaDose;
    }

    public void setSegundaDose(boolean segundaDose) {
        this.segundaDose = segundaDose;
    }
}
