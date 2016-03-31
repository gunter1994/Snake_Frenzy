package application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class MainGame {
    Stage primaryStage;
    Scene scene;
    GamePlayer player1;
    GamePlayer player2;
    GamePlayer player3;
    GamePlayer player4;

    public MainGame() {
        primaryStage = new Stage();
        HBox hbox = new HBox();
        VBox v1 = new VBox();
        VBox v2 = new VBox();

        scene = new Scene(hbox, Color.WHITE);

        //preset playernames for now
        KeyCode[] keys1 = {KeyCode.RIGHT, KeyCode.DOWN, KeyCode.LEFT, KeyCode.UP};
        player1 = new GamePlayer(keys1, "Hunter");
        KeyCode[] keys2 = {KeyCode.D, KeyCode.S, KeyCode.A, KeyCode.W};
        player2 = new GamePlayer(keys2, "Nathaniel");
        KeyCode[] keys3 = {KeyCode.NUMPAD6, KeyCode.NUMPAD5, KeyCode.NUMPAD4, KeyCode.NUMPAD8};
        player3 = new GamePlayer(keys3, "Sam");
        KeyCode[] keys4 = {KeyCode.L, KeyCode.K, KeyCode.J, KeyCode.I};
        player4 = new GamePlayer(keys4, "Adam");

        VBox layout1 = new VBox();
        VBox layout2 = new VBox();
        VBox layout3 = new VBox();
        VBox layout4 = new VBox();

        layout1.setStyle("-fx-border-color: black;");
        layout2.setStyle("-fx-border-color: black;");
        layout3.setStyle("-fx-border-color: black;");
        layout4.setStyle("-fx-border-color: black;");

        layout1.getChildren().addAll(player1.getScoreText(), player1.getGrid());
        layout2.getChildren().addAll(player2.getScoreText(), player2.getGrid());
        layout3.getChildren().addAll(player3.getScoreText(), player3.getGrid());
        layout4.getChildren().addAll(player4.getScoreText(), player4.getGrid());

        v1.getChildren().addAll(layout1, layout2);
        v2.getChildren().addAll(layout3, layout4);
        hbox.getChildren().addAll(v1,v2);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Snake Frenzy");
        primaryStage.show();

        player1.start();
        player2.start();
        player3.start();
        player4.start();
    }

    public void done() {
        primaryStage.close();
    }

    private class GamePlayer extends Thread {
        private GridPane grid;
        private Timeline timeline;
        private boolean alive;
        private Snake s;
        private int dir;
        private int storedDir;
        private Position food;
        private boolean moved;
        private int score;
        private Text scoreText;
        private String name;

        public GamePlayer(KeyCode[] keys, String n){
            name = n;
            alive = true;
            timeline = new Timeline();
            moved = false;
            score = 0;
            grid = new GridPane();
            grid.setStyle("-fx-background-color: palegreen");
            grid.setPrefSize(820,420);
            scoreText = new Text("Score: " + score);
            for (int i = 0; i < 40; i++) {
                ColumnConstraints column = new ColumnConstraints(20);
                grid.getColumnConstraints().add(column);
            }

            for (int i = 0; i < 20; i++) {
                RowConstraints row = new RowConstraints(20);
                grid.getRowConstraints().add(row);
            }

            Position p = new Position(17,10);
            s = new Snake(HomeController.custom, p);
            s.drawSnake(grid);

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

        public GridPane getGrid() {
            return grid;
        }

        public boolean getAlive() {
            return alive;
        }

        public Text getScoreText() {
            return scoreText;
        }

        public String getPName() {
            return name;
        }

        public void run() {
            dir = 0;
            storedDir = 4;
            score = 0;
            newFood(s);

            timeline.setCycleCount(Timeline.INDEFINITE);

            KeyValue keyValueX = new KeyValue(grid.scaleXProperty(), 1);
            KeyValue keyValueY = new KeyValue(grid.scaleYProperty(), 1);
            Duration duration = Duration.millis(100);

            EventHandler onFinished = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    if (s.checkCollision()) {
                        timeline.stop();
                        gameOver(grid);
                    }
                    if (storedDir != 4 && dir == s.tail.getOrientation()) {
                        s.move(grid, storedDir);
                        dir = storedDir;
                        storedDir = 4;
                    } else {
                        s.move(grid, dir);
                    }
                    checkFood(grid, s);
                    moved = false;
                }
            };

            KeyFrame keyFrame = new KeyFrame(duration, onFinished, keyValueX, keyValueY);
            //add the keyframe to the timeline
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        }

        public void gameOver(GridPane grid) {
            alive = false;
            grid.setStyle("-fx-background-color: gray");
            timeline.stop();
        }

        private void checkFood(GridPane grid, Snake s) {
            if (food.equals(s.tail.getPos())) {
                s.grow += 3;
                newFood(s);
                score++;
                scoreText.setText("Score: " + score);
            }
            placeFood(grid);
        }

        private void newFood(Snake s) {
            Random rand = new Random();
            int x = 0;
            int y = 0;
            boolean available = false;
            while (!available) {
                available = true;
                x = rand.nextInt(40);
                y = rand.nextInt(20);
                food = new Position(x, y);
                SnakePart c = s.head;
                while (c != null) {
                    if (food.equals(c.getPos())) {
                        available = false;
                        break;
                    }
                    c = c.getNext();
                }
            }
            food.setX(x);
            food.setY(y);
        }

        private void placeFood(GridPane grid) {
            Image foodPic = new Image(new File("snake_art/food.png").toURI().toString());
            ImageView foodView = new ImageView();
            foodView.setImage(foodPic);
            grid.add(foodView, food.getX(), food.getY());
        }
    }
}