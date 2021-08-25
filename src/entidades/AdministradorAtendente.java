/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author luanl
 */
public class AdministradorAtendente {
    private Usuario usuario;
    private Socket clientAdm, clientUser;
    private BufferedReader inboundAdm, inboundUser;
    private PrintWriter outboundAdm, outboundUser;
    private boolean disponivel;

    public AdministradorAtendente(
        Usuario usuario, Socket clientListener, BufferedReader inboundListener,
        PrintWriter outboundListener
    ) {
        this.usuario = usuario;
        this.clientAdm = clientListener;
        this.inboundAdm = inboundListener;
        this.outboundAdm = outboundListener;
        this.disponivel = false;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Socket getClientAdm() {
        return clientAdm;
    }

    public void setClientAdm(Socket clientAdm) {
        this.clientAdm = clientAdm;
    }

    public BufferedReader getInboundAdm() {
        return inboundAdm;
    }

    public void setInboundAdm(BufferedReader inboundAdm) {
        this.inboundAdm = inboundAdm;
    }

    public PrintWriter getOutboundAdm() {
        return outboundAdm;
    }

    public void setOutboundAdm(PrintWriter outboundAdm) {
        this.outboundAdm = outboundAdm;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Socket getClientUser() {
        return clientUser;
    }

    public void setClientUser(Socket clientUser) {
        this.clientUser = clientUser;
    }

    public BufferedReader getInboundUser() {
        return inboundUser;
    }

    public void setInboundUser(BufferedReader inboundUser) {
        this.inboundUser = inboundUser;
    }

    public PrintWriter getOutboundUser() {
        return outboundUser;
    }

    public void setOutboundUser(PrintWriter outboundUser) {
        this.outboundUser = outboundUser;
    }
    
    public void fechaSocketEDutos() {
        try {
            Mensageiro.fechaSocketEDutos(clientAdm, outboundAdm, inboundAdm
            );
            clientAdm = null;
            outboundAdm = null;
            inboundAdm = null;
        } catch (IOException e) {
            System.err.println( e.getMessage() );
        }
    }
}
