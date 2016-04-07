package application;

import java.io.*;


public class Player {

    private String username;
    int highscore;
    File newFile = null;
    private String custom;

    public Player() {

    }

    public void setCustom(String c) {
        this.custom = c;
    }

    public String getCustom() {
        return this.custom;
    }

    public void setUsername(String u) {
        this.username = u;
    }

    public String getUsername() {
        return this.username;
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
