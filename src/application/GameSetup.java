package application;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


class GameSetup {

    private Player player;
    private Group root;
    private Stage stage;
    private int pattern;
    private int colour;

    private void newGame(String pattern, String colour, String name) {
        setSnake(pattern, colour, name);
        stage.close();
        new MainGame(this.player);
    }

    //sets snake customization for all players in here
    GameSetup() {
        BorderPane layout = new BorderPane();
        Scene scene = new Scene(layout, 350, 300);

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

        Label controlLabel = new Label("Use the ↑ ↓ arrow keys to adjust the snake pattern \n" +
                "and the ← → arrow keys to adjust the snake colour");
        controlLabel.setTextFill(Color.RED);

        VBox vBox = new VBox(namePrompt, nameField, start, controlLabel);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        vBox.setPrefSize(350, 250);

        start.setOnAction(e -> newGame(patterns[this.pattern], colours[this.colour], nameField.getText()));

        this.root.getChildren().addAll(vBox);

        //creates default coloured snake
        Snake s = new Snake(this.player.getCustom(), 6, 11);
        updateSnake(s, "None", "Green");


        // adds buttons to change snake colours and patterns
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
    private void updateSnake(Snake s, String pattern, String colour) {

        // sets player's selected colour/pattern
        // then changes the picture folder in snake object
        this.player.setCustom(pattern + "/" + colour);
        s.setPic(player.getCustom());
        s.clearSnake(this.root);
        s.drawSnake(this.root);
    }

    // updates the snake and name (not preview)
    private void setSnake(String pattern, String colour, String name) {
        this.player.setCustom(pattern + "/" + colour);

        if (name.equals("")){
            this.player.setUsername("NoName");
        }
        else {
            this.player.setUsername(name.replaceAll("\\s", ""));
        }

        if(name.equalsIgnoreCase("Shrek")){
            this.player.setCustom(".shrek");
        } else if(name.equalsIgnoreCase("Sam") || name.equalsIgnoreCase("Samus")){
            this.player.setCustom(".samus");
        }

    }

    // cycles through the pattern to change it
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

    // cycles through the colour to change it
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
