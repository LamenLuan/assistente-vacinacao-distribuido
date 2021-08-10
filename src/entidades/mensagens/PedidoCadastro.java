/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.mensagens;

import entidades.TipoMensagem;
import entidades.Usuario;

/**
 *
 * @author luanl
 */
public class PedidoCadastro extends Mensagem {
    Usuario usuario;

    public PedidoCadastro(Usuario usuario) {
        super(TipoMensagem.PEDIDO_CADASTRO);
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
