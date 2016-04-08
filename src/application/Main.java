package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static java.awt.SystemColor.text;

//creates the main menu window
public class Main extends Application {
    private static Stage primaryStage;
    private Stage selectGameStage;
    private static ComboBox<String> playerNum;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        VBox vBox = new VBox(15);
        vBox.setStyle("-fx-background-color: lightgreen");

        // snake picture for above the menu button
        Image img = new Image("CartoonSnake.png");
        ImageView imgview = new ImageView(img);
        imgview.setFitWidth(125); imgview.setFitHeight(125);
        imgview.setPreserveRatio(true);

        Text title = new Text("SNAKE");
        title.setFill(Color.GREEN);
        //set custom ttf or otf font
        title.setFont(Font.loadFont("file:resources/fonts/snake.ttf", 75));


        //Create buttons
        Button onePlayerBtn = new Button("Single Player");
        Button multiPlayerButton = new Button("Local Multiplayer");
        Button highScoresButton = new Button("High Scores");
        Button settingsButton = new Button("Settings");
        Button quitGame = new Button ("Quit");

        vBox.getChildren().addAll(title, imgview, onePlayerBtn,multiPlayerButton,highScoresButton,settingsButton,quitGame);
        vBox.setAlignment(Pos.CENTER);

        /*Passes either Single Player, Multiplayer or High Scores to the gameSelection
        method and performs the correct action */
        onePlayerBtn.setOnAction(e -> gameSelection(onePlayerBtn.getText()));
        multiPlayerButton.setOnAction(e -> gameSelection(multiPlayerButton.getText()));
        highScoresButton.addEventHandler(ActionEvent.ACTION, (e) -> {primaryStage.hide(); new HighScoresWindow();});
        settingsButton.setOnAction(e -> Settings());
        quitGame.setOnAction(e -> System.exit(0));

        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(vBox, 450, 500));
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
        gameModes.getItems().addAll("Classic Snake", "Speed Snake", "Timed Snake", "Obstacle Snake");

        //Create strings for the rules of each game mode
        String classicRules = "Use 'wasd' or the arrow keys to maneuver your snake and eat objects to collect points." +
                " As your snake eats objects your snake will grow in length. Make sure not to bump into the walls" +
                " or yourself or else it is game over!";

        String speedRules = "Use 'wasd' or the arrow keys to maneuver your snake and eat objects to collect points." +
                " Beware though! Your snake will move faster than normal, so make sure you can make those quick turns" +
                " without bumping into anything, or else it os game over!";

        String timedRules =  "";

        String obstaclerRules = "";

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-size: 15px");

        gameModes.setOnAction(new EventHandler<ActionEvent>() {
            String mode = "Classic Snake";
            @Override public void handle(ActionEvent e) {
                if(mode.equals("Classic Snake")) {
                    textArea.clear();
                    textArea.appendText(classicRules);
                }
                if(mode.equals("Speed Snake")) {
                    textArea.clear();
                    textArea.appendText(speedRules);
                }
                if(mode.equals("Timed Snake")) {

                }
                if(mode.equals("Obstacle Snake")) {

                }
            }
        });

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
        vBox2.getChildren().addAll(new Label("Game Overview"), textArea);
        textArea.setEditable(false);
        borderPane.setBottom(vBox2);

        Scene gameSelectionScene = new Scene(borderPane,350,300); //(width, height)
        selectGameStage.setTitle("Select Game");
        selectGameStage.setScene(gameSelectionScene);
        selectGameStage.show();
    }

    /*determines the next step after a game mode is chosen
    differs for single player multiplayer*/
    private void next(String option, ComboBox<String> playerNum) {
        selectGameStage.close();

        if(option.equals("Single Player")) {
            new GameSetup();
        }
        else if(option.equals("Local Multiplayer")) {
            String[] s = playerNum.getSelectionModel().getSelectedItem().toString().split(" ");
            int num = Integer.parseInt(s[0]);
        }
    }

    // returns to main menu
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