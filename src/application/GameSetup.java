package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GameSetup {

    public static String custom = "None/Green";
    public static Stage stage = null;

    private static void handleNewGame() {
        stage.close();
        MainGame m = new MainGame();
        try{
            Stage primaryStage = new Stage();
            m.start(primaryStage);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //window for choosing name and snake design before starting game
    public static BorderPane preGameLobby() {

        GridPane grid = new GridPane();
        BorderPane borderPane = new BorderPane();
        VBox vBox1 = new VBox(), vBox2 = new VBox();
        vBox1.setSpacing(20);

        TextField textField = new TextField();
        textField.setPromptText("Enter Player Name");
        textField.setMaxSize(100,10);

        ComboBox<String> colours = new ComboBox<>();
        colours.setPromptText("Colour");
        colours.getItems().addAll("Blue","Red", "Yellow", "Green", "Orange", "Purple");
        ComboBox<String> patterns = new ComboBox<>();
        patterns.setPromptText("Pattern");
        patterns.getItems().addAll("None","Spotted","Striped");

        Button update = new Button("Update");
        update.setOnAction(e -> updateSnake(patterns.getValue(), colours.getValue()));

        vBox1.getChildren().addAll(textField,colours,patterns, update);
        vBox1.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox1);

        Button start = new Button("Start Game");
        start.setOnAction(e -> handleNewGame());

        vBox2.getChildren().addAll(snakePreview(grid), start);
        vBox2.setAlignment(Pos.CENTER);
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
            scene = new Scene(hBox1, 400, 350);
        }
        else if (players == 3) {
            hBox1.getChildren().addAll(borderPane1, borderPane2, borderPane3);
            scene = new Scene(hBox1, 500, 350);
        }
        else {
            hBox1.getChildren().addAll(borderPane1, borderPane2, borderPane3, borderPane4);
            scene = new Scene(hBox1, 600, 350);
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

    public static void updateSnake(String selectedPattern, String selectedColour){
        custom = selectedPattern + "/" + selectedColour;
    }
}
