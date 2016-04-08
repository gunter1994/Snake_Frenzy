package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

//creates the main menu window
public class Main extends Application {
    private static Stage primaryStage;
    private Stage selectGameStage;
    private static ComboBox<String> playerNum;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        VBox vBox = new VBox(15);
        vBox.setStyle("-fx-background-color: lightgray");

        // snake picture for above the menu button
        Image img = new Image("CartoonSnake.png");
        ImageView iv = new ImageView(img);
        iv.setFitWidth(125); iv.setFitHeight(125);
        iv.setPreserveRatio(true);


        //Create buttons
        Button btn1 = new Button("Single Player");
        Button btn2 = new Button("Local Multiplayer");
        Button btn3 = new Button("High Scores");
        Button btn4 = new Button("Settings");
        vBox.getChildren().addAll(iv, btn1,btn2,btn3,btn4);
        vBox.setAlignment(Pos.CENTER);

        /*Passes either Single Player, Multiplayer or High Scores to the gameSelection
        method and performs the correct action */
        btn1.setOnAction(e -> gameSelection(btn1.getText()));
        btn2.setOnAction(e -> gameSelection(btn2.getText()));
        btn3.addEventHandler(ActionEvent.ACTION, (e) -> {primaryStage.hide(); new HighScoresWindow();});
        btn4.setOnAction(e -> Settings());

        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(vBox, 350, 350));
        primaryStage.show();
    }

    //game selection window for single player, multiplayer and high scores
    private void gameSelection(String option) {
        primaryStage.hide();
        selectGameStage = new Stage();

        // creates back and next buttons
        BorderPane borderPane = new BorderPane();
        Button back = new Button("Back");
        back.setOnAction(e -> back());
        Button next = new Button("Next");
        next.setOnAction(e -> next(option, playerNum));

        ComboBox<String> gameModes = new ComboBox();
        gameModes.setPromptText("Game Mode");
        gameModes.getItems().addAll("Classic", "Another Mode");

        VBox vBox1 = new VBox(), vBox2 = new VBox(); HBox hBox1 = new HBox(), hBox2 = new HBox();
        hBox1.getChildren().addAll(back, next); hBox2.getChildren().add(gameModes);

        if (option.equals("Local Multiplayer")) {

            // adds box for number of players
            playerNum = new ComboBox<>();
            playerNum.setValue("2 Player");
            playerNum.getItems().addAll("2 Player", "3 Player", "4 Player");
            HBox hBox3 = new HBox();
            hBox3.getChildren().add(playerNum);
            hBox3.setPadding(new Insets(15,0,0,0));
            hBox3.setAlignment(Pos.CENTER);
            vBox1.getChildren().addAll(hBox1, hBox2, hBox3);
        }
        else { vBox1.getChildren().addAll(hBox1, hBox2); }

        hBox1.setAlignment(Pos.CENTER);
        hBox1.setSpacing(15); //set space between buttons
        hBox1.setPadding(new Insets(50,0,15,0)); //Insets(top,right,bottom,left)
        hBox2.setAlignment(Pos.CENTER);

        borderPane.setCenter(vBox1);
        TextArea textArea = new TextArea();
        vBox2.getChildren().addAll(new Label("Game Overview"), textArea);
        textArea.setEditable(false);
        borderPane.setBottom(vBox2);

        Scene gameSelectionScene = new Scene(borderPane,400,350); //(width, height)
        selectGameStage.setTitle("Select Game");
        selectGameStage.setScene(gameSelectionScene);
        selectGameStage.show();
    }

    /*determines the next step after a game mode is chosen
    differs for single player multiplayer and high scores*/
    private void next(String option, ComboBox<String> playerNum) {
        selectGameStage.close();

        if(option.equals("Single Player")) {
            Stage setupStage = new Stage();
            BorderPane layout = new BorderPane();
            Scene scene = new Scene(layout, 350, 200);
            GameSetup window = new GameSetup(scene);
            layout.getChildren().add(window.getRoot());

            setupStage.setTitle("Game Setup");
            setupStage.setScene(scene);
            setupStage.show();
        }
        else if(option.equals("Local Multiplayer")) {
            String[] s = playerNum.getSelectionModel().getSelectedItem().toString().split(" ");
            int num = Integer.parseInt(s[0]);
        }
    }

    private void back(){
        selectGameStage.close();
        showMainMenu();
    }

    private void Settings() {}

    public static void showMainMenu() {primaryStage.show();}

    public static void main(String[] args) {
        launch(args);
    }
}