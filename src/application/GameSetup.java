package application;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GameSetup {

    public static Player player;

    // closes the gameSetup window, updates the snake (in case player doesn't click update)
    // then starts the game
    private static void handleNewGame(String pattern, String colour, String name) {
        updateSnake(pattern, colour, name);
        MainGame m = new MainGame(player);
    }

    //sets snake customization for all players in here
    public static BorderPane setupWindow() {

        Group root = new Group();
        BorderPane borderPane = new BorderPane();

        VBox vBox1 = new VBox(), vBox2 = new VBox();
        HBox hBox1 = new HBox(), hBox2 = new HBox();

        hBox1.setSpacing(5);
        hBox2.setSpacing(5);
        vBox1.setSpacing(20);

        Label patternLabel = new Label("Pattern:");
        Label colourLabel = new Label("Colour:");

        //Player Name field
        TextField nameField = new TextField();
        nameField.setPromptText("Enter Player Name");
        nameField.setMaxSize(150,10);

        //Colour Selection field
        ComboBox<String> colours = new ComboBox<>();
        colours.setValue("Green");
        colours.getItems().addAll("Blue","Red", "Yellow", "Green", "Orange", "Purple");
        hBox1.getChildren().addAll(colourLabel, colours);

        //Pattern Selection field
        ComboBox<String> patterns = new ComboBox<>();
        patterns.setValue("None");
        patterns.getItems().addAll("None","Spotted","Striped");
        hBox2.getChildren().addAll(patternLabel, patterns);

        // button to update the snake preview
        Button update = new Button("Update");
        update.setOnAction(e -> updateSnake(root, vBox2, patterns.getValue(), colours.getValue(), nameField.getText()));

        vBox1.getChildren().addAll(nameField,hBox1,hBox2, update);
        vBox1.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox1);

        player = new Player();
        player.setCustom("None/Green");

        // closes window, updates snake, starts the game
        Button start = new Button("Start Game");
        start.setOnAction(e -> handleNewGame(patterns.getValue(), colours.getValue(), nameField.getText()));

        vBox2.getChildren().addAll(snakePreview(root), start, new Region());
        vBox2.setAlignment(Pos.TOP_CENTER);
        borderPane.setBottom(vBox2);


        return borderPane;
    }

    // preview of the player's customized snake
    public static Group snakePreview(Group root) {
        Snake s = new Snake(player.getCustom(), 0, 0);
        s.drawSnake(root);
        return root;
    }

    // updates the snake preview and name
    public static void updateSnake(Group root, VBox vbox, String pattern, String colour, String name){
        player.setCustom(pattern + "/" + colour);

        root = new Group();
        vbox.getChildren().set(0, snakePreview(root));

        if (name.equals("")){
            player.setUsername("NoName");
        } else {
            player.setUsername(name);
        }
    }

    // updates the snake and name (not preview
    public static void updateSnake(String pattern, String colour, String name){
        player.setCustom(pattern + "/" + colour);

        if (name.equals("")){
            player.setUsername("NoName");
        } else {
            player.setUsername(name);
        }
    }
}
