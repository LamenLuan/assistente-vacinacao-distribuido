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
public class DiaVacinacao {
    private String dia;
    private ArrayList<Slot> slots;

    public DiaVacinacao(String dia, ArrayList<Slot> slots) {
        this.dia = dia;
        this.slots = slots;
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
        return this.dia;
    }
    
}
