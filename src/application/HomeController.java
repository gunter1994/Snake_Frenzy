package application;

import javafx.animation.Timeline;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by samantha on 23/03/16.
 */
public class HomeController {

    @FXML
    ToggleGroup BodyColours, EyeColours, Patterns;

    public static String custom = "Blue/Blue/None";

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

    public void saveSnakeDesign(ActionEvent event) {
        RadioMenuItem body = (RadioMenuItem)BodyColours.getSelectedToggle(); //test
        String bodyS = body.getText();
        RadioMenuItem eye = (RadioMenuItem)EyeColours.getSelectedToggle(); //test
        String eyeS = eye.getText();
        RadioMenuItem pattern = (RadioMenuItem)Patterns.getSelectedToggle(); //test
        String patternS = pattern.getText();
        this.custom = bodyS + "/" + eyeS + "/" + patternS;
    }



}
