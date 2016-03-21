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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.event.KeyListener;
import java.io.File;
import java.security.Key;

public class Main extends Application {
    Scene scene;
    private Timeline timeline;
    private AnimationTimer timer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();

        GridPane grid = new GridPane();
        for (int i = 0; i < 20; i++) {
            ColumnConstraints column = new ColumnConstraints(20);
            grid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < 20; i++) {
            RowConstraints row = new RowConstraints(20);
            grid.getRowConstraints().add(row);
        }
        Snake s = new Snake();
        s.drawSnake(grid);

        root.getChildren().add(grid);
        scene = new Scene(root, Color.WHITE);
        primaryStage.setScene(scene);
        primaryStage.show();

        play(grid, s);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void play(GridPane grid, Snake s) {
        int dir = 1;

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()==KeyCode.LEFT) {
                System.out.println("You pressed Left");
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()==KeyCode.RIGHT) {
                System.out.println("You pressed Right");
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
        Duration duration = Duration.millis(500);
        EventHandler onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                s.move(grid, dir);
            }
        };
        KeyFrame keyFrame = new KeyFrame(duration, onFinished , keyValueX, keyValueY);
        //add the keyframe to the timeline
        timeline.getKeyFrames().add(keyFrame);

        timeline.play();
        timer.start();
    }
}

