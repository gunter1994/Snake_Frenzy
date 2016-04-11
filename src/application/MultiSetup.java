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


class MultiSetup {

    private Player[] players;
    private Group[] root;
    private Stage stage;
    private int[] pattern;
    private int[] colour;

    private void newGame(String[] patterns, String[] colours, String[] names, Player[] players) {
        for (int i = 0; i < players.length; i++) {
            setSnake(patterns[pattern[i]], colours[colour[i]], names[i], players[i]);
        }
        stage.close();
        new Multiplayer(players);
    }

    //sets snake customization for all players in here
    MultiSetup(int numPlayers) {
        HBox playerBoxes = new HBox();
        Scene scene = new Scene(playerBoxes, 350*numPlayers, 300);

        stage = new Stage();
        players = new Player[numPlayers];
        root = new Group[numPlayers];
        colour = new int[numPlayers];
        pattern = new int[numPlayers];

        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player();
            players[i].setCustom("None/Green");
            pattern[i] = 2;
            colour[i] = 3;
            root[i] = new Group();
        }

        String[] patterns = {"Striped","Spotted","None"};
        String[] colours = {"Blue", "Red", "Yellow", "Green", "Orange", "Purple"};

        VBox[] vBoxes = new VBox[numPlayers];
        Button[] ready = new Button[numPlayers];
        Button[] enterName = new Button[numPlayers];
        TextField[] nameField = new TextField[numPlayers];
        Label controlLabel[] = new Label[numPlayers];

        KeyCode[] keys1 = {KeyCode.D, KeyCode.S, KeyCode.A, KeyCode.W};
        KeyCode[] keys2 = {KeyCode.RIGHT, KeyCode.DOWN, KeyCode.LEFT, KeyCode.UP};
        KeyCode[] keys3 = {KeyCode.NUMPAD6, KeyCode.NUMPAD5, KeyCode.NUMPAD4, KeyCode.NUMPAD8};
        KeyCode[] keys4 = {KeyCode.L, KeyCode.K, KeyCode.J, KeyCode.I};

        KeyCode[][] keys = {keys1,keys2,keys3,keys4};

