package application;

import javafx.application.Application;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        GridPane gridPane = new GridPane();
        VBox vBox = new VBox(15);

        //Create buttons
        Button btn1 = new Button("Single Player"); Button btn2 = new Button("Local Multiplayer");
        Button btn3 = new Button("High Scores"); Button btn4 = new Button("Settings");
        vBox.getChildren().addAll(btn1,btn2,btn3,btn4);

        //add and position buttons
        gridPane.getChildren().add(vBox);
        gridPane.setAlignment(Pos.CENTER);

        /*Passes either Single Player, Multiplayer or High Scores to the gameSelection
        method and performs the correct action */
        btn1.setOnAction(e -> gameSelection(btn1.getText()));
        btn2.setOnAction(e -> gameSelection(btn2.getText()));
        btn3.setOnAction(e -> gameSelection(btn3.getText()));
        btn4.setOnAction(e -> Settings());

        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(gridPane, 350, 350));
        primaryStage.show();

    }

    /*determines the next step after a game mode is chosen
    differs for single player multiplayer and high scores*/
    public void next(String option) {

        if(option.equals("Single Player")) {
            GameSetup.preGameLobby();
        }
        if(option.equals("Multiplayer")) {
            GameSetup.preGameLobby();
        }
        if(option.equals("High Scores")) {}
    }

    public void back(){

    }

    //game selection for single player, multiplayer and high scores
    public void gameSelection(String option) {
        Stage primaryStage = new Stage();
        BorderPane borderPane = new BorderPane();
        Button back = new Button("back"); Button next = new Button("next");
        back.setOnAction(e -> primaryStage.close());

        next.setOnAction(e -> next(option));

        ComboBox<String> comboBox = new ComboBox();
        comboBox.setPromptText("Choose a Game Mode");
        comboBox.getItems().addAll("Classic", "Another Mode");

        VBox vBox1 = new VBox(), vBox2 = new VBox(); HBox hBox1 = new HBox(), hbox2 = new HBox();
        hBox1.getChildren().addAll(back, next); hbox2.getChildren().add(comboBox);
        vBox1.getChildren().addAll(hBox1, hbox2);

        hBox1.setAlignment(Pos.CENTER);
        hBox1.setSpacing(15); //set space between buttons
        hBox1.setPadding(new Insets(75,0,15,0)); //Insets(top,right,bottom,left)
        hbox2.setAlignment(Pos.CENTER);

        borderPane.setCenter(vBox1);
        TextArea textArea = new TextArea();
        vBox2.getChildren().addAll(new Label("Game Overview"), textArea);
        textArea.setEditable(false);
        borderPane.setBottom(vBox2);

        Scene gameSelection = new Scene(borderPane,400,350); //(width, height)
        primaryStage.setScene(gameSelection);
        primaryStage.show();
    }

    public void Settings(){}

    public static void main(String[] args) {
        launch(args);
    }
}
