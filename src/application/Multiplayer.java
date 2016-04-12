package application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.util.Random;

public class Multiplayer {
    Stage primaryStage;
    Scene scene;
    GamePlayer[] players;
    int numAlive;
    Audio audio;

    public Multiplayer(Player[] ps, Audio a) {
        //make basic stage
        audio = a;
        primaryStage = new Stage();
        HBox hbox = new HBox();
        VBox v1 = new VBox();
        VBox v2 = new VBox();

        players = new GamePlayer[ps.length];

        scene = new Scene(hbox, Color.WHITE);

        //set controls for players, and add players to their in game counterparts
        KeyCode[] keys1 = {KeyCode.D, KeyCode.S, KeyCode.A, KeyCode.W};
        players[0] = new GamePlayer(keys1, ps[0]);
        KeyCode[] keys2 = {KeyCode.RIGHT, KeyCode.DOWN, KeyCode.LEFT, KeyCode.UP};
        players[1] = new GamePlayer(keys2, ps[1]);

        if (ps.length > 2) {
            KeyCode[] keys3 = {KeyCode.NUMPAD6, KeyCode.NUMPAD5, KeyCode.NUMPAD4, KeyCode.NUMPAD8};
            players[2] = new GamePlayer(keys3, ps[2]);
        }
        if (ps.length > 3) {
            KeyCode[] keys4 = {KeyCode.L, KeyCode.K, KeyCode.J, KeyCode.I};
            players[3] = new GamePlayer(keys4, ps[3]);
        }

        //setup all players displays
        VBox[] vBoxes = new VBox[ps.length];

        for (int k = 0; k < vBoxes.length; k++) {
            vBoxes[k] = new VBox();
            vBoxes[k].setStyle("-fx-border-color: black;");
            vBoxes[k].getChildren().addAll(players[k].getRoot());
        }

        v1.getChildren().add(vBoxes[0]);
        v2.getChildren().add(vBoxes[1]);
        if (players.length > 2) {
            v1.getChildren().add(vBoxes[2]);
        }
        if (players.length > 3){
            v2.getChildren().add(vBoxes[3]);
        }
        hbox.getChildren().addAll(v1,v2);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Snake Frenzy");
        primaryStage.show();

        //makes program an fx application thread
        for (int h = 0; h < players.length; h++) {
            Platform.runLater(players[h]);
        }

        numAlive = players.length;
    }

