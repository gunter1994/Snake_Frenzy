package application;

import java.io.*;


public class Player {

    String username;
    int highscore;
    File newFile = null;

    public Player() {

    }

    /*public void HighScore(){
        File newFile = new File("Highscore.csv");
        if (newFile.exists())
            System.out.println("");
        else{
            try{
                newFile.createNewFile();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public String WriteHighScore(){
        try{
            FileWriter wFile = new FileWriter(newFile);
            BufferedWriter buffWrite = new BufferedWriter(wFile);
        }
        catch(Exception e){
            return "";
        }

    }*/
}
