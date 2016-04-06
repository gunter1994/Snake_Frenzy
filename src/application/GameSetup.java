package application;

import javafx.animation.Timeline;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sun.awt.image.GifImageDecoder;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by samantha on 23/03/16.
 */
public class GameSetup {

    GridPane grid = new GridPane();
    Stage stage;

    public static String custom = "None/Green";

    private static void handleNewGame() {
        MainGame m = new MainGame();
        try{
            Stage primaryStage = new Stage();
            m.start(primaryStage);
        }catch(Exception e){System.err.println();}
    }

    //window for choosing name and snake design before starting game
    public static void preGameLobby() {
        Stage primaryStage = new Stage();
        GridPane grid = new GridPane();
        BorderPane borderPane = new BorderPane();
        VBox vBox1 = new VBox(), vBox2 = new VBox();
        vBox1.setSpacing(20);

        TextField textField = new TextField();
        textField.setPromptText("Enter Player Name");
        textField.setMaxSize(100,10);

        ComboBox<String> colours = new ComboBox<>();
        colours.getItems().addAll("Blue","Red", "Yellow", "Green", "Orange", "Purple");
        ComboBox<String> patterns = new ComboBox<>();
        patterns.getItems().addAll("None","Spotted","Striped");

        colours.setOnAction(e -> System.out.println(patterns.getSelectionModel().getSelectedItem() + "/" + colours.getSelectionModel().getSelectedItem()));
        patterns.setOnAction(e -> custom = patterns.getSelectionModel().getSelectedItem() + "/" + colours.getSelectionModel().getSelectedItem());

        vBox1.getChildren().addAll(textField,colours,patterns);
        vBox1.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox1);

        Button start = new Button("Start Game");
        start.setOnAction(e -> handleNewGame());

        vBox2.getChildren().addAll(snakePreview(grid), start);
        vBox2.setAlignment(Pos.CENTER);
        borderPane.setBottom(vBox2);

        Scene pregameLobby = new Scene(borderPane,400,350); //(width, height)
        primaryStage.setScene(pregameLobby);
        primaryStage.show();

    }

    public static GridPane snakePreview(GridPane grid) {

        Button start = new Button("Start Game");

        for (int i = 0; i < 5; i++) {
            RowConstraints row = new RowConstraints(20);
            grid.getRowConstraints().add(row);
        }
        //grid.getChildren().add(start);
        grid.setAlignment(Pos.CENTER);
        Position p = new Position(0,0);
        Snake s = new Snake(custom, p);
        s.drawSnake(grid);
        return grid;
    }

}
