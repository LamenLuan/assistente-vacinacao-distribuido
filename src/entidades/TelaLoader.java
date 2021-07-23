/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author luanl
 */
public class TelaLoader {
    public static void Load(Object obj, Pane root, String path, String title) {
        Parent parent;
        try {
            parent = FXMLLoader.load( obj.getClass().getResource(path) );
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setTitle(title);
            stage.setScene( new Scene(parent) );
        } catch (IOException | NullPointerException ex) {
            Alerta.mostraAlerta(
                "Erro no carregamento", "A página não pode ser carregada"
            );
        }
    }
}
