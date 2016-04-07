package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// server to accept the name and score
// of player sent when their snake dies
public class scoreServer {
    private ServerSocket serverSocket;

    // constructs the server
    public scoreServer(int port){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // starts a new handler thread when player connects
    public void requestHandler() throws IOException{
        System.out.println("High Score Server Listening..");

        while(true){
            Socket clientSocket = serverSocket.accept();
            Thread handlerThread = new Thread(new scoreHandler(clientSocket));
            handlerThread.start();
        }
    }

    // creates server and starts handler
    public static void main(String[] args){
        scoreServer server = new scoreServer(8080);
        try{
            server.requestHandler();
        }catch(IOException e){
            System.err.println("Error");
            e.printStackTrace();
        }
    }
}