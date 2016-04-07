package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class scoreServer {
    private ServerSocket serverSocket;

    public scoreServer(int port){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void requestHandler() throws IOException{
        System.out.println("High Score Server Listening..");

        while(true){
            Socket clientSocket = serverSocket.accept();
            Thread handlerThread = new Thread(new scorePrinter(clientSocket));
            handlerThread.start();
        }
    }

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