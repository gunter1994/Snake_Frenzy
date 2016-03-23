package application;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by samantha on 23/03/16.
 */
public class HomeController {

    public void handleNewGame(ActionEvent event) {
        MainGame g = new MainGame();
        try{
            Stage primaryStage = new Stage();
            g.start(primaryStage);
        }catch(Exception e){System.err.println();}
    }

    public void customizeSnake(ActionEvent event) throws IOException {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Snake Customization");
        Parent root = FXMLLoader.load(getClass().getResource("Customize_Snake.fxml"));
        Scene customizeSnake = new Scene(root,300,300);
        primaryStage.setScene(customizeSnake);
        primaryStage.show();




    }



}
