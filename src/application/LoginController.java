package application;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUserName;

    @FXML
    private Label lblStatus;

    public void Login(ActionEvent event) throws IOException {
        if(txtUserName.getText().equals("user")) {
            lblStatus.setText("Login failed. Username taken.");
        }
        else {
            lblStatus.setText("Login Success");
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Home_Screen.fxml"));
            Scene home_screen_scene = new Scene(root,400,400);
            primaryStage.setScene(home_screen_scene);
            primaryStage.show();
        }

    }
}
