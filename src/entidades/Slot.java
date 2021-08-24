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
public class Slot {
    private String slotVacinacao;
    private int qtdSlotVacinacao;

    public Slot(String periodo, int qtddVacinacao) {
        this.slotVacinacao = periodo;
        this.qtdSlotVacinacao = qtddVacinacao;
    }

    public String getSlotVacinacao() {
        return slotVacinacao;
    }

    public void setSlotVacinacao(String slotVacinacao) {
        this.slotVacinacao = slotVacinacao;
    }

    public int getQtdSlotVacinacao() {
        return qtdSlotVacinacao;
    }

    public void setQtdSlotVacinacao(int qtdSlotVacinacao) {
        this.qtdSlotVacinacao = qtdSlotVacinacao;
    }

    @Override
    public String toString() {
        return slotVacinacao + " || Quantidade de vacinas realizadas no Slot: "
                + qtdSlotVacinacao;
    }
}
