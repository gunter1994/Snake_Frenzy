package application;

import java.io.*;
import java.net.Socket;

public class scorePrinter implements Runnable{
    private Socket socket;

    public scorePrinter(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            String[] score = in.readLine().split(" ");
            FileWriter out = new FileWriter("highScores.csv", true);
            out.append(score[0] + ", " + score[1] + "\n");

            out.close();
            in.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}