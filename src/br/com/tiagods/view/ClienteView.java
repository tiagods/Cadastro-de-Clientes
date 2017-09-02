package br.com.tiagods.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Created by Tiago on 07/07/2017.
 */
public class ClienteView extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            URL url = getClass().getResource("Cliente.fxml");
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Controle de Clientes");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            //System.exit(0);
        }
    }
}
