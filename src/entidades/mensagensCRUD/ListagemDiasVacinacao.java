/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.mensagensCRUD;

import entidades.Dia;
import entidades.TipoMensagem;
import java.util.ArrayList;

/**
 *
 * @author a2057387
 */
public class ListagemDiasVacinacao extends MensagemCRUD {
    private String nomePosto;
    private ArrayList<Dia> diaPosto;

    public ListagemDiasVacinacao(String nomePosto, ArrayList<Dia> diaPosto) {
        super(TipoMensagem.LISTA_DIAS_RESPOSTA);
        this.nomePosto = nomePosto;
        this.diaPosto = diaPosto;
    }

    public String getNomePosto() {
        return nomePosto;
    }

    public void setNomePosto(String nomePosto) {
        this.nomePosto = nomePosto;
    }

    public ArrayList<Dia> getDiaPosto() {
        return diaPosto;
    }

    public void setDiaPosto(ArrayList<Dia> diaPosto) {
        this.diaPosto = diaPosto;
    }
}
