package application;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Created by samantha on 23/03/16.
 */
public class HomeController {

    @FXML
    public void handleNewGame(ActionEvent event) {
        MainGame g = new MainGame();
        try{
            Stage primaryStage = new Stage();
            g.start(primaryStage);
        }catch(Exception e){System.err.println();}
    }

}
