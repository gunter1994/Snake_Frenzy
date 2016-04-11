package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.System.exit;

// server to accept the name and score
// of player sent when their snake dies
public class scoreServer implements Runnable {
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
    private void requestHandler() throws IOException{
        System.out.println("High Score Server Listening..");

        while(true){
            Socket clientSocket = serverSocket.accept();
            Thread handlerThread = new Thread(new RequestHandler(clientSocket));
            handlerThread.start();
        }
    }

    // creates server and starts handler
    public void run(){
        try{
            requestHandler();
        }catch(IOException e){
            System.err.println("Error");
            e.printStackTrace();
        }
    }

    public static void stopServer(){
        exit(0);
    }
}