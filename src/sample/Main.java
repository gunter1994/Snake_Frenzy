package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.Random;

public class Main extends Application {
    Scene scene;
    private Timeline timeline;
    private AnimationTimer timer;
    private int dir;
    private Position food;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();

        GridPane grid = new GridPane();
        for (int i = 0; i < 40; i++) {
            ColumnConstraints column = new ColumnConstraints(20);
            grid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < 40; i++) {
            RowConstraints row = new RowConstraints(20);
            grid.getRowConstraints().add(row);
        }
        Snake s = new Snake();
        s.drawSnake(grid);
        s.move(grid, dir); //snake must move once for collision purposes
        newFood(grid, s);

        root.getChildren().add(grid);
        scene = new Scene(root, Color.WHITE);
        primaryStage.setScene(scene);
        primaryStage.show();

        play(grid, s, primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void play(GridPane grid, Snake s, Stage primaryStage) {
        dir = 0;

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()==KeyCode.LEFT) {
                dirLeft();
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()==KeyCode.RIGHT) {
                dirRight();
            }
        });

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
            }
        };

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
                s.move(grid, dir);
                checkFood(grid, s);
            }
        };
        KeyFrame keyFrame = new KeyFrame(duration, onFinished , keyValueX, keyValueY);
        //add the keyframe to the timeline
        timeline.getKeyFrames().add(keyFrame);

        timeline.play();
        timer.start();
    }

    public void dirLeft()
    {
        dir--;
        if (dir == -1) {
            dir = 3;
        }
    }

    public void dirRight()
    {
        dir++;
        if (dir == 4) {
            dir = 0;
        }
    }

    public void gameOver(GridPane grid, Stage primaryStage)
    {
        Stage stage = new Stage();
        stage.setTitle("Game Over");

        Button play = new Button("Play Again");
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Snake s = new Snake();
                s.move(grid, dir);
                stage.close();
                play(grid, s, primaryStage);
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
            newFood(grid, s);
        }
        placeFood(grid);
    }

    public void newFood(GridPane grid, Snake s)
    {
        Random rand = new Random();
        int x = 0;
        int y = 0;
        boolean available = false;
        while (!available) {
            available = true;
            x = rand.nextInt(40);
            y = rand.nextInt(40);
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

    public void placeFood(GridPane grid)
    {
        Image foodPic = new Image(new File("snake_art/food.png").toURI().toString());
        ImageView foodView = new ImageView();
        foodView.setImage(foodPic);
        grid.add(foodView, food.getX(), food.getY());
    }
}

