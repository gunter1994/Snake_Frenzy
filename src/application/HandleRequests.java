package application;

import java.io.*;
import java.net.Socket;

public class HandleRequests implements Runnable{
    private Socket socket;

    public ServerThread(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try{}
        catch(IOException e){
            e.printStackTrace();
        }
    }
}