    public void doneCheck() {
        numAlive--;
        //if less than 2 players are alive, it ends the game
        if (numAlive == 0) {
            Player[] winners = new Player[4];
            for (int k = 0; k < 4; k++) {
                winners[k] = new Player();
            }
            int topScore = -1;
            int current;//first find the top score
            for (int i = 0; i < players.length; i++) {
                current = players[i].getScore();
                if (current > topScore) {
                    topScore = current;
                }
            }

            int j = 0; //any players equal to the top score have won
            for (int h = 0; h < players.length; h++) {
                current = players[h].getScore();
                if (current == topScore) {
                    topScore = current;
                    winners[j] = players[h].getPlayer();
                    j++;
                }
            }
            //prints the winner
            String winner = "";
            for (int l = 0; l < j; l++) {
                winner += winners[l].getUsername() + " ";
                if ((j - l) != 1) {
                    winner += "and ";
                }
                winners[l].win();
            }

            Text winText; //decide if it was a tie or clear winner
            if (j > 1) {
                winText = new Text(winner + "tie!");
            }
            else {
                winText = new Text(winner + "wins!");
            }
            Stage stage = new Stage();
            stage.setTitle("Game Over");

            Player[] returningPlayers = new Player[players.length];
            for (int k = 0; k < players.length; k++){
                returningPlayers[k] = players[k].getPlayer();
            }

            Button play = new Button("Play Again");
            play.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                    primaryStage.close();
                    new Multiplayer(returningPlayers, audio);
                }
            });

            Button mainMenu = new Button("Main Menu");
            mainMenu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                    primaryStage.close();
                    audio.changeSong("loop.mp3");
                    Main.showMainMenu();
                }
            });

            Button quitGame = new Button("Quit");
            quitGame.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.exit(0);
                }
            });

            HBox layout = new HBox(15);
            layout.setStyle("-fx-background-color: white; -fx-padding: 20;");
            layout.getChildren().addAll(winText, play, mainMenu, quitGame);
            stage.setScene(new Scene(layout));
            stage.show();
        }
    }

    private class GamePlayer extends Thread {
        private Group root;
        private Timeline timeline;
        private Snake s;
        private int dir;
        private int storedDir;
        private Food food;
        private boolean moved;
        private int score;
        private Text scoreText;
        private Player player;
        private Rectangle rect;

        public GamePlayer(KeyCode[] keys, Player p){
            //sets up all parameters for game
            timeline = new Timeline();
            moved = false;
            score = 0;
            //sets up the players gameScreen
            root = new Group();
            rect = new Rectangle(0, 0, 820, 420);
            rect.setFill(Color.WHITE);
            Rectangle wall = new Rectangle(-20, -20, 860,460); //so that the snake can move off screen when it dies
            wall.setFill(Color.DARKGRAY);
            scoreText = new Text(-5, -5, "Score: " + score);
            player = p;
            Text wins = new Text(725, -5, "Wins: " + player.getWins());
            root.getChildren().addAll(wall, rect, scoreText, wins);

            //adds keyboard controls
            scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if(key.getCode()==keys[0]) {
                    if ((dir == 3 || dir == 1) && !moved) {
                        dir = 0;
                        moved = true;
                    }
                    else if (dir != 2 && moved)
                    {
                        storedDir = 0;
                    }
                }
            });

            scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if(key.getCode()==keys[1]) {
                    if ((dir == 0 || dir == 2) && !moved) {
                        dir = 1;
                        moved = true;
                    }
                    else if (dir != 3 && moved)
                    {
                        storedDir = 1;
                    }
                }
            });

            scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if(key.getCode()==keys[2]) {
                    if ((dir == 3 || dir == 1) && !moved) {
                        dir = 2;
                        moved = true;
                    }
                    else if (dir != 0 && moved){
                        storedDir = 2;
                    }
                }
            });

            scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if(key.getCode()==keys[3]) {
                    if ((dir == 0 || dir == 2) && !moved) {
                        dir = 3;
                        moved = true;
                    }
                    else if (dir != 1 && moved){
                        storedDir = 3;
                    }
                }
            });
        }

        public Group getRoot() {
            return root;
        }

        public Text getScoreText() {
            return scoreText;
        }

        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player p) {
            player = p;
        }

        public Timeline getTimeline() {
            return timeline;
        }

        public int getScore() {
            return score;
        }

        public void run() {
            //creates snake and sets up score/directions
            dir = 0;
            storedDir = 4;
            score = 0;
            s = new Snake(player.getCustom(), 17, 10);
            s.drawSnake(root);
            food = new Food(s, root);

            if (player.getUsername().equalsIgnoreCase("Samus")) {
                food.changeFood(new Image(new File("resources/snake_art/.samus/food.png").toURI().toString()));
            }

            //makes game run until stopped
            timeline.setCycleCount(Timeline.INDEFINITE);

            //sets up keyframes to normal properties
            KeyValue keyValueX = new KeyValue(root.scaleXProperty(), 1);
            KeyValue keyValueY = new KeyValue(root.scaleYProperty(), 1);
            //sets each movement to 100 duration
            Duration duration = Duration.millis(100);

            EventHandler onFinished = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    //if the snake collided, end game for player
                    if (s.checkCollision()) {
                        timeline.stop();
                        gameOver();
                    }
                    //uses stored dir if necessary for smoother movement
                    else if (storedDir != 4 && dir == s.tail.getImage().getRotate()/90) {
                        s.move(root, storedDir);
                        dir = storedDir;
                        storedDir = 4;
                    } else { //move as normal
                        s.move(root, dir);
                    }
                    //checks if the snake ate the food
                    if (food.checkFood(root, s)) {
                        s.grow += 3;
                        score++;
                        scoreText.setText("Score: " + score);
                    }
                    moved = false;
                }
            };

            //create keyframe to do event when frame finishes (every 100 millies
            KeyFrame keyFrame = new KeyFrame(duration, onFinished, keyValueX, keyValueY);
            timeline.getKeyFrames().add(keyFrame);
            //start game
            timeline.play();
        }

        //end game for player
        public void gameOver() {
            rect.setFill(Color.GRAY);
            timeline.stop();
            doneCheck();
        }
    }
}