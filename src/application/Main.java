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

//creates the main menu window
public class Main extends Application {
    public static Stage primaryStage;
    private Stage selectGameStage;
    private static ComboBox<String> playerNum;
    private static Audio music;
    private static boolean muted;

    @Override
    public void start(Stage primaryStage) throws Exception{

        // makes new server thread to run along with game
        Thread scoreServer = new Thread(new scoreServer(8080));
        scoreServer.start();

        // stops server when game ends
        primaryStage.setOnCloseRequest(e -> application.scoreServer.stopServer());

        this.primaryStage = primaryStage;
        VBox vBox = new VBox(15);
        vBox.setStyle("-fx-background-color: lightgreen");

        //sets up music
        startMusic();

        // snake picture for above the menu button
        Image img = new Image("CartoonSnake.png");
        ImageView imgView = new ImageView(img);
        imgView.setFitWidth(125); imgView.setFitHeight(125);
        imgView.setPreserveRatio(true);

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

        vBox.getChildren().addAll(title, imgView, onePlayerBtn,multiPlayerButton,highScoresButton,settingsButton,quitGame);
        vBox.setAlignment(Pos.CENTER);

        //Sets up the action for each button press in the main window
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
        gameModes.getItems().addAll("Classic Snake", "Speed Snake", "Timed Snake");

        //Create strings for the rules of each game mode
        String classicRules = "Use 'wasd' or the arrow keys to maneuver your snake and eat food to collect points." +
                " ESC button is used to pause. As your snake eats food your snake will grow in length. You have as" +
                " much time as you want to make your way over to food. Make sure not to bump into the walls or " +
                "yourself or else it is game over!";

        String speedRules = "Using the same controls and mechanics as Classic Snake, maneuver your snake to eat " +
                "food, grow and collect points. Again, you have as much time as you want to get to the food. " +
                "Beware though! Your snake will move faster than normal, so make sure you can make those quick turns " +
                "without bumping into anything, or else it is game over!";

        String timedRules = "Using the same controls and mechanics as Classic Snake, maneuver your snake to eat " +
                "food, grow and and collect points. For those looking for an extra challenge, these games are now " +
                "timed. You have 2 minutes to collect as many points as you can. Make sure you get to the food on time " +
                "though, because they won't stay there forever!";

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-size: 14px");

        gameModes.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String mode = gameModes.getValue();
                if(mode.equals("Classic Snake")) {
                    textArea.clear();
                    textArea.appendText(classicRules);
                }else if(mode.equals("Speed Snake")) {
                    textArea.clear();
                    textArea.appendText(speedRules);
                }else if(mode.equals("Timed Snake")) {
                    textArea.clear();
                    textArea.appendText(timedRules);
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
            new GameSetup(music);
        }
        else if(option.equals("Local Multiplayer")) {
            String[] s = playerNum.getSelectionModel().getSelectedItem().split(" ");
            int num = Integer.parseInt(s[0]);
            new MultiSetup(num, music);
        }
    }

    // returns to main menu
    private void back() {
        selectGameStage.close();
        showMainMenu();
    }

    private void Settings() {

        Stage settings = new Stage();
        Button mute;
        if (!getMuted()) {
            mute = new Button("Mute Music");
            muted = false;
        } else {
            mute = new Button("UnMute Music");
            muted = true;
        }

        mute.setOnAction(e-> {
            music.muteSong();
            if (muted) {
                muted = false;
                mute.setText("Mute Music");
            }
            else {
                muted = true;
                mute.setText("UnMute Music");
            }
        });
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(mute);

        settings.setScene(new Scene(vBox, 300,300));
        settings.show();

    }

    static boolean getMuted() {
        return muted;
    }

    static Audio getAudio(){
        return music;
    }

    static void showMainMenu() {
        primaryStage.show();
        music = new Audio();
        music.startGame();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void startMusic() {
        music = new Audio();
        music.startGame();
    }
}