package application;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by adam on 07/04/16.
 */
public class DataSource {
    public static ObservableList<HighScore> getAllHighScores() throws IOException {
        Socket socket = new Socket("localhost", 8080);
        ObservableList<HighScore> highScoreList = FXCollections.observableArrayList();

        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.println("Get");
        out.flush();
        //send "get"
        //socket receives score
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String[] score;

        while ((score = in.readLine().split(",")) !=null){
            highScoreList.add(new HighScore(score[0], Integer.parseInt(score[1])));
        }

    return null;
    }

}