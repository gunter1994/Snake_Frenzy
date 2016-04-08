package application;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

// prints player name and score to a CSV file when socket connects
public class RequestHandler implements Runnable{
    private Socket socket;

    public RequestHandler(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            String[] input = in.readLine().split(",");

            // runs function based on command received
            if (input[0].equals("GET")){
                BufferedReader csvIn = new BufferedReader(new FileReader(new File("highScores.csv")));
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                String temp;
                while ((temp = csvIn.readLine()) != null){
                    out.println(temp);
                }
                out.flush();
                out.close();
                csvIn.close();
            } else if (input[0].equals("ADD")){
                // input[0] is name input[1] is score
                // appends to CSV file
                FileWriter out = new FileWriter("highScores.csv", true);
                out.append(input[1] + "," + input[2] + "\n");

                out.close();
                in.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}