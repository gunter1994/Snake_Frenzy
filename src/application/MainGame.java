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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class MainGame {
    private Stage primaryStage;
    private Scene scene;
    private GamePlayer player1;

    public MainGame(Player p1) {
        //make basic stage
        primaryStage = new Stage();
        VBox layout = new VBox();
        scene = new Scene(layout, Color.WHITE);

        //set controls for players
        player1 = new GamePlayer();

        //add players to the in game players
        player1.setPlayer(p1);

        layout.getChildren().add(player1.getRoot()); //have to do this to avoid errors with control setup
        primaryStage.setScene(scene);
        primaryStage.setTitle("Snake Frenzy");
        primaryStage.show();

        player1.run();
    }

    private class GamePlayer {
        private Group root;
        private Timeline timeline;
        private boolean alive;
        private boolean paused = false;
        private Snake s;
        private int dir;
        private int storedDir;
        private ImageView foodPic;
        private boolean moved;
        private int score;
        private Text scoreText;
        private Player player;
        private Rectangle rect;

        public GamePlayer() {
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
            Rectangle wall = new Rectangle(-20, -20, 860, 460); //so that the snake can move off screen when it dies
            wall.setFill(Color.DARKGRAY);
            scoreText = new Text(-5, -5, "Score: " + score);
            root.getChildren().addAll(wall, rect, scoreText);

            //adds keyboard controls
            scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if (key.getCode() == KeyCode.RIGHT || key.getCode() == KeyCode.D) {
                    if ((dir == 3 || dir == 1) && !moved) {
                        dir = 0;
                        moved = true;
                    } else if (dir != 2 && moved) {
                        storedDir = 0;
                    }
                }
            });

            scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if (key.getCode() == KeyCode.DOWN || key.getCode() == KeyCode.S) {
                    if ((dir == 0 || dir == 2) && !moved) {
                        dir = 1;
                        moved = true;
                    } else if (dir != 3 && moved) {
                        storedDir = 1;
                    }
                }
            });

            scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if (key.getCode() == KeyCode.LEFT || key.getCode() == KeyCode.A) {
                    if ((dir == 3 || dir == 1) && !moved) {
                        dir = 2;
                        moved = true;
                    } else if (dir != 0 && moved) {
                        storedDir = 2;
                    }
                }
            });

            scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if (key.getCode() == KeyCode.UP || key.getCode() == KeyCode.W) {
                    if ((dir == 0 || dir == 2) && !moved) {
                        dir = 3;
                        moved = true;
                    } else if (dir != 1 && moved) {
                        storedDir = 3;
                    }
                }
            });

            scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if (key.getCode() == KeyCode.ESCAPE) {
                    if (paused) {
                        timeline.play();
                        paused = false;
                        rect.setFill(Color.WHITE);
                    }
                    else {
                        timeline.pause();
                        paused = true;
                        rect.setFill(Color.GRAY);
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
                    //if the snake collided, end game for player
                    if (s.checkCollision()) {
                        timeline.stop();
                        gameOver();
                    }
                    //uses stored dir if necessary for smoother movement
                    else if (storedDir != 4 && dir == s.tail.getImage().getRotate() / 90) {
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

            //create keyframe to do event when frame finishes (every 100 millis)
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

            try {
                Socket socket = new Socket("localhost", 8080);
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                out.println("ADD," + player.getUsername() + "," + score);
                out.close();
            } catch(IOException e){
                System.err.println("Cannot connect to server");
            }

            Stage stage = new Stage();
            stage.setTitle("Game Over");

            Button play = new Button("Play Again");
            play.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                    primaryStage.close();
                    new MainGame(player);
                }
            });

            Button mainMenu = new Button("Main Menu");
            mainMenu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                    primaryStage.close();
                    Main.showMainMenu();
                }
            });

            HBox layout = new HBox(10);
            layout.setStyle("-fx-background-color: white; -fx-padding: 20;");
            layout.getChildren().addAll(play, mainMenu);
            stage.setScene(new Scene(layout));
            stage.show();
        }

        //check if the snake ate the food
        private void checkFood() {
            if (foodPic.getX() == s.tail.getImage().getX() && foodPic.getY() == s.tail.getImage().getY()) {
                root.getChildren().remove(foodPic); //deletes the food if eaten
                s.grow += 3;
                score++;
                scoreText.setText("Score: " + score);
                newFood(s);
            }
        }

        //creates a new food
        private void newFood(Snake s) {
            Random rand = new Random();
            int x = 0;
            int y = 0;
            boolean available = false;
            while (!available) { //makes sure the snake isn't in the way of the new food
                available = true;
                x = rand.nextInt(40)*20;
                y = rand.nextInt(20)*20;
                SnakePart c = s.head;

                while (c != null) {
                    if (x == c.getImage().getX() && y == c.getImage().getY()) {
                        available = false;
                        break;
                    }
                    c = c.getNext();
                }
            }
            foodPic.setX(x);
            foodPic.setY(y);
            root.getChildren().add(foodPic); //adds the food to the screen
        }
    }
}