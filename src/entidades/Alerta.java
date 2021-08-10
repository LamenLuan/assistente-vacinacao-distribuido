/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import javafx.scene.control.Alert;

/**
 *
 * @author luanl
 */
public class Alerta {
    
    public static void mostraAlerta(String title, String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
    
    public static void mostraConfirmacao(String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Êxito!");
        alert.setHeaderText(header);
        alert.showAndWait();
    }
    
    public static void mostrarCampoInvalido(String header) {
        mostraAlerta("Campo inválido!", header);
    }
    
    public static void mostrarErroComunicacao() {
        Alerta.mostraAlerta(
            "Erro de comunicação",
            "Aplicação foi incapaz de se comunicar com servidor, "
            + "tente novamente mais tarde."
        );
    }
}
