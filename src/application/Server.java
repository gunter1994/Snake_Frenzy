package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(int port){
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
        Server server = new Server(8080);
        try{
            server.requestHandler();
        }catch(IOException e){
            System.err.println("Error");
            e.printStackTrace();
        }
    }
}