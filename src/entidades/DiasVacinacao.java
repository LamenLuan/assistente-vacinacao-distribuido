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
public class DiasVacinacao {
    private String dia;
    private ArrayList<Slot> slots;

    public DiasVacinacao(String dia) {
        this.dia = dia;
        this.slots = new ArrayList<>();
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public ArrayList<Slot> getSlots() {
        return slots;
    }

    public void setSlots(ArrayList<Slot> slots) {
        this.slots = slots;
    }

    @Override
    public String toString() {
        return dia; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
