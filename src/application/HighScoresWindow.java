package application;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by adam on 07/04/16.
 */


public class HighScoresWindow {
    public static Stage primaryStage;
    public TableView<HighScore> highScoreTable;

    public HighScoresWindow(){

        primaryStage = new Stage();
        primaryStage.setTitle("SnakeScores");

        try {
            highScoreTable = new TableView<>();
            highScoreTable.setItems(DataSource.getAllHighScores());
        } catch(IOException e){
            System.err.println("Cannot connect to server");
        }

        TableColumn<HighScore, String> highColumn;
        highColumn = new TableColumn<>("HighScoresWindow");
        highColumn.setMinWidth(200);
        highColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        //highScoreTable.getColumns().add(highColumn);


        //primaryStage.set
    }

}

