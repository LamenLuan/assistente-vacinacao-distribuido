package aplicativos;

import entidades.MensageiroCliente;
import java.io.IOException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author luanl
 */
public class Cliente extends Application {
    
    Scene scene;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent parent = FXMLLoader.load( 
            getClass().getClassLoader().getResource("telas/TelaLogin.fxml") 
        );
        
        scene = new Scene(parent);
        
        primaryStage.setTitle("Assistente de Vacinação - Acesso ao sistema");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("IP:");
//        MensageiroCliente.ip = scanner.nextLine();
//        System.out.println("Porta:");
//        MensageiroCliente.porta = scanner.nextInt();
        
        launch(args);
    }
}
