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
            setSnake(patterns[this.pattern[i]], colours[this.colour[i]], names[i], players[i]);
        }
        stage.close();
        new Multiplayer(this.players);
    }

    //sets snake customization for all players in here
    MultiSetup(int numPlayers) {
        HBox playerBoxes = new HBox();
        Scene scene = new Scene(playerBoxes, 350*numPlayers, 300);

        this.stage = new Stage();
        this.players = new Player[numPlayers];
        this.root = new Group[numPlayers];
        this.colour = new int[numPlayers];
        this.pattern = new int[numPlayers];

        for (int i = 0; i < numPlayers; i++) {
            this.players[i] = new Player();
            this.players[i].setCustom("None/Green");
            this.pattern[i] = 2;
            this.colour[i] = 3;
            this.root[i] = new Group();
        }

        String[] patterns = {"Striped","Spotted","None"};
        String[] colours = {"Blue", "Red", "Yellow", "Green", "Orange", "Purple"};

        VBox[] vBoxes = new VBox[numPlayers];
        Button[] ready = new Button[numPlayers];
        Label[] namePrompt = new Label[numPlayers];
        TextField[] nameField = new TextField[numPlayers];

        KeyCode[] keys1 = {KeyCode.RIGHT, KeyCode.DOWN, KeyCode.LEFT, KeyCode.UP};
        KeyCode[] keys2 = {KeyCode.D, KeyCode.S, KeyCode.A, KeyCode.W};
        KeyCode[] keys3 = {KeyCode.NUMPAD6, KeyCode.NUMPAD5, KeyCode.NUMPAD4, KeyCode.NUMPAD8};
        KeyCode[] keys4 = {KeyCode.L, KeyCode.K, KeyCode.J, KeyCode.I};

        KeyCode[][] keys = {keys1,keys2,keys3,keys4};

        for (int j = 0; j < numPlayers; j ++) {
            ready[j] = new Button("Ready?");
            namePrompt[j] = new Label("Enter Name:");
            nameField[j] = new TextField();
            nameField[j].setAlignment(Pos.CENTER);
            nameField[j].setMaxWidth(150);

            Label controlLabel = new Label("Use the ↑ ↓ arrow keys to adjust the snake pattern \n" +
                    "and the ← → arrow keys to adjust the snake colour");
            controlLabel.setTextFill(Color.RED);

            vBoxes[j] = new VBox(namePrompt[j], nameField[j], ready[j], controlLabel);
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

            this.root[j].getChildren().addAll(vBoxes[j]);

            //creates default coloured snake
            Snake s = new Snake(this.players[j].getCustom(), 6, 11);
            updateSnake(s, "None", "Green", j);


            // adds buttons to change snake colours and patterns
            KeyCode keyup = keys[j][2]; //must be final or non changed for even handler
            KeyCode keydown = keys[j][1];
            KeyCode keyright = keys[j][0];
            KeyCode keyleft = keys[j][3];
            int CurrentPattern = this.pattern[j];
            int CurrentColour = this.colour[j];
            int CurrentPlayer = j;
            vBoxes[j].addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if (key.getCode() == keyleft) {
                    cycleColour(true, CurrentPlayer);
                    updateSnake(s, patterns[CurrentPattern], colours[CurrentColour], CurrentPlayer);
                }
            });
            vBoxes[j].addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if (key.getCode() == keyright) {
                    cycleColour(false, CurrentPlayer);
                    updateSnake(s, patterns[CurrentPattern], colours[CurrentColour], CurrentPlayer);
                }
            });
            vBoxes[j].addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if (key.getCode() == keydown) {
                    cyclePattern(true, CurrentPlayer);
                    updateSnake(s, patterns[CurrentPattern], colours[CurrentColour], CurrentPlayer);
                }
            });
            vBoxes[j].addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if (key.getCode() == keyup) {
                    cyclePattern(false, CurrentPlayer);
                    updateSnake(s, patterns[CurrentPattern], colours[CurrentColour], CurrentPlayer);
                }
            });

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
        this.players[i].setCustom(pattern + "/" + colour);
        s.setPic(players[i].getCustom());
        s.clearSnake(this.root[i]);
        s.drawSnake(this.root[i]);
    }

    // updates the snake and name (not preview)
    private void setSnake(String pattern, String colour, String name, Player player) {
        player.setCustom(pattern + "/" + colour);

        if (name.equals("")){
            player.setUsername("NoName");
        } else if(name.equalsIgnoreCase("Shrek")){
            player.setCustom(".shrek");
        } else if(name.equalsIgnoreCase("Sam") || name.equalsIgnoreCase("Samus")){
            player.setCustom(".samus");
        }
        else {
            player.setUsername(name.replaceAll("\\s", ""));
        }

    }

    // cycles through the pattern to change it
    private void cyclePattern(boolean t, int i) {
        if (t) {
            this.pattern[i]--;
            if (this.pattern[i] == -1) {
                this.pattern[i] = 2;
            }
        } else {
            this.pattern[i]++;
            if (this.pattern[i] == 3) {
                this.pattern[i] = 0;
            }
        }
    }

    // cycles through the colour to change it
    private void cycleColour(boolean t, int i) {
        if (t) {
            this.colour[i]--;
            if (this.colour[i] == -1) {
                this.colour[i] = 5;
            }
        } else {
            this.colour[i]++;
            if (this.colour[i] == 6) {
                this.colour[i] = 0;
            }
        }
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
