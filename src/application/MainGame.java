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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;

// this is creates and runs the actual game
class MainGame {
    private Stage primaryStage;
    private Scene scene;
    private GamePlayer player1;
    private Audio audio;

    MainGame(Player p1, Audio a) {
        //make basic stage
        audio = a;
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
        private boolean paused = false;
        private Snake s;
        private int dir;
        private int storedDir;
        private Food food;
        private boolean moved;
        private int score;
        private int highScore;
        private Text scoreText;
        private Text highScoreText;
        private Player player;
        private Rectangle rect;

        GamePlayer() {
            //sets up all parameters for game
            timeline = new Timeline();
            moved = false;
            score = 0;

            // gets high score from local high score file
            // to print in the top corner of game
            try {
                BufferedReader br = new BufferedReader(new FileReader("localScores.csv"));
                int high = -1;
                String line;
                while ((line = br.readLine()) != null){
                    int temp = Integer.parseInt(line.split(",")[1]);
                    if (temp > high)
                        high = temp;
                }
                highScore = high;
            } catch (FileNotFoundException e) {
                highScore = 0;
            } catch (IOException e){
                e.printStackTrace();
            }

            //sets up the players gameScreen
            root = new Group();
            rect = new Rectangle(0, 0, 820, 420);
            rect.setFill(Color.WHITE);
            Rectangle wall = new Rectangle(-20, -20, 860, 460); //so that the snake can move off screen when it dies
            wall.setFill(Color.DARKGRAY);
            scoreText = new Text(0, -5, "Score: " + score);
            highScoreText = new Text(725, -5, "High Score: " + highScore);
            root.getChildren().addAll(wall, rect, scoreText, highScoreText);

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

            // pauses or unpauses the game
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

            primaryStage.setOnCloseRequest(e -> application.scoreServer.stopServer());
        }

        Group getRoot() {
            return root;
        }

        void setPlayer(Player p) {
            player = p;
        }

        void run() {
            //creates snake and sets up score/directions
            dir = 0;
            storedDir = 4;
            score = 0;
            s = new Snake(player.getCustom(), 17, 10);
            s.drawSnake(root);

            food = new Food(s, root);

            if (player.getUsername().equalsIgnoreCase("Samus")) {
                food.changeFood(new Image(new File("snake_art/.samus/food.png").toURI().toString()));
            }

            //makes game run until stopped
            timeline.setCycleCount(Timeline.INDEFINITE);

            //sets up keyframes to normal properties
            KeyValue keyValueX = new KeyValue(root.scaleXProperty(), 1);
            KeyValue keyValueY = new KeyValue(root.scaleYProperty(), 1);

            //sets each movement to 100 duration
            Duration duration = Duration.millis(100);

            if (player.getUsername().equalsIgnoreCase("sonic")) {
                food.changeFood(new Image(new File("snake_art/.sonic/food.png").toURI().toString()));
                duration = Duration.millis(50);
            }

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
                    if (food.checkFood(root, s)) {
                        s.grow += 3;
                        score++;

                        // updates the high score if the score is greater
                        if (score > highScore) {
                            highScore = score;
                            highScoreText.setText("Highscore: " + highScore);
                        }

                        scoreText.setText("Score: " + score);
                    }
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
        private void gameOver() {
            rect.setFill(Color.GRAY);
            timeline.stop();


            // sends name and score to the high score server
            try {
                Socket socket = new Socket("localhost", 8080);
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                out.println("ADD," + player.getUsername() + "," + score);
                out.close();
            } catch(IOException e){
                System.err.println("Cannot connect to server");
                try {
                    FileWriter out = new FileWriter("localScores.csv", true);
                    out.append(player.getUsername() + "," + score + "\n");

                    out.close();
                }catch(IOException i){
                    i.printStackTrace();
                }
            }


            // creates Game Over popup window
            Stage stage = new Stage();
            stage.setTitle("Game Over");

            Button play = new Button("Play Again");
            play.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                    primaryStage.close();
                    new MainGame(player, audio);
                }
            });

            Button mainMenu = new Button("Main Menu");
            mainMenu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    audio.stopSong();
                    stage.close();
                    primaryStage.close();
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
            layout.getChildren().addAll(play, mainMenu, quitGame);
            stage.setScene(new Scene(layout));
            stage.show();
        }
    }
}