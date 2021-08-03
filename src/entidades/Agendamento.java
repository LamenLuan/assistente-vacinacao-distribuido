/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 *
 * @author luanl
 */
public class Agendamento {
    private String local, data, hora, vacina;
    private boolean segundaDose;

    public Agendamento(String local, String data, String hora, String vacina, boolean segundaDose) {
        this.local = local;
        this.data = data;
        this.hora = hora;
        this.vacina = vacina;
        this.segundaDose = segundaDose;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getVacina() {
        return vacina;
    }

    public void setVacina(String vacina) {
        this.vacina = vacina;
    }

    public boolean isSegundaDose() {
        return segundaDose;
    }

    public void setSegundaDose(boolean segundaDose) {
        this.segundaDose = segundaDose;
    }
}