        for (int j = 0; j < numPlayers; j ++) {
            ready[j] = new Button("Ready?");
            enterName[j] = new Button("Enter Name");
            nameField[j] = new TextField();
            nameField[j].setAlignment(Pos.CENTER);
            nameField[j].setMaxWidth(150);
            nameField[j].setPromptText("e.x. Samus or Shrek");

            controlLabel[0] = new Label("\tSubmit name first!\nThan use the W D keys to adjust the snake pattern \n" +
                    "and the A D keys to adjust the snake colour");
            controlLabel[0].setTextFill(Color.RED);
            controlLabel[1] = new Label("\tSubmit name first!\nThan use the ↑ ↓ arrow keys to adjust the snake pattern \n" +
                    "and the ← → arrow keys to adjust the snake colour");
            controlLabel[1].setTextFill(Color.RED);
            if (numPlayers > 2) {
                controlLabel[2] = new Label("\tSubmit name first!\nThan use the 8 5 numpad keys to adjust the snake pattern \n" +
                        "and the 4 6 numpad keys to adjust the snake colour");
                controlLabel[2].setTextFill(Color.RED);
            }
            if (numPlayers > 3) {
                controlLabel[3] = new Label("\tSubmit name first!\nThan use the I K keys to adjust the snake pattern \n" +
                        "and the J L keys to adjust the snake colour");
                controlLabel[3].setTextFill(Color.RED);
            }

            vBoxes[j] = new VBox(enterName[j], nameField[j], ready[j], controlLabel[j]);
            vBoxes[j].setAlignment(Pos.CENTER);
            vBoxes[j].setSpacing(20);
            vBoxes[j].setPrefSize(350, 250);


            Button temp = ready[j]; //won't allow access through j

            ready[j].setOnAction(e -> {
                temp.setText("Ready!");
                temp.setStyle("-fx-background-color: green");

                String[] names = new String[numPlayers];
                for (int k = 0; k < numPlayers; k++) {
                    try {
                        names[k] = nameField[k].getText();
                    } catch(NullPointerException error) {
                        names[k] = "";
                    }
                }

                readyChecker(ready, patterns, colours, names, players, numPlayers);
            });

            Button temp2 = enterName[j];
            TextField temp3 = nameField[j];

            KeyCode keyup = keys[j][3]; //must be final or non changed for even handler
            KeyCode keydown = keys[j][1];
            KeyCode keyright = keys[j][0];
            KeyCode keyleft = keys[j][2];
            int CurrentPlayer = j;
            //creates default coloured snake
            Snake s = new Snake(this.players[j].getCustom(), 6, 11);
            updateSnake(s, "None", "Green", j);
            enterName[j].setOnAction(e -> { //textfield interferes with this for obvious reasons
                temp2.setText("Submitted!");
                temp3.setDisable(true);
                scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                    if (key.getCode() == keyleft) {
                        updateSnake(s, patterns[getPattern(CurrentPlayer)]
                                , colours[cycleColour(true, CurrentPlayer)], CurrentPlayer);
                    }
                });
                scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                    if (key.getCode() == keyright) {
                        updateSnake(s, patterns[getPattern(CurrentPlayer)]
                                , colours[cycleColour(false, CurrentPlayer)], CurrentPlayer);
                    }
                });
                scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                    if (key.getCode() == keydown) {
                        updateSnake(s, patterns[cyclePattern(true, CurrentPlayer)]
                                , colours[getColour(CurrentPlayer)], CurrentPlayer);
                    }
                });
                scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                    if (key.getCode() == keyup) {
                        updateSnake(s, patterns[cyclePattern(false, CurrentPlayer)]
                                , colours[getColour(CurrentPlayer)], CurrentPlayer);
                    }
                });
            });

            root[j].getChildren().addAll(vBoxes[j]);
            playerBoxes.getChildren().add(root[j]);
        }

        stage.setTitle("Game Setup");
        stage.setScene(scene);
        stage.show();
    }



    // preview of the player's customized snake

    // updates the snake preview and name
    private void updateSnake(Snake s, String pattern, String colour, int i) {
        // sets player's selected colour/pattern
        // then changes the picture folder in snake object
        players[i].setCustom(pattern + "/" + colour);
        s.setPic(players[i].getCustom());
        s.clearSnake(root[i]);
        s.drawSnake(root[i]);
    }

    // updates the snake and name (not preview)
    private void setSnake(String pattern, String colour, String name, Player player) {
        player.setCustom(pattern + "/" + colour);

        if(name.equalsIgnoreCase("Shrek")){
            player.setCustom(".shrek");
        } else if(name.equalsIgnoreCase("Sam") || name.equalsIgnoreCase("Samus")){
            player.setCustom(".samus");
        } else if (name.equalsIgnoreCase("sonic")) {
            player.setCustom(".sonic");
        }
        player.setUsername(name.replaceAll("\\s", ""));
        if (name.equals("")) {
            player.setUsername("NoName");
        }
    }

    // cycles through the pattern to change it
    private int cyclePattern(boolean t, int i) {
        if (t) {
            pattern[i]--;
            if (pattern[i] == -1) {
                pattern[i] = 2;
            }
        } else {
            this.pattern[i]++;
            if (pattern[i] == 3) {
                pattern[i] = 0;
            }
        }
        return pattern[i];
    }

    // cycles through the colour to change it
    private int cycleColour(boolean t, int i) {
        if (t) {
            colour[i]--;
            if (colour[i] == -1) {
                colour[i] = 5;
            }
        } else {
            colour[i]++;
            if (colour[i] == 6) {
                colour[i] = 0;
            }
        }
        return colour[i];
    }

    private int getPattern(int i) {
        return pattern[i];
    }

    private int getColour(int i) {
        return colour[i];
    }

    private void readyChecker(Button[] ready, String[] patterns, String[] colours, String[] names, Player[] players, int numPlayers) {
        int numReady = 0;
        for (int i = 0; i < players.length; i++) {
            if (ready[i].getText().equals("Ready!")) {
                numReady++;
            }
        }
        if (numReady == numPlayers) {
            newGame(patterns, colours, names, players);
        }
    }
}
