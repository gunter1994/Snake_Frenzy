package application;

import java.io.*;
import java.net.Socket;

// prints player name and score to a CSV file when socket connects
public class scoreHandler implements Runnable{
    private Socket socket;

    public scoreHandler(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            // playerScore[0] is name playerScore[1] is score
            // appends to
            String[] playerScore = in.readLine().split(" ");
            FileWriter out = new FileWriter("highScores.csv", true);
            out.append(playerScore[0] + ", " + playerScore[1] + "\n");

            out.close();
            in.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}