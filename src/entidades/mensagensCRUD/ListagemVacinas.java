/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.mensagensCRUD;

import entidades.ListaVacinas;
import entidades.TipoMensagem;
import java.util.ArrayList;

/**
 *
 * @author a2057387
 */
public class ListagemVacinas extends MensagemCRUD {
    private ArrayList<ListaVacinas> vacinasCadastradas;

    public ListagemVacinas(ArrayList<ListaVacinas> vacinasCadastradas) {
        super(TipoMensagem.LISTA_VACINAS_RESPOSTA);
        this.vacinasCadastradas = vacinasCadastradas;
    }

    public ArrayList<ListaVacinas> getVacinasCadastradas() {
        return vacinasCadastradas;
    }

    public void setVacinasCadastradas(ArrayList<ListaVacinas> vacinasCadastradas) {
        this.vacinasCadastradas = vacinasCadastradas;
    }
}
