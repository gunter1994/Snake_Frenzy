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
    GridPane grid = new GridPane();

    public void handleNewGame(ActionEvent event) {
        MainGame m = new MainGame();
    }

    public void customizeSnake(ActionEvent event) throws IOException {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Snake Customization");
        Parent root = FXMLLoader.load(getClass().getResource("Customize_Snake.fxml"));
        for (int i = 0; i < 5; i++) {
            RowConstraints row = new RowConstraints(20);
            grid.getRowConstraints().add(row);
        }
        Position p = new Position(0,0);
        Snake s = new Snake(custom, p);
        s.drawSnake(grid);
        BorderPane layout = new BorderPane();
        layout.setCenter(root);
        layout.setBottom(grid);
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
        Position p = new Position(0,0);
        Snake s = new Snake(this.custom, p);
        s.drawSnake(grid);
    }



}
