package application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
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
    GamePlayer player1;
    GamePlayer player2;
    GamePlayer player3;
    GamePlayer player4;
    int numAlive;

    public Multiplayer(Player p1, Player p2, Player p3, Player p4) {
        //make basic stage
        primaryStage = new Stage();
        HBox hbox = new HBox();
        VBox v1 = new VBox();
        VBox v2 = new VBox();

        scene = new Scene(hbox, Color.WHITE);

        //set controls for players
        KeyCode[] keys1 = {KeyCode.RIGHT, KeyCode.DOWN, KeyCode.LEFT, KeyCode.UP};
        player1 = new GamePlayer(keys1);
        KeyCode[] keys2 = {KeyCode.D, KeyCode.S, KeyCode.A, KeyCode.W};
        player2 = new GamePlayer(keys2);
        KeyCode[] keys3 = {KeyCode.NUMPAD6, KeyCode.NUMPAD5, KeyCode.NUMPAD4, KeyCode.NUMPAD8};
        player3 = new GamePlayer(keys3);
        KeyCode[] keys4 = {KeyCode.L, KeyCode.K, KeyCode.J, KeyCode.I};
        player4 = new GamePlayer(keys4);

        //add players to the in game players
        player1.setPlayer(p1);
        player2.setPlayer(p2);
        player3.setPlayer(p3);
        player4.setPlayer(p4);

        //setup all 4 players displays
        VBox layout1 = new VBox();
        VBox layout2 = new VBox();
        VBox layout3 = new VBox();
        VBox layout4 = new VBox();


        layout1.setStyle("-fx-border-color: black;");
        layout2.setStyle("-fx-border-color: black;");
        layout3.setStyle("-fx-border-color: black;");
        layout4.setStyle("-fx-border-color: black;");

        layout1.getChildren().addAll(player1.getRoot());
        layout2.getChildren().addAll(player2.getRoot());
        layout3.getChildren().addAll(player3.getRoot());
        layout4.getChildren().addAll(player4.getRoot());

        v1.getChildren().addAll(layout1, layout3);
        v2.getChildren().addAll(layout2, layout4);
        hbox.getChildren().addAll(v1,v2);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Snake Frenzy");
        primaryStage.show();

        //makes program an fx application thread
        Platform.runLater(player1);
        Platform.runLater(player2);
        Platform.runLater(player3);
        Platform.runLater(player4);

        numAlive = 4;
    }

    public void doneCheck() {
        numAlive--;
        //if less than 2 players are alive, it ends the game
        if (numAlive < 2){
            String winner;
            //primaryStage.close();
            if (player1.getAlive()){
                winner = player1.getPlayer().getUsername();
                player1.getTimeline().stop();
            }
            else if (player2.getAlive()){
                winner = player2.getPlayer().getUsername();
                player2.getTimeline().stop();
            }
            else if (player3.getAlive()){
                winner = player3.getPlayer().getUsername();
                player3.getTimeline().stop();
            }
            else if (player4.getAlive()){
                winner = player4.getPlayer().getUsername();
                player4.getTimeline().stop();
            }
            else {
                winner = "no one";
            }
            //prints the winner
            Text text = new Text(winner + " wins!");
            Stage popup = new Stage();
            BorderPane layout = new BorderPane();
            layout.setCenter(text);
            popup.setTitle("GAME OVER");
            Scene popScene = new Scene(layout);
            popup.setScene(popScene);
            popup.show();
        }
    }

    private class GamePlayer extends Thread {
        private Group root;
        private Timeline timeline;
        private boolean alive;
        private Snake s;
        private int dir;
        private int storedDir;
        private ImageView foodPic;
        private boolean moved;
        private int score;
        private Text scoreText;
        private Player player;
        private Rectangle rect;

        public GamePlayer(KeyCode[] keys){
            //sets up all parameters for game
            foodPic = new ImageView(new Image(new File("snake_art/food.png").toURI().toString()));
            alive = true;
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
            root.getChildren().addAll(wall, rect, scoreText);

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

        public boolean getAlive() {
            return alive;
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

        public void run() {
            //creates snake and sets up score/directions
            dir = 0;
            storedDir = 4;
            score = 0;
            s = new Snake(player.getCustom(), 17, 10);
            s.drawSnake(root);
            newFood(s);

            //makes game run until stopped
            timeline.setCycleCount(Timeline.INDEFINITE);

            //sets up keyframes to normal properties
            KeyValue keyValueX = new KeyValue(root.scaleXProperty(), 1);
            KeyValue keyValueY = new KeyValue(root.scaleYProperty(), 1);
            //sets each movement to 100 duration
            Duration duration = Duration.millis(100);

            EventHandler onFinished = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    if (checkFood()) {
                        newFood(s);
                    }
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
                    checkFood();
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
            alive = false;
            rect.setFill(Color.GRAY);
            timeline.stop();
            doneCheck();
        }

        //check if the snake ate the food
        //check if the snake ate the food
        private boolean checkFood() {
            if (foodPic.getX() == s.tail.getImage().getX() && foodPic.getY() == s.tail.getImage().getY()) {
                s.grow += 3;
                root.getChildren().remove(foodPic); //deletes the food if eaten
                score++;
                scoreText.setText("Score: " + score);
                return true;
            }
            return false;
        }

        //creates a new food
        private void newFood(Snake s) {
            Platform.setImplicitExit(false);
            Random rand = new Random();
            int x = 0;
            int y = 0;
            boolean available = false;
            while (!available) { //makes sure the snake isn't in the way of the new food
                available = true;
                x = rand.nextInt(40);
                y = rand.nextInt(20);
                SnakePart c = s.head;
                while (c != null) {
                    if (x == c.getImage().getX() && y == c.getImage().getY()) {
                        available = false;
                        break;
                    }
                    c = c.getNext();
                }
            }
            foodPic.setX(x * 20);
            foodPic.setY(y * 20);
            root.getChildren().add(foodPic); //adds the food to the screen
        }
    }
}