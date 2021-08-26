/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.mensagensCRUD;

import entidades.DiasVacinacao;
import entidades.TipoMensagem;
import java.util.ArrayList;

/**
 *
 * @author a2057387
 */
public class ListagemSlots extends MensagemCRUD {
    private String nomePosto;
    private ArrayList<DiasVacinacao> diaPosto;

    public ListagemSlots(String nomePosto, ArrayList<DiasVacinacao> diaPosto) {
        super(TipoMensagem.LISTA_SLOTS_RESPOSTA);
        this.nomePosto = nomePosto;
        this.diaPosto = diaPosto;
    }

    public ArrayList<DiasVacinacao> getDiaPosto() {
        return diaPosto;
    }

    public void setDiaPosto(ArrayList<DiasVacinacao> diaPosto) {
        this.diaPosto = diaPosto;
    }

    public String getNomePosto() {
        return nomePosto;
    }

    public void setNomePosto(String nomePosto) {
        this.nomePosto = nomePosto;
    }
}
