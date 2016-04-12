package application;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;


class HighScoresWindow {
    private BorderPane layout;
    private static Stage primaryStage;
    private TableView<HighScore> highScoreTable;

    HighScoresWindow(){

        primaryStage = new Stage();
        primaryStage.setTitle("Top 20 High Scores");

        try {
            highScoreTable = new TableView<>();
            highScoreTable.setItems(DataSource.getAllHighScores());
        } catch(IOException e){
            System.err.println("Cannot connect to server");
        }

        TableColumn<HighScore, Number> placeColumn;
        placeColumn = new TableColumn("Place");
        placeColumn.setMinWidth(25);
        placeColumn.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(highScoreTable.getItems().indexOf(column.getValue()) + 1));

        TableColumn<HighScore, String> nameColumn;
        nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(150);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        TableColumn<HighScore, Integer> scoreColumn;
        scoreColumn = new TableColumn<>("Score");
        scoreColumn.setMinWidth(150);
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("Score"));
        scoreColumn.setSortType(TableColumn.SortType.DESCENDING); //sets sort order to descending

        Button back = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.close();
                Main.endMusic();
                Main.showMainMenu();
            }
        });

        // adds box to place the button in the centre
        HBox backButton = new HBox();
        backButton.getChildren().add(back);
        backButton.setPadding(new Insets(5,0,5,0)); // sets top and bottom spacing
        backButton.setAlignment(Pos.CENTER);

        highScoreTable.getColumns().add(placeColumn);
        highScoreTable.getColumns().add(nameColumn);
        highScoreTable.getColumns().add(scoreColumn);
        highScoreTable.getSortOrder().add(scoreColumn); // sorts table by high scores

        layout = new BorderPane();
        layout.setCenter(highScoreTable);
        layout.setBottom(backButton);

        Scene scene = new Scene(layout, 350, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

