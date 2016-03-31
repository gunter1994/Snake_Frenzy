package application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    Scene scene;

    public MainGame() {
    }

    public void start(Stage primaryStage) throws Exception {
        SplitPane split1 = new SplitPane();
        SplitPane split2 = new SplitPane();
        SplitPane split3 = new SplitPane();
        split1.setOrientation(Orientation.HORIZONTAL);
        split2.setOrientation(Orientation.VERTICAL);
        split3.setOrientation(Orientation.VERTICAL);

        scene = new Scene(split1, Color.WHITE);

        KeyCode[] keys1 = {KeyCode.RIGHT, KeyCode.DOWN, KeyCode.LEFT, KeyCode.UP};
        GamePlayer player1 = new GamePlayer(keys1);
        KeyCode[] keys2 = {KeyCode.D, KeyCode.S, KeyCode.A, KeyCode.W};
        GamePlayer player2 = new GamePlayer(keys2);
        KeyCode[] keys3 = {KeyCode.NUMPAD6, KeyCode.NUMPAD5, KeyCode.NUMPAD4, KeyCode.NUMPAD8};
        GamePlayer player3 = new GamePlayer(keys3);
        KeyCode[] keys4 = {KeyCode.L, KeyCode.K, KeyCode.J, KeyCode.I};
        GamePlayer player4 = new GamePlayer(keys4);

        BorderPane layout1 = new BorderPane();
        BorderPane layout2 = new BorderPane();
        BorderPane layout3 = new BorderPane();
        BorderPane layout4 = new BorderPane();


        layout1.setTop(player1.getScoreText());
        layout2.setTop(player2.getScoreText());
        layout3.setTop(player3.getScoreText());
        layout4.setTop(player4.getScoreText());

        layout1.setCenter(player1.getGrid());
        layout2.setCenter(player2.getGrid());
        layout3.setCenter(player3.getGrid());
        layout4.setCenter(player4.getGrid());

        split2.getItems().addAll(layout1, layout2);
        split3.getItems().addAll(layout3, layout4);
        split1.getItems().addAll(split2, split3);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Snake Frenzy");
        primaryStage.show();

        player1.start();
        player2.start();
        player3.start();
        player4.start();
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

        public GamePlayer(KeyCode[] keys){
            alive = true;
            timeline = new Timeline();
            moved = false;
            score = 0;
            grid = new GridPane();
            grid.setStyle("-fx-border: 1px solid; -fx-border-color: black;");
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

        public Timeline getTimeline() {
            return timeline;
        }

        public boolean getAlive() {
            return alive;
        }

        public Text getScoreText() {
            return scoreText;
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