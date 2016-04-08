package application;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static java.awt.Color.RED;
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
        Scene scene = new Scene(layout, 350, 280);

        this.stage = new Stage();
        this.player = new Player();
        this.player.setCustom("None/Green");
        this.root = new Group();

        String[] patterns = {"Striped","Spotted","None"};
        String[] colours = {"Blue", "Red", "Yellow", "Green", "Orange", "Purple"};
        this.pattern = 2;
        this.colour = 3;

        Button start = new Button("Start Game");
        Label namePrompt = new Label("Enter Name:");
        TextField nameField = new TextField();
        nameField.setAlignment(Pos.CENTER);
        nameField.setMaxWidth(150);

        Label label = new Label("Use the ↑ ↓ arrow keys to adjust the snake pattern \nand the ← → arrow keys to adjust the snake colour");
        label.setTextFill(Color.RED);

        VBox vBox = new VBox(namePrompt, nameField, start, label);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        vBox.setPrefSize(350, 250);

        start.setOnAction(e -> newGame(patterns[this.pattern], colours[this.colour], nameField.getText()));

        this.root.getChildren().addAll(vBox);

        Snake s = new Snake(this.player.getCustom(), 6, 11); //Snake(design, hor. pos, ver pos) in pane
        updateSnake(s, "None", "Green");

        nameField.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode() == KeyCode.LEFT) {
                cycleColour(true);
                updateSnake(s, patterns[this.pattern], colours[this.colour]);
            }
        });

        nameField.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode() == KeyCode.RIGHT) {
                cycleColour(false);
                updateSnake(s, patterns[this.pattern], colours[this.colour]);
            }
        });

        nameField.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode() == KeyCode.DOWN) {
                cyclePattern(true);
                updateSnake(s, patterns[this.pattern], colours[this.colour]);
            }
        });

        nameField.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode() == KeyCode.UP) {
                cyclePattern(false);
                updateSnake(s, patterns[this.pattern], colours[this.colour]);
            }
        });


        layout.getChildren().add(this.root);
        stage.setTitle("Game Setup");
        stage.setScene(scene);
        stage.show();
    }

    // preview of the player's customized snake

    // updates the snake preview and name
    private void updateSnake(Snake s, String pattern, String colour){

        // sets player's selected colour/pattern
        // then changes the picture folder in snake object
        this.player.setCustom(pattern + "/" + colour);
        s.setPic(player.getCustom());
        s.clearSnake(this.root);
        s.drawSnake(this.root);
    }

    // updates the snake and name (not preview)
    public void setSnake(String pattern, String colour, String name){
        this.player.setCustom(pattern + "/" + colour);

        if (name.equals("")){
            this.player.setUsername("NoName");
        } else {
            this.player.setUsername(name.replaceAll("\\s", ""));
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
