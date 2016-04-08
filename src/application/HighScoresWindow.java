package application;

import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;


public class HighScoresWindow {
    private BorderPane layout;
    private static Stage primaryStage;
    private TableView<HighScore> highScoreTable;

    public HighScoresWindow(){

        primaryStage = new Stage();
        primaryStage.setTitle("SnakeScores");

        try {
            highScoreTable = new TableView<>();
            highScoreTable.setItems(DataSource.getAllHighScores());
        } catch(IOException e){
            System.err.println("Cannot connect to server");
        }

        TableColumn<HighScore, String> nameColumn;
        nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(175);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        TableColumn<HighScore, Integer> scoreColumn;
        scoreColumn = new TableColumn<>("Score");
        scoreColumn.setMinWidth(173);
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("Score"));
        scoreColumn.setSortType(TableColumn.SortType.DESCENDING); //sets sort order to descending

        highScoreTable.getColumns().add(nameColumn);
        highScoreTable.getColumns().add(scoreColumn);
        highScoreTable.getSortOrder().add(scoreColumn); // sorts table by high scores

        layout = new BorderPane();
        layout.setCenter(highScoreTable);

        Scene scene = new Scene(layout, 350, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

