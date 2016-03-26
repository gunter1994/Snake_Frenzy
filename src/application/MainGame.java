package application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.util.Random;

public class MainGame {
    Scene scene;
    private int dir;
    private int storedDir;
    private Position food;
    private boolean moved;
    private int score;

    public MainGame() {
        this.dir = 0;
        this.food = new Position(0,0);
        this.moved = false;
        this.storedDir = 4;
    }

    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();

        GridPane grid = new GridPane();
        for (int i = 0; i < 36; i++) {
            ColumnConstraints column = new ColumnConstraints(20);
            grid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < 36; i++) {
            RowConstraints row = new RowConstraints(20);
            grid.getRowConstraints().add(row);
        }

        root.getChildren().add(grid);
        scene = new Scene(root, Color.WHITE);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Snake Frenzy");
        primaryStage.show();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()==KeyCode.RIGHT) {
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
            if(key.getCode()==KeyCode.DOWN && !moved) {
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
            if(key.getCode()==KeyCode.LEFT) {
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
            if(key.getCode()==KeyCode.UP) {
                if ((dir == 0 || dir == 2) && !moved) {
                    dir = 3;
                    moved = true;
                }
                else if (dir != 1 && moved){
                    storedDir = 3;
                }
            }
        });

        play(grid, primaryStage);
    }

    private void play(GridPane grid, Stage primaryStage){
        dir = 0;
        storedDir = 4;
        score = 0;
        Position p = new Position(15,18);
        Snake s = new Snake(HomeController.custom, p);
        s.drawSnake(grid);
        newFood(s);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyValue keyValueX = new KeyValue(grid.scaleXProperty(), 1);
        KeyValue keyValueY = new KeyValue(grid.scaleYProperty(), 1);
        Duration duration = Duration.millis(100);
        EventHandler onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                if (s.checkCollision())
                {
                    timeline.stop();
                    gameOver(grid, primaryStage);
                }
                if (storedDir != 4 && dir == s.tail.getOrientation())
                {
                    s.move(grid, storedDir);
                    dir = storedDir;
                    storedDir = 4;
                }
                else {
                    s.move(grid, dir);
                }
                checkFood(grid, s);
                moved = false;
            }
        };
        KeyFrame keyFrame = new KeyFrame(duration, onFinished , keyValueX, keyValueY);
        //add the keyframe to the timeline
        timeline.getKeyFrames().add(keyFrame);

        timeline.play();
    }

    private void gameOver(GridPane grid, Stage primaryStage)
    {
        Stage stage = new Stage();
        System.out.println("Final score: " + score);
        stage.setTitle("Game Over");

        Button play = new Button("Play Again");
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                play(grid, primaryStage);
            }
        });

        Button quit = new Button("Quit");
        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                primaryStage.close();
            }
        });

        HBox layout = new HBox(10);
        layout.setStyle("-fx-background-color: white; -fx-padding: 20;");
        layout.getChildren().addAll(play, quit);
        stage.setScene(new Scene(layout));
        stage.show();
    }

    public void checkFood(GridPane grid, Snake s)
    {
        if (food.equals(s.tail.getPos()))
        {
            s.grow += 3;
            newFood(s);
            score++;
        }
        placeFood(grid);
    }

    private void newFood(Snake s)
    {
        Random rand = new Random();
        int x = 0;
        int y = 0;
        boolean available = false;
        while (!available) {
            available = true;
            x = rand.nextInt(36);
            y = rand.nextInt(36);
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

    private void placeFood(GridPane grid)
    {
        Image foodPic = new Image(new File("snake_art/food.png").toURI().toString());
        ImageView foodView = new ImageView();
        foodView.setImage(foodPic);
        grid.add(foodView, food.getX(), food.getY());
    }

}