/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.mensagensCRUD;

import entidades.ListaPosto;
import entidades.TipoMensagem;
import java.util.ArrayList;

/**
 *
 * @author a2057387
 */
public class ListagemPostos extends MensagemCRUD {
    private ArrayList<ListaPosto> postosCadastrados;

    public ListagemPostos(ArrayList<ListaPosto> postosCadastrados) {
        super(TipoMensagem.LISTA_POSTOS_NOMES_RESPOSTA);
        this.postosCadastrados = postosCadastrados;
    }

    public ArrayList<ListaPosto> getPostosCadastrados() {
        return postosCadastrados;
    }

    public void setPostosCadastrados(ArrayList<ListaPosto> postosCadastrados) {
        this.postosCadastrados = postosCadastrados;
    }
    
    
}
