package application;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.io.*;
import java.net.*;

class DataSource {
    static ObservableList<HighScore> getAllHighScores() throws IOException {

        try {
            Socket socket = new Socket("localhost", 8080);
            ObservableList<HighScore> highScoreList = FXCollections.observableArrayList();

            //send "get" to server
            //receives score from server
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("GET");

            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String[] score;
            String temp;

            // adds HighScore objects (name and score) to observableList
            while ((temp = in.readLine()) != null) {
                score = temp.split(",");
                highScoreList.add(new HighScore(score[0], Integer.parseInt(score[1])));
            }
            return highScoreList;
        } catch(IOException e){
            System.err.println("Cannot connect to server");

            // reads from local high scores file if server unavailable
            ObservableList<HighScore> localScoreList = FXCollections.observableArrayList();
            BufferedReader read = new BufferedReader(new FileReader("localScores.csv"));
            String[] lscore;
            String ltemp;

            // splits lines and adds them to the observableList
            while((ltemp = read.readLine()) != null){
                lscore = ltemp.split(",");
                localScoreList.add(new HighScore(lscore[0], Integer.parseInt(lscore[1])));
            }
            return localScoreList;
        }
    }

}