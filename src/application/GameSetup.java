package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GameSetup {

    public static String custom = "None/Green";

    private static void handleNewGame() {
        MainGame m = new MainGame();
        try{
            Stage primaryStage = new Stage();
            m.start(primaryStage);
        }catch(Exception e){System.err.println();}
    }

    //window for choosing name and snake design before starting game
    public static void preGameLobby() {
        Stage primaryStage = new Stage();
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

        vBox1.getChildren().addAll(textField,colours,patterns);
        vBox1.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox1);

        Button start = new Button("Start Game");
        start.setOnAction(e -> handleNewGame());

        vBox2.getChildren().addAll(snakePreview(grid), start);
        vBox2.setAlignment(Pos.CENTER);
        borderPane.setBottom(vBox2);

        Scene pregameLobby = new Scene(borderPane,400,350); //(width, height)
        primaryStage.setScene(pregameLobby);
        primaryStage.show();

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

}
