package application;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.io.*;
import java.net.*;


public class DataSource {
    public static ObservableList<HighScore> getAllHighScores() throws IOException {

        try {
            Socket socket = new Socket("localhost", 8080);


            ObservableList<HighScore> highScoreList = FXCollections.observableArrayList();

            //send "get"
            //socket receives score
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("GET");
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String[] score;
            String temp;

            while ((temp = in.readLine()) != null) {
                score = temp.split(",");
                highScoreList.add(new HighScore(score[0], Integer.parseInt(score[1])));
            }

            return highScoreList;
        } catch(IOException e){
            ObservableList<HighScore> localScoreList = FXCollections.observableArrayList();
            BufferedReader read = new BufferedReader(new FileReader("localScores.csv"));
            String[] lscore;
            String ltemp;

            while((ltemp = read.readLine()) != null){
                lscore = ltemp.split(",");
                localScoreList.add(new HighScore(lscore[0], Integer.parseInt(lscore[1])));


            }


            return localScoreList;
        }
    }

}