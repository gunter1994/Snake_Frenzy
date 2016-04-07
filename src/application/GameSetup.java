package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GameSetup {

    public static String custom = "None/Green";
    public static Stage stage;
    public static String playerName;

    private static void handleNewGame(String pattern, String colour, String name) {
        stage.close();
        updateSnake(pattern, colour, name);
        MainGame m = new MainGame();
        try{
            Stage primaryStage = new Stage();
            m.start(primaryStage);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //window for choosing playerName and snake design before starting game
    public static BorderPane preGameLobby() {

        GridPane grid = new GridPane();
        BorderPane borderPane = new BorderPane();

        VBox vBox1 = new VBox(), vBox2 = new VBox();
        HBox hBox1 = new HBox(), hBox2 = new HBox();

        hBox1.setSpacing(5);
        hBox2.setSpacing(5);
        vBox1.setSpacing(20);

        Label patternLabel = new Label("Pattern:");
        Label colourLabel = new Label("Colour:");

        //Player Name
        TextField nameField = new TextField();
        nameField.setPromptText("Enter Player Name");
        nameField.setMaxSize(150,10);

        //Colour Selection
        ComboBox<String> colours = new ComboBox<>();
        colours.setValue("Green");
        colours.getItems().addAll("Blue","Red", "Yellow", "Green", "Orange", "Purple");
        hBox1.getChildren().addAll(colourLabel, colours);

        //Pattern Selection
        ComboBox<String> patterns = new ComboBox<>();
        patterns.setValue("None");
        patterns.getItems().addAll("None","Spotted","Striped");
        hBox2.getChildren().addAll(patternLabel, patterns);

        Button update = new Button("Update");
        update.setOnAction(e -> updateSnake(grid, vBox2, patterns.getValue(), colours.getValue(), nameField.getText()));

        vBox1.getChildren().addAll(nameField,hBox1,hBox2, update);
        vBox1.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox1);

        Button start = new Button("Start Game");
        start.setOnAction(e -> handleNewGame(patterns.getValue(), colours.getValue(), nameField.getText()));

        vBox2.getChildren().addAll(snakePreview(grid), start, new Region());
        vBox2.setAlignment(Pos.TOP_CENTER);
        borderPane.setBottom(vBox2);

        return borderPane;
    }

    //sets snake customization for all players in here
    public static void Players(int players) {
        stage = new Stage();
        Scene scene;

        BorderPane borderPane1 = preGameLobby();
        BorderPane borderPane2 = preGameLobby();
        BorderPane borderPane3 = preGameLobby();
        BorderPane borderPane4 = preGameLobby();

        HBox hBox1 = new HBox();
        hBox1.setSpacing(50);

        if(players == 1) {
            hBox1.getChildren().add(borderPane1);
            scene = new Scene(hBox1, 350, 350);
        }
        else if (players == 2) {
            hBox1.getChildren().addAll(borderPane1, borderPane2);
            scene = new Scene(hBox1, 450, 400);
        }
        else if (players == 3) {
            hBox1.getChildren().addAll(borderPane1, borderPane2, borderPane3);
            scene = new Scene(hBox1, 650, 400);
        }
        else {
            hBox1.getChildren().addAll(borderPane1, borderPane2, borderPane3, borderPane4);
            scene = new Scene(hBox1, 850, 450);
        }

        hBox1.setAlignment(Pos.CENTER);
        stage.setScene(scene);
        stage.show();
    }

    public static GridPane snakePreview(GridPane grid) {
        for (int i = 0; i < 5; i++) {
            RowConstraints row = new RowConstraints(20);
            grid.getRowConstraints().add(row);
        }
        grid.setAlignment(Pos.CENTER);
        Position p = new Position(0,0);
        Snake s = new Snake(custom, p);
        s.drawSnake(grid);
        return grid;
    }

    public static String getName(){
        return playerName;
    }

    public static void updateSnake(GridPane grid, VBox vbox, String pattern, String colour, String name){
        custom = pattern + "/" + colour;

        grid = new GridPane();
        vbox.getChildren().set(0, snakePreview(grid));

        if (name.equals("")){
            playerName = "NoName";
        } else {
            playerName = name;
        }
    }

    public static void updateSnake(String pattern, String colour, String name){
        custom = pattern + "/" + colour;

        if (name.equals("")){
            playerName = "NoName";
        } else {
            playerName = name;
        }
    }
}
