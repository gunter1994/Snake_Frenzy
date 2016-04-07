package application;

import javafx.animation.Timeline;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by samantha on 23/03/16.
 */
public class HomeController {

    @FXML
    ToggleGroup BodyColours, EyeColours, Patterns;

    public static String custom = "Blue/Blue/None";
    Group g = new Group();

    public void handleNewGame(ActionEvent event) {
        Player player1, player2, player3, player4;
        player1 = new Player("test1");
        player2 = new Player("test2");
        player3 = new Player("test3");
        player4 = new Player("test4");
        MultiGame m = new MultiGame(player1,player2,player3,player4);
    }

    public void customizeSnake(ActionEvent event) throws IOException {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Snake Customization");
        Parent root = FXMLLoader.load(getClass().getResource("Customize_Snake.fxml"));
        Snake s = new Snake(custom, 0, 0);
        s.drawSnake(g);
        BorderPane layout = new BorderPane();
        layout.setCenter(root);
        layout.setBottom(g);
        Scene customizeSnake = new Scene(layout,300,500);
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
        g.getChildren().removeAll();
        Snake s = new Snake(this.custom, 0, 0);
        s.drawSnake(g);
    }



}
