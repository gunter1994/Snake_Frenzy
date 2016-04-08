package application;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import static java.awt.SystemColor.window;

public class GameSetup {

    private Player player;
    private Group root;
    private Stage stage;
    private int pattern;
    private int colour;

    public Group getRoot() {
        return this.root;
    }

    private void newGame(String pattern, String colour, String name) {
        setSnake(pattern, colour, name);
        stage.close();
        new MainGame(this.player);
    }

    //sets snake customization for all players in here
    public GameSetup() {
        BorderPane layout = new BorderPane();
        Scene scene = new Scene(layout, 350, 200);

        this.stage = new Stage();
        this.player = new Player();
        this.player.setCustom("None/Green");
        this.root = new Group();

        String[] patterns = {"Striped","Spotted","None"};
        String[] colours = {"Blue", "Red", "Yellow", "Green", "Orange", "Purple"};
        this.pattern = 0;
        this.colour = 0;

        Button start = new Button("Start Game");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter Name");
        nameField.setMaxWidth(200);

        VBox vBox = new VBox(nameField,start);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(30);
        vBox.setPrefSize(350, 150);

        start.setOnAction(e -> newGame(patterns[this.pattern], colours[this.colour], nameField.getText()));

        this.root.getChildren().addAll(vBox);

        Snake s = new Snake(this.player.getCustom(), 6, 7);
        updateSnake(s, "None", "Green", "");

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode() == KeyCode.LEFT) {
                cycleColour(true);
                updateSnake(s, colours[this.colour], patterns[this.pattern], nameField.getText());
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode() == KeyCode.RIGHT) {
                cycleColour(false);
                updateSnake(s, colours[this.colour], patterns[this.pattern], nameField.getText());
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode() == KeyCode.DOWN) {
                cyclePattern(true);
                updateSnake(s, colours[this.colour], patterns[this.pattern], nameField.getText());
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode() == KeyCode.UP) {
                cyclePattern(false);
                updateSnake(s, colours[this.colour], patterns[this.pattern], nameField.getText());
            }
        });


        layout.getChildren().add(this.root);
        stage.setTitle("Game Setup");
        stage.setScene(scene);
        stage.show();
    }

    // preview of the player's customized snake

    // updates the snake preview and name
    private void updateSnake(Snake s, String pattern, String colour, String name){
        this.player.setCustom(pattern + "/" + colour);

        s.clearSnake(this.root);
        s.drawSnake(this.root);

        if (name.equals("")){
            this.player.setUsername("NoName");
        } else {
            this.player.setUsername(name);
        }
    }

    // updates the snake and name (not preview
    public void setSnake(String pattern, String colour, String name){
        this.player.setCustom(pattern + "/" + colour);

        if (name.equals("")){
            this.player.setUsername("NoName");
        } else {
            this.player.setUsername(name);
        }
    }

    private void cyclePattern(boolean t) {
        if (t) {
            this.pattern--;
            if (this.pattern == -1) {
                this.pattern = 2;
            }
        } else {
            this.pattern++;
            if (this.pattern == 3) {
                this.pattern = 0;
            }
        }
    }

    private void cycleColour(boolean t) {
        if (t) {
            this.colour--;
            if (this.colour == -1) {
                this.colour = 5;
            }
        } else {
            this.colour++;
            if (this.colour == 6) {
                this.colour = 0;
            }
        }
    }
}
