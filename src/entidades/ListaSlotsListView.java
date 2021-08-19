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
public class ListaSlotsListView {
    private String nomePosto;
    private String data;
    private Slot slot;

    public ListaSlotsListView(String nomePosto, String data, Slot slot) {
        this.nomePosto = nomePosto;
        this.data = data;
        this.slot = slot;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public String getNomePosto() {
        return nomePosto;
    }

    public void setNomePosto(String nomePosto) {
        this.nomePosto = nomePosto;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Nome do Posto: " + nomePosto + " || Data: " + data + " Slot: "
                + slot.toString();
    }
    
    
}